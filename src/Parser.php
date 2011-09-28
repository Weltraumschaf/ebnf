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

use \DOMDocument as DOMDocument;
use \DOMElement  as DOMElement;

/**
 * @see {Token}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Token.php';
/**
 * @see {SyntaxtException}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'SyntaxtException.php';

/**
 * Parses a stream of EBNF tokens and generate a XML DOM tree.
 *
 * This class provides only one public method which provides returns
 * the syntax tree as XML DOM tree.
 *
 * @todo Use objects of type Token instead of strings.
 * @todo Throw SyntaxtException with propper positions.
 */
class Parser {
    const META = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";

    const NODE_TYPE_IDENTIFIER = "identifier";
    const NODE_TYPE_TERMINAL   = "terminal";
    const NODE_TYPE_OPTION     = "option";
    const NODE_TYPE_LOOP       = "loop";
    const NODE_TYPE_SEQUENCE   = "sequence";
    const NODE_TYPE_CHOISE     = "choise";
    const NODE_TYPE_SYNTAX     = "syntax";

    /**
     * Used to receive the tokens.
     *
     * @var Scanner
     */
    private $scanner;
    /**
     * Current token.
     *
     * @var int
     */
    private $current;
    /**
     * Array of tokens.
     *
     * @var array
     */
    private $tokens;

    /**
     * Initialized with a scanner which produced the token stream.
     *
     * @param Scanner $scanner
     */
    public function __construct(Scanner $scanner) {
        $this->scanner = $scanner;
        $this->current = 0;
    }

    /**
     * Parses the EBNF tokens and returns a syntax tree as DOMDocument object on success.
     *
     * On semantic syntax errors a SyntaxtException will be thrown.
     *
     * @throws SyntaxtException
     * @return DOMDocument
     */
    public function parse() {
        $this->tokens = $this->scanner->scan();
        $dom    = new DOMDocument();
        $syntax = $dom->createElement("syntax");
        $syntax->setAttribute('meta', self::META);
        $dom->appendChild($syntax);
        $this->current = 0;
        $token = $this->tokens[$this->current++];

        if ($token['type'] === Token::LITERAL) {
            $syntax->setAttribute('title', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));
            $token = $this->tokens[$this->current++];
        }

        if (!$this->checkToken($token, Token::OPERATOR, '{')) {
            throw new SyntaxtException("Syntax must start with '{': {$token['pos']}", null);
        }

        $token = $this->tokens[$this->current];

        while ($this->current < count($this->tokens) && $token['type'] === Token::IDENTIFIER) {
            $syntax->appendChild($this->parseProduction($dom, $this->tokens, $this->current));

            if ($this->current < count($this->tokens)) {
                $token = $this->tokens[$this->current];
            }
        }

        $this->current++;

        if (!$this->checkToken($token, Token::OPERATOR, '}')) {
            throw new SyntaxtException("Syntax must end with '}': " . $this->tokens[count($this->tokens) - 1]['pos'], null);
        }

