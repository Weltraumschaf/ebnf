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
        $t = new Token(Token::COMMENT, "", $p);
        $this->assertEquals("COMMENT", $t->getTypeAsString());
        $t = new Token(Token::LITERAL, "", $p);
        $this->assertEquals("LITERAL", $t->getTypeAsString());
        $t = new Token(Token::EOF, "", $p);
        $this->assertEquals("EOF", $t->getTypeAsString());
        $t = new Token(4711, "", $p);
        $this->assertEquals("(4711)", $t->getTypeAsString());
    }

    public function testToString() {
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

    public function testIsType() {
        $p = new Position(5, 10);
        $t = new Token(Token::OPERATOR, "", $p);
        $this->assertTrue($t->isType(Token::OPERATOR));
        $this->assertFalse($t->isType(Token::IDENTIFIER));
        $this->assertFalse($t->isType(Token::COMMENT));
        $this->assertFalse($t->isType(Token::LITERAL));
        $this->assertFalse($t->isType(Token::EOF));

        $t = new Token(Token::IDENTIFIER, "", $p);
        $this->assertTrue($t->isType(Token::IDENTIFIER));
        $this->assertFalse($t->isType(Token::OPERATOR));
        $this->assertFalse($t->isType(Token::COMMENT));
        $this->assertFalse($t->isType(Token::LITERAL));
        $this->assertFalse($t->isType(Token::EOF));

        $t = new Token(Token::COMMENT, "", $p);
        $this->assertTrue($t->isType(Token::COMMENT));
        $this->assertFalse($t->isType(Token::IDENTIFIER));
        $this->assertFalse($t->isType(Token::OPERATOR));
        $this->assertFalse($t->isType(Token::LITERAL));
        $this->assertFalse($t->isType(Token::EOF));

        $t = new Token(Token::LITERAL, "", $p);
        $this->assertTrue($t->isType(Token::LITERAL));
        $this->assertFalse($t->isType(Token::IDENTIFIER));
        $this->assertFalse($t->isType(Token::COMMENT));
        $this->assertFalse($t->isType(Token::OPERATOR));
        $this->assertFalse($t->isType(Token::EOF));

        $t = new Token(Token::EOF, "", $p);
        $this->assertTrue($t->isType(Token::EOF));
        $this->assertFalse($t->isType(Token::IDENTIFIER));
        $this->assertFalse($t->isType(Token::COMMENT));
        $this->assertFalse($t->isType(Token::LITERAL));
        $this->assertFalse($t->isType(Token::OPERATOR));

        $t = new Token(4711, "", $p);
        $this->assertFalse($t->isType(Token::OPERATOR));
        $this->assertFalse($t->isType(Token::IDENTIFIER));
        $this->assertFalse($t->isType(Token::COMMENT));
        $this->assertFalse($t->isType(Token::LITERAL));
        $this->assertFalse($t->isType(Token::EOF));
    }

    public function testIsEqual() {
        $p = new Position(5, 10);
        $t = new Token(Token::LITERAL, "foo", $p);
        $this->assertTrue($t->isEqual("foo"));
        $this->assertFalse($t->isEqual("bar"));

        $this->assertFalse($t->isNotEqual("foo"));
        $this->assertTrue($t->isNotEqual("bar"));
    }

    public function testIsEquals() {
        $tokens = array(
            new Token(0, "a", new Position(0, 0)),
            new Token(0, "b", new Position(0, 0)),
            new Token(0, "c", new Position(0, 0)),
        );

        foreach ($tokens as $token) {
            /* @var $token Token */
            $this->assertTrue($token->isEquals(array("a", "b", "c")), $token->getValue());
            $this->assertFalse($token->isEquals(array("x", "y", "z")), $token->getValue());
            $this->assertFalse($token->isNotEquals(array("a", "b", "c")), $token->getValue());
            $this->assertTrue($token->isNotEquals(array("x", "y", "z")), $token->getValue());
        }
    }

    public function testUnquoteString() {
        $this->assertEquals('a test string', Token::unquoteString('"a test string"'));
        $this->assertEquals('a "test" string', Token::unquoteString('"a "test" string"'));
        $this->assertEquals('a "test" string', Token::unquoteString('"a \"test\" string"'));
        $this->assertEquals("a test string", Token::unquoteString("'a test string'"));
        $this->assertEquals("a 'test' string", Token::unquoteString("'a 'test' string'"));
        $this->assertEquals("a 'test' string", Token::unquoteString("'a \'test\' string'"));

        $t = new Token(0, '"a \"test\" string"', new Position(0, 0));
        $this->assertEquals('"a \"test\" string"', $t->getValue());
        $this->assertEquals('a "test" string', $t->getValue(true));
    }

    public function testGetPostion() {
        $t = new Token(0, "abc", new Position(1, 5));
        $end = $t->getPosition(true);
        $this->assertInstanceOf("de\weltraumschaf\ebnf\Position", $end);
        $this->assertEquals(1, $end->getLine());
        $this->assertEquals(8, $end->getColumn());
    }
}
