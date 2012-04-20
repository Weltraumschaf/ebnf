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

/**
 * @see Node
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Node.php';

/**
 * Abstract representation of AST nodes which are not the root
 * node {@link Syntax} but any other kind of {@link Node}.
 *
 * @package ast
 * @version @@version@@
 */
abstract class AbstractNode implements Node {

    /**
     * The direct ancestor int the AST tree.
     *
     * @var Node
     */
    private $parent;

    /**
     * Initializes the node with it's parent. This is imutable.
     *
     * May the node itself change, it is not possible to set the
     * reference to the parent node.
     *
     * @param Node $parent Ancestor node.
     */
    public function __construct(Node $parent) {
        $this->parent = $parent;
    }

    /**
     * Returns the parent node.
     *
     * @return Node
     */
    public function getParent() {
        return $this->parent;
    }
}