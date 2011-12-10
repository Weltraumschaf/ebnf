<?php

namespace de\weltraumschaf\ebnf\ast;

require_once __DIR__ . DIRECTORY_SEPARATOR . 'Node.php';

abstract class AbstractNode implements Node {
    /**
     *
     * @var Node
     */
    private $parent;

    public function __construct(Node $parent) {
        $this->parent = $parent;
    }

    public function hasParent() {
        return null !== $this->parent;
    }

    public function getParent() {
        return $this->parent;
    }
}