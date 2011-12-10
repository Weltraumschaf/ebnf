<?php

namespace de\weltraumschaf\ebnf\ast;

use \IteratorAggregate;

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