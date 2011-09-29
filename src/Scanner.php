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
 * @see {Token}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Token.php';
/**
 * @see {SyntaxtException}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'SyntaxtException.php';
/**
 * @see {Position}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Position.php';

/**
 * Scanns an input string for EBNF syntax tokens.
 *
 * This class provides only one public method which returns
 * the scanned tokens as an array of strings.
 *
 * On lexiacl syntax errors a SyntaxException will be thrown.
 *
 * @todo Return an array of Token objects with according position objects.
 * @todo Throw SyntaxException with according position of syntax error.
 */
class Scanner {

    /**
     * Definiton of PREGs for tokens.
     *
     * @var array
     */
    private $lexemes = array(
        array('type' => Token::OPERATOR,   'expr' => '[={}()|.;[\]]'),
        array('type' => Token::LITERAL,    'expr' => "\"[^\"]*\""),
        array('type' => Token::LITERAL,    'expr' => "'[^']*'"),
        array('type' => Token::IDENTIFIER, 'expr' => "[a-zA-Z0-9_-]+"),
        array('type' => Token::WHITESPACE, 'expr' => "\\s+")
    );

    /**
     * Grammar string.
     *
     * @var string
     */
    private $input;
    /**
     * File from where the gramamr comes.
     *
     * @var string
     */
    private $file;
    /**
     * Current character position.
     *
     * @var int
     */
    private $current;
    /**
     * Length of input.
     *
     * @var int
     */
    private $length;
    private $column;
    private $line;

    /**
     * Initializes the scanner with a grammar string.
     *
     * Can take an optional file frome where the input strims was loaded.
     * This is only for error handling.
     *
     * @param string $input
     * @param string $file  .
     */
    public function __construct($input, $file = null) {
        $this->input = (string)$input;

        if (null !== $file) {
            $this->file = (String)$file;
        }

        $this->current = -1;
        $this->length  = strlen($this->input);
        $this->column  = 0;
        $this->line    = 1;
    }

    public static function isAlpha($c) {
        $o = ord($c);
        return $o > 64 && $o < 91 || $o > 96 && $o < 123;
    }

    public static  function isNum($c) {
        $o = ord($c);
        return $o > 47 && $o < 58;
    }

    public static  function isAlphaNum($c) {
        return self::isAlpha($c) || self::isNum($c);
    }

    public static  function isOperator($c) {
        return "{" === $c || "}" === $c ||
               "(" === $c || ")" === $c ||
               "[" === $c || "]" === $c ||
               "=" === $c || "." === $c;
    }

    public static  function isWhiteSpace($c) {
        return " " === $c || "\t" === $c || "\n" === $c || "\r" === $c;
    }

    public static function isQuote($c) {
        return "'" === $c || '"' === $c;
    }

    /**
     * Returns an array of tokens.
     *
     * @throws SyntaxtException
     * @return array
     */
    public function scan() {
        $i = 0;
        $n = strlen($this->input);
        $m = count($this->lexemes);
        $tokens = array();

        while ($i < $n) {
            $j = 0;

            while ($j < $m && preg_match("/^{$this->lexemes[$j]['expr']}/", substr($this->input, $i), $matches) === 0) {
                $j++;
            }

            if ($j < $m) {
                if ($this->lexemes[$j]['type'] !== Token::WHITESPACE) {
                    $tokens[] = array(
                        'type'  => $this->lexemes[$j]['type'],
                        'value' => $matches[0],
                        'pos'   => $i
                    );
                }

                $i += strlen($matches[0]);
            } else {
                $pos = new Position(0, 0, $this->getFile());
                $msg = "Invalid token at position {$i}: " . substr($this->input, $i, 10) . "...";
                throw new SyntaxtException($msg, $pos);
            }
        }

        return $tokens;
    }

    /**
     * Returns the file frm where the input stream comes.
     *
     * May be null.
     *
     * @return string
     */
    public function getFile() {
        return $this->file;
    }

    private function hasNextChar() {
        return $this->current < $this->length - 1;
    }

    private function nextChar() {
        $this->current++;
        $this->column++;
    }

    private function currentChar() {
        return $this->input[$this->current];
    }

    private function backup() {
        $this->current--;
        $this->column--;
    }

    private function peek() {
        $this->nextChar();
        $c = $this->currentChar();
        $this->backup();
        return $c;
    }
    private function raiseError($msg) {
        throw new SyntaxtException($msg, $this->createPosition());
    }

    private function createPosition() {
        return new Position($this->line, $this->column, $this->file);
    }

    public function next() {
        while ($this->hasNextChar()) {
            $this->nextChar();

            if (self::isAlpha($this->currentChar())) {
                return $this->scannIdentifier();
            } else if (self::isQuote($this->currentChar())) {
                return $this->scanLiteral();
            } else if (self::isOperator($this->currentChar())) {
                return $this->scanOperator();
            } else if (self::isWhiteSpace($this->currentChar())) {
                // ignore white spaces
            } else {
                $this->raiseError("Invalid character!");
            }

            if ("\n" === $this->currentChar() || "\r" === $this->currentChar()) {
                $this->line++;
                $this->column = 1;
            }
        }

        return new Token(Token::EOF, "EOF", $this->createPosition());
    }

    protected function scannIdentifier() {
        $pos = $this->createPosition();
        $str = $this->currentChar();

        while ($this->hasNextChar()) {
            $this->nextChar();

            if (self::isAlphaNum($this->currentChar()) || "-" === $this->currentChar()) {
                $str .= $this->currentChar();
            } else {
                $this->backup();
                break;
            }
        }

        return new Token(Token::IDENTIFIER, $str, $pos);
    }

    protected function scanLiteral() {
        $pos = $this->createPosition();
        $str = $this->currentChar();

        while ($this->hasNextChar()) {
            $this->nextChar();
            $str .= $this->currentChar();

            if (self::isQuote($this->currentChar())) {
                break;
            }
        }

        return new Token(Token::LITERAL, $str, $pos);
    }

    protected function scanOperator() {
        if ($this->hasNextChar() && !self::isWhiteSpace($this->peek())) {
            $this->raiseError("After operator a whitespace is expected!");
        }

        return new Token(Token::OPERATOR, $this->currentChar(), $this->createPosition());
    }
}
