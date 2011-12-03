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
 * @see SyntaxBuilder
 */
require_once 'ast/builder/SyntaxBuilder.php';
require_once 'visitor//Visitor.php';
require_once 'visitor//TextSyntaxTree.php';

use de\weltraumschaf\ebnf\ast\builder\SyntaxBuilder;
use de\weltraumschaf\ebnf\ast\Node;

class TextRailroad implements Visitor {

    /**
     * The formatted ASCII text.
     *
     * Lazy computed.
     *
     * @var string
     */
    private $text;
    /**
     * The matrix.
     *
     * Two dimenaionals array. Initialized on visiting a {@link Syntax} node.
     * So it is important that the syntax node is the root node of the tree.
     * The matrix grows row by row by visiting each child node. A child node
     * represents a row.
     *
     * @var array
     */
    private $matrix = array();

    /**
     * Returns the two dimensional matrix.
     *
     * @return array
     */
    public function getMatrix() {
        return $this->matrix;
    }

    public function beforeVisit(Node $visitable) {
        if ($visitable instanceof Syntax) {
            $this->matrix = array();
        }

        // While we're visiting the output will change anyway.
        $this->text = null;
    }

    public function visit(Node $visitable) {

    }

    public function afterVisit(Node $visitable) {

    }

    /**
     * Concatenates the matrix columns and rows adn returns the ASCII formatted text.
     *
     * After all visiting is done this method only generates the string once and memizes
     * the result.
     *
     * @return string
     */
    public function getText() {
        if (null === $this->text) {
            $buffer = "";

            foreach ($this->matrix as $row) {
                $buffer .= implode("", $row) . PHP_EOL;
            }

            $this->text = $buffer;
        }
        return $this->text;
    }
}

/**
 * Test for {@link TextSyntaxTree}.
 *
 * @package tests
 * @version @@version@@
 */
class TextRailroadTest extends \PHPUnit_Framework_TestCase {

    public function testGenerateMatrix() {

    }

    public function testGetText() {
//        $this->markTestIncomplete();
        $builder = new SyntaxBuilder();
        $builder->syntax("foobar")
                ->rule("rule")
                    ->identifier("identifier");

//        $visitor = new TextRailroad();
        $visitor = new TextSyntaxTree();
        $ast     = $builder->getAst();
        $ast->accept($visitor);
        $this->assertEquals(
            "rule" . PHP_EOL .
            "---------->-[identifier]->---|" . PHP_EOL, $visitor->getText()
        );
    }
}
