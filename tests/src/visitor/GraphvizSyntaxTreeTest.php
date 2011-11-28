<?php
/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license http://www.gnu.org/licenses/ GNU General Public License
 * @author  Sven Strittmatter <ich@weltraumschaf.de>
 * @package tests
 */

namespace de\weltraumschaf\ebnf\visitor;

require_once 'Scanner.php';
require_once 'Parser.php';
require_once 'visitor/GraphvizSyntaxTree.php';

use de\weltraumschaf\ebnf\Scanner;
use de\weltraumschaf\ebnf\Parser;

/**
 * Test for {@link GraphvizSyntaxTree}.
 *
 * @package tests
 */
class GraphvizSyntaxTreeTest extends \PHPUnit_Framework_TestCase {
    public function testFoo() {
//        $file    = EBNF_TESTS_FIXTURS . "/Renderer/test_grammar.ebnf";
        $file    = EBNF_TESTS_FIXTURS . "/rules_with_literals.ebnf";
        $scanner = new Scanner(file_get_contents($file));
        $parser  = new Parser($scanner);
        $parser->parse();

        $ast     = $parser->getAst();
        $visitor = new GraphvizSyntaxTree($file);
        $ast->accept($visitor);
        $this->assertEquals('digraph "' . $file . '" {
    size        = "8,8";
    ordering    = out;

    node [shape=record]; n4; n5; n7; n8; n10; n11; n13; n14;
    node [shape=ellipse]; n0; n1; n2; n3; n6; n9; n12;

    n0 [label="syntax"];
    n1 [label="{rule|literal}"];
    n2 [label="choice"];
    n3 [label="sequence"];
    n4 [label="{terminal|\'}"];
    n5 [label="{identifier|character}"];
    n6 [label="loop"];
    n7 [label="{identifier|character}"];
    n8 [label="{terminal|\'}"];
    n9 [label="sequence"];
    n10 [label="{terminal|\"}"];
    n11 [label="{identifier|character}"];
    n12 [label="loop"];
    n13 [label="{identifier|character}"];
    n14 [label="{terminal|\"}"];

    n0 -> n1;
    n1 -> n2;
    n2 -> { n3 n9 };
    n3 -> { n4 n5 n6 n8 };
    n6 -> n7;
    n9 -> { n10 n11 n12 n14 };
    n12 -> n13;
}', $visitor->getDotString());
    }
}
