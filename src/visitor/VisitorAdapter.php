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
 * 
 * @package visitor
 */
abstract class VisitorAdapter implements Visitor {
    
    /**
     * Implements generic visitor interface.
     * 
     * Delegates to {@link Node} specific visitor methods.
     * 
     * @param Node $visitable Visited node.
     * 
     * @return void
     */
    final public function visit(Node $visitable) {
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
    }

    /**
     * Template method to hook into before the main {@link visit()} method is called.
     * 
     * {@link Composite} nodes are responsible to call this method.
     * 
     * @param Node $visitable Visited node.
     * 
     * @return void
     */
    public function afterVisit(Node $visitable) {
        return null;
    }

    /**
     * Template method to hook into after the main {@link visit()} method is called.
     * 
     * {@link Composite} nodes are responsible to call this method.
     * 
     * @param Node $visitable Visited node.
     * 
     * @return void
     */
    public function beforeVisit(Node $visitable) {
        return null;
    }

    /**
     * Template method to visit an {@link Identifier} node.
     * 
     * @param Identifier $identifier Visited {@link Identifier} node.
     * 
     * @return void
     */
    protected function visitIdentifier(Identifier $identifier) {
        return null;
    }
    
    /**
     * Template method to visit an {@link Loop} node.
     * 
     * @param Loop $loop Visited {@link Loop} node.
     * 
     * @return void
     */
    protected function visitLoop(Loop $loop) {
        return null;
    }
    
    /**
     * Template method to visit an {@link Option} node.
     * 
     * @param Option $option Visited {@link Option} node.
     * 
     * @return void
     */
    protected function visitOption(Option $option) {
        return null;
    }
    
    /**
     * Template method to visit an {@link Rule} node.
     * 
     * @param Rule $rule Visited {@link Rule} node.
     * 
     * @return void
     */
    protected function visitRule(Rule $rule) {
        return null;
    }
    
    /**
     * Template method to visit an {@link Sequence} node.
     * 
     * @param Sequence $sequence Visited {@link Sequence} node.
     * 
     * @return void
     */
    protected function visitSequence(Sequence $sequence) {
        return null;
    }
    
    /**
     * Template method to visit an {@link Syntax} node.
     * 
     * @param Syntax $syntax Visited {@link Syntax} node.
     *
     * @return void
     */
    protected function visitSyntax(Syntax $syntax) {
        return null;
    }
    
    /**
     * Template method to visit an {@link Terminal} node.
     * 
     * @param Terminal $terminal Visited {@link Terminal} node.
     * 
     * @return void
     */
    protected function visitTerminal(Terminal $terminal) {
        return null;
    }
    
}
