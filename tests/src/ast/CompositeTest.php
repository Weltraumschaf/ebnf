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
 * @see Notification
 */
require_once 'ast/Notification.php';
/**
 * @see Node
 */
require_once 'ast/Node.php';
/**
 * @see Rule
 */
require_once 'ast/Rule.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';

/**
 * Tests for {@link Composite}.
 *
 * @package tests
 */
class CompositeTest extends \PHPUnit_Framework_TestCase {

    public function testAddHasChildrenAndGetIterator() {
        $composite = $this->getMockForAbstractClass("de\weltraumschaf\ebnf\ast\Composite");
        $this->assertInstanceOf("\IteratorAggregate", $composite);
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

    public function testProbeEquivalenceInternal() {
        $comp = $this->getMockForAbstractClass('de\weltraumschaf\ebnf\ast\Composite', array(), "SomeComposite");
        $n = new Notification();
        $comp->probeEquivalence(new Terminal(), $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals(
            "Probed node is not a composite node: 'SomeComposite' vs. 'de\weltraumschaf\ebnf\ast\Terminal'!",
            $n->report()
        );

        $n = new Notification();
        $comp->probeEquivalence(new Rule(), $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals(
            "Probed node types mismatch: 'SomeComposite' != 'de\weltraumschaf\ebnf\ast\Rule'!",
            $n->report()
        );
    }
}
