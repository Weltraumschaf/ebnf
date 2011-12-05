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
        $n = new Terminal();
        $n->value = "term";
        $this->assertEquals("-(term)-", TextRailroad::formatNode($n));
        $n = new Identifier();
        $n->value = "ident";
        $this->assertEquals("-[ident]-", TextRailroad::formatNode($n));
        $n = new Rule();
        $n->name = "foobar";
        $this->assertEquals("foobar", TextRailroad::formatNode($n));

        foreach (array(
            new Choice(), new Loop(), new Option(), new Sequence(), new Syntax()
        ) as $n) {
            $this->assertEquals("", TextRailroad::formatNode($n));
        }
        $this->markTestIncomplete("all other nodes.");
    }

    public function testGenerateMatrix() {
        $builder = new SyntaxBuilder();
        $builder->syntax("foobar")
                ->rule("rule")
                    ->identifier("identifier")
                ->end();
        $visitor = new TextRailroad();
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
        $ast     = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(array(
            array("rule"),
            array("-----", "->-", "-(terminal)-", "->-", "--|")
        ), $visitor->getMatrix());

        $this->markTestIncomplete();
    }

    public function testGetText() {
        $this->markTestIncomplete();
        $builder = new SyntaxBuilder();
        $builder->syntax("foobar")
                ->rule("rule")
                    ->identifier("identifier");

        $visitor = new TextRailroad();
        $ast     = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "rule" . PHP_EOL .
            "---------->-[identifier]->---|" . PHP_EOL, $visitor->getText()
        );
    }
}
