# PHP EBNF Image Generator

This package contains classes for scanning and parsing [EBNF][1]
grammar files and generates images with railroad diagrams of
that grammar.

The original code I discovered [here][2]. But that project seems
to be disconinued. So I decided to refactor and port the code
to PHP5.

## Short introduction to EBNF

> EBNF is a code that expresses the grammar of a computer language. An EBNF
> consists of terminal symbol and non-terminal production rules which are the
> restrictions governing how terminal symbols can be combined into a legal
> sequence." [Wikipedia][1]

### Describing EBNF with EBNF as an example:

    syntax     = [ title ] "{" { rule } "}" [ comment ] .
    rule       = identifier ( "=" | ":" | ":==" ) expression ( "." | ";" ) .
    expression = term { "|" term } .
    term       = factor { factor } .
    factor     = identifier
               | literal
               | range
               | "[" expression "]"
               | "(" expression ")"
               | "{" expression "}" .
    identifier = character { character } .
    range      = character ".." character .
    title      = literal .
    comment    = literal .
    literal    = "'" character { character } "'"
               | '"' character { character } '"' .
    character  = "a" .. "z"
               | "A" .. "Z"
               | "0" .. "9" .

### Table of symbols

This is a list of symbols implemented in ths package. There are a lot 
of variants of (E)BNFs wih some more or other symbols.
    
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

## Usage

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
    $file     = "..."; // Where to save.
    $scanner = new Scanner($input);
    $parser  = new Parser($scanner);
    $dom     = $parser->parse();
    $renderer = new Renderer($format, $file, $dom);
    $renderer->save();

## Development

If you want to build the project (unittests, apidoc etc.) clone the repo

    $ git clone git://github.com/Weltraumschaf/ebnf.git

and install the required PECL/PEAR dependencies

    $ cd ebnf
    $ ./install_deps

After that you can invoke the [Phing][3] targets

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

## Todo
- implement ranges 'a' .. 'z'
- make "syntax desc" { .... } optional
- implement grammar validation

[1]: http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form
[2]: http://karmin.ch/ebnf/index
[3]: http://www.phing.info/