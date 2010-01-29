================================================================================
Overview
================================================================================

Ripple is a dedicated scripting language for the Semantic Web.  Ripple programs
not only query the Semantic Web, but also reside within it as RDF data
structures, forming a global network of interlinked programs.  Ripple is a
relational stack language, closely related to functional stack languages such as
Joy, Factor and Cat.  As a Semantic Web interface, Ripple is a fast, text-based
linked data crawler and browser with all of the flexibility of a Turing-complete
programming language.

This Java implementation includes a query API, an extensible library of
primitive functions, and an interactive command-line interpreter.

For more information, including documentation and code samples, see:

    http://ripple.fortytwo.net/


================================================================================
Run
================================================================================

To begin an interpreter session, use one of the shortcut scripts (ripple.sh or
ripple.bat):

    $ ./ripple.sh

or invoke the executable JAR directly:

    $ java -jar target/ripple-*-standalone.jar

By default, this demo uses the LinkedDataSail semantic web querying and
caching layer, with Sesame's MemoryStore as a cache for the duration of the
command-line session.  To override Ripple's default behaviors, pass a Ripple
configuration file to the startup script as an argument:

    $ ./ripple.sh example.properties

Insofar as Ripple's configuration properties are documented at this time, you'll
find them on the wiki at

    http://code.google.com/p/ripple/w/list


================================================================================
Build
================================================================================

To build the demo application from scratch, issue the command:

    $ mvn install

from the directory containing pom.xml (note: requires Maven 2 and Java 5).


================================================================================
Bugs
================================================================================

Please submit bug reports to the issue tracker at:

    http://code.google.com/p/ripple/issues/list

or send mail to josh@fortytwo.net.  Happy scripting!