        if ($this->current < count($this->tokens)) {
            $token = $this->tokens[$this->current];

            if ($token['type'] === Token::LITERAL) {
                $syntax->setAttribute('meta', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));
            }
        }

        return $dom;
    }

    /**
     * Parses an EBNF production: rule = identifier "=" expression ( "." | ";" ) .
     *
     * @param DOMElement $dom
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseProduction($dom) {
        $token = $this->tokens[$this->current++];

        if ($token['type'] !== Token::IDENTIFIER) {
            throw new SyntaxtException("Production must start with an identifier'{': {$token['pos']}", null);
        }

        $production = $dom->createElement("rule");
        $production->setAttribute('name', $token['value']);
        $token = $this->tokens[$this->current++];

        if (!$this->checkToken($token, Token::OPERATOR, "=")) {
            throw new SyntaxtException("Identifier must be followed by '=': {$token['pos']}", null);
        }

        $production->appendChild($this->parseExpression($dom, $this->tokens, $this->current));
        $token = $this->tokens[$this->current++];

        if (!$this->checkToken($token, Token::OPERATOR, '.') && !$this->checkToken($token, Token::OPERATOR, ';')) {
            throw new SyntaxtException("Rule must end with '.' or ';' : {$token['pos']}", null);
        }

        return $production;
    }

    /**
     * Parses an EBNF expression: expression = term { "|" term } .
     *
     * @param DOMElement $dom
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseExpression($dom) {
        $choise = $dom->createElement("choise");
        $choise->appendChild($this->parseTerm($dom, $this->tokens, $this->current));
        $token = $this->tokens[$this->current];
        $mul = false;

        while ($this->checkToken($token, Token::OPERATOR, '|')) {
            $this->current++;
            $choise->appendChild($this->parseTerm($dom, $this->tokens, $this->current));
            $token = $this->tokens[$this->current];
            $mul = true;
        }

        return $mul ? $choise : $choise->removeChild($choise->firstChild);
    }

    /**
     * Parses an EBNF term: term = factor { factor } .
     *
     * @param DOMElement $dom
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseTerm($dom) {
        $sequence = $dom->createElement(self::NODE_TYPE_SEQUENCE);
        $factor = $this->parseFactor($dom, $this->tokens, $this->current);
        $sequence->appendChild($factor);
        $token = $this->tokens[$this->current];
        $mul = false;

        while ($token['value'] !== '.' && $token['value'] !== '=' && $token['value'] !== '|' &&
               $token['value'] !== ')' && $token['value'] !== ']' && $token['value'] !== '}') {
            $sequence->appendChild($this->parseFactor($dom, $this->tokens, $this->current));
            $token = $this->tokens[$this->current];
            $mul   = true;
        }

        if ($mul) {
            return $sequence;
        }

        return $sequence->removeChild($sequence->firstChild);
    }

    /**
     * Parses an EBNF factor:
     * factor = identifier
     *        | literal
     *        | "[" expression "]"
     *        | "(" expression ")"
     *        | "{" expression "}" .
     *
     * @param DOMElement $dom
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseFactor($dom) {
        $token = $this->tokens[$this->current++];

        if ($token['type'] === Token::IDENTIFIER) {
            $identifier = $dom->createElement(self::NODE_TYPE_IDENTIFIER);
            $identifier->setAttribute('value', $token['value']);

            return $identifier;
        }

        if ($token['type'] === Token::LITERAL) {
            $literal = $dom->createElement(self::NODE_TYPE_TERMINAL);
            $literal->setAttribute('value', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));

            return $literal;
        }

        if ($this->checkToken($token, Token::OPERATOR, '(')) {
            $expression = $this->parseExpression($dom, $this->tokens, $this->current);
            $token = $this->tokens[$this->current++];

            if (!$this->checkToken($token, Token::OPERATOR, ')')) {
                throw new SyntaxtException("Group must end with ')': {$token['pos']}", null);
            }

            return $expression;
        }

        if ($this->checkToken($token, Token::OPERATOR, '[')) {
            $option = $dom->createElement(self::NODE_TYPE_OPTION);
            $option->appendChild($this->parseExpression($dom, $this->tokens, $this->current));
            $token = $this->tokens[$this->current++];

            if (!$this->checkToken($token, Token::OPERATOR, ']')) {
                throw new SyntaxtException("Option must end with ']': {$token['pos']}", null);
            }

            return $option;
        }

        if ($this->checkToken($token, Token::OPERATOR, '{')) {
            $loop = $dom->createElement(self::NODE_TYPE_LOOP);
            $loop->appendChild($this->parseExpression($dom, $this->tokens, $this->current));
            $token = $this->tokens[$this->current++];

            if (!$this->checkToken($token, Token::OPERATOR, '}')) {
                throw new SyntaxtException("Loop must end with '}': {$token['pos']}", null);
            }

            return $loop;
        }

        throw new SyntaxtException("Factor expected: {$token['pos']}", null);
    }

    private function checkToken($token, $type, $value) {
        return $token['type'] === $type && $token['value'] === $value;
    }
}
