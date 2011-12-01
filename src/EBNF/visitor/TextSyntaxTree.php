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
 * @package visitor
 */
class TextSyntaxTree implements Visitor {

    const DEFAULT_INDENTATION = 4;

    private $text = "";
    private $indentationLevel = 0;

    private $pipes = array();

    private function indent() {
        $return = str_repeat(" ", max(0, $this->indentationLevel * self::DEFAULT_INDENTATION));

        return $return;
    }

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

    public function beforeVisit(Node $visitable) {
        if ($visitable instanceof Syntax || $visitable instanceof Rule) {
            return;
        }

        $this->indentationLevel++;
    }

    public function visit(Node $visitable) {
        if ( ! $visitable instanceof Syntax) {
            $text = $this->indent();

//            foreach ($this->pipes as $index => $isShowed) {
//                if ($isShowed) {
//                    $text[$index] = "|";
//                }
//            }

            $this->text .= "{$text} +--";
        }

        $this->text .= self::formatNode($visitable) . PHP_EOL;

        if ($visitable instanceof Composite && $visitable->countChildren() > 1) {
            $pipe = ($this->indentationLevel + 1) * self::DEFAULT_INDENTATION + 1;
            $this->pipes[$pipe] = true;
        }
    }

    public function afterVisit(Node $visitable) {
        if ($visitable instanceof Composite && $visitable->countChildren() > 1) {
            $pipe = ($this->indentationLevel + 1) * self::DEFAULT_INDENTATION + 1;
            $this->pipes[$pipe] = false ;
        }

        if ($visitable instanceof Syntax) {
            return;
        }

        $this->indentationLevel--;
    }

    public function getText() {
        return $this->text;
    }

}
