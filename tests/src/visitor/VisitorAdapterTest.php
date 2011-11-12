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
 * @package tests
 */

namespace de\weltraumschaf\ebnf\visitor;

/**
 * @see VisitorAdapter
 */
require_once 'visitor/VisitorAdapter.php';
/**
 * @see Choice
 */
require_once 'ast/Choice.php';
/**
 * @see Identifier
 */
require_once 'ast/Identifier.php';
/**
 * @see Loop
 */
require_once 'ast/Loop.php';
/**
 * @see Option
 */
require_once 'ast/Option.php';
/**
 * @see Rule
 */
require_once 'ast/Rule.php';
/**
 * @see Sequence
 */
require_once 'ast/Sequence.php';
/**
 * @see Syntax
 */
require_once 'ast/Syntax.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';

/**
 * Tests for {@link VisitorAdapter}.
 * 
 * @package tests
 */
class VisitorAdapterTest extends \PHPUnit_Framework_TestCase {
    
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
            $adapter->expects($this->once())
                    ->method($visitorMethod)
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