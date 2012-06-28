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
 * @see Choice
 */
require_once 'ast/Choice.php';
/**
 * @see Identifier
 */
require_once 'ast/Identifier.php';
/**
 * @see Loop
 */
require_once 'ast/Loop.php';
/**
 * @see Option
 */
require_once 'ast/Option.php';
/**
 * @see Rule
 */
require_once 'ast/Rule.php';
/**
 * @see Sequence
 */
require_once 'ast/Sequence.php';
/**
 * @see Syntax
 */
require_once 'ast/Syntax.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';
/**
 * @see SyntaxBuilder
 */
require_once 'ast/builder/SyntaxBuilder.php';
/**
 * @see TextRailroad
 */
require_once 'visitor/TextRailroad.php';

use de\weltraumschaf\ebnf\ast\builder\SyntaxBuilder;
use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;

/**
 * Test for {@link TextSyntaxTree}.
 *
 * @package tests
 * @version @@version@@
 */
class TextRailroadTest extends \PHPUnit_Framework_TestCase {

    public function testFormatNode() {
        $n = new Terminal($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $n->value = "term";
        $this->assertEquals("-(term)-", TextRailroad::formatNode($n));
        $n = new Identifier($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $n->value = "ident";
        $this->assertEquals("-[ident]-", TextRailroad::formatNode($n));
        $n = new Rule($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $n->name = "foobar";
        $this->assertEquals("foobar", TextRailroad::formatNode($n));

        foreach (array(
            new Choice($this->getMock('de\weltraumschaf\ebnf\ast\Node')),
            new Loop($this->getMock('de\weltraumschaf\ebnf\ast\Node')),
            new Option($this->getMock('de\weltraumschaf\ebnf\ast\Node')),
            new Sequence($this->getMock('de\weltraumschaf\ebnf\ast\Node')),
            new Syntax($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        ) as $n) {
            $this->assertEquals("", TextRailroad::formatNode($n));
        }
    }

    public function testGenerateMatrix() {
        $builder = new SyntaxBuilder();
        $visitor = new TextRailroad();
        $builder->syntax("foobar")
                ->rule("rule")
                    ->identifier("identifier")
                ->end();

        $ast     = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("rule"),
            array("-----", "->-", "-[identifier]-", "->-", "--|")
        ), $visitor->getMatrix());

        $builder->clear()
                ->syntax("foobar")
                ->rule("rule")
                    ->terminal("terminal")
                ->end();
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("rule"),
            array("-----", "->-", "-(terminal)-", "->-", "--|")
        ), $visitor->getMatrix());

        $builder->clear()
                ->syntax("foobar")
                ->rule("rule1")
                    ->identifier("identifier")
                ->end()
                ->rule("rule2")
                    ->terminal("terminal")
                ->end();
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("rule1"),
            array("------", "->-", "-[identifier]-", "->-", "--|"),
            array("rule2"),
            array("------", "->-", "-(terminal)-", "->-", "--|")
        ), $visitor->getMatrix());

        $builder->clear()
                ->syntax("foobar")
                ->rule("rule1")
                    ->sequence()
                        ->identifier("identifier")
                        ->terminal("terminal")
                        ->identifier("identifier")
                    ->end()
                ->end();
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("rule1"),
            array("------", "->-", "-[identifier]-", "->-", "-(terminal)-", "->-", "-[identifier]-", "->-", "--|")
        ), $visitor->getMatrix());
$this->markTestIncomplete();
        $builder->clear()
                ->syntax("foo")
                ->rule("literal")
                    ->choice()
                        ->terminal("'")
                        ->terminal('"')
                    ->end()
                ->end();
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("literal"),array(),array(),array(),
//            array("--------", "-+-", "->-",  "-(')-", "->-", "-+-", "--|"),
//            array("        ", " | ", "   ",  "     ", "   ", " | "),
//            array("        ", " +-", "->-", "-(\")-", "->_", "-+ "),
        ), $visitor->getMatrix());

$this->markTestIncomplete();

        $builder->clear()
                ->syntax("foo")
                ->rule("literal")
                    ->choice()
                        ->sequence()
                            ->terminal("'")
                            ->identifier("character")
                            ->terminal("'")
                        ->end()
                        ->sequence()
                            ->terminal('"')
                            ->identifier("character")
                            ->terminal('"')
                        ->end()
                    ->end()
                ->end();
        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("literal"),
            array("--------", "-+-",  "-(')-", "->-", "-[character]-", "->-",  "-(')-", "->-", "-+-", "--|"),
            array("        ", " | ",  "     ", "   ", "             ", "   ",  "     ", "   ", " | "),
            array("        ", " +-", "-(\")-", "->-", "-[character]-", "->-", "-(\")-", "->-", "-+ "),
        ), $visitor->getMatrix());
$this->markTestIncomplete();
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
                    ->end()
                ->end();

        $ast = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("literal"),array(),
//            array("--------", "-+-",  "-(')-", "->-", "-[character]-", "->-", "-+-", "---", "------>------", "---", "-+-", "->-",  "-(')-", "->-", "-+-", "--|"),
//            array("        ", " | ",  "     ", "   ", "             ", "   ", " | ", "   ", "             ", "   ", " | ", "   ",  "     ", "   ", " | "),
//            array("        ", " | ",  "     ", "   ", "             ", "   ", " +-", "-<-", "-[character]-", "-<-", "-+ ", "   ",  "     ", "   ", " | "),
//            array("        ", " | ",  "     ", "   ", "             ", "   ", "   ", "   ", "             ", "   ", "   ", "   ",  "     ", "   ", " | "),
//            array("        ", "-+-", "-(\")-", "->-", "-[character]-", "->-", "-+-", "---", "------>------", "---", "-+-", "->-", "-(\")-", "->-", "-+-"),
//            array("        ", "   ",  "     ", "   ", "             ", "   ", " | ", "   ", "             ", "   ", " | ", "   ",  "     ", "   ", "   "),
//            array("        ", "   ",  "     ", "   ", "             ", "   ", " +-", "-<-", "-[character]-", "-<-", "-+ ", "   ",  "     ", "   ", "   ")
        ), $visitor->getMatrix());

        $this->markTestIncomplete();
    }

    public function testGetText() {
        $builder = new SyntaxBuilder();
        $builder->syntax("foobar")
                ->rule("rule")
                    ->identifier("identifier")
                ->end();

        $visitor = new TextRailroad();
        $ast     = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "rule" . PHP_EOL .
            "------>--[identifier]-->---|" . PHP_EOL, $visitor->getText()
        );

        $builder->clear()
                ->syntax("foobar")
                ->rule("rule")
                    ->terminal("terminal")
                ->end();

        $ast     = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "rule" . PHP_EOL .
            "------>--(terminal)-->---|" . PHP_EOL, $visitor->getText()
        );

        $this->markTestIncomplete();
    }
}
