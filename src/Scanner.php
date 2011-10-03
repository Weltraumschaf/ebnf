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
 * This class implements a standard lexical scanner pattern with one
 * character lookahead and interaotr interface for receiving token by token.
 * On lexiacl syntax errors a SyntaxException will be thrown.
 *
 * Example:
 * <code>
 * <?php
 * $grammar = "..."; // the EBNF grammar
 * try {
 *     $scanner = new Scanner($grammar);
 *
 *     while ($scanner->hasNextToken()) {
 *         $scanner->nextToken();
 *         $token = $scanner->currentToken();
 *         echo($token->__toString()) . PHP_EOL;
 *     }
 * } catch (SyntaxtException $e) {
 *     echo $e . PHP_EOL;
 * }
 * </code>
 */
class Scanner {
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
    private $currentCharacter;
    /**
     * Length of input.
     *
     * @var int
     */
    private $inputLength;
    /**
     * The actual scanned column.
     * Begins on 1 on each new line.
     *
     * @var int
     */
    private $column;
    /**
     * The actual scanned line.
     * Begins at 1.
     *
     * @var int
     */
    private $line;
    /**
     * @var Token
     */
    private $currentToken;

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

        $this->currentCharacter = -1;
        $this->inputLength      = strlen($this->input);
        $this->column = 0;
        $this->line   = 1;
    }

    /**
     * Checks whether a character is a alpha [a-zA-Z].
     *
     * @param string $c A single character.
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
     * @return bool
     */
    public static  function isNum($c) {
        $o = ord($c);
        return $o > 47 && $o < 58;
    }

    /**
     * Checks whether a character is a number or alpha [0-9a-zA-Z].
     *
     * @param string $c A single character.
     * @return bool
     */
    public static  function isAlphaNum($c) {
        return self::isAlpha($c) || self::isNum($c);
    }

    /**
     * Checks whether a character is a operator.
     *
     * @param string $c A single character.
     * @return bool
     */
    public static  function isOperator($c) {
        return "{" === $c || "}" === $c ||
               "(" === $c || ")" === $c ||
               "[" === $c || "]" === $c ||
               ";" === $c || "." === $c ||
               "|" === $c || "=" === $c;
    }

    /**
     * Checks whether a character is a whitespace.
     *
     * @param string $c A single character.
     * @return bool
     */
    public static  function isWhiteSpace($c) {
        return " " === $c || "\t" === $c || "\n" === $c || "\r" === $c;
    }

    /**
     * Checks whether a character is a quote ["|'].
     *
     * @param string $c A single character.
     * @return bool
     */
    public static function isQuote($c) {
        return "'" === $c || '"' === $c;
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

    /**
     * Retruns if there is a next character in the input stream.
     *
     * @return bool
     */
    private function hasNextCharacter() {
        return $this->currentCharacter < $this->inputLength - 1;
    }

    /**
     * Increments the character cursor.
     */
    private function nextCharacter() {
        $this->currentCharacter++;
        $this->column++;
    }

    /**
     * Returns the character at the current cursor from the input stream.
     *
     * @return string
     */
    private function currentCharacter() {
        return $this->input[$this->currentCharacter];
    }

    /**
     * Decrements the character cursor.
     */
    private function backupCharacter() {
        $this->currentCharacter--;
        $this->column--;
    }

    /**
     * Returns next character without advancing the cursor.
     *
     * @return string
     */
    private function peekCharacter() {
        $this->nextCharacter();
        $c = $this->currentCharacter();
        $this->backupCharacter();
        return $c;
    }

    /**
     * Throws a {SyntaxException} with the current {Position} in the input stream.
     *
     * @param string $msg
     * @throws SyntaxtException
     */
    private function raiseError($msg) {
        throw new SyntaxtException($msg, $this->createPosition());
    }

    /**
     * Creates a {Position} from the current line and column in the input stream.
     *
     * @return Position
     */
    private function createPosition() {
        return new Position($this->line, $this->column, $this->file);
    }

    /**
     * Returns the current scanned token. May be null if never {nextToken()}
     * was called.
     *
     * @return Token
     */
    public function currentToken() {
        return $this->currentToken;
    }

    /**
     * Returns if there are more tokens. This is allways true if never {nextToken()}
     * was called. No more tokens are indicated if the current token is of type {Token::EOF}.
     *
     * @return bool
     */
    public function hasNextToken() {
        if (null === $this->currentToken) {
            return true;
        }

        return $this->currentToken->getType() !== Token::EOF;
    }

    /**
     * Start the scanning of the next token.
     *
     * This method should be called until {hasNextToken()} returns false.
     *
     * @return void
     */
    public function nextToken() {
        while ($this->hasNextCharacter()) {
            $this->nextCharacter();

            if (self::isAlpha($this->currentCharacter())) {
                $this->currentToken = $this->scannIdentifier();
                return;
            } else if (self::isQuote($this->currentCharacter())) {
                $this->currentToken = $this->scanLiteral();
                return;
            } else if (self::isOperator($this->currentCharacter())) {
                if ("(" === $this->currentCharacter() && "*" === $this->peekCharacter()) {
                    $this->currentToken = $this->scanComment();
                    return;
                }

                $this->currentToken = new Token(Token::OPERATOR, $this->currentCharacter(), $this->createPosition());
                return;
            } else if (self::isWhiteSpace($this->currentCharacter())) {
                // ignore white spaces
            } else {
                $this->raiseError("Invalid character!");
            }

            if ("\n" === $this->currentCharacter() || "\r" === $this->currentCharacter()) {
                $this->line++;
                $this->column = 0;
            }
        }

        $this->currentToken = new Token(Token::EOF, "", $this->createPosition());
    }

    /**
     * Scanns an identifier [a-zA-Z\-_].
     *
     * @return Token
     */
    private function scannIdentifier() {
        $pos = $this->createPosition();
        $str = $this->currentCharacter();

        while ($this->hasNextCharacter()) {
            $this->nextCharacter();

            if (self::isAlphaNum($this->currentCharacter()) || "-" === $this->currentCharacter() || "_" === $this->currentCharacter()) {
                $str .= $this->currentCharacter();
            } else {
                $this->backupCharacter();
                break;
            }
        }

        return new Token(Token::IDENTIFIER, $str, $pos);
    }

    /**
     * Scanns a literal (any character inside single or double quotes.
     *
     * @return Token
     */
    private function scanLiteral() {
        $pos   = $this->createPosition();
        $start = $this->currentCharacter();
        $str   = $start;

        while ($this->hasNextCharacter()) {
            $this->nextCharacter();
            $str .= $this->currentCharacter();

            // Ensure that a lieral opened with " is not temrinated by ' and vice versa.
            if (self::isQuote($this->currentCharacter()) && $this->currentCharacter() === $start) {
                break;
            }
        }

        return new Token(Token::LITERAL, $str, $pos);
    }

    private function scanComment() {
        $pos = $this->createPosition();
        $str = $this->currentCharacter();

        while ($this->hasNextCharacter()) {
            $this->nextCharacter();
            $str .= $this->currentCharacter();

            if ("*" === $this->currentCharacter() && ")" === $this->peekCharacter()) {
                $this->nextCharacter();
                $str .= $this->currentCharacter();
                break;
            }
        }

        return new Token(Token::COMMENT, $str, $pos);
    }
}
