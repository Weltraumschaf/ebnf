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

require_once 'visitor/Tester.php';
require_once 'ast/Syntax.php';

use de\weltraumschaf\ebnf\ast\Syntax as Syntax;
use de\weltraumschaf\ebnf\ast\Rule as Rule;

class TesterTest extends \PHPUnit_Framework_TestCase {
    
    public function testGetRepresentative() {
        $syntax = new Syntax();
        $syntax->meta  = "foo";
        $syntax->title = "bar";
        
        $tester = new Tester();
        $syntax->accept($tester);
        $this->assertEquals(array(
            array(
                "name" => "syntax",
                "attr" => array(
                    "meta" => "foo",
                    "title" => "bar"
                ),
                "nodes" => array()
            )
        ), $tester->getRepresentative());
        
        $this->markTestIncomplete();
        
        $tester = new Tester();
        $ruleFoo = new Rule();
        $ruleFoo->name = "foo";
        $syntax->addChild($ruleFoo);
        $ruleBar = new Rule();
        $ruleBar->name = "bar";
        $syntax->addChild($ruleBar);
        $syntax->accept($tester);
        $this->assertEquals(array(
            array(
                "name" => "syntax",
                "attr" => array(
                    "meta" => "foo",
                    "title" => "bar"
                ),
                "nodes" => array(
                    array(
                        "name" => "rule",
                        "attr" => array(
                            "name" => "foo"
                        ),
                        "nodes" => array()
                    ),
                    array(
                        "name" => "rule",
                        "attr" => array(
                            "name" => "bar"
                        ),
                        "nodes" => array()
                    )
                )
            )
        ), $tester->getRepresentative());
        
    }
    
    public function testAssertSyntax() {
        $this->markTestIncomplete();
        
        $tester = new Tester();
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