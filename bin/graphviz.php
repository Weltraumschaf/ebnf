#!/usr/bin/env php
<?php

namespace de\weltraumschaf\ebnf;

set_include_path(implode(PATH_SEPARATOR, array(
    dirname(__DIR__) . DIRECTORY_SEPARATOR . "src",
    get_include_path()
)));

require_once 'EBNF/Scanner.php';
require_once 'EBNF/Parser.php';
require_once 'EBNF/visitor/GraphvizSyntaxTree.php';

use de\weltraumschaf\ebnf\visitor\GraphvizSyntaxTree;
use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Syntax;

//$file    = dirname(__DIR__) . "/tests/fixtures/Renderer/test_grammar.ebnf";
$file    = dirname(__DIR__) . "/tests/fixtures/rules_with_literals.ebnf";
$scanner = new Scanner(file_get_contents($file));
$parser  = new Parser($scanner);
$parser->parse();

$ast     = $parser->getAst();
$visitor = new GraphvizSyntaxTree($file);
$ast->accept($visitor);
echo $visitor->getDotString();
exit(0);