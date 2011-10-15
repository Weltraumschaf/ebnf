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

namespace de\weltraumschaf\ebnf;

require_once "Position.php";

/**
 * Testcase for class {Position}.
 */
class PositionTest extends \PHPUnit_Framework_TestCase {

    public function testtoString() {
        $p = new Position(5, 10);
        $this->assertEquals("(5, 10)", $p->__toString());

        $p = new Position(7, 8, "/foo/bar/baz.ebnf");
        $this->assertEquals("/foo/bar/baz.ebnf (7, 8)", $p->__toString());
    }

}
