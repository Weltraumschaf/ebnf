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

use \de\weltraumschaf\ebnf\ast\Node;

/**
 * Defines interface for an AST tree visitor.
 *
 * Interfce for {@linkt http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
 *
 * @package visitor
 */
interface Visitor {

    /**
     * Template method to hook in before specific node vsitor method
     * will be invoked.
     *
     * @param Node $visitable Visied {@link Node}.
     *
     * @return void
     */
    public function beforeVisit(Node $visitable);

    /**
     * Generic visitor method called by a visited {@link Node}.
     *
     * @param Node $visitable Visied {@link Node}.
     *
     * @return void
     */
    public function visit(Node $visitable);

    /**
     * Template method to hook in after specific node vsitor method
     * will be invoked.
     *
     * @param Node $visitable Visied {@link Node}.
     *
     * @return void
     */
    public function afterVisit(Node $visitable);

}