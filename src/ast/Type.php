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
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf\ast;

/**
 * Represents the type of an EBNF node type.
 * 
 * @package ebnf
 * @subpackage ast
 */
class Type {
    const CHOICE     = "choice";
    const IDENTIFIER = "identifier";
    const LOOP       = "loop";
    const OPTION     = "option";
    const RANGE      = "range";
    const RULE       = "rule";
    const SEQUENCE   = "sequence";
    const SYNTAX     = "syntax";
    const TERMINAL   = "terminal";
    
    /**
     * String representation.
     * 
     * @var string
     */
    private $type;
    
    /**
     * Inititializes imutabe object.
     * 
     * @param (string) $type 
     */
    public function __construct($type) {
        $this->type = (string)$type;
    }
    
    /**
     * Returns string representation.
     * 
     * @return string
     */
    public function __toString() {
        return $this->type;
    }

    /**
     * Compares with given string representation.
     *
     * @param string $type
     * 
     * @return bool
     */
    public function is($type) {
        return $this->type === (string)$type;
    }
}
