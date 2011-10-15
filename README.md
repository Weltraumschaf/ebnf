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

Development
-----------

If you want to build the project (unittests, apidoc etc.) clone the repo

    $ git clone git://github.com/Weltraumschaf/ebnf.git

and install the required PECL/PEAR dependencies

    $ cd ebnf
    $ ./install_deps

After that you can invoke the [Phing][3] targets

To show all available targets type:

    $ phing -l

Run the unittests (generates report and coberage in <kbd>reports/</kbd>):

    $ phing test

Run the codesniffer (generates report in <kbd>reports/</kbd>):

    $ phing checkstyle

Generate API doc (in the folder <kbd>doc/</kbd>):

    $ phing doc

Or you run all targets with:

    $ phing

Todo
----
- implement ranges 'a' .. 'z'
- make "syntax desc" { .... } optional

[1]: http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form
[2]: http://karmin.ch/ebnf/index
[3]: http://www.phing.info/