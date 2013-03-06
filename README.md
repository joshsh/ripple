<!-- This README can be viewed at https://github.com/joshsh/ripple/wiki -->

![Ripple logo|width=420px|height=100px](https://github.com/joshsh/ripple/wiki/graphics/ripple-logo-text-medium.png)

Welcome to the Ripple wiki!
Ripple is a declarative, stack-oriented dataflow language for exploring the Semantic Web and other multi-relational networks.
Ripple programs resemble path expressions as in [XPath](http://www.w3.org/TR/xpath/)
and postfix-style procedures as in
[Forth](http://en.wikipedia.org/wiki/Forth_&#40;programming_language\&#41;).
Every program has an [RDF](http://www.w3.org/RDF/) representation,
so you can embed programs in the Web of Data as well as querying against it.
This implementation is written in Java and includes an interactive command-line interpreter as well as a query API which interoperates with [Sesame 2.0](http://openrdf.org/).

## Contents

* [Running Ripple](https://github.com/joshsh/ripple/wiki/Running-Ripple): getting the software, using the command-line interpreter, and embedding Ripple in Java programs
* Examples
    * Ripple on [Linked Data](https://github.com/joshsh/ripple/wiki/ripple-on-linked-data)
    * Ripple on [JSON](https://github.com/joshsh/ripple/wiki/ripple-on-json)
    * [The Web of Programs](https://github.com/joshsh/ripple/wiki/The-Web-of-Programs): publishing Ripple programs as Linked Data
* Language reference
    * [Syntax](https://github.com/joshsh/ripple/wiki/Syntax): Ripple's RDF-oriented syntax for commands and queries
    * [Commands](https://github.com/joshsh/ripple/wiki/Commands): how to define programs and inspect the scripting environment
* Libraries and primitives
    * Core libraries
        * [control library](https://github.com/joshsh/ripple/wiki/control-library): mappings and program flow, regular expressions, looping and branching
        * [data library](https://github.com/joshsh/ripple/wiki/data-library): atomic values and datatypes, comparison, type conversion
        * [graph library](https://github.com/joshsh/ripple/wiki/graph-library): reading and writing RDF statements, SPARQL support, key/value objects and JSON
        * [logic library](https://github.com/joshsh/ripple/wiki/logic-library): boolean algebra
        * [math library](https://github.com/joshsh/ripple/wiki/math-library): arithmetic, roots and exponentials, trigonometry 
        * [stack library](https://github.com/joshsh/ripple/wiki/stack-library): list- and stack-oriented primitives inherited from [Joy](http://en.wikipedia.org/wiki/Joy_(programming_language)
        * [stream library](https://github.com/joshsh/ripple/wiki/stream-library): stream splitting and intersection, filters for deduplication and pruning, closed-world operations
        * [string library](https://github.com/joshsh/ripple/wiki/string-library): string manipulation
        * [system library](https://github.com/joshsh/ripple/wiki/system-library): system calls and network operations, other scripting languages
    * Extensions
        * [media library](https://github.com/joshsh/ripple/wiki/media-library): primitives for playing audio, showing images, and speaking text
        * [blueprints library](https://github.com/joshsh/ripple/wiki/blueprints-library): graph traversal on the [Blueprints](https://github.com/tinkerpop/blueprints/wiki/) API
* Miscellaneous
    * [Ripple configuration properties](https://github.com/joshsh/ripple/wiki/Ripple-configuration-properties)
    * [LinkedDataSail](https://github.com/joshsh/ripple/wiki/LinkedDataSail): Ripple's dynamic view of the Web of Data
    * [Naming conventions](https://github.com/joshsh/ripple/wiki/Naming-conventions) for Ripple programs
* External links
    * [Functional programs as Linked Data](http://sunsite.informatik.rwth-aachen.de/Publications/CEUR-WS/Vol-248/paper10.pdf) (the original paper on Ripple)
    * The [demo screencast](http://ripple.googlecode.com/svn/trunk/docs/screencast/index.html) from the [SFSW 2007 Scripting Challenge](http://web.archive.org/web/20120326083323/http://www.semanticscripting.org/SFSW2007/)
    * [ripple.fortytwo.net](http://ripple.fortytwo.net) (the Ripple blog)
    * [clj-ripple](https://github.com/eduardoejp/clj-ripple): a library for embedding Ripple in Clojure.  Sweet!
