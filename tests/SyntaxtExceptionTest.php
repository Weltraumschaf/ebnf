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

require_once "SyntaxtException.php";
require_once "Position.php";

/**
 * Testcase for class {SyntaxtException}.
 */
class SyntaxtExceptionTest extends \PHPUnit_Framework_TestCase {

    public function testToString() {
        $e = new SyntaxtException("foo bar", new Position(3, 4));
        $this->assertEquals("Error: foo bar at (3, 4) (code: 0)!", $e->__toString());
        $e = new SyntaxtException("foo bar", new Position(3, 4), 4);
        $this->assertEquals("Error: foo bar at (3, 4) (code: 4)!", $e->__toString());
        $e = new SyntaxtException("foo bar", new Position(3, 4, "foo.ebnf"));
        $this->assertEquals("Error: foo bar at foo.ebnf (3, 4) (code: 0)!", $e->__toString());
    }

}
