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
 * @author Vincent Tscherter <tscherter@karmin.ch>
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf;

/**
 * Represents a scanned EBNF token with its type, value and position in the source file.
 *
 * A token is a imutable value object.
 */
class Token {
    const OPERATOR   = 1;
    const LITERAL    = 2;
    const COMMENT    = 3;
    const IDENTIFIER = 4;
    const EOF        = 5;

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
     * @param string   $type
     * @param int      $value
     * @param Position $position
     */
    public function __construct($type, $value, Position $position) {
        $this->type     = (int)$type;
        $this->value    = (string)$value;
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
     * @return string
     */
    public function getValue() {
        return $this->value;
    }

    /**
     * Returns the start position of the token string in the source.
     *
     * @return Position
     */
    public function getPosition() {
        return $this->position;
    }

    /**
     * Wheter it is of the type or not.
     *
     * @param int $type
     * @return bool
     */
    public function isType($type) {
        return $this->getType() === (int)$type;
    }

    /**
     * Wheter the token value is equal to the passed string.
     *
     * @param string $string
     * @return bool
     */
    public function isEqual($string) {
        return $this->getValue() === (string)$string;
    }

    /**
     * Wheter the token value is NOT equal to the passed string.
     *
     * @param string $string
     * @return bool
     */
    public function isNotEqual($string) {
        return !$this->isEqual($string);
    }

    /**
     * Human readable string representation.
     *
     * @return string
     */
    public function __toString() {
        $str = "<";

        if ("" !== $this->getValue()) {
            $str .= "'{$this->getValue()}', ";
        }

        $str .= "{$this->getTypeAsString()}, {$this->getPosition()}>";
        return $str;
    }

}
