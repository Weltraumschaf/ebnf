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

use \de\weltraumschaf\ebnf\ast\Node as Node;

/**
 * Defines interface for an AST tree visitor.
 * 
 * Interfce for {@linkt http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
 */
interface Visitor {
    
    /**
     * Templeta method to hook in before specific node vsitor method 
     * will be invoked.
     * 
     * @param Node $visitable 
     */
    public function beforeVisit(Node $visitable);
    
    /**
     * @param Node $visitable 
     */
    public function visit(Node $visitable);
    
    /**
     * Templeta method to hook in after specific node vsitor method 
     * will be invoked.
     * 
     * @param Node $visitable 
     */
    public function afterVisit(Node $visitable);

}