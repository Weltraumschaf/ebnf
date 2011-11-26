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
 * @package symbol
 */

namespace de\weltraumschaf\ebnf\symbol;

use \InvalidArgumentException;

/**
 * @package symbol
 * @version @@version@@
 */
class RuleSymbolTable {

    /**
     * @var array
     */
    private $rules;

    public function __construct() {
        $this->rules = array();
    }

    /**
     * @param Symbol $s
     */
    public function define(RuleSymbol $s) {
        if (null !== $this->resolve($s->getName())) {
            throw new InvalidArgumentException();
        }

        $this->rules[$s->getName()] = $s;
    }

    /**
     *
     * @param string $name
     * @return Symbol
     */
    public function resolve($name) {
        if (array_key_exists((string) $name, $this->rules)) {
            return $this->rules[(string) $name];
        }

        return null;
    }
}