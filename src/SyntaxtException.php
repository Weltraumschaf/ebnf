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

use \Exception as Exception;

/**
 * Exception for signaling syntax errors.
 *
 * Provides a {Position} where in the input stream the syntax error occured.
 * 
 * @package ebnf
 */
class SyntaxtException extends Exception {

    /**
     * Where in the source the exception occured.
     *
     * @var Position
     */
    private $position;

    /**
     * Initializes the exception.
     *
     * @param string    $message  Error message.
     * @param Position  $pos      Where the error occured.
     * @param int       $code     Optional error code.
     * @param Exception $previous Optional previous exception.
     */
    public function __construct($message, Position $pos, $code = 0, Exception $previous = null) {
        parent::__construct($message, $code, $previous);
        $this->position = $pos;
    }

    /**
     * Returns the position where the error occured.
     *
     * @return Position
     */
    public function getPosition() {
        return $this->position;
    }

    /**
     * Human readable representation.
     *
     * @return string
     */
    public function __toString() {
        return "Syntax error: {$this->getMessage()} at {$this->getPosition()} (code: {$this->getCode()})!";
    }

}
