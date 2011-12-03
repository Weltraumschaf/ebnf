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

//@codingStandardsIgnoreFile

namespace de\weltraumschaf\ebnf\visitor;

/**
 * @see Visitor
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Visitor.php';

use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Terminal;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Rule;
use \de\weltraumschaf\ebnf\ast\Composite;

/**
 * @package visitor
 */
class GraphvizSyntaxTree implements Visitor {

    private $file;
    private $currentNumber;
    private $nodeFormatDecl;
    private $leafFormatDecl;
    private $nodeDecl;
    private $edgeDecl;
    private $idNodeMap;
    private $firstInvoker;

    public function __construct($file) {
        $this->file           = (string) $file;
        $this->currentNumber  = 0;
        $this->nodeFormatDecl = "    node [shape=ellipse];";
        $this->leafFormatDecl = "    node [shape=record];";
        $this->nodeDecl       = "";
        $this->edgeDecl       = "";
        $this->idNodeMap      = array();
    }

    public function beforeVisit(Node $visitable) {
        if (null === $this->firstInvoker) {
            $this->firstInvoker = $visitable;
        }
    }

    public function visit(Node $visitable) {
        // node format
        if ($visitable instanceof Terminal || $visitable instanceof Identifier) {
            $this->leafFormatDecl .= " n{$this->currentNumber};";
        } else {
            $this->nodeFormatDecl  .= " n{$this->currentNumber};";
        }

        // node decl
        if ($visitable instanceof Terminal || $visitable instanceof Identifier) {
            $label = "{{$visitable->getNodeName()}|" . addcslashes($visitable->value, '"{}|') ."}";
        } else if ($visitable instanceof Rule) {
            $label = "{{$visitable->getNodeName()}|{$visitable->name}}";
        } else {
            $label = $visitable->getNodeName();
        }

        $this->nodeDecl .= "    n{$this->currentNumber} [label=\"{$label}\"];" . PHP_EOL;
        // collect for edge decl
        $this->idNodeMap[$this->currentNumber] = $visitable;
        // next
        $this->currentNumber++;
    }

    public function afterVisit(Node $visitable) {
        if ($this->firstInvoker !== $visitable) {
            return;
        }

        foreach ($this->idNodeMap as $id => $node) {
            if ($node instanceof Composite && $node->hasChildren()) {
                $children = $node->getIterator();
                $buffer    = "    n{$id} -> ";
                $childDecl = array();

                foreach ($children as $child) {
                    $childDecl[] = "n{$this->lookpUpChildNodeIndex($child)}";
                }

                if (count($childDecl) === 1) {
                    $buffer .= $childDecl[0];
                } else {
                    $buffer .= "{ " . implode(" ", $childDecl) . " }";
                }

                $buffer .= ";";
                $this->edgeDecl .= $buffer . PHP_EOL;
            }
        }
    }

    private function lookpUpChildNodeIndex($childNode) {
        foreach ($this->idNodeMap as $index => $node) {
            if ($childNode === $node) {
                return $index;
            }
        }

        return -1;
    }

    private function generatePreambel($file) {
        $preambel  = "digraph \"{$file}\" {" . PHP_EOL;
        $preambel .= '    size        = "8,8";' . PHP_EOL;
        $preambel .= '    ordering    = out;' . PHP_EOL;
        return $preambel;
    }

    public function getDotString() {
        return $this->generatePreambel($this->file) . PHP_EOL .
               $this->leafFormatDecl . PHP_EOL .
               $this->nodeFormatDecl . PHP_EOL . PHP_EOL .
               $this->nodeDecl . PHP_EOL .
               $this->edgeDecl .
               "}";
    }
}
