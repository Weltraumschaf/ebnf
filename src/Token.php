<?php

namespace Weltraumschaf\Ebnf;

/**
 * Description of Token
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @license http://www.weltraumschaf.de/the-beer-ware-license.txt THE BEER-WARE LICENSE
 */
class Token {
    const OPERATOR   = 1;
    const LITERAL    = 2;
    const WHITESPACE = 3;
    const IDENTIFIER = 4;

    /**
     * @var int
     */
    private $type;
    /**
     * @var string
     */
    private $value;
    /**
     * @var Position
     */
    private $position;

    private static $map = array(
        self::OPERATOR   => "OPERATOR",
        self::LITERAL    => "LITERAL",
        self::WHITESPACE => "WHITESPACE",
        self::IDENTIFIER => "IDENTIFIER"
    );

    public function __construct($type, $value, Position $position) {
        $this->type     = (int)$type;
        $this->value    = (string)$value;
        $this->position = $position;
    }

    public function getType() {
        return $this->type;
    }

    public function getValue() {
        return $this->value;
    }

    public function getPosition() {
        return $this->position;
    }

    public function getTypeAsString() {
        if (isset(self::$map[$this->getType()])) {
            return self::$map[$this->getType()];
        }

        return "({$this->getType()})";
    }

    public function __toString() {
        return "<{$this->getValue()}, {$this->getTypeAsString()}, {$this->getPosition()}>";
    }

}
