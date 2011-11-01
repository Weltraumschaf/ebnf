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
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf\visitor;

require_once __DIR__. DIRECTORY_SEPARATOR . "Visitor.php";

use de\weltraumschaf\ebnf\ast\Node as Node;
use de\weltraumschaf\ebnf\ast\Syntax as Syntax;
use de\weltraumschaf\ebnf\ast\Rule as Rule;

/**
 * Implements an AST tree vsitor for testing purposes.
 * 
 */
class Tester implements Visitor {
    
    /**
     * @var array
     */
    private $representative = array();
    
    public function getRepresentative() {
        return $this->representative;
    }

    public function visit(Node $visitable) {
        if ($visitable instanceof Syntax) {
            $this->visitSyntax($visitable);
        } else if ($visitable instanceof Rule) {
            $this->visitRule($visitable);
        } else {
            throw new \InvalidArgumentException("Unsupportd visitable: " . get_class($visitable));
        }
    }

    private function visitSyntax(Syntax $syntax) {
        $this->representative[$syntax->getNodeName()] = array(
            "meta"  => $syntax->meta,
            "title" => $syntax->title,
            "rules" => array()
        );
    }
    
    private function visitRule(Rule $rule) {
        if (!isset($this->representative["syntax"])) {
            throw new \Exception("Does not visited a syntax node before vsiting rule!");
        }
        
        $this->representative["syntax"]["rules"][$rule->name] = array();
    }
    
    public function assert(array $expected) {
        
    }

}