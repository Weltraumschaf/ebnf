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
 * @see DepthCalculator
 */
require_once 'ast/DepthCalculator.php';
/**
 * @see Node
 */
require_once 'ast/Node.php';
/**
 * @see AbstractComposite
 */
require_once 'ast/AbstractComposite.php';

/**
 * Tests for {@link DepthCalculator}.
 *
 * @package tests
 */
class DepthCalculatorTest extends \PHPUnit_Framework_TestCase {

    /**
     * @param int $depth
     * @return Node
     */
    private function createNode($depth = 1) {
        $n = $this->getMock('de\weltraumschaf\ebnf\ast\Node');
        $n->expects($this->atLeastOnce())
          ->method("depth")
          ->will($this->returnValue($depth));
        return $n;
    }

    public function testDepth() {
        /* @var $subject AbstractComposite */
        $subject = $this->getMockForAbstractClass(
            'de\weltraumschaf\ebnf\ast\AbstractComposite',
            array($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );

        $calc = new DepthCalculator($subject);
        $this->assertEquals(0, $subject->countChildren());
        $this->assertEquals(1, $calc->depth());

        $subject->addChild($this->createNode());
        $this->assertEquals(1, $subject->countChildren());
        $calc = new DepthCalculator($subject);
        $this->assertEquals(2, $calc->depth());

        $subject->addChild($this->createNode());
        $this->assertEquals(2, $subject->countChildren());
        $calc = new DepthCalculator($subject);
        $this->assertEquals(2, $calc->depth());

        $subject->addChild($this->createNode());
        $this->assertEquals(3, $subject->countChildren());
        $calc = new DepthCalculator($subject);
        $this->assertEquals(2, $calc->depth());

        /* @var $subject AbstractComposite */
        $subject = $this->getMockForAbstractClass(
            'de\weltraumschaf\ebnf\ast\AbstractComposite',
            array($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $subject->addChild($this->createNode(2));
        $calc = new DepthCalculator($subject);
        $this->assertEquals(3, $calc->depth());

        $subject->addChild($this->createNode(5));
        $calc = new DepthCalculator($subject);
        $this->assertEquals(6, $calc->depth());

        $subject->addChild($this->createNode(1));
        $calc = new DepthCalculator($subject);
        $this->assertEquals(6, $calc->depth());

        $subject->addChild($this->createNode(8));
        $calc = new DepthCalculator($subject);
        $this->assertEquals(9, $calc->depth());
    }

}
