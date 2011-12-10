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
 * @package ast
 */

namespace de\weltraumschaf\ebnf\ast;

use \IteratorAggregate;

/**
 * Represents an object in the AST model which can have
 * some child {@link Node nodes}.
 *
 * @package ast
 * @version @@version@@
 */
interface Composite extends IteratorAggregate {

    /**
     * Count of direct children nodes.
     *
     * @return int
     */
    public function countChildren();

    /**
     * Whether the node has direct child nodes or not.
     *
     * @return bool
     */
    public function hasChildren();

    /**
     * Append a child {@link Node} to the list of children.
     *
     * @param Node $child Child node to add.
     *
     * @return void
     */
    public function addChild(Node $child);
    
}