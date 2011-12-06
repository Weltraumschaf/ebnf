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

require_once __DIR__ . DIRECTORY_SEPARATOR .  'Visitor.php';

use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Composite;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;

class TextRailroad implements Visitor {

    const LINE = "-";
    const END  = "--|";
    const LTR  = "->-";
    const RTL  = "-<-";
    const FORK = "-+-";

    const PARAM_TOKEN = "@param@";

    const IDENT = "-[@param@]-";
    const TERM  = "-(@param@)-";
    const RULE  = "@param@";

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
    private $currentRow;
    private $currentCol;

    /**
     * Returns the two dimensional matrix.
     *
     * @return array
     */
    public function getMatrix() {
        return $this->matrix;
    }

    public static function renderStringTemplate($t, $param = "") {
        return str_replace(self::PARAM_TOKEN, (string) $param, (string) $t);
    }

    public static function formatNode(Node $n) {
        $t = $p = "";

        if ($n instanceof Terminal) {
            $t= self::TERM;
            $p = $n->value;
        } else if ($n instanceof Identifier) {
            $t= self::IDENT;
            $p = $n->value;
        } else if ($n instanceof Rule) {
            $t= self::RULE;
            $p = $n->name;;
        }

        return self::renderStringTemplate($t, $p);
    }

    public function beforeVisit(Node $visitable) {
        if ($visitable instanceof Syntax) {
            $this->matrix = array();
            $this->currentRow = 0;
            $this->currentCol = 0;
        }

        // While we're visiting the output will change anyway.
        $this->text = null;
    }

    public function visit(Node $visitable) {
        if ($visitable instanceof Rule) {
            $this->matrix[] = array($visitable->name);
            $this->matrix[] = array(str_repeat("-", strlen($visitable->name) + 1), self::LTR);
        } else if ($visitable instanceof Identifier || $visitable instanceof Terminal) {
            $this->matrix[count($this->matrix) - 1][] = self::formatNode($visitable);
            $this->matrix[count($this->matrix) - 1][] = self::LTR;
        }

    }

    public function afterVisit(Node $visitable) {
        if ($visitable instanceof Rule) {
            $this->matrix[count($this->matrix) - 1][] = self::END;
        }
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