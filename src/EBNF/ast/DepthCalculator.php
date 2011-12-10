<?php

namespace de\weltraumschaf\ebnf\ast;

/**
 * @see Composite
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Composite.php';

class DepthCalculator {
    /**
     * @var Composite
     */
    private $node;

    public function __construct(Composite $node) {
        $this->node = $node;
    }

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