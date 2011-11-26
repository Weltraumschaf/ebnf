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
 * @author  Vincent Tscherter <tscherter@karmin.ch>
 * @author  Sven Strittmatter <ich@weltraumschaf.de>
 * @package ebnf
 */

namespace de\weltraumschaf\ebnf;

/**
 * Helper methods for the scanner.
 *
 * @package ebnf
 * @version @@version@@
 */
class ScannerHelper {
    /**
     * Checks whether a character is a alpha [a-zA-Z].
     *
     * @param string $c A single character.
     *
     * @return bool
     */
    public static function isAlpha($c) {
        $o = ord($c);
        return $o > 64 && $o < 91 || $o > 96 && $o < 123;
    }

    /**
     * Checks whether a character is a number [0-9].
     *
     * @param string $c A single character.
     *
     * @return bool
     */
    public static function isNum($c) {
        $o = ord($c);
        return $o > 47 && $o < 58;
    }

    /**
     * Checks whether a character is a number or alpha [0-9a-zA-Z].
     *
     * @param string $c A single character.
     *
     * @return bool
     */
    public static function isAlphaNum($c) {
        return self::isAlpha($c) || self::isNum($c);
    }

    /**
     * Checks whether a character is a operator.
     *
     * @param string $c A single character.
     *
     * @return bool
     */
    public static function isOperator($c) {
        return in_array($c, array("{", "}", "(", ")", "[", "]", ",", ";", ".", ":", "|", "=", "-"));
    }

    /**
     * Checks whether a character is a whitespace.
     *
     * @param string $c A single character.
     *
     * @return bool
     */
    public static function isWhiteSpace($c) {
        return " " === $c || "\t" === $c || "\n" === $c || "\r" === $c;
    }

    /**
     * Checks whether a character is a quote ["|'].
     *
     * @param string $c A single character.
     *
     * @return bool
     */
    public static function isQuote($c) {
        return "'" === $c || '"' === $c;
    }

    /**
     * Tests a given character if it is equal to ona of the passed test characters.
     *
     * @param string $c     Character to test.
     * @param array  $chars Array of characters to test against.
     *
     * @return bool
     */
    public static function isEquals($c, array $chars) {
        foreach ($chars as $char) {
            if ($c === $char) {
                return true;
            }
        }

        return false;
    }
}
