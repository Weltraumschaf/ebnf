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

/**
 * @see Scanner
 */
require_once 'Scanner.php';
/**
 * @see Parser
 */
require_once 'Parser.php';
/**
 * @see TextSyntaxTree
 */
require_once 'visitor/TextSyntaxTree.php';
/**
 * @see SyntaxBuilder
 */
require_once 'ast/builder/SyntaxBuilder.php';

use de\weltraumschaf\ebnf\Scanner;
use de\weltraumschaf\ebnf\Parser;
use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Composite;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;
use de\weltraumschaf\ebnf\ast\builder\SyntaxBuilder;

/**
 * Test for {@link TextSyntaxTree}.
 *
 * @package tests
 * @version @@version@@
 */
class TextSyntaxTreeTest extends \PHPUnit_Framework_TestCase {

    public function testCreateRow() {
        $this->assertEquals(array(), TextSyntaxTree::createRow(-3));
        $this->assertEquals(array(), TextSyntaxTree::createRow(0));
        $this->assertEquals(array("", ), TextSyntaxTree::createRow(1));
        $this->assertEquals(array("", "", ""), TextSyntaxTree::createRow(3));
        $this->assertEquals(array("", "", "", "", ""), TextSyntaxTree::createRow(5));
    }

    public function testGenerateMatrix() {
        $builder = new SyntaxBuilder();
        $builder->syntax("fobar")
                ->rule("one")
                ->rule("two")
                ->rule("three");
        $ast = $builder->getAst();
        $this->assertEquals(2, $ast->depth());
        $visitor = new TextSyntaxTree();
        $this->assertEquals(array(), $visitor->getMatrix());
        $this->assertEquals(0, $visitor->getDepth());
        $ast->accept($visitor);

        $this->assertEquals(2, $visitor->getDepth());
        $matrix = $visitor->getMatrix();
        $this->assertEquals(4, count($matrix));
        $this->assertEquals("[syntax]",  $matrix[0][0]);
        $this->assertEquals("",          $matrix[0][1]);
        $this->assertEquals(" +--",       $matrix[1][0]);
        $this->assertEquals("[rule='one']", $matrix[1][1]);
        $this->assertEquals(" +--",       $matrix[2][0]);
        $this->assertEquals("[rule='two']", $matrix[2][1]);
        $this->assertEquals(" +--",       $matrix[3][0]);
        $this->assertEquals("[rule='three']", $matrix[3][1]);

        $builder->clear()
                ->syntax("foo")
                ->rule("literal")
                    ->choice()
                        ->sequence()
                            ->terminal("'")
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal("'")
                        ->end()
                        ->sequence()
                            ->terminal('"')
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal('"')
                        ->end()
                    ->end();

        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(6, $visitor->getDepth());
        $this->assertEquals(array(
            array("[syntax]", "", "", "", "", ""),
            array(" +--",     "[rule='literal']", "", "", "", ""),
            array("    ",     " +--", "[choice]", "", "", ""),
            array("    ",     "    ", " +--", "[sequence]", "", ""),
            array("    ",     "    ", " |  ", " +--",       "[terminal=''']", ""),
            array("    ",     "    ", " |  ", " +--",       "[identifier='character']", ""),
            array("    ",     "    ", " |  ", " +--",       "[loop]", ""),
            array("    ",     "    ", " |  ", " |  ",       " +--", "[identifier='character']"),
            array("    ",     "    ", " |  ", " +--",       "[terminal=''']", ""),
            array("    ",     "    ", " +--", "[sequence]", "", ""),
            array("    ",     "    ", "    ", " +--",       "[terminal='\"']", ""),
            array("    ",     "    ", "    ", " +--",       "[identifier='character']", ""),
            array("    ",     "    ", "    ", " +--",       "[loop]", ""),
            array("    ",     "    ", "    ", " |  ",       " +--", "[identifier='character']"),
            array("    ",     "    ", "    ", " +--",       "[terminal='\"']", ""),
        ), $visitor->getMatrix());
    }

