<?php

namespace Weltraumschaf\Ebnf;

/**
 * Description of Exception
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @license http://www.weltraumschaf.de/the-beer-ware-license.txt THE BEER-WARE LICENSE
 */
class SyntaxtException extends \Exception {
    /**
     * Where in the source the exception occured.
     *
     * @var Position
     */
    private $position;

    /**
     *
     * @param string    $message
     * @param int       $code
     * @param Position  $pos
     * @param Exception $previous
     */
    public function __construct($message, Position $pos, $code = 0, Exception $previous = null) {
        parent::__construct($message, $code, $previous);
        $this->position = $pos;
    }

    /**
     * @return Position
     */
    public function getPosition() {
        return $this->position;
    }

    public function __toString() {
        return "Error: {$this->getMessage()} at {$this->getPosition()} ({$this->getCode()})!";
    }

}
