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

require_once "Scanner.php";

/**
 * Testcase for class {Scanner}.
 */
class ScannerTest extends \PHPUnit_Framework_TestCase {

    public function testGetFile() {
        $s = new Scanner("");
        $this->assertNull($s->getFile());
        $s = new Scanner("", "/foo/bar");
        $this->assertEquals("/foo/bar", $s->getFile());
    }

    private function loadFixture($file) {
        return file_get_contents(EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . $file);
    }

    public function testNext() {
        $this->assertTokens($this->loadFixture("rules_with_ranges.ebnf"), array(
            array("value" => '"Rules with ranges."',
                                        "type" => Token::LITERAL,    "line" => 1, "col" => 1),
            array("value" => "{",       "type" => Token::OPERATOR,   "line" => 1, "col" => 22),

            array("value" => "lower", "type" => Token::IDENTIFIER, "line" => 2, "col" => 5),
            array("value" => "=",     "type" => Token::OPERATOR,   "line" => 2, "col" => 15),
            array("value" => '"a"',   "type" => Token::LITERAL,    "line" => 2, "col" => 17),
            array("value" => "..",    "type" => Token::OPERATOR,   "line" => 2, "col" => 21),
            array("value" => '"z"',   "type" => Token::LITERAL,    "line" => 2, "col" => 24),
            array("value" => ".",     "type" => Token::OPERATOR,   "line" => 2, "col" => 28),

            array("value" => "upper", "type" => Token::IDENTIFIER, "line" => 3, "col" => 5),
            array("value" => "=",     "type" => Token::OPERATOR,   "line" => 3, "col" => 15),
            array("value" => '"A"',   "type" => Token::LITERAL,    "line" => 3, "col" => 17),
            array("value" => "..",    "type" => Token::OPERATOR,   "line" => 3, "col" => 21),
            array("value" => '"Z"',   "type" => Token::LITERAL,    "line" => 3, "col" => 24),
            array("value" => ".",     "type" => Token::OPERATOR,   "line" => 3, "col" => 28),

            array("value" => "number", "type" => Token::IDENTIFIER, "line" => 4, "col" => 5),
            array("value" => "=",      "type" => Token::OPERATOR,   "line" => 4, "col" => 15),
            array("value" => '"0"',    "type" => Token::LITERAL,    "line" => 4, "col" => 17),
            array("value" => "..",     "type" => Token::OPERATOR,   "line" => 4, "col" => 21),
            array("value" => '"9"',    "type" => Token::LITERAL,    "line" => 4, "col" => 24),
            array("value" => ".",      "type" => Token::OPERATOR,   "line" => 4, "col" => 28),

            array("value" => "alpha-num", "type" => Token::IDENTIFIER, "line" => 5, "col" => 5),
            array("value" => "=",         "type" => Token::OPERATOR,   "line" => 5, "col" => 15),
            array("value" => '"a"',       "type" => Token::LITERAL,    "line" => 5, "col" => 17),
            array("value" => "..",        "type" => Token::OPERATOR,   "line" => 5, "col" => 21),
            array("value" => '"z"',       "type" => Token::LITERAL,    "line" => 5, "col" => 24),
            array("value" => "|",         "type" => Token::OPERATOR,   "line" => 5, "col" => 28),
            array("value" => '"0"',       "type" => Token::LITERAL,    "line" => 5, "col" => 30),
            array("value" => "..",        "type" => Token::OPERATOR,   "line" => 5, "col" => 34),
            array("value" => '"9"',       "type" => Token::LITERAL,    "line" => 5, "col" => 37),
            array("value" => ".",         "type" => Token::OPERATOR,   "line" => 5, "col" => 41),

            array("value" => "}", "type" => Token::OPERATOR, "line" => 6, "col" => 1),
            array("value" => "",  "type" => Token::EOF,      "line" => 6, "col" => 1),
        ), "Rules with range.");

        $this->assertTokens($this->loadFixture("rules_with_comments.ebnf"), array(
            array("value" => '"Rules with comments."',
                                        "type" => Token::LITERAL,    "line" => 1, "col" => 1),
            array("value" => "{",       "type" => Token::OPERATOR,   "line" => 1, "col" => 24),

            array("value" => "title",   "type" => Token::IDENTIFIER, "line" => 2, "col" => 5),
            array("value" => "=",       "type" => Token::OPERATOR,   "line" => 2, "col" => 16),
            array("value" => "literal", "type" => Token::IDENTIFIER, "line" => 2, "col" => 18),
            array("value" => ".",       "type" => Token::OPERATOR,   "line" => 2, "col" => 26),
            array("value" => "(* Comment * at the end of line *)",
                                        "type" => Token::COMMENT,    "line" => 2, "col" => 28),

            array("value" => "comment", "type" => Token::IDENTIFIER, "line" => 3, "col" => 5),
            array("value" => "=",       "type" => Token::OPERATOR,   "line" => 3, "col" => 16),
            array("value" => "literal", "type" => Token::IDENTIFIER, "line" => 3, "col" => 18),
            array("value" => ".",       "type" => Token::OPERATOR,   "line" => 3, "col" => 26),

            array("value" => "(*  This is a multi\n        line comment. *)",
                                        "type" => Token::COMMENT,    "line" => 4, "col" => 5),

            array("value" => "comment", "type" => Token::IDENTIFIER, "line" => 6, "col" => 5),
            array("value" => "=",       "type" => Token::OPERATOR,   "line" => 6, "col" => 16),
            array("value" => "literal", "type" => Token::IDENTIFIER, "line" => 6, "col" => 18),
            array("value" => ".",       "type" => Token::OPERATOR,   "line" => 6, "col" => 26),

            array("value" => "}",       "type" => Token::OPERATOR,   "line" => 7, "col" => 1),
            array("value" => "",        "type" => Token::EOF,        "line" => 7, "col" => 1),
        ), "Rule with comment.");

        $this->assertTokens($this->loadFixture("rules_with_different_assignment_ops.ebnf"), array(
            array("value" => '"Rules with different assignment operators."',
                                        "type" => Token::LITERAL,    "line" => 1, "col" => 1),
            array("value" => "{",       "type" => Token::OPERATOR,   "line" => 1, "col" => 46),

            array("value" => "comment", "type" => Token::IDENTIFIER, "line" => 2, "col" => 5),
            array("value" => "=",       "type" => Token::OPERATOR,   "line" => 2, "col" => 13),
            array("value" => "literal", "type" => Token::IDENTIFIER, "line" => 2, "col" => 17),
            array("value" => ".",       "type" => Token::OPERATOR,   "line" => 2, "col" => 25),

            array("value" => "comment", "type" => Token::IDENTIFIER, "line" => 3, "col" => 5),
            array("value" => ":",       "type" => Token::OPERATOR,   "line" => 3, "col" => 13),
            array("value" => "literal", "type" => Token::IDENTIFIER, "line" => 3, "col" => 17),
            array("value" => ".",       "type" => Token::OPERATOR,   "line" => 3, "col" => 25),

            array("value" => "comment", "type" => Token::IDENTIFIER, "line" => 4, "col" => 5),
            array("value" => ":==",     "type" => Token::OPERATOR,   "line" => 4, "col" => 13),
            array("value" => "literal", "type" => Token::IDENTIFIER, "line" => 4, "col" => 17),
            array("value" => ".",       "type" => Token::OPERATOR,   "line" => 4, "col" => 25),

            array("value" => "}",       "type" => Token::OPERATOR,   "line" => 5, "col" => 1),
            array("value" => "",        "type" => Token::EOF,        "line" => 5, "col" => 1),
        ), "Assignemnt operators.");

        $this->assertTokens($this->loadFixture("rules_with_literals.ebnf"), array(
            array("value" => '"Rules with literal."', "type" => Token::LITERAL,    "line" => 1, "col" => 1),
            array("value" => "{",                     "type" => Token::OPERATOR,   "line" => 1, "col" => 23),

            array("value" => "literal",   "type" => Token::IDENTIFIER, "line" => 2, "col" => 5),
            array("value" => "=",         "type" => Token::OPERATOR,   "line" => 2, "col" => 13),
            array("value" => '"\'"',      "type" => Token::LITERAL,    "line" => 2, "col" => 15),
            array("value" => "character", "type" => Token::IDENTIFIER, "line" => 2, "col" => 19),
            array("value" => "{",         "type" => Token::OPERATOR,   "line" => 2, "col" => 29),
            array("value" => "character", "type" => Token::IDENTIFIER, "line" => 2, "col" => 31),
            array("value" => "}",         "type" => Token::OPERATOR,   "line" => 2, "col" => 41),
            array("value" => '"\'"',      "type" => Token::LITERAL,    "line" => 2, "col" => 43),

            array("value" => "|",         "type" => Token::OPERATOR,   "line" => 3, "col" => 13),
            array("value" => "'\"'",      "type" => Token::LITERAL,    "line" => 3, "col" => 15),
            array("value" => "character", "type" => Token::IDENTIFIER, "line" => 3, "col" => 19),
            array("value" => "{",         "type" => Token::OPERATOR,   "line" => 3, "col" => 29),
            array("value" => "character", "type" => Token::IDENTIFIER, "line" => 3, "col" => 31),
            array("value" => "}",         "type" => Token::OPERATOR,   "line" => 3, "col" => 41),
            array("value" => "'\"'",      "type" => Token::LITERAL,    "line" => 3, "col" => 43),
            array("value" => ".",         "type" => Token::OPERATOR,   "line" => 3, "col" => 47),

            array("value" => "}",       "type" => Token::OPERATOR,   "line" => 4, "col" => 1),
            array("value" => "",        "type" => Token::EOF,        "line" => 4, "col" => 1),
        ), "Rules with literal.");

        $this->assertTokens($this->loadFixture("testgrammar_1.ebnf"), array(
            array("value" => '"EBNF defined in itself."',   "type" => Token::LITERAL, "line" => 1, "col" => 1),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 1,  "col" => 27),

            array("value" => "syntax",     "type" => Token::IDENTIFIER, "line" => 2,  "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 14),
            array("value" => "[",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 16),
            array("value" => "title",      "type" => Token::IDENTIFIER, "line" => 2,  "col" => 18),
            array("value" => "]",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 24),
            array("value" => '"{"',        "type" => Token::LITERAL,    "line" => 2,  "col" => 26),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 30),
            array("value" => "rule",       "type" => Token::IDENTIFIER, "line" => 2,  "col" => 32),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 37),
            array("value" => '"}"',        "type" => Token::LITERAL,    "line" => 2,  "col" => 39),
            array("value" => "[",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 43),
            array("value" => "comment",    "type" => Token::IDENTIFIER, "line" => 2,  "col" => 45),
            array("value" => "]",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 53),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 2,  "col" => 55),

