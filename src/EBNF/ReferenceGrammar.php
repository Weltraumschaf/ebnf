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
 * @package ebnf
 */

namespace de\weltraumschaf\ebnf;

/**
 * Defines a reference grammar.
 *
 * @codeCoverageIgnore
 * @package ebnf
 * @version @@version@@
 */
class ReferenceGrammar {

    /**
     * Returns the reference grammar as string.
     *
     * @return string
     */
    public function __toString() {
        return '"EBNF defined in itself." {
          syntax     = [ title ] "{" { rule } "}" [ comment ] .
          rule       = identifier ( "=" | ":" | ":==" ) expression ( "." | ";" ) .
          expression = term { "|" term } .
          term       = factor { factor } .
          factor     = identifier
                     | literal
                     | range
                     | "[" expression "]"
                     | "(" expression ")"
                     | "{" expression "}" .
          identifier = character { character } .
          range      = character ".." character .
          title      = literal .
          comment    = literal .
          literal    = "\'" character { character } "\'"
                     | \'"\' character { character } \'"\' .
          character  = "a" .. "z"
                     | "A" .. "Z"
                     | "0" .. "9" .
        }';
    }

}