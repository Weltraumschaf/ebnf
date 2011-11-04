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

namespace de\weltraumschaf\ebnf\visitor;

require_once __DIR__. DIRECTORY_SEPARATOR . "VisitorAdapter.php";
require_once __DIR__. DIRECTORY_SEPARATOR . "ValidaorException.php";
require_once dirname(__DIR__) . DIRECTORY_SEPARATOR . 'ast/Type.php';

use de\weltraumschaf\ebnf\ast\Type        as Type;
use \de\weltraumschaf\ebnf\ast\Identifier as Identifier;
use \de\weltraumschaf\ebnf\ast\Loop       as Loop;
use de\weltraumschaf\ebnf\ast\Node        as Node;
use de\weltraumschaf\ebnf\ast\Option      as Option;
use de\weltraumschaf\ebnf\ast\Rule        as Rule;
use de\weltraumschaf\ebnf\ast\Sequence    as Sequence;
use de\weltraumschaf\ebnf\ast\Syntax      as Syntax;
use de\weltraumschaf\ebnf\ast\Terminal    as Terminal;
 
/**
 * Implements an AST tree vsitor for testing purposes.
 * 
 */
class Validator extends VisitorAdapter {
    
    /**
     * @var array
     */
    private $representative = array();
    
    public function getRepresentative() {
        return $this->representative;
    }

    protected function visitSyntax(Syntax $syntax) {
        if ($this->isSyntaxDefined()) {
            throw new ValidaorException("You can specify a syntax only once!", ValidaorException::SYNTAXT_DUPLICATED);
        }
        
        $this->representative[$syntax->getNodeName()] = array(
            "meta"  => $syntax->meta,
            "title" => $syntax->title,
            "rules" => array()
        );
    }
//    
//    protected function visitRule(Rule $rule) {
//        if (!isset($this->representative["syntax"])) {
//            throw new \Exception("Does not visited a syntax node before vsiting rule!");
//        }
//        
//        $this->representative["syntax"]["rules"][$rule->name] = array();
//    }

    public function assert(array $expected) {
        
    }

    public function isSyntaxDefined() {
        return array_key_exists(Type::SYNTAX, $this->representative);
    }
}