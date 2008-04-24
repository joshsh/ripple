/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.linkeddata.rdfizers;

import org.openrdf.rio.RDFHandler;
import org.openrdf.model.URI;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.metadata.IIOMetadata;
import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.linkeddata.Rdfizer;
import net.fortytwo.linkeddata.ContextMemo;

// TODO: follow instructions at http://www.barregren.se/blog/how-read-exif-and-iptc-java-image-i-o-api
// example images: http://www.exif.org/samples/
// EXIF RDF vocabulary: http://www.w3.org/2003/12/exif/

/**
 * Author: josh
 * Date: Mar 24, 2008
 * Time: 1:43:02 PM
 */
public class ImageRdfizer implements Rdfizer
{
    private static boolean initialized = false;

    private static void initialize()
    {
        ImageIO.setUseCache( false );
        ImageIO.scanForPlugins();  // probably not necessary...
        initialized = true;
    }
    
    public ContextMemo.Status handle( final InputStream is,
                                      final RDFHandler handler,
                                      final URI resourceUri,
                                      final String baseUri )
    {
        if ( !initialized )
        {
            initialize();
        }

        try
        {
            ImageInputStream iis = ImageIO.createImageInputStream( is );

            // get an iterator over all readers that claim to be able to read the image
            Iterator<ImageReader> readers = ImageIO.getImageReaders( iis );

            // check the iterator for having the ImageReader
            if ( readers.hasNext() )
            {
                // get an ImageReader to parse and decode the image to process
                ImageReader reader = readers.next();

                // set the input of the ImageReader
                reader.setInput(iis);
                
                // print the reader of the processed file
                System.out.println("[Trace] Image Reader : " + reader);

                // print the format of the processed file
                System.out.println("[Trace] Image Format Name : " + reader.getFormatName());

                // print the input of the reader
                System.out.println("[Trace] Input : " + reader.getInput());

                /*
                // get the metadata of the processed file
                IIOMetadata metadata = reader.getImageMetadata(0);

                // get the metadata format names
                String[] names = metadata.getMetadataFormatNames();

                // print the metadata format names
                System.out.println(names);
                */

                // get the metadata of the processed file
                IIOMetadata metadata = reader.getImageMetadata(0);

                // get the metadata format names
                String[] names = metadata.getMetadataFormatNames();

                // print the metadata format names
                for (int i = 0; i < names.length; i++) {
                    System.out.println("[Trace] MetadataFormat Name " + i + " : " + names[i]);

                    // get an XML DOM Node object that represents the root of a tree of metadata
                    Node node = metadata.getAsTree(names[i]);

                    try {
                        StringBuilder sb = new StringBuilder();
                        showNode(node, sb, "");
                        System.out.println(sb);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }

                return ContextMemo.Status.Success;
            }

            else
            {
                new RippleException( "could not find a reader for image" ).logError( false );
                return ContextMemo.Status.RdfizerError;
            }
        }

        catch ( IOException e ) {
            new RippleException( e ).logError( true );
            return ContextMemo.Status.RdfizerError;
        }
    }

    private static void showNode( final Node node, final StringBuilder sb, String indent )
    {
        sb.append( indent ).append( node.getLocalName() );// + ", " + node.getNamespaceURI() + node.getLocalName() );
        indent += "    ";

        NamedNodeMap map = node.getAttributes();
        if (0 < map.getLength()) {
            sb.append( " {" );
            for (int i = 0; i < map.getLength(); i++) {
                if ( 0 < i ) {
                    sb.append( ", " );
                }
                Node n = map.item(i);
                sb.append( n.getNodeName() + "=" + n.getNodeValue() );
                //sb.append( "\n" ).append( indent ).append( "attr: " ).append(n.getLocalName());
            }
            sb.append( "}" );
        }

        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            sb.append( "\n" );
            showNode( n, sb, indent );
        }

/*
        try {
            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            document.adoptNode(node);

            OutputFormat format = new OutputFormat();//((Document)core);
            format.setLineSeparator(LineSeparator.Windows);
            format.setIndenting(true);
            format.setLineWidth(0);
            format.setPreserveSpace(true);
            XMLSerializer serializer = new XMLSerializer(
                System.out, format);
            serializer.asDOMSerializer();
            serializer.serialize(document);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
