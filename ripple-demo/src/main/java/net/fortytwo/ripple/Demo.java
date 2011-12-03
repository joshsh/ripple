/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import net.fortytwo.ripple.cli.RippleCommandLine;
import net.fortytwo.ripple.config.SailConfiguration;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.StackEvaluator;
import org.openrdf.sail.Sail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;


/**
 * Demo application.
 */
public final class Demo {
    private Demo() {
    }

    public static void demo(final InputStream in,
                            final PrintStream out,
                            final PrintStream err)
            throws RippleException {
        // Create a Sesame triple store.
        URIMap uriMap = new URIMap();
        SailConfiguration sailConfig = new SailConfiguration(uriMap);
        Sail sail = sailConfig.getSail();

        // Attach a Ripple model to the repository.
        Model model = new SesameModel(sail);

        // Attach a query engine to the model.
        StackEvaluator evaluator = new LazyStackEvaluator();
        QueryEngine qe
                = new QueryEngine(model, evaluator, out, err);

        // Attach an interpreter to the query engine and let it query from
        // standard input.
        RippleCommandLine r = new RippleCommandLine(qe, in);
        r.run();

        // Shut down.
        model.shutDown();
    }

    private static void printUsage() {
        System.out.println("Usage:  ripple [options] [configuration file]");
        System.out.println("Options:\n"
                + "  -h           Print this help and exit\n"
                + "  -q           Suppress normal output\n"
                + "  -v           Print version information and exit");
        System.out.println("For more information, please see:\n"
                + "  <URL:http://ripple.fortytwo.net/>.");
    }

    private static void printVersion() {
        System.out.println(Ripple.getName() + " " + Ripple.getVersion());

        // Would be nice: list of libraries
    }

    public static void main(final String[] args) {
        // Default values.
        boolean quiet = false, showVersion = false, showHelp = false;
        File configFile = null;

        // Long options are available but are not advertised.
        LongOpt[] longOptions = {
                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
                new LongOpt("quiet", LongOpt.NO_ARGUMENT, null, 'q'),
                new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v')};

        Getopt g = new Getopt(Ripple.getName(), args, "hqv", longOptions);
        int c;
        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 'h':
                case 0:
                    showHelp = true;
                    break;
                case 'q':
                case 1:
                    quiet = true;
                    break;
                case 'v':
                case 2:
                    showVersion = true;
                    break;
                case '?':
                    // Note: getopt() already printed an error
                    printUsage();
                    System.exit(1);
                    break;
                default:
                    System.err.print("getopt() returned " + c + "\n");
            }
        }

        int i = g.getOptind();
        if (i < args.length) {
            // Too many non-option arguments.
            if (args.length - i > 1) {
                printUsage();
                System.exit(1);
            }

            configFile = new File(args[i]);
        }

        if (showHelp) {
            printUsage();
            System.exit(0);
        }

        if (showVersion) {
            printVersion();
            System.exit(0);
        }

        try {
            // Load Ripple configuration.
            if (null == configFile) {
                Ripple.initialize();
            } else {
                Properties p = new Properties();

                try {
                    p.load(new FileInputStream(configFile));
                } catch (IOException e) {
                    throw new RippleException(e);
                }

                Ripple.initialize(p);
            }
        } catch (RippleException e) {
            System.err.println("Initialization error: " + e);
            e.logError();
            System.exit(1);
        }

        Ripple.setQuiet(quiet);
// System.out.println( "quiet = " + quiet );
// System.out.println( "showVersion = " + showVersion );
// System.out.println( "format = " + format );
// System.out.println( "store = " + store );

        try {
            demo(System.in, System.out, System.err);
        } catch (RippleException e) {
            System.out.println("Exited with error: " + e);
            e.logError();
            System.exit(1);
        }

        // Exit despite any remaining active threads.
        System.exit(0);
    }
}

