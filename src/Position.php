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

/**
 * Description of Position
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @license http://www.weltraumschaf.de/the-beer-ware-license.txt THE BEER-WARE LICENSE
 */
class Position {
    private $file;
    private $line;
    private $column;

    function __construct($file, $line, $column) {
        $this->file   = (string)$file;
        $this->line   = (int)$line;
        $this->column = (int)$column;
    }

    public function getFile() {
        return $this->file;
    }

    public function getLine() {
        return $this->line;
    }

    public function getColumn() {
        return $this->column;
    }

    public function __toString() {
        return "{$this->getFile()} ({$this->getLine()}, {$this->getColumn()})";
    }

}
