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

namespace de\weltraumschaf\ebnf\ast;

require_once "ast/Composite.php";
require_once "ast/Terminal.php";
require_once "ast/Identifier.php";
require_once "ast/Choice.php";
require_once "ast/Loop.php";
require_once "ast/Option.php";
require_once "ast/Rule.php";
require_once "ast/Sequence.php";
require_once "ast/Syntax.php";

class IntegrationTest extends \PHPUnit_Framework_TestCase {
    
    public function testThatNodeIsNotComposite() {
        $this->assertNotInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Terminal());
        $this->assertNotInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Identifier());
    }
    
    public function testThatNodeIsComposite() {
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Choice());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Loop());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Option());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Rule());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Sequence());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Syntax());
        $composite = $this->getMockForAbstractClass("de\weltraumschaf\ebnf\ast\Composite");
        $this->assertInstanceOf("\IteratorAggregate", $composite);
    }
    
    public function testComposite() {
        $composite = $this->getMockForAbstractClass("de\weltraumschaf\ebnf\ast\Composite");
        /* @var $composite Composite */
        $this->assertFalse($composite->hasChildren());
        $this->assertEquals(0, $composite->countChildren());
        $iterator = $composite->getIterator();
        $this->assertInstanceOf("\ArrayIterator", $iterator);
        $this->assertEquals(0, $iterator->count());
        
        $nodeOne = $this->getMock("de\weltraumschaf\ebnf\ast\Node");
        $nodeOne->name = "one";
        $composite->addChild($nodeOne);
        $this->assertTrue($composite->hasChildren());
        $this->assertEquals(1, $composite->countChildren());
        $iterator = $composite->getIterator();
        $this->assertInstanceOf("\ArrayIterator", $iterator);
        $this->assertEquals(1, $iterator->count());
        $this->assertTrue($iterator->offsetExists(0));
        $this->assertSame($nodeOne, $iterator->offsetGet(0));
        
        $nodeTwo = $this->getMock("de\weltraumschaf\ebnf\ast\Node");
        $nodeTwo->name = "two";
        $composite->addChild($nodeTwo);
        $this->assertTrue($composite->hasChildren());
        $this->assertEquals(2, $composite->countChildren());
        $iterator = $composite->getIterator();
        $this->assertInstanceOf("\ArrayIterator", $iterator);
        $this->assertEquals(2, $iterator->count());
        $this->assertTrue($iterator->offsetExists(0));
        $this->assertSame($nodeOne, $iterator->offsetGet(0));
        $this->assertTrue($iterator->offsetExists(1));
        $this->assertSame($nodeTwo, $iterator->offsetGet(1));
    }
    
    public function testIntegration() {
        $syntax = new Syntax();
        $syntax->meta = "foo";
        $syntax->title = "bar";
        
        $firstRule = new Rule();
        $firstRule->name = "first";
        $syntax->addChild($firstRule);
        
        $secondRule = new Rule();
        $secondRule->name = "second";
        $syntax->addChild($secondRule);

        $this->markTestIncomplete();
    }
    
}