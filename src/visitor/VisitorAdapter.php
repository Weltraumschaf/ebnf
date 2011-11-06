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
 * @author Vincent Tscherter <tscherter@karmin.ch>
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf\visitor;

require_once __DIR__. DIRECTORY_SEPARATOR . "Visitor.php";

use de\weltraumschaf\ebnf\ast\Identifier as Identifier;
use de\weltraumschaf\ebnf\ast\Loop       as Loop;
use de\weltraumschaf\ebnf\ast\Node       as Node;
use de\weltraumschaf\ebnf\ast\Option     as Option;
use de\weltraumschaf\ebnf\ast\Rule       as Rule;
use de\weltraumschaf\ebnf\ast\Sequence   as Sequence;
use de\weltraumschaf\ebnf\ast\Syntax     as Syntax;
use de\weltraumschaf\ebnf\ast\Terminal   as Terminal;
use \InvalidArgumentException            as InvalidArgumentException;

/**
 * Abstract adapbter visitor which provides template methods 
 * for all visitable node types.
 */
abstract class VisitorAdapter implements Visitor {
    
    /**
     * Implements generic visitor interface.
     * 
     * @param Node $visitable 
     * 
     * @return void
     */
    final public function visit(Node $visitable) {
        $this->beforeVisit($visitable);
        
        if ($visitable instanceof Identifier) {
            $this->visitIdentifier($visitable);
        } else if ($visitable instanceof Loop) {
            $this->visitLoop($visitable);
        } else if ($visitable instanceof Option) {
            $this->visitOption($visitable);
        } else if ($visitable instanceof Rule) {
            $this->visitRule($visitable);
        } else if ($visitable instanceof Sequence) {
            $this->visitSequence($visitable);
        } else if ($visitable instanceof Syntax) {
            $this->visitSyntax($visitable);
        } else if ($visitable instanceof Terminal) {
            $this->visitTerminal($visitable);
        } else {
            throw new InvalidArgumentException(
                "Unsupportd node: " . get_class($visitable) . "!"
            );
        }
        
        $this->afterVisit($visitable);
    }

    /**
     * Templeta method to hook in before specific node vsitor method 
     * will be invoked.
     * 
     * @param Node $visitable 
     */
    protected function beforeVisit(Node $visitable) {}
    
    /**
     * Templeta method to hook in after specific node vsitor method 
     * will be invoked.
     * 
     * @param Node $visitable 
     */
    protected function afterVisit(Node $visitable) {}

    /**
     * Templeta method to visit an {Identifier} node.
     * 
     * @param Identifier $identifier 
     * 
     * @return void
     */
    protected function visitIdentifier(Identifier $identifier) {}
    
    /**
     * Templeta method to visit an {Loop} node.
     * 
     * @param Loop $loop 
     * 
     * @return void
     */
    protected function visitLoop(Loop $loop) {}
    
    /**
     * Templeta method to visit an {Option} node.
     * 
     * @param Option $option 
     * 
     * @return void
     */
    protected function visitOption(Option $option) {}
    
    /**
     * Templeta method to visit an {Rule} node.
     * 
     * @param Rule $rule 
     * 
     * @return void
     */
    protected function visitRule(Rule $rule) {}
    
    /**
     * Templeta method to visit an {Sequence} node.
     * 
     * @param Sequence $sequence 
     * 
     * @return void
     */
    protected function visitSequence(Sequence $sequence) {}
    
    /**
     * Templeta method to visit an {Syntax} node.
     * 
     * @param Syntax $syntax 
     *
     * @return void
     */
    protected function visitSyntax(Syntax $syntax) {}
    
    /**
     * Templeta method to visit an {Terminal} node.
     * 
     * @param Terminal $terminal 
     * 
     * @return void
     */
    protected function visitTerminal(Terminal $terminal) {}
    
}
