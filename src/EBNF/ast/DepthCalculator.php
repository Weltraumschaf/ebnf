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
 * @see Composite
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Composite.php';

/**
 * Encapsulates the algorithm to calculate the depth of an {@link Composite} node.
 *
 * @package ast
 * @version @@version@@
 */
class DepthCalculator {

    /**
     * The subject to calculate for.
     *
     * @var Composite
     */
    private $node;

    /**
     * Initializes the imutable object.
     *
     * @param Composite $node Calculation subject.
     */
    public function __construct(Composite $node) {
        $this->node = $node;
    }

    /**
     * Calculates the depth on each call.
     *
     * It will return at least 1 if the subject node as no children.
     *
     * @return int
     */
    public function depth() {
        if ( ! $this->node->hasChildren()) {
            return 1;
        }

        $depths = array();

        foreach ($this->node->getIterator() as $subnode) {
            $depths[] = $subnode->depth();
        }

        return max($depths) + 1;
    }

}