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
 * @see Notification
 */
require_once 'ast/Notification.php';

/**
 * Tests for {@link Notification}.
 *
 * @package tests
 */
class NotificationTest  extends \PHPUnit_Framework_TestCase {

    public function testErrorCollectingAndReporting() {
        $n = new Notification();
        $this->assertTrue($n->isOk());
        $this->assertEquals("", $n->report());
        $n->error("An error!");
        $this->assertFalse($n->isOk());
        $n->error("Some %s occured at %s!", "FOO", "BAR");
        $this->assertFalse($n->isOk());
        $n->error("Error: %s at line %d occued because %s!", "SNAFU", 5, "FOOBAR");
        $this->assertEquals(
            "An error!" . PHP_EOL .
            "Some FOO occured at BAR!" . PHP_EOL .
            "Error: SNAFU at line 5 occued because FOOBAR!", $n->report());
    }
}