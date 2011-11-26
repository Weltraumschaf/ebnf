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
 * @author  Vincent Tscherter <tscherter@karmin.ch>
 * @author  Sven Strittmatter <ich@weltraumschaf.de>
 * @package ebnf
 */

namespace de\weltraumschaf\ebnf;

/**
 * @see ScannerHelper
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ScannerHelper.php';

/**
 * Represents a scanned EBNF token with its type, value and position in the source file.
 *
 * A token is a imutable value object.
 *
 * @package ebnf
 */
class Token {
    /**
     * @deprecated
     */
    const OPERATOR = 1;
    // Special type:
    const LITERAL    = 2;
    const COMMENT    = 3;
    const IDENTIFIER = 4;
    const EOF        = 5;
    // Operator types:
    /**
     * :== or : or =
     */
    const ASIGN = 11;
    /**
     * . or ;
     */
    const END_OF_RULE = 12;
    /**
     * (
     */
    const L_PAREN = 13;
    /**
     * [
     */
    const L_BRACK = 14;
    /**
     * {
     */
    const L_BRACE = 15;
    /**
     * )
     */
    const R_PAREN = 16;
    /**
     * ]
     */
    const R_BRACK = 17;
    /**
     * }
     */
    const R_BRACE = 18;
    /**
     * ..
     */
    const RANGE = 19;
    /**
     * |
     */
    const CHOICE = 20;

    /**
     * One of the class constants.
     *
     * @var int
     */
    private $type;

    /**
     * The literal string.
     *
     * @var string
     */
    private $value;

    /**
     * Start position in source.
     *
     * @var Position
     */
    private $position;

    /**
     * Maps int type to human readable type.
     *
     * @var array
     */
    private static $map = array(
        self::OPERATOR   => "OPERATOR",
        self::LITERAL    => "LITERAL",
        self::COMMENT    => "COMMENT",
        self::IDENTIFIER => "IDENTIFIER",
        self::EOF        => "EOF"
    );

    /**
     * Initializes the imutable object.
     *
     * @param int      $type     Type of token. One of the class constants.
     * @param string   $value    The scanned token string.
     * @param Position $position The start position of scanned token.
     */
    public function __construct($type, $value, Position $position) {
        $this->type     = (int) $type;
        $this->value    = (string) $value;
        $this->position = $position;
    }

    /**
     * Returns token type as string.
     *
     * @return int
     */
    public function getType() {
        return $this->type;
    }

    /**
     * Returns the token type as human redable string if its mapped.
     *
     * @return string
     */
    public function getTypeAsString() {
        if (isset(self::$map[$this->getType()])) {
            return self::$map[$this->getType()];
        }

        return "({$this->getType()})";
    }

    /**
     * Returns the scanned token string.
     *
     * @param bool $unquote Whether to unquote a literal value.
     *
     * @return string
     */
    public function getValue($unquote = false) {
        if ($unquote) {
            return self::unquoteString($this->value);
        }

        return $this->value;
    }

    /**
     * Checks if a passes string is encapsulated in quotes and removes them.
     * Also unescape inside quotes.
     *
     * @param string $str The string to unquote.
     *
     * @return string
     */
    public static function unquoteString($str) {
        $start  = 0;
        $length = strlen($str) - 1;

        if (ScannerHelper::isQuote($str[$start])) {
            $start++;
        }

        if (ScannerHelper::isQuote($str[$length])) {
            $length--;
        }

        $str = substr($str, $start, $length);
        return stripcslashes($str);
    }

    /**
     * Returns the start position of the token string in the source.
     *
     * @param bool $end If true the tokens endposition is returned.
     *
     * @return Position
     */
    public function getPosition($end = false) {
        if ($end) {
            return new Position(
                $this->position->getLine(),
                $this->position->getColumn() + strlen($this->getValue()),
                $this->position->getFile()
            );
        }

        return $this->position;
    }

    /**
     * Wheter it is of the type or not.
     *
     * @param int $type The token type to test against.
     *
     * @return bool
     */
    public function isType($type) {
        return $this->getType() === (int) $type;
    }

    /**
     * Wheter the token value is equal to the passed string.
     *
     * @param string $string The string to test against.
     *
     * @return bool
     */
    public function isEqual($string) {
        return $this->getValue() === (string) $string;
    }

    /**
     * Test if the token value is equal to one the passed strings.
     *
     * @param array $strings Array of strings.
     *
     * @return bool
     */
    public function isEquals(array $strings) {
        foreach ($strings as $string) {
            if ($this->isEqual($string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Wheter the token value is NOT equal to the passed string.
     *
     * @param string $string The string to test against.
     *
     * @return bool
     */
    public function isNotEqual($string) {
        return !$this->isEqual($string);
    }

    /**
     * Test if the token value is NOT equal to one the passed strings.
     *
     * @param array $strings Array of strings.
     *
     * @return bool
     */
    public function isNotEquals(array $strings) {
        return !$this->isEquals($strings);
    }

    /**
     * Human readable string representation.
     *
     * Token values longer than 15 characters are shorened.
     *
     * @return string
     */
    public function __toString() {
        $str = "<";

        if ("" !== $this->getValue()) {
            $value = $this->getValue();

            if (strlen($value) > 15) {
                $value = substr($value, 0, 15) . "...";
            }

            $str .= "'{$value}', ";
        }

        $str .= "{$this->getTypeAsString()}, {$this->getPosition()}>";
        return $str;
    }

    /**
     * Returns whether the token is of an operator type or not.
     *
     * @staticvar array $lookup
     *
     * @return bool
     */
    public function isOperator() {
        static $lookup = array(
            self::ASIGN, self::CHOICE, self::END_OF_RULE,
            self::RANGE,
            self::L_BRACE, self::L_BRACK, self::L_PAREN,
            self::R_BRACE, self::R_BRACK, self::R_PAREN
        );

        return in_array($this->getType(), $lookup);
    }
}
