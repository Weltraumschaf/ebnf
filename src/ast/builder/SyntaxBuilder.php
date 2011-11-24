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
/**
 * @see RuleBuilder
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'RuleBuilder.php';
/**
 * @see Rule
 */
require_once dirname(__DIR__) . DIRECTORY_SEPARATOR . 'Rule.php';
/**
 * @see Syntax
 */
require_once dirname(__DIR__) . DIRECTORY_SEPARATOR . 'Syntax.php';

use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Syntax;

/**
 * Builder to generate a {@link Syntax} AST with a fluent interface.
 */
class SyntaxBuilder {
    /**
     * @var Syntax
     */
    private $syntax;

    /**
     * Initializes a {@link Syntax} object.
     */
    public function __construct() {
        $this->syntax = new Syntax();
    }

    /**
     * Declare titel and meta of {@link Syntax}.
     *
     * @param string $title Title of the syntaxt.
     * @param string $meta  Meta of the syntax.
     *
     * @return SyntaxBuilder
     */
    public function syntaxt($title, $meta) {
        $this->syntax->title = (string) $title;
        $this->syntax->meta  = (string) $meta;
        return $this;
    }

    /**
     * Declare a {@link Rule} for the syntax.
     *
     * @param string $name Name of the rule.
     *
     * @return RuleBuilder
     */
    public function rule($name) {
        $rule = new Rule();
        $rule->name = (string) $name;
        $this->syntax->addChild($rule);
        return new RuleBuilder($rule, $this);
    }

    /**
     * Return the AST sytax node.
     *
     * @return Syntax
     */
    public function getAst() {
        return $this->syntax;
    }
}
