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

require_once "ScannerHelper.php";

/**
 * Test cases for {ScannerHelper}.
 */
class ScannerHelperTest extends \PHPUnit_Framework_TestCase {
    
    private $ops = array("(", ")", "[", "]", "{", "}", "=", ".", ";", "|", ",", "-", ":");
    private $lowAlpha = array("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
        "s", "t", "u", "v", "w", "x", "y", "z");
    private $upAlpha = array("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z");
    private $nums = array("1", "2", "3", "4", "5",  "6",  "7",  "8",  "9", "0");
    private $ws = array(" ", "\n", "\r", "\t");

    public function testIsAlpha() {
        foreach ($this->lowAlpha as $c) {
            $this->assertTrue(ScannerHelper::isAlpha($c), $c);
        }

        foreach ($this->upAlpha as $c) {
            $this->assertTrue(ScannerHelper::isAlpha($c), $c);
        }

        foreach ($this->nums as $c) {
            $this->assertFalse(ScannerHelper::isAlpha($c), $c);
        }

        foreach ($this->ops as $c) {
            $this->assertFalse(ScannerHelper::isAlpha($c), $c);
        }

        foreach ($this->ws as $c) {
            $this->assertFalse(ScannerHelper::isAlpha($c), $c);
        }
    }

    public function testIsNum() {
        foreach ($this->nums as $c) {
            $this->assertTrue(ScannerHelper::isNum($c), $c);
        }

        foreach ($this->ops as $c) {
            $this->assertFalse(ScannerHelper::isNum($c), $c);
        }

        foreach ($this->lowAlpha as $c) {
            $this->assertFalse(ScannerHelper::isNum($c), $c);
        }

        foreach ($this->upAlpha as $c) {
            $this->assertFalse(ScannerHelper::isNum($c), $c);
        }

        foreach ($this->ws as $c) {
            $this->assertFalse(ScannerHelper::isNum($c), $c);
        }
    }

    public function testIsAlphaNum() {
        foreach ($this->nums as $c) {
            $this->assertTrue(ScannerHelper::isAlphaNum($c), $c);
        }

        foreach ($this->lowAlpha as $c) {
            $this->assertTrue(ScannerHelper::isAlphaNum($c), $c);
        }

        foreach ($this->upAlpha as $c) {
            $this->assertTrue(ScannerHelper::isAlphaNum($c), $c);
        }

        foreach ($this->ops as $c) {
            $this->assertFalse(ScannerHelper::isAlphaNum($c), $c);
        }

        foreach ($this->ws as $c) {
            $this->assertFalse(ScannerHelper::isAlphaNum($c), $c);
        }
    }

    public function testIsOperator() {
        foreach ($this->ops as $c) {
            $this->assertTrue(ScannerHelper::isOperator($c), $c);
        }

        foreach ($this->nums as $c) {
            $this->assertFalse(ScannerHelper::isOperator($c), $c);
        }

        foreach ($this->lowAlpha as $c) {
            $this->assertFalse(ScannerHelper::isOperator($c), $c);
        }

        foreach ($this->upAlpha as $c) {
            $this->assertFalse(ScannerHelper::isOperator($c), $c);
        }

        foreach ($this->ws as $c) {
            $this->assertFalse(ScannerHelper::isOperator($c), $c);
        }
    }

    public function testIsWhiteSpace() {
        foreach ($this->ws as $c) {
            $this->assertTrue(ScannerHelper::isWhiteSpace($c), $c);
        }

        foreach ($this->ops as $c) {
            $this->assertFalse(ScannerHelper::isWhiteSpace($c), $c);
        }

        foreach ($this->nums as $c) {
            $this->assertFalse(ScannerHelper::isWhiteSpace($c), $c);
        }

        foreach ($this->lowAlpha as $c) {
            $this->assertFalse(ScannerHelper::isWhiteSpace($c), $c);
        }

        foreach ($this->upAlpha as $c) {
            $this->assertFalse(ScannerHelper::isWhiteSpace($c), $c);
        }
    }

    public function testIsQuote() {
        $this->assertTrue(ScannerHelper::isQuote('"'));
        $this->assertTrue(ScannerHelper::isQuote("'"));

        foreach ($this->ws as $c) {
            $this->assertFalse(ScannerHelper::isQuote($c), $c);
        }

        foreach ($this->ops as $c) {
            $this->assertFalse(ScannerHelper::isQuote($c), $c);
        }

        foreach ($this->nums as $c) {
            $this->assertFalse(ScannerHelper::isQuote($c), $c);
        }

        foreach ($this->lowAlpha as $c) {
            $this->assertFalse(ScannerHelper::isQuote($c), $c);
        }

        foreach ($this->upAlpha as $c) {
            $this->assertFalse(ScannerHelper::isQuote($c), $c);
        }
    }

    public function testIsEquals() {
        $this->assertTrue(ScannerHelper::isEquals("-", array("-", "_")));
        $this->assertTrue(ScannerHelper::isEquals("_", array("-", "_")));
        $this->assertFalse(ScannerHelper::isEquals("a", array("-", "_")));
    }
}
