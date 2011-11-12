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

namespace de\weltraumschaf\ebnf;

/**
 * @see SyntaxtException
 */
require_once 'SyntaxtException.php';
/**
 * @see Position
 */
require_once 'Position.php';

/**
 * Testcase for class {@link SyntaxtException}.
 * 
 * @package tests
 */
class SyntaxtExceptionTest extends \PHPUnit_Framework_TestCase {

    public function testToString() {
        $e = new SyntaxtException("foo bar", new Position(3, 4));
        $this->assertEquals("Syntax error: foo bar at (3, 4) (code: 0)!", $e->__toString());
        $e = new SyntaxtException("foo bar", new Position(3, 4), 4);
        $this->assertEquals("Syntax error: foo bar at (3, 4) (code: 4)!", $e->__toString());
        $e = new SyntaxtException("foo bar", new Position(3, 4, "foo.ebnf"));
        $this->assertEquals("Syntax error: foo bar at foo.ebnf (3, 4) (code: 0)!", $e->__toString());
    }

}
