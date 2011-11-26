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
 * @see Node
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Node.php';
/**
 * @see Type
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Type.php';

/**
 * Syntax node.
 *
 * The root of the AST.
 *
 * @package ast
 * @version @@version@@
 */
class Syntax extends Composite implements Node {

    /**
     * Title literal of string.
     *
     * @var string
     */
    public $title = "";
    /**
     * Meta literal of string.
     *
     * @var string
     */
    public $meta = "";

    /**
     * Returns the name of a node.
     *
     * @return string
     */
    public function getNodeName() {
        return Type::SYNTAX;
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         $other  Node to compare against.
     * @param Notification $result Object which collects all equivlanece violations.
     *
     * @return void
     */
    public function probeEquivalence(Node $other, Notification $result) {
        if ( ! $other instanceof Syntax) {
            $result->error(
                "Probed node types mismatch: '%s' != '%s'!",
                get_class($this),
                get_class($other)
            );
            return;
        }

        if ($this->title !== $other->title) {
            $result->error("Titles of syntx differs: '%s' != '%s'!", $this->title, $other->title);
        }

        if ($this->meta !== $other->meta) {
            $result->error("Meta of syntx differs: '%s' != '%s'!", $this->meta, $other->meta);
        }

        parent::probeEquivalence($other, $result);
    }

}