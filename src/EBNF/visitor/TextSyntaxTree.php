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
 * @package visitor
 */

namespace de\weltraumschaf\ebnf\visitor;

/**
 * @see Visitor
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Visitor.php';

use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Composite;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;

/**
 * Generates an ASCII formatted tree of the visited AST {@link Syntax} node.
 *
 * Example:
 *
 * The file <kbf>tests/fixtures/rules_with_literals.ebnf</kbd> will produce
 *
 * <pre>
 * [syntax]
 *  +--[rule='literal']
 *      +--[choice]
 *          +--[sequence]
 *          |   +--[terminal=''']
 *          |   +--[identifier='character']
 *          |   +--[loop]
 *          |   |   +--[identifier='character']
 *          |   +--[terminal=''']
 *          +--[sequence]
 *              +--[terminal='"']
 *              +--[identifier='character']
 *              +--[loop]
 *              |   +--[identifier='character']
 *              +--[terminal='"']
 * </pre>
 *
 * For generating the tree lines a two dimenasional array is used as
 * "render" matrix. The text is lazy computed by comning the array filed
 * column by column and row by row.
 *
 * @package visitor
 */
class TextSyntaxTree implements Visitor {

    /**
     * The formatted ASCII text.
     *
     * Lazy computed.
     *
     * @var string
     */
    private $text;
    /**
     * Depth of the visited tree. Asked in before visit from {@link Syntax} node.
     *
     * @var int
     */
    private $depth;
    /**
     * The indention level in the matrix.
     *
     * @var int
     */
    private $level = 0;
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

    /**
     * Returns the depth.
     *
     * @return int
     */
    public function getDepth() {
        return $this->depth;
    }

    /**
     * Formats nodes two strings.
     *
     * {@link Rule}, {@link Terminal} ans {@link Identifier} nodes will be
     * rendered with their attributes name or value.
     *
     * @param Node $n Formatted node.
     *
     * @return string
     */
    public static function formatNode(Node $n) {
        $text = "[{$n->getNodeName()}";

        if ($n instanceof Rule && ! empty($n->name)) {
                $text .= "='{$n->name}'";
        } else if ($n instanceof Terminal || $n instanceof Identifier) {
            if ( ! empty($n->value)) {
                $text .= "='{$n->value}'";
            }
        }

        $text .= "]";
        return $text;
    }

    /**
     * Returns an array of $colCount empty strings as elements.
     *
     * @param int $colCount Count of colums with empty strings.
     *
     * @return array
     */
    public static function createRow($colCount) {
        $colCount = max(0, (int) $colCount);
        $row      = array();

        for ($i = 0; $i < $colCount; $i++) {
            $row[] = "";
        }

        return $row;
    }

    /**
     * If as {@link Syntax} node comes around the visitor will be initializez.
     * Which means that the depth property is read, the matrix and level properties
     * will be initialized. All other {@link Node} types increment the level property.
     *
     * @param Node $visitable Visited node.
     *
     * @return void
     */
    public function beforeVisit(Node $visitable) {
        if ($visitable instanceof Syntax) {
            $this->depth  = $visitable->depth();
            $this->matrix = array();
            $this->level  = 0;
        } else {
            $this->level++;
        }

        // While we're visiting the output will change anyway.
        $this->text = null;
    }

    /**
     * Genertates the string contents  in the row of the visited node.
     *
     * @param Node $visitable Visited node.
     *
     * @return void
     */
    public function visit(Node $visitable) {
        $row = self::createRow($this->depth);

        if ($this->level > 0) {
            for ($i = 0 ; $i < $this->level - 1; $i++) {
                $row[$i] = "    ";
            }

            $row[$this->level - 1] = " +--";
            $row[$this->level]     = self::formatNode($visitable);
        }

        $row[$this->level] = self::formatNode($visitable);
        $this->matrix[]    = $row;
    }

    /**
     * Also "climbs" all rows in the current level and sets a "|" to parent nodes
     * id appropriate.
     *
     * Ans decrements the level until it reaches 0.
     *
     * @param Node $visitable visited node.
     *
     * @return void
     */
    public function afterVisit(Node $visitable) {
        $rowCnt = count($this->matrix);

        for ($i = $rowCnt - 1; $i > -1; $i--) {
            if ($this->matrix[$i][$this->level] === " +--" || $this->matrix[$i][$this->level] === " |  ") {
                if ($this->matrix[$i - 1][$this->level] === "    ") {
                    $this->matrix[$i - 1][$this->level] = " |  ";
                }
            }
        }

        $this->level = max(0, $this->level - 1);
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
