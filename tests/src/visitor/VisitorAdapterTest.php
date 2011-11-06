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

require_once 'visitor/VisitorAdapter.php';

require_once 'ast/Choice.php';
require_once 'ast/Identifier.php';
require_once 'ast/Loop.php';
require_once 'ast/Option.php';
require_once 'ast/Rule.php';
require_once 'ast/Sequence.php';
require_once 'ast/Syntax.php';
require_once 'ast/Terminal.php';

class ValidatorAdapterTest extends \PHPUnit_Framework_TestCase {
    
    public function testBeforeAndAfterInvocation() {
        foreach(array(
            "Syntax"
        ) as $nodeType) {
            $class = "de\\weltraumschaf\\ebnf\\ast\\{$nodeType}";
            $node  = new $class();
            $visitorMethod = "visit{$nodeType}";
            $adapter = $this->getMock(
                "de\\weltraumschaf\\ebnf\\visitor\\VisitorAdapter",
                array("beforeVisit", "afterVisit", $visitorMethod)
            );
            $adapter->expects($this->at(0))
                    ->method("beforeVisit")
                    ->with($node);
            $adapter->expects($this->at(1))
                    ->method($visitorMethod)
                    ->with($node);
            $adapter->expects($this->at(2))
                    ->method("afterVisit")
                    ->with($node);
            $adapter->visit($node);
        }
    }
    
    /**
     * @expectedException        InvalidArgumentException
     * @expectedExceptionMessage Unsupportd node: SomeNode!
     */
    public function testThrowExcpetionOnUnsupportedNode() {
        $adapter = $this->getMock("de\\weltraumschaf\\ebnf\\visitor\\VisitorAdapter");
        $adapter->visit(
            $this->getMock(
                "de\\weltraumschaf\\ebnf\\ast\\Node", 
                array(), 
                array(), 
                "SomeNode"
             )
        );
    }
}