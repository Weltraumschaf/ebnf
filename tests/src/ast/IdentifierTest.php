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
 * @see Identifier
 */
require_once 'ast/Identifier.php';
/**
 * @see Notification
 */
require_once 'ast/Notification.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';

/**
 * Tests for {@link Identifier}.
 *
 * @package tests
 */
class IdentifierTest extends \PHPUnit_Framework_TestCase {

    public function testProbeEquivalence() {
        $n = new Notification();
        $ident1 = new Identifier($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $ident1->value = "a";
        $ident2 = new Identifier($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $ident2->value = "b";

        $ident1->probeEquivalence(new Terminal($this->getMock('de\weltraumschaf\ebnf\ast\Node')), $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals(
            "Probed node types mismatch: 'de\weltraumschaf\ebnf\ast\Identifier' != 'de\weltraumschaf\ebnf\ast\Terminal'!",
            $n->report()
        );

        $n = new Notification();
        $ident1->probeEquivalence($ident2, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals("Identifier value mismatch: 'a' != 'b'!", $n->report());

        $n = new Notification();
        $ident2->probeEquivalence($ident1, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals("Identifier value mismatch: 'b' != 'a'!",$n->report());
    }

    public function testDepth() {
        $ident = new Identifier($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $this->assertEquals(1, $ident->depth());
    }

}