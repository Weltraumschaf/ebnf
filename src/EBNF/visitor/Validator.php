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
 * @package visitor
 */

namespace de\weltraumschaf\ebnf\visitor;

/**
 * @see VisitorAdapter
 */
require_once __DIR__. DIRECTORY_SEPARATOR . 'VisitorAdapter.php';
/**
 * @see ValidaorException
 */
require_once __DIR__. DIRECTORY_SEPARATOR . 'ValidaorException.php';
/**
 * @see Type
 */
require_once dirname(__DIR__) . DIRECTORY_SEPARATOR . 'ast/Type.php';

use de\weltraumschaf\ebnf\ast\Type;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;

/**
 * Implements an AST tree vsitor for testing purposes.
 *
 * @package visitor
 */
class Validator extends VisitorAdapter {
// @codingStandardsIgnoreStart
    /**
     * @var array
     */
    private $representative = array();
    /**
     *
     * @var array
     */
    private $currentRule;

    public function getRepresentative() {
        return $this->representative;
    }

    protected function visitSyntax(Syntax $syntax) {
        if ($this->isSyntaxDefined()) {
            throw new ValidaorException(
                "You can specify a syntax only once!",
                ValidaorException::SYNTAXT_REDECLARATION
            );
        }

        $this->representative[$syntax->getNodeName()] = array(
            "meta"  => $syntax->meta,
            "title" => $syntax->title,
            "rule" => array()
        );
    }

    protected function visitRule(Rule $rule) {
        if (!$this->isSyntaxDefined()) {
            throw new ValidaorException(
                "You must specify a syntax at very first!",
                ValidaorException::NO_SYNTAXT_DECLARED
            );
        }

        $this->currentRule = array();
    }

    protected function addRule(array $rule, $name) {
        if (array_key_exists($name, $this->representative[Type::SYNTAX][Type::RULE])) {
            throw new ValidaorException(
                "Rule with name '{$name}' already declared!",
                ValidaorException::RULE_REDECLARATION
            );
        }
        $this->representative[Type::SYNTAX][Type::RULE][$name] = $rule;
    }

    public function assert(array $expected) {

    }

    public function isSyntaxDefined() {
        return array_key_exists(Type::SYNTAX, $this->representative);
    }

    public function afterVisit(Node $visitable) {
        if ($visitable instanceof Rule) {
            $this->addRule($this->currentRule, $visitable->name);
        }
    }
// @codingStandardsIgnoreEnd
}