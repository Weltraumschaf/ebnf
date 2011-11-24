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
 * Tests for {@link Terminal}.
 *
 * @package tests
 */
class TerminalTest extends \PHPUnit_Framework_TestCase {

    public function testProbeEquivalence() {
        $n = new Notification();
        $term1 = new Terminal();
        $term1->value = "a";
        $term2 = new Terminal();
        $term2->value = "b";

        $term1->probeEquivalence(new Identifier(), $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals(
            "Probed node types mismatch: 'de\weltraumschaf\ebnf\ast\Terminal' != 'de\weltraumschaf\ebnf\ast\Identifier'!",
            $n->report()
        );

        $n = new Notification();
        $term1->probeEquivalence($term2, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals("Terminal value mismatch: 'a' != 'b'!", $n->report());

        $n = new Notification();
        $term2->probeEquivalence($term1, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals("Terminal value mismatch: 'b' != 'a'!", $n->report());
    }

}