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

use de\weltraumschaf\ebnf\visitor\Visitor;

/**
 * Interface of an AST node.
 *
 * @package ast
 */
interface Node {

    /**
     * Returns the name of a node.
     *
     * @return string
     */
    public function getNodeName();

    /**
     * Defines method to accept {@link Visitors}.
     *
     * Imlements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor $visitor Object which visits te node.
     *
     * @return void
     */
    public function accept(Visitor $visitor);

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         $other  Node to compare against.
     * @param Notification $result Object which collects all equivlanece violations.
     *
     * @return void
     */
    public function probeEquivalence(Node $other, Notification $result);
}
