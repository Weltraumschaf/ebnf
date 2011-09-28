<?php

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
