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

namespace de\weltraumschaf\ebnf\visitor;

require_once 'visitor/Validator.php';
require_once 'ast/Syntax.php';
require_once 'ast/Rule.php';

use de\weltraumschaf\ebnf\ast\Syntax as Syntax;
use de\weltraumschaf\ebnf\ast\Rule as Rule;

class ValidatorTest extends \PHPUnit_Framework_TestCase {
    
    public function testIsSyntaxDefined() {
        $syntax = new Syntax();
        $syntax->meta  = "foo";
        $syntax->title = "bar";
        
        $validator = new Validator();
        $this->assertFalse($validator->isSyntaxDefined());
        $syntax->accept($validator);
        $this->assertTrue($validator->isSyntaxDefined());
        
        try {
            $syntax->accept($validator);
            $this->fail("Exceptd exception not thrown!");
        } catch(ValidaorException $e) {
            $this->assertEquals(ValidaorException::SYNTAXT_REDECLARATION, $e->getCode());
            $this->assertEquals("You can specify a syntax only once!", $e->getMessage());
        }
    }
    
    public function testGetRepresentative() {
        
        $syntax = new Syntax();
        $syntax->meta  = "foo meta";
        $syntax->title = "bar title";
        
        $tester = new Validator();
        $syntax->accept($tester);
        $this->assertEquals(array(
            "syntax" => array(
                "meta"  => "foo meta",
                "title" => "bar title",
                "rule" => array()
            )
        ), $tester->getRepresentative());
        
        $tester = new Validator();
        $ruleFoo = new Rule();
        $ruleFoo->name = "foo";
        $syntax->addChild($ruleFoo);
        $ruleBar = new Rule();
        $ruleBar->name = "bar";
        $syntax->addChild($ruleBar);
        $syntax->accept($tester);
        $this->assertEquals(array(
            "syntax" => array(
                "meta"  => "foo meta",
                "title" => "bar title",
                "rule" => array(
                    "foo" => array(),
                    "bar" => array()
                )
            )
        ), $tester->getRepresentative());
        
        $this->markTestIncomplete();
    }
    
    public function testAssertSyntax() {
        $this->markTestIncomplete();
        
        $tester = new Validator();
        $syntax = new Syntax();
        $syntax->meta  = "foo";
        $syntax->title = "bar";
        $syntax->accept($tester);
        
        try {
            $tester->assert(array(
                "syntax" => array(
                    "meta" => "foo",
                    "title" => "bar"
                )
            ));
        } catch (\Exception $e) {
            $this->fail("Unexpected excpeton thrown!");
        }
    }
    
}