package net.fortytwo.ripple;

import com.knowledgereefsystems.agsail.AllegroSail;
import net.fortytwo.linkeddata.sail.LinkedDataSail;
import net.fortytwo.ripple.rdf.RDFUtils;
import net.fortytwo.ripple.sesametools.readonly.ReadOnlySail;
import org.apache.log4j.Logger;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.nativerdf.NativeStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: josh
 * Date: Feb 27, 2009
 * Time: 12:05:43 PM
 */
public class SailConfiguration {
    private static final Logger LOGGER = Logger.getLogger(SailConfiguration.class);

    private final String sailType;
    private final URIMap uriMap;
    private File persistFile;
    private RDFFormat persistFileFormat;
    private Sail mainSail;
    private Sail persistSail;
    private Sail linkedDataSailBaseSail;

    public SailConfiguration(final URIMap uriMap) throws RippleException {
        this.uriMap = uriMap;
        RippleProperties props = Ripple.getProperties();
        sailType = props.getString(Ripple.SAIL_TYPE).trim();
    }

    public void initialize() throws RippleException {
        mainSail = createSail(sailType, uriMap);

        // Load a dump file if so instructed.  Note: this is done only once,
        // not once per Sail.
        loadDumpFile(mainSail);
    }

    public Sail getSail() throws RippleException {
        if (null == mainSail) {
            throw new RippleException("not yet initialized");
        }

        return mainSail;
    }

    public void shutDown() throws RippleException {
        try {
            // If we have an LDSail on top of a base Sail:
            if (null != linkedDataSailBaseSail) {
                mainSail.shutDown();
                saveToPersistFile();
                linkedDataSailBaseSail.shutDown();
            }

            // If there is a single Sail:
            else {
                saveToPersistFile();
                mainSail.shutDown();
            }
        } catch (SailException e) {
            throw new RippleException(e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private Sail createSail(final String sailType,
                            final URIMap uriMap) throws RippleException {
        System.out.println("creating Sail of type: " + sailType);
        Sail sail;

        if (sailType.equals(MemoryStore.class.getName())) {
            sail = createMemoryStore();
        } else if (sailType.equals(NativeStore.class.getName())) {
            sail = createNativeStore();
        } else if (sailType.equals(LinkedDataSail.class.getName())) {
            sail = createLinkedDataSail(uriMap);
        } else if (sailType.equals(AllegroSail.class.getName())) {
            sail = createAllegroSail();
        } else {
            throw new RippleException("unhandled Sail type: " + sailType);
        }

        boolean readOnly = Ripple.getProperties().getBoolean(Ripple.READ_ONLY, false);
        if (readOnly) {
            sail = new ReadOnlySail(sail);
        }

        return sail;
    }

    private Sail createMemoryStore() throws RippleException {
        Sail sail = new MemoryStore();
        try {
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        loadFromPersistFile(sail);

        return sail;
    }

    private Sail createNativeStore() throws RippleException {
        RippleProperties props = Ripple.getProperties();
        File dir = props.getFile(Ripple.NATIVESTORE_DIRECTORY);

        Sail sail = new NativeStore(dir);
        try {
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }

    private Sail createLinkedDataSail(final URIMap uriMap) throws RippleException {
        RippleProperties props = Ripple.getProperties();
        String baseSailType = props.getString(Ripple.LINKEDDATASAIL_BASE_SAIL);

        linkedDataSailBaseSail = createSail(baseSailType, uriMap);
        Sail sail = new LinkedDataSail(linkedDataSailBaseSail, uriMap);
        try {
            // Note: base Sail is already initialized.
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }

    private static Sail createAllegroSail() throws RippleException {
        RippleProperties props = Ripple.getProperties();

        String host = props.getString(Ripple.ALLEGROSAIL_HOST);
        int port = props.getInt(Ripple.ALLEGROSAIL_PORT);
        boolean start = props.getBoolean(Ripple.ALLEGROSAIL_START);
        String name = props.getString(Ripple.ALLEGROSAIL_NAME);
        File directory = props.getFile(Ripple.ALLEGROSAIL_DIRECTORY);

        Sail sail;
        //try {
        sail = new AllegroSail(host, port, start, name, directory, 0, 0, false, false);
        //} catch (AllegroSailException e) {
        //    throw new RippleException(e);
        //}

        try {
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }

    ////////////////////////////////////////////////////////////////////////////

    private void loadFromPersistFile(final Sail sail) throws RippleException {
        RippleProperties props = Ripple.getProperties();
        persistFile = props.getFile(Ripple.MEMORYSTORE_PERSIST_FILE, null);

        // If a persist file has been specified, attempt to load from it.  A
        // missing persist file is tolerated.
        if (null != persistFile) {
            persistSail = sail;
            persistFileFormat = getRDFFormat(Ripple.MEMORYSTORE_PERSIST_FILE_FORMAT);

            if (persistFile.exists()) {
                String baseURI = props.getString(Ripple.MEMORYSTORE_PERSIST_FILE_BASE_URI);
                importRDFDumpFile(sail, persistFile, persistFileFormat, baseURI);
            } else {
                LOGGER.info("file " + persistFile + " does not yet exist. It will be created on shutdown.");
            }
        }
    }

    private void saveToPersistFile() throws RippleException {
        if (null != persistSail) {
            boolean readOnly = Ripple.getProperties().getBoolean(Ripple.READ_ONLY, false);

            // If the Sail is configured as read-only, we not only shouldn't
            // write to the Sail during execution, but we also shouldn't write
            // to the persist file it was read in from.
            if (!readOnly) {
                exportRDFDumpFile(persistSail, persistFile, persistFileFormat);
            }
        }
    }

    private void loadDumpFile(final Sail sail) throws RippleException {
        RippleProperties props = Ripple.getProperties();
        File loadFile = props.getFile(Ripple.LOAD_FILE, null);

        if (null != loadFile) {
            RDFFormat format = getRDFFormat(Ripple.LOAD_FILE_FORMAT);
            String baseURI = props.getString(Ripple.LOAD_FILE_BASE_URI);
            importRDFDumpFile(sail, loadFile, format, baseURI);
        }
    }

    private RDFFormat getRDFFormat(final String propertyName) throws RippleException {
        String value = Ripple.getProperties().getString(propertyName);

        RDFFormat format = RDFUtils.findFormat(value);

        if (null == format) {
            throw new RippleException("unknown RDF format: " + value);
        }

        return format;
    }

    private void importRDFDumpFile(final Sail sail,
                                   final File file,
                                   final RDFFormat format,
                                   final String baseURI) throws RippleException {
        Repository repo = new SailRepository(sail);
        try {
            RepositoryConnection rc = repo.getConnection();
            try {
                rc.add(file, baseURI, format);
                rc.commit();
            } finally {
                rc.close();
            }
        } catch (RepositoryException e) {
            throw new RippleException(e);
        } catch (IOException e) {
            throw new RippleException(e);
        } catch (RDFParseException e) {
            throw new RippleException(e);
        }
    }

    private void exportRDFDumpFile(final Sail sail,
                                   final File file,
                                   final RDFFormat format) throws RippleException {
        Repository repo = new SailRepository(sail);
        try {
            OutputStream out = new FileOutputStream(file);
            try {
                RepositoryConnection rc = repo.getConnection();
                try {
                    RDFHandler handler = Rio.createWriter(format, out);
                    rc.export(handler);
                } finally {
                    rc.close();
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new RippleException(e);
        } catch (RepositoryException e) {
            throw new RippleException(e);
        } catch (RDFHandlerException e) {
            throw new RippleException(e);
        }
    }
}
