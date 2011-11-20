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
 * @see Type
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Type.php';

use de\weltraumschaf\ebnf\visitor\Visitor as Visitor;

/**
 * Identifier node.
 *
 * Has no subnodes.
 *
 * @package ast
 */
class Identifier implements Node {

    /**
     * The literal string.
     *
     * @var string
     */
    public $value = "";

    /**
     * Returns the name of a node.
     *
     * @return string
     */
    public function getNodeName() {
        return Type::IDENTIFIER;
    }

    /**
     * Defines method to accept {@link Visitors}.
     *
     * Imlements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor $visitor Object which visits te node.
     *
     * @return void
     */
    public function accept(Visitor $visitor) {
        $visitor->beforeVisit($this);
        $visitor->visit($this);
        $visitor->afterVisit($this);
    }

    protected function probeEquivalenceInternal(Node $other, Notification $result) {
        if ( ! $other instanceof Identifier) {
            $result->error("Probed node types mismatch: '%s' != '%s'!", get_class($this), get_class($other));
            return;
        }

        if ($this->value !== $other->value) {
            $this->error("Identifier value mismatch: %s != %s!", $this->value, $other->value);
        }
    }
}