            array("value" => "rule",       "type" => Token::IDENTIFIER, "line" => 3,  "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 14),
            array("value" => "identifier", "type" => Token::IDENTIFIER, "line" => 3,  "col" => 16),
            array("value" => "(",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 27),
            array("value" => '"="',        "type" => Token::LITERAL,    "line" => 3,  "col" => 29),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 33),
            array("value" => '":"',        "type" => Token::LITERAL,    "line" => 3,  "col" => 35),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 39),
            array("value" => '":=="',      "type" => Token::LITERAL,    "line" => 3,  "col" => 41),
            array("value" => ")",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 47),
            array("value" => "expression", "type" => Token::IDENTIFIER, "line" => 3,  "col" => 49),
            array("value" => "(",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 60),
            array("value" => '"."',        "type" => Token::LITERAL,    "line" => 3,  "col" => 62),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 66),
            array("value" => '";"',        "type" => Token::LITERAL,    "line" => 3,  "col" => 68),
            array("value" => ")",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 72),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 3,  "col" => 74),

            array("value" => "expression", "type" => Token::IDENTIFIER, "line" => 4,  "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 4,  "col" => 14),
            array("value" => "term",       "type" => Token::IDENTIFIER, "line" => 4,  "col" => 16),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 4,  "col" => 21),
            array("value" => '"|"',        "type" => Token::LITERAL,    "line" => 4,  "col" => 23),
            array("value" => "term",       "type" => Token::IDENTIFIER, "line" => 4,  "col" => 27),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 4,  "col" => 32),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 4,  "col" => 34),
            array("value" => "term",       "type" => Token::IDENTIFIER, "line" => 5,  "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 5,  "col" => 14),
            array("value" => "factor",     "type" => Token::IDENTIFIER, "line" => 5,  "col" => 16),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 5,  "col" => 23),
            array("value" => "factor",     "type" => Token::IDENTIFIER, "line" => 5,  "col" => 25),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 5,  "col" => 32),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 5,  "col" => 34),
            array("value" => "factor",     "type" => Token::IDENTIFIER, "line" => 6,  "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 6,  "col" => 14),
            array("value" => "identifier", "type" => Token::IDENTIFIER, "line" => 6,  "col" => 16),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 7,  "col" => 14),
            array("value" => "literal",    "type" => Token::IDENTIFIER, "line" => 7,  "col" => 16),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 8,  "col" => 14),
            array("value" => '"["',        "type" => Token::LITERAL,    "line" => 8,  "col" => 16),
            array("value" => "expression", "type" => Token::IDENTIFIER, "line" => 8,  "col" => 20),
            array("value" => '"]"',        "type" => Token::LITERAL,    "line" => 8,  "col" => 31),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 9,  "col" => 14),
            array("value" => '"("',        "type" => Token::LITERAL,    "line" => 9,  "col" => 16),
            array("value" => "expression", "type" => Token::IDENTIFIER, "line" => 9,  "col" => 20),
            array("value" => '")"',        "type" => Token::LITERAL,    "line" => 9,  "col" => 31),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 10, "col" => 14),
            array("value" => '"{"',        "type" => Token::LITERAL,    "line" => 10, "col" => 16),
            array("value" => "expression", "type" => Token::IDENTIFIER, "line" => 10, "col" => 20),
            array("value" => '"}"',        "type" => Token::LITERAL,    "line" => 10, "col" => 31),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 10, "col" => 35),
            array("value" => "identifier", "type" => Token::IDENTIFIER, "line" => 11, "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 11, "col" => 14),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 11, "col" => 16),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 11, "col" => 26),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 11, "col" => 28),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 11, "col" => 38),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 11, "col" => 40),
            array("value" => "title",      "type" => Token::IDENTIFIER, "line" => 12, "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 12, "col" => 14),
            array("value" => "literal",    "type" => Token::IDENTIFIER, "line" => 12, "col" => 16),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 12, "col" => 24),
            array("value" => "comment",    "type" => Token::IDENTIFIER, "line" => 13, "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 13, "col" => 14),
            array("value" => "literal",    "type" => Token::IDENTIFIER, "line" => 13, "col" => 16),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 13, "col" => 24),
            array("value" => "literal",    "type" => Token::IDENTIFIER, "line" => 14, "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 14, "col" => 14),
            array("value" => '"\'"',       "type" => Token::LITERAL,    "line" => 14, "col" => 16),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 14, "col" => 20),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 14, "col" => 30),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 14, "col" => 32),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 14, "col" => 42),
            array("value" => '"\'"',       "type" => Token::LITERAL,    "line" => 14, "col" => 44),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 15, "col" => 14),
            array("value" => "'\"'",       "type" => Token::LITERAL,    "line" => 15, "col" => 16),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 15, "col" => 20),
            array("value" => "{",          "type" => Token::OPERATOR,   "line" => 15, "col" => 30),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 15, "col" => 32),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 15, "col" => 42),
            array("value" => "'\"'",       "type" => Token::LITERAL,    "line" => 15, "col" => 44),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 15, "col" => 48),
            array("value" => "character",  "type" => Token::IDENTIFIER, "line" => 16, "col" => 3),
            array("value" => "=",          "type" => Token::OPERATOR,   "line" => 16, "col" => 14),
            array("value" => '"a"',        "type" => Token::LITERAL,    "line" => 16, "col" => 16),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 20),
            array("value" => '"b"',        "type" => Token::LITERAL,    "line" => 16, "col" => 22),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 26),
            array("value" => '"c"',        "type" => Token::LITERAL,    "line" => 16, "col" => 28),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 32),
            array("value" => '"d"',        "type" => Token::LITERAL,    "line" => 16, "col" => 34),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 38),
            array("value" => '"e"',        "type" => Token::LITERAL,    "line" => 16, "col" => 40),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 44),
            array("value" => '"f"',        "type" => Token::LITERAL,    "line" => 16, "col" => 46),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 50),
            array("value" => '"g"',        "type" => Token::LITERAL,    "line" => 16, "col" => 52),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 56),
            array("value" => '"h"',        "type" => Token::LITERAL,    "line" => 16, "col" => 58),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 62),
            array("value" => '"i"',        "type" => Token::LITERAL,    "line" => 16, "col" => 64),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 68),
            array("value" => '"j"',        "type" => Token::LITERAL,    "line" => 16, "col" => 70),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 74),
            array("value" => '"k"',        "type" => Token::LITERAL,    "line" => 16, "col" => 76),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 80),
            array("value" => '"l"',        "type" => Token::LITERAL,    "line" => 16, "col" => 82),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 86),
            array("value" => '"m"',        "type" => Token::LITERAL,    "line" => 16, "col" => 88),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 16, "col" => 92),
            array("value" => '"n"',        "type" => Token::LITERAL,    "line" => 16, "col" => 94),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 14),
            array("value" => '"o"',        "type" => Token::LITERAL,    "line" => 17, "col" => 16),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 20),
            array("value" => '"p"',        "type" => Token::LITERAL,    "line" => 17, "col" => 22),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 26),
            array("value" => '"q"',        "type" => Token::LITERAL,    "line" => 17, "col" => 28),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 32),
            array("value" => '"r"',        "type" => Token::LITERAL,    "line" => 17, "col" => 34),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 38),
            array("value" => '"s"',        "type" => Token::LITERAL,    "line" => 17, "col" => 40),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 44),
            array("value" => '"t"',        "type" => Token::LITERAL,    "line" => 17, "col" => 46),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 50),
            array("value" => '"u"',        "type" => Token::LITERAL,    "line" => 17, "col" => 52),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 56),
            array("value" => '"v"',        "type" => Token::LITERAL,    "line" => 17, "col" => 58),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 62),
            array("value" => '"w"',        "type" => Token::LITERAL,    "line" => 17, "col" => 64),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 68),
            array("value" => '"x"',        "type" => Token::LITERAL,    "line" => 17, "col" => 70),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 74),
            array("value" => '"y"',        "type" => Token::LITERAL,    "line" => 17, "col" => 76),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 17, "col" => 80),
            array("value" => '"z"',        "type" => Token::LITERAL,    "line" => 17, "col" => 82),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 14),
            array("value" => '"A"',        "type" => Token::LITERAL,    "line" => 18, "col" => 16),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 20),
            array("value" => '"B"',        "type" => Token::LITERAL,    "line" => 18, "col" => 22),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 26),
            array("value" => '"C"',        "type" => Token::LITERAL,    "line" => 18, "col" => 28),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 32),
            array("value" => '"D"',        "type" => Token::LITERAL,    "line" => 18, "col" => 34),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 38),
            array("value" => '"E"',        "type" => Token::LITERAL,    "line" => 18, "col" => 40),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 44),
            array("value" => '"F"',        "type" => Token::LITERAL,    "line" => 18, "col" => 46),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 50),
            array("value" => '"G"',        "type" => Token::LITERAL,    "line" => 18, "col" => 52),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 56),
            array("value" => '"H"',        "type" => Token::LITERAL,    "line" => 18, "col" => 58),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 62),
            array("value" => '"I"',        "type" => Token::LITERAL,    "line" => 18, "col" => 64),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 68),
            array("value" => '"J"',        "type" => Token::LITERAL,    "line" => 18, "col" => 70),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 74),
            array("value" => '"K"',        "type" => Token::LITERAL,    "line" => 18, "col" => 76),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 80),
            array("value" => '"L"',        "type" => Token::LITERAL,    "line" => 18, "col" => 82),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 86),
            array("value" => '"M"',        "type" => Token::LITERAL,    "line" => 18, "col" => 88),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 18, "col" => 92),
            array("value" => '"N"',        "type" => Token::LITERAL,    "line" => 18, "col" => 94),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 14),
            array("value" => '"O"',        "type" => Token::LITERAL,    "line" => 19, "col" => 16),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 20),
            array("value" => '"P"',        "type" => Token::LITERAL,    "line" => 19, "col" => 22),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 26),
            array("value" => '"Q"',        "type" => Token::LITERAL,    "line" => 19, "col" => 28),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 32),
            array("value" => '"R"',        "type" => Token::LITERAL,    "line" => 19, "col" => 34),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 38),
            array("value" => '"S"',        "type" => Token::LITERAL,    "line" => 19, "col" => 40),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 44),
            array("value" => '"T"',        "type" => Token::LITERAL,    "line" => 19, "col" => 46),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 50),
            array("value" => '"U"',        "type" => Token::LITERAL,    "line" => 19, "col" => 52),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 56),
            array("value" => '"V"',        "type" => Token::LITERAL,    "line" => 19, "col" => 58),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 62),
            array("value" => '"W"',        "type" => Token::LITERAL,    "line" => 19, "col" => 64),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 68),
            array("value" => '"X"',        "type" => Token::LITERAL,    "line" => 19, "col" => 70),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 74),
            array("value" => '"Y"',        "type" => Token::LITERAL,    "line" => 19, "col" => 76),
            array("value" => "|",          "type" => Token::OPERATOR,   "line" => 19, "col" => 80),
            array("value" => '"Z"',        "type" => Token::LITERAL,    "line" => 19, "col" => 82),
            array("value" => ".",          "type" => Token::OPERATOR,   "line" => 19, "col" => 86),
            array("value" => "}",          "type" => Token::OPERATOR,   "line" => 20, "col" => 1),
            array("value" => "",           "type" => Token::EOF,        "line" => 20, "col" => 1),
        ), "testgrammar_1.ebnf");

