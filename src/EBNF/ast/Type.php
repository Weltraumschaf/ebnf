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

namespace de\weltraumschaf\ebnf\ast;

/**
 * Represents the type of an EBNF node type.
 * 
 * @package ast
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
     * Inititializes imutable object usualy with one of the class constants.
     * 
     * @param string $type The string representation of ths type.
     */
    public function __construct($type) {
        $this->type = (string) $type;
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
     * @param string $type Usualy one of the class constants.
     * 
     * @return bool
     */
    public function is($type) {
        return $this->type === (string) $type;
    }
}
