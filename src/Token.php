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
