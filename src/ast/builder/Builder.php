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

namespace de\weltraumschaf\ebnf\ast\builder;

/**
 * @see Choice
 */
require_once 'ast/Choice.php';
/**
 * @see Composite
 */
require_once 'ast/Composite.php';
/**
 * @see Identifier
 */
require_once 'ast/Identifier.php';
/**
 * @see Loop
 */
require_once 'ast/Loop.php';
/**
 * @see Node
 */
require_once 'ast/Node.php';
/**
 * @see Option
 */
require_once 'ast/Option.php';
/**
 * @see Sequence
 */
require_once 'ast/Sequence.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';

use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Composite;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Terminal;

/**
 *
 */
class Builder {

    /**
     * @var Composite
     */
    protected $node;

    /**
     * @var Builder
     */
    protected $parent;

    public function __construct(Composite $node, Builder $parent) {
        $this->node   = $node;
        $this->parent = $parent;
    }

    public function end() {
        return $this->parent;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->node->addChild($t);
        return $this;
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->node->addChild($i);
        return $this;
    }

    public function sequence() {
        $seq = new Sequence();
        $this->node->addChild($seq);
        return new Builder($seq, $this);
    }

    public function option() {
        $option = new Option();
        $this->node->addChild($option);
        return new Builder($option, $this);
    }

    public function choice() {
        $choice = new Choice();
        $this->node->addChild($choice);
        return new Builder($choice, $this);
    }

    public function loop() {
        $loop = new Loop();
        $this->node->addChild($loop);
        return new Builder($loop, $this);
    }
}