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

//@codingStandardsIgnoreFile

namespace de\weltraumschaf\ebnf\symbol;

/**
 * @package symbol
 * @version @@version@@
 */
class RuleSymbol {
    private $name;

    public function __construct($identifier) {
        $this->name = $identifier;
    }

    public function getName() {
        return $this->name;
    }

}