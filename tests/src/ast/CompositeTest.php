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

namespace de\weltraumschaf\ebnf\ast;

/**
 * @see Composite
 */
require_once 'ast/Composite.php';

/**
 * Tests for {@link Composite}.
 *
 * @package tests
 */
class CompositeTest {
    public function testAddHasChildrenAndGetIterator() {
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
}
