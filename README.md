PHP EBNF Image Generator
========================

This package contains classes for scanning and parsing [EBNF][1]
grammar files and generates images with railroad diagrams of
that grammar.

The original code I discovered [here][2]. But that project seems
to be disconinued. So I decided to refactor and port the code
to PHP5.

Usage
-----

You can either use the shell script from <kbd>bin/ebnf</kbd> for
generating images or XML from a grammar file:

    $ ./bin/ebnf -s mygrammar.ebnf
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.png
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.jpg -f jpg
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.gif -f gif
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.xml -f xml

Or you can use the classes for embedding the functionality in your code:

    <?php
    $input   = "..."; // The grammar as string.
    file     = "..."; // Where to save.
    $scanner = new Scanner($input);
    $parser  = new Parser($scanner);
    $dom     = $parser->parse();
    $renderer = new Renderer($format, $file, $dom);
    $renderer->save();

Todo
----
- implement comments
- implement special sequence
- implement exceptions

[1]: http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form
[2]: http://karmin.ch/ebnf/index