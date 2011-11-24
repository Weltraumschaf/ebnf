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


namespace de\weltraumschaf\ebnf\ast\builder;

/**
 * @see Builder
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Builder.php';

use de\weltraumschaf\ebnf\ast\Rule;

/**
 *
 */
class RuleBuilder extends Builder {

    public function __construct(Rule $rule, SyntaxBuilder $parent) {
        $this->node = $rule;
        $this->parent = $parent;
    }

    public function rule($name) {
        return $this->parent->rule($name);
    }

}
