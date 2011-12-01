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
require_once 'visitor/TextSyntaxTree.php';

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

/**
 * Test for {@link TextSyntaxTree}.
 *
 * @package tests
 */
class TextSyntaxTreeTest extends \PHPUnit_Framework_TestCase {

    public function testGenerateText() {
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
        $this->markTestIncomplete();
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
