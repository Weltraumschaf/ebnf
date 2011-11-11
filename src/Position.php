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

/**
 * Represents a token position in the source string.
 *
 * The position contains the line, column and filename where the
 * token occured. The file name is optional.
 * 
 * @package ebnf
 */
class Position {

    /**
     * File of the source string.
     *
     * @var strign
     */
    private $file;

    /**
     * Line of occurence.
     *
     * @var int
     */
    private $line;

    /**
     * Column of occurence.
     *
     * @var int
     */
    private $column;

    /**
     * Initializes imutable object.
     *
     * File is optional e.g. if string is parsed directly without any file.
     *
     * @param int    $line   Line of occurence.
     * @param int    $column Column of occurence.
     * @param string $file   Optional file name.
     */
    function __construct($line, $column, $file = null) {
        $this->line   = (int) $line;
        $this->column = (int) $column;

        if (null !== $file) {
            $this->file = (string) $file;
        }
    }

    /**
     * Returns the file name of the source.
     *
     * May be null.
     *
     * @return string
     */
    public function getFile() {
        return $this->file;
    }

    /**
     * Returns line of occurence in source.
     *
     * @return int
     */
    public function getLine() {
        return $this->line;
    }

    /**
     * Returns column of occurence in source.
     *
     * @return int
     */
    public function getColumn() {
        return $this->column;
    }

    /**
     * Returns human readable string representtion.
     *
     * @return string
     */
    public function __toString() {
        $str = "";

        if ($this->getFile() !== null) {
            $str = "{$this->getFile()} ";
        }

        $str .= "({$this->getLine()}, {$this->getColumn()})";
        return $str;
    }

}
