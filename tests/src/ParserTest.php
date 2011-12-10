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
 * @see Parser
 */
require_once 'Parser.php';
/**
 * @see Scanner
 */
require_once 'Scanner.php';
/**
 * @see Token
 */
require_once 'Token.php';
/**
 * @see SyntaxBuilder
 */
require_once 'ast/builder/SyntaxBuilder.php';
require_once 'ast/Notification.php';

use de\weltraumschaf\ebnf\ast\builder\SyntaxBuilder;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Notification;

/**
 * Expose protected methods of  {@link Parser} for testing.
 *
 * @package tests
 */
class ExposedParser extends Parser {
    public function exposedAssertToken(Token $token, $type, $value) {
        return parent::assertToken($token, $type, $value);
    }

    public function exposedAssertTokens(Token $token, $type, array $values) {
        return parent::assertTokens($token, $type, $values);
    }
}

/**
 * Testcase for class {@link Parser}.
 *
 * @package tests
 */
class ParserTest extends \PHPUnit_Framework_TestCase {

    private function loadFixture($file) {
        return file_get_contents(EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . $file);
    }


    private function assertEquivalentSyntax(Syntax $expected, Syntax $actual) {
        $n = new Notification();
        $expected->probeEquivalence($actual, $n);
        $this->assertNotificationOk($n);
        $n = new Notification();
        $actual->probeEquivalence($expected, $n);
        $this->assertNotificationOk($n);
    }

    private function assertNotificationOk(Notification $n) {
        $this->assertTrue($n->isOk(), $n->report());
    }

    public function testAssertToken() {
        $p  = new ExposedParser(new Scanner(""));
        $t1 = new Token(Token::OPERATOR, ":", new Position(0, 0));
        $t2 = new Token(Token::IDENTIFIER, "foobar", new Position(0, 0));

        $this->assertTrue($p->exposedAssertToken($t1, Token::OPERATOR, ":"));
        $this->assertFalse($p->exposedAssertToken($t1, Token::IDENTIFIER, ":"));
        $this->assertFalse($p->exposedAssertToken($t1, Token::OPERATOR, ","));
        $this->assertFalse($p->exposedAssertToken($t1, Token::IDENTIFIER, ","));

        $this->assertTrue($p->exposedAssertToken($t2, Token::IDENTIFIER, "foobar"));
        $this->assertFalse($p->exposedAssertToken($t2, Token::OPERATOR, "foobar"));
        $this->assertFalse($p->exposedAssertToken($t2, Token::IDENTIFIER, "snafu"));
        $this->assertFalse($p->exposedAssertToken($t2, Token::OPERATOR, "snafu"));
    }

    public function testAssertTokens() {
        $p = new ExposedParser(new Scanner(""));
        $t1 = new Token(Token::OPERATOR, ":", new Position(0, 0));
        $t2 = new Token(Token::OPERATOR, "=", new Position(0, 0));

        $this->assertTrue($p->exposedAssertTokens($t1, Token::OPERATOR, array("=", ":", ":==")));
        $this->assertFalse($p->exposedAssertTokens($t1, Token::OPERATOR, array("+", "-", "*")));
        $this->assertTrue($p->exposedAssertTokens($t2, Token::OPERATOR, array("=", ":", ":==")));
        $this->assertFalse($p->exposedAssertTokens($t2, Token::OPERATOR, array("+", "-", "*")));
    }

    public function testParse() {
        $p = new Parser(new Scanner($this->loadFixture("rules_with_different_assignment_ops.ebnf")));
        $this->assertXmlStringEqualsXmlFile(
            EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "rules_with_different_assignment_ops.xml",
            $p->parse()->saveXML(),
            "rules with different assignment ops"
        );

        $p = new Parser(new Scanner($this->loadFixture("rules_with_literals.ebnf")));
        $this->assertXmlStringEqualsXmlFile(
            EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "rules_with_literals.xml",
            $p->parse()->saveXML(),
            "rules with literals"
        );

        $p = new Parser(new Scanner($this->loadFixture("testgrammar_1.old.ebnf")));
        $this->assertXmlStringEqualsXmlFile(
            EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "testgrammar_1.xml",
            $p->parse()->saveXML(),
            "testgrammar 1"
        );

        $this->markTestIncomplete("Implement range parsing.");
        $p = new Parser(new Scanner($this->loadFixture("rules_with_ranges.ebnf")));
        $this->assertXmlStringEqualsXmlFile(
            EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "rules_with_ranges.xml",
            $p->parse()->saveXML(),
            "rules with ranges"
        );
    }

