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
use \de\weltraumschaf\ebnf\ast\Choice;
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

    private function addRow() {
        $this->currentRow++;
        $this->matrix[$this->currentRow] = array();
        $this->currentCol = 0;
    }

    public function addCol() {
        $cols = func_get_args();

        if ( ! empty($cols)) {
            foreach ($cols as $col) {
                $this->matrix[$this->currentRow][$this->currentCol] = $col;
                $this->currentCol++;
            }
        }
    }

    public function beforeVisit(Node $visitable) {
//        echo "before {$visitable->getNodeName()}\n";
        if ($visitable instanceof Syntax) {
            $this->matrix     = array();
            $this->currentRow = -1;
        }

        // While we're visiting the output will change anyway.
        $this->text = null;
    }

    public function getElement($col, $row = null) {
        if (null === $row) {
            $row = $this->currentRow;
        }

        return $this->matrix[$row][$col];
    }

    public function setElement($val, $col, $row = null) {
        if (null === $row) {
            $row = $this->currentRow;
        }

        $this->matrix[$row][$col] = $val;
    }

    public function visit(Node $visitable) {
//        echo "{$visitable->getNodeName()}\n";
        if ($visitable instanceof Rule) {
            $this->addRow();
            $this->addCol(self::formatNode($visitable));
            $this->addRow();
            $this->addCol(str_repeat("-", strlen($visitable->name) + 1));
        } else if ($visitable instanceof Choice) {
            $this->addCol(self::FORK);
        } else if ($visitable instanceof Loop) {
            $this->addCol(self::FORK);
        } else if ($visitable instanceof Identifier || $visitable instanceof Terminal) {
            if ($this->getElement($this->currentCol - 1) !== self::FORK) {
                $this->addCol(self::LTR);
            }

            $this->addCol(self::formatNode($visitable));
        }

    }

    public function afterVisit(Node $visitable) {
//        echo "after {$visitable->getNodeName()}\n";
        if ($visitable instanceof Rule) {
            $this->addCol(self::LTR, self::END);
        } else if ($visitable instanceof Choice) {
            $this->addCol(self::FORK);
            $this->addRow();
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

    /**
     * Returns the two dimensional matrix.
     *
     * @return array
     */
    public function getMatrix() {
        return $this->matrix;
    }
}