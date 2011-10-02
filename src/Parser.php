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
 * @see {Position}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Position.php';

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
     * Initialized with a scanner which produced the token stream.
     *
     * @param Scanner $scanner
     */
    public function __construct(Scanner $scanner) {
        $this->scanner = $scanner;
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
        $dom    = new DOMDocument();
        $syntax = $dom->createElement("syntax");
        $syntax->setAttribute('meta', self::META);
        $dom->appendChild($syntax);
        $this->scanner->nextToken();
        $token = $this->scanner->currentToken();
        $this->scanner->nextToken();

        if ($token->isType(Token::LITERAL)) {
            $syntax->setAttribute('title', stripcslashes(substr($token->getValue(), 1, strlen($token->getValue()) - 2)));
            $token = $this->scanner->currentToken();
            $this->scanner->nextToken();
        }

        if (!$this->assertToken($token, Token::OPERATOR, '{')) {
            throw new SyntaxtException("Syntax must start with '{'", $token->getPosition());
        }

        $token = $this->scanner->currentToken();

        while ($this->scanner->hasNextToken() && $token->isType(Token::IDENTIFIER)) {
            $syntax->appendChild($this->parseProduction($dom));
            $token = $this->scanner->currentToken();
        }

        $this->scanner->nextToken();

        if (!$this->assertToken($token, Token::OPERATOR, '}')) {
            throw new SyntaxtException("Syntax must end with '}'", $token->getPosition());
        }

        if ($this->scanner->hasNextToken()) {
            $token = $this->scanner->currentToken();

            if ($token->isType(Token::LITERAL)) {
                $syntax->setAttribute('meta', stripcslashes(substr($token->getValue(), 1, strlen($token->getValue()) - 2)));
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
        $token = $this->scanner->currentToken();
        $this->scanner->nextToken();

        if (!$token->isType(Token::IDENTIFIER)) {
            throw new SyntaxtException("Production must start with an identifier", $token->getPosition());
        }

        $production = $dom->createElement("rule");
        $production->setAttribute('name', $token->getValue());
        $token = $this->scanner->currentToken();
        $this->scanner->nextToken();

        if (!$this->assertToken($token, Token::OPERATOR, "=")) {
            throw new SyntaxtException("Identifier must be followed by '='", $token->getPosition());
        }

        $production->appendChild($this->parseExpression($dom));
        $token = $this->scanner->currentToken();
        $this->scanner->nextToken();

        if (!$this->assertToken($token, Token::OPERATOR, '.') && !$this->assertToken($token, Token::OPERATOR, ';')) {
            throw new SyntaxtException("Rule must end with '.' or ';'", $token->getPosition());
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
        $choise->appendChild($this->parseTerm($dom));
        $token = $this->scanner->currentToken();
        $mul   = false;

        while ($this->assertToken($token, Token::OPERATOR, '|')) {
            $this->scanner->nextToken();
            $choise->appendChild($this->parseTerm($dom));
            $token = $this->scanner->currentToken();
            $mul   = true;
        }

        if ($mul) {
            return $choise;
        }

        return $choise->removeChild($choise->firstChild);
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
        $factor   = $this->parseFactor($dom);
        $sequence->appendChild($factor);
        $token = $this->scanner->currentToken();
        $mul   = false;

        while ($token->isNotEqual('.') && $token->isNotEqual('=') && $token->isNotEqual('|') &&
               $token->isNotEqual(')') && $token->isNotEqual(']') && $token->isNotEqual('}')) {
            $sequence->appendChild($this->parseFactor($dom));
            $token = $this->scanner->currentToken();
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
        $token = $this->scanner->currentToken();
        $this->scanner->nextToken();

        if ($token->isType(Token::IDENTIFIER)) {
            $identifier = $dom->createElement(self::NODE_TYPE_IDENTIFIER);
            $identifier->setAttribute('value', $token->getValue());

            return $identifier;
        }

        if ($token->isType(Token::LITERAL)) {
            $literal = $dom->createElement(self::NODE_TYPE_TERMINAL);
            $literal->setAttribute('value', stripcslashes(substr($token->getValue(), 1, strlen($token->getValue()) - 2)));

            return $literal;
        }

        if ($this->assertToken($token, Token::OPERATOR, '(')) {
            $expression = $this->parseExpression($dom);
            $token = $this->scanner->currentToken();
            $this->scanner->nextToken();

            if (!$this->assertToken($token, Token::OPERATOR, ')')) {
                throw new SyntaxtException("Group must end with ')'", $token->getPosition());
            }

            return $expression;
        }

        if ($this->assertToken($token, Token::OPERATOR, '[')) {
            $option = $dom->createElement(self::NODE_TYPE_OPTION);
            $option->appendChild($this->parseExpression($dom));
            $token = $this->scanner->currentToken();
            $this->scanner->nextToken();

            if (!$this->assertToken($token, Token::OPERATOR, ']')) {
                throw new SyntaxtException("Option must end with ']'", $token->getPosition());
            }

            return $option;
        }

        if ($this->assertToken($token, Token::OPERATOR, '{')) {
            $loop = $dom->createElement(self::NODE_TYPE_LOOP);
            $loop->appendChild($this->parseExpression($dom));
            $token = $this->scanner->currentToken();
            $this->scanner->nextToken();

            if (!$this->assertToken($token, Token::OPERATOR, '}')) {
                throw new SyntaxtException("Loop must end with '}'", $token->getPosition());
            }

            return $loop;
        }

        throw new SyntaxtException("Factor expected", $token->getPosition());
    }

    /**
     * Checks wheter a token is of a type and is equalt to a string literal or not.
     *
     * @param Token $token
     * @param int $type
     * @param string $value
     * @return bool
     */
    private function assertToken(Token $token, $type, $value) {
        return $token->isType($type) && $token->isEqual($value);
    }
}
