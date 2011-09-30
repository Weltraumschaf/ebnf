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

namespace Weltraumschaf\Ebnf;

require_once "Token.php";

/**
 * Testcase for class {Token}.
 */
class TokenTest extends \PHPUnit_Framework_TestCase {

    public function testGetTypeAsString() {
        $p = new Position(5, 10);
        $t = new Token(Token::OPERATOR, "", $p);
        $this->assertEquals("OPERATOR", $t->getTypeAsString());
        $t = new Token(Token::IDENTIFIER, "", $p);
        $this->assertEquals("IDENTIFIER", $t->getTypeAsString());
        $t = new Token(Token::WHITESPACE, "", $p);
        $this->assertEquals("WHITESPACE", $t->getTypeAsString());
        $t = new Token(Token::LITERAL, "", $p);
        $this->assertEquals("LITERAL", $t->getTypeAsString());
        $t = new Token(Token::EOF, "", $p);
        $this->assertEquals("EOF", $t->getTypeAsString());
        $t = new Token(4711, "", $p);
        $this->assertEquals("(4711)", $t->getTypeAsString());
    }

    public function testtoString() {
        $p = new Position(5, 10);
        $t = new Token(Token::IDENTIFIER, "ident", $p);
        $this->assertEquals("<'ident', IDENTIFIER, (5, 10)>", $t->__toString());
        $p = new Position(5, 10, "/foo/bar.ebnf");
        $t = new Token(Token::IDENTIFIER, "ident", $p);
        $this->assertEquals("<'ident', IDENTIFIER, /foo/bar.ebnf (5, 10)>", $t->__toString());
        $p = new Position(5, 10, "/foo/bar.ebnf");
        $t = new Token(Token::IDENTIFIER, "", $p);
        $this->assertEquals("<IDENTIFIER, /foo/bar.ebnf (5, 10)>", $t->__toString());


    }

}
