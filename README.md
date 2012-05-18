# EBNF Parser and Image Generator for PHP/Java

This package contains classes for scanning and parsing [EBNF][WP-EBNF]
grammar files and generate images with railroad diagrams for
that grammar.

This repo contains a PHP implementation and a work in progess implementation in Java.

The original code I discovered [here][KARMIN]. But that project seems
to be disconinued. So I decided to refactor and port the code
to PHP5.

## Install

You can install the EBNF package library and the command line tool via
[PEAR][PEAR]:

### Registering the channel:

    pear channel-discover pear.weltraumschaf.de

### Installing a package:

    pear install weltraumschaf/EBNF

This package has no more dependency than the [GD][GD] extension. After the
successful installation you should be able to invoke the command line tool:

    $ ebnf -h

## Usage

You can either use the shell script <kbd>bin/ebnf</kbd> for
generating images or XML from a grammar file:

    $ ./bin/ebnf -s mygrammar.ebnf
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.png
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.jpg -f jpg
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.gif -f gif
    $ ./bin/ebnf -s mygrammar.ebnf -o mygrammar.xml -f xml

Or you can use the classes for embedding the functionality in your code:

    <?php
    require_once "EBNF/Scanner.php";
    require_once "EBNF/Parser.php";
    require_once "EBNF/Renderer.php";

    $input    = "..."; // The grammar as string.
    $file     = "..."; // Where to save.
    $scanner  = new Scanner($input);
    $parser   = new Parser($scanner);
    $dom      = $parser->parse();
    $renderer = new Renderer($format, $file, $dom);
    $renderer->save();

It's necessary to add the PEAR source directory to the include path or include
the files absolute to work.

## Short introduction to EBNF

> EBNF is a code that expresses the grammar of a computer language. An EBNF
> consists of terminal symbol and non-terminal production rules which are the
> restrictions governing how terminal symbols can be combined into a legal
> sequence. [Wikipedia][WP-EBNF]

### Describing EBNF with EBNF as an example:

    syntax     = [ title ] "{" { rule } "}" [ meta ] .
    rule       = identifier ( "=" | ":" | ":==" ) expression ( "." | ";" ) .
    expression = term { "|" term } .
    term       = factor { factor } .
    factor     = identifier
               | literal
               | range
               | comment
               | "[" expression "]"
               | "(" expression ")"
               | "{" expression "}" .
    identifier = character { character } .
    range      = character ".." character .
    title      = literal .
    meta       = literal .
    literal    = "'" character { character } "'"
               | '"' character { character } '"' .
    comment    = "(*" character { character } "*)" .
    character  = "a" .. "z"
               | "A" .. "Z"
               | "0" .. "9" .

### Table of symbols

Here is a list of symbols implemented in the package. There are a lot
of [variants of (E)BNFs][EBNF-VARIANTS] out in the wild with some more
or other symbols. This package implements only a reasonable subset.

<dl>
    <dt>definition</dt>
        <dd>= or : or :==</dd>
    <dt>termination</dt>
        <dd>; or .</dd>
    <dt>alternation</dt>
        <dd>|</dd>
    <dt>option</dt>
        <dd>[ ... ]</dd>
    <dt>repetition</dt>
        <dd>{ ... }</dd>
    <dt>grouping</dt>
        <dd>( ... )</dd>
    <dt>terminal string
        <dd>" ... " or ' ... '</dd>
    <dt>comment</dt>
        <dd>(* ... *)</dd>
</dl>

## Development

If you want to build the project (unittests, apidoc etc.) clone the repo

    $ git clone git://github.com/Weltraumschaf/ebnf.git

and install the required PECL/PEAR dependencies

    $ cd ebnf
    $ ./install_deps

After that you can invoke the [Phing][PHING] targets

To show all available targets type:

    $ phing -l

Run the unittests (generates report and coverage in <kbd>reports/</kbd>):

    $ phing test

Run the codesniffer (generates report in <kbd>reports/</kbd>):

    $ phing checkstyle

Generate API doc (in the folder <kbd>doc/</kbd>):

    $ phing doc

Or you run all targets with:

    $ phing

## Ideas for an IDE

The IDE has two panes:

1. Text editor for the EBNF syntax with highlighting. Highlighted tokens are identifier and terminals. Matched highlighting on same identifiers or corresponding braces. The editor has line numbers and signals syntax errors.
2. Graphic preview which renders the EBNF as railroad diagram.

The view mode may be switched between horizontal split, vertical split and tabbed view. In the tabbed view source and preview are tabs. The IDE provides export to XML, PNG, JPG, GIF, and PDF. 

The IDE also provides a project view. It holds references to a project configuration file for each project and expands a tree view to files and directories added to the project. 

Project file format (JSON):
./ebnf.project:
{
    "name":  "The Project Name",
    "files": ["/foo/bar/baz.ebnf", "..."],
    "directories": ["/foo/snafu", "..."]
}

[WP-EBNF]:       http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form
[PEAR]:          http://pear.weltraumschaf.de/
[GD]:            http://php.net/manual/de/book.image.php
[KARMIN]:        http://karmin.ch/ebnf/index
[EBNF-VARIANTS]: http://www.cs.man.ac.uk/~pjj/bnf/ebnf.html
[PHING]:         http://www.phing.info/