$grammar = <<<EOD
comment := literal .
EOD;
        $scanner = new Scanner(trim($grammar));
        try {
            while ($scanner->hasNextToken()) {
                $scanner->nextToken();
            }
            $this->fail("Expected exception not thrown!");
        } catch (SyntaxtException $e) {
            $this->assertEquals("Expecting '=' but seen ' '", $e->getMessage());
            $pos = $e->getPosition();
            $this->assertEquals(1, $pos->getLine());
            $this->assertEquals(11, $pos->getColumn());
        }
    }

    private function assertTokens($grammar, array $expectations, $msg = "") {
        $scanner = new Scanner(trim($grammar));
        $count   = 0;

        while ($scanner->hasNextToken()) {
            $scanner->nextToken();
            $token = $scanner->currentToken();
            $expectation = $expectations[$count];

            $this->assertInstanceOf("de\weltraumschaf\ebnf\Token", $token, "{$msg} {$count}: {$token->getValue()}");
            $this->assertEquals($expectation["type"], $token->getType(), "{$msg} {$count} type: {$token->getValue()}");
            $this->assertEquals($expectation["value"], $token->getValue(), "{$msg} {$count} value: {$token->getValue()}");

            $position = $token->getPosition();
            $this->assertInstanceOf("de\weltraumschaf\ebnf\Position", $position, "{$msg} {$count}: {$token->getValue()}");
            $this->assertNull($position->getFile(), $count);
            $this->assertEquals($expectation["line"], $position->getLine(), "{$msg} {$count} line: {$token->getValue()}");
            $this->assertEquals($expectation["col"], $position->getColumn(), "{$msg} {$count} col: {$token->getValue()}");
            $count++;
        }

        $this->assertEquals(count($expectations), $count, "{$msg}: Not enough tokens!");

        foreach (array(1, 3, 5, 20, 200000) as $backTracks) {
            $index = $count - ($backTracks + 1);

            if ($index < 0) {
                try {
                    $token = $scanner->backtrackToken($backTracks);
                    $this->fail("Expected excpetion not thrown!");
                } catch (\InvalidArgumentException $e) {
                }
                continue;
            } else {
                $token = $scanner->backtrackToken($backTracks);
            }

            $expectation = $expectations[$index];

            $this->assertInstanceOf("de\weltraumschaf\ebnf\Token", $token);
            $this->assertEquals($expectation["type"], $token->getType());
            $this->assertEquals($expectation["value"], $token->getValue());

            $position = $token->getPosition();
            $this->assertInstanceOf("de\weltraumschaf\ebnf\Position", $position);
            $this->assertNull($position->getFile(), $count);
            $this->assertEquals($expectation["line"], $position->getLine());
            $this->assertEquals($expectation["col"], $position->getColumn());
        }
    }

    public function testRaiseError() {
        $pos = new Position(5, 3);
        $s = $this->getMock("de\weltraumschaf\ebnf\Scanner", array("createPosition"), array(""));
        $s->expects($this->once())
          ->method("createPosition")
          ->will($this->returnValue($pos));
        $msg = "an error";

        try {
            $s->raiseError($msg);
            $this->fail("Expected exception not thrown!");
        } catch (SyntaxtException $e) {
            $this->assertSame($e->getPosition(), $pos);
            $this->assertEquals("Syntax error: {$msg} at {$pos} (code: 0)!", $e->__toString());
        }
    }
}