    public function testGenerateText() {
        $builder = new SyntaxBuilder();
        $builder->syntax("foo");
        $ast     = $builder->getAst();
        $visitor = new TextSyntaxTree();
        $ast->accept($visitor);
        $this->assertEquals(
            "[syntax]" . PHP_EOL,
            $visitor->getText()
        );

        $visitor = new TextSyntaxTree();
        $builder->rule("rule-1");
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "[syntax]" . PHP_EOL .
            " +--[rule='rule-1']" . PHP_EOL,
            $visitor->getText()
        );

        $visitor = new TextSyntaxTree();
        $builder->rule("rule-2");
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "[syntax]" . PHP_EOL .
            " +--[rule='rule-1']" . PHP_EOL .
            " +--[rule='rule-2']" . PHP_EOL,
            $visitor->getText()
        );

        $choice = $builder->clear()
                          ->rule("name")
                              ->choice();
        $ast     = $builder->getAst();
        $visitor = new TextSyntaxTree();
        $ast->accept($visitor);
        $this->assertEquals(
            "[syntax]" . PHP_EOL .
            " +--[rule='name']" . PHP_EOL .
            "     +--[choice]" . PHP_EOL,
            $visitor->getText()
        );

        $choice->identifier("ident")
               ->terminal("term")
               ->end();
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "[syntax]" . PHP_EOL .
            " +--[rule='name']" . PHP_EOL .
            "     +--[choice]" . PHP_EOL .
            "         +--[identifier='ident']" . PHP_EOL .
            "         +--[terminal='term']" . PHP_EOL,
            $visitor->getText()
        );

        $visitor = new TextSyntaxTree();
        $builder->clear()
                ->syntax("foobar")
                    ->rule("one")
                        ->choice()
                        ->end()
                    ->rule("two");
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "[syntax]" . PHP_EOL .
            " +--[rule='one']" . PHP_EOL .
            " |   +--[choice]" . PHP_EOL .
            " +--[rule='two']" . PHP_EOL ,
            $visitor->getText()
        );

        $fixtureDir = EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "visitor" . DIRECTORY_SEPARATOR . "TextSyntaxTree";
        $file       = EBNF_TESTS_FIXTURS . "/rules_with_literals.ebnf";
        $scanner    = new Scanner(file_get_contents($file));
        $parser     = new Parser($scanner);
        $parser->parse();

        $ast     = $parser->getAst();
        $visitor = new TextSyntaxTree();
        $ast->accept($visitor);
        $this->assertEquals(
            file_get_contents($fixtureDir . DIRECTORY_SEPARATOR . "rules_with_literals"),
            $visitor->getText()
        );
    }

    public function testFormatNode() {
        $this->assertEquals("[choice]", TextSyntaxTree::formatNode(new Choice()));
        $this->assertEquals("[identifier]", TextSyntaxTree::formatNode(new Identifier()));
        $ident = new Identifier();
        $ident->value = "foobar";
        $this->assertEquals("[identifier='foobar']", TextSyntaxTree::formatNode($ident));
        $this->assertEquals("[loop]", TextSyntaxTree::formatNode(new Loop()));
        $this->assertEquals("[option]", TextSyntaxTree::formatNode(new Option()));
        $this->assertEquals("[rule]", TextSyntaxTree::formatNode(new Rule()));
        $rule = new Rule();
        $rule->name = "snafu";
        $this->assertEquals("[rule='snafu']", TextSyntaxTree::formatNode($rule));
        $this->assertEquals("[sequence]", TextSyntaxTree::formatNode(new Sequence()));
        $this->assertEquals("[syntax]", TextSyntaxTree::formatNode(new Syntax()));
        $this->assertEquals("[terminal]", TextSyntaxTree::formatNode(new Terminal()));
        $term = new Terminal();
        $term->value = "foobar";
        $this->assertEquals("[terminal='foobar']", TextSyntaxTree::formatNode($term));
    }
}