    public function testParseAst() {
        $p = new Parser(new Scanner($this->loadFixture("rules_with_different_assignment_ops.ebnf")));
        $p->parse();
        $builder = new SyntaxBuilder();
        $builder->syntax("Rules with different assignment operators.")
                    ->rule("comment1")
                        ->identifier("literal1")
                    ->end()
                    ->rule("comment2")
                        ->identifier("literal2")
                    ->end()
                    ->rule("comment3")
                        ->identifier("literal3")
                    ->end();

        $this->assertEquivalentSyntax($builder->getAst(), $p->getAst());

        $p = new Parser(new Scanner($this->loadFixture("rules_with_literals.ebnf")));
        $p->parse();
        $builder->clear()
                ->syntax("Rules with literal.")
                ->rule("literal")
                    ->choice()
                        ->sequence()
                            ->terminal("'")
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal("'")
                        ->end()
                        ->sequence()
                            ->terminal('"')
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal('"')
                        ->end()
                    ->end()
                ->end();

        $this->assertEquivalentSyntax($builder->getAst(), $p->getAst());

        $p = new Parser(new Scanner($this->loadFixture("testgrammar_1.old.ebnf")));
        $p->parse();
        $builder->clear()
                ->syntax("EBNF defined in itself.")
                ->rule("syntax")
                    ->sequence()
                        ->option()
                            ->identifier("title")
                        ->end()
                        ->terminal("{")
                        ->loop()
                            ->identifier("rule")
                        ->end()
                        ->terminal("}")
                        ->option()
                            ->identifier("comment")
                        ->end()
                    ->end()
                ->end()
                ->rule("rule")
                    ->sequence()
                        ->identifier("identifier")
                        ->choice()
                            ->terminal("=")
                            ->terminal(":")
                            ->terminal(":==")
                        ->end()
                        ->identifier("expression")
                        ->choice()
                            ->terminal(".")
                            ->terminal(";")
                        ->end()
                    ->end()
                ->end()
                ->rule("expression")
                    ->sequence()
                        ->identifier("term")
                        ->loop()
                            ->sequence()
                                ->terminal("|")
                                ->identifier("term")
                            ->end()
                        ->end()
                    ->end()
                ->end()
                ->rule("term")
                    ->sequence()
                        ->identifier("factor")
                        ->loop()
                            ->identifier("factor")
                        ->end()
                    ->end()
                ->end()
                ->rule("factor")
                    ->choice()
                        ->identifier("identifier")
                        ->identifier("literal")
                        ->identifier("range")
                        ->sequence()
                            ->terminal("[")
                            ->identifier("expression")
                            ->terminal("]")
                        ->end()
                        ->sequence()
                            ->terminal("(")
                            ->identifier("expression")
                            ->terminal(")")
                        ->end()
                        ->sequence()
                            ->terminal("{")
                            ->identifier("expression")
                            ->terminal("}")
                        ->end()
                    ->end()
                ->end()
                ->rule("identifier")
                    ->sequence()
                        ->identifier("character")
                        ->loop()
                            ->identifier("character")
                        ->end()
                    ->end()
                ->end()
                ->rule("range")
                    ->sequence()
                        ->identifier("character")
                        ->terminal("..")
                        ->identifier("character")
                    ->end()
                ->end()
                ->rule("title")
                    ->identifier("literal")
                ->end()
                ->rule("comment")
                    ->identifier("literal")
                ->end()
                ->rule("literal")
                    ->choice()
                        ->sequence()
                            ->terminal("'")
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal("'")
                        ->end()
                        ->sequence()
                            ->terminal('"')
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal('"')
                        ->end()
                    ->end()
                ->end()
                ->rule("character")
                    ->choice()
                        ->terminal("a")
                        ->terminal("b")
                        ->terminal("c")
                        ->terminal("d")
                        ->terminal("e")
                        ->terminal("f")
                        ->terminal("g")
                        ->terminal("h")
                        ->terminal("i")
                        ->terminal("j")
                        ->terminal("k")
                        ->terminal("l")
                        ->terminal("m")
                        ->terminal("n")
                        ->terminal("o")
                        ->terminal("p")
                        ->terminal("q")
                        ->terminal("r")
                        ->terminal("s")
                        ->terminal("t")
                        ->terminal("u")
                        ->terminal("v")
                        ->terminal("w")
                        ->terminal("x")
                        ->terminal("y")
                        ->terminal("z")
                        ->terminal("A")
                        ->terminal("B")
                        ->terminal("C")
                        ->terminal("D")
                        ->terminal("E")
                        ->terminal("F")
                        ->terminal("G")
                        ->terminal("H")
                        ->terminal("I")
                        ->terminal("J")
                        ->terminal("K")
                        ->terminal("L")
                        ->terminal("M")
                        ->terminal("N")
                        ->terminal("O")
                        ->terminal("P")
                        ->terminal("Q")
                        ->terminal("R")
                        ->terminal("S")
                        ->terminal("T")
                        ->terminal("U")
                        ->terminal("V")
                        ->terminal("W")
                        ->terminal("X")
                        ->terminal("Y")
                        ->terminal("Z")
                    ->end()
                ->end();

        $this->assertEquivalentSyntax($builder->getAst(), $p->getAst());
    }

    public function testParseErrors() {
        $this->markTestIncomplete("Implement test with errornous syntax fixtures.");
    }
}
