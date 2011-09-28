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

require_once __DIR__ . DIRECTORY_SEPARATOR . 'Token.php';

/**
 * Description of Parser
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @license http://www.weltraumschaf.de/the-beer-ware-license.txt THE BEER-WARE LICENSE
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
     * @var Scanner
     */
    private $scanner;

    /**
     *
     * @param Scanner $scanner
     */
    public function __construct(Scanner $scanner) {
        $this->scanner = $scanner;
    }

    /**
     *
     * @return DOMDocument
     */
    public function parse() {
        $tokens = $this->scanner->scan();
        $dom    = new DOMDocument();
        $syntax = $dom->createElement("syntax");
        $syntax->setAttribute('meta', self::META);
        $dom->appendChild($syntax);
        $i = 0;
        $token = $tokens[$i++];

        if ($token['type'] === Token::LITERAL) {
            $syntax->setAttribute('title', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));
            $token = $tokens[$i++];
        }

        if (!$this->checkToken($token, Token::OPERATOR, '{')) {
            throw new Exception("Syntax must start with '{': {$token['pos']}");
        }

        $token = $tokens[$i];

        while ($i < count($tokens) && $token['type'] === Token::IDENTIFIER) {
            $syntax->appendChild($this->parseProduction($dom, $tokens, $i));

            if ($i < count($tokens)) {
                $token = $tokens[$i];
            }
        }

        $i++;

        if (!$this->checkToken($token, Token::OPERATOR, '}')) {
            throw new Exception("Syntax must end with '}': " . $tokens[count($tokens) - 1]['pos']);
        }

        if ($i < count($tokens)) {
            $token = $tokens[$i];

            if ($token['type'] === Token::LITERAL) {
                $syntax->setAttribute('meta', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));
            }
        }

        return $dom;
    }

    private function parseProduction(&$dom, &$tokens, &$i) {
        $token = $tokens[$i++];

        if ($token['type'] !== Token::IDENTIFIER) {
            throw new Exception("Production must start with an identifier'{': {$token['pos']}");
        }

        $production = $dom->createElement("rule");
        $production->setAttribute('name', $token['value']);
        $token = $tokens[$i++];

        if (!$this->checkToken($token, Token::OPERATOR, "=")) {
            throw new Exception("Identifier must be followed by '=': {$token['pos']}");
        }

        $production->appendChild($this->parseExpression($dom, $tokens, $i));
        $token = $tokens[$i++];

        if (!$this->checkToken($token, Token::OPERATOR, '.') && !$this->checkToken($token, Token::OPERATOR, ';')) {
            throw new Exception("Rule must end with '.' or ';' : {$token['pos']}");
        }

        return $production;
    }

    private function parseExpression(&$dom, &$tokens, &$i) {
        $choise = $dom->createElement("choise");
        $choise->appendChild($this->parseTerm($dom, $tokens, $i));
        $token = $tokens[$i];
        $mul = false;

        while ($this->checkToken($token, Token::OPERATOR, '|')) {
            $i++;
            $choise->appendChild($this->parseTerm($dom, $tokens, $i));
            $token = $tokens[$i];
            $mul = true;
        }

        return $mul ? $choise : $choise->removeChild($choise->firstChild);
    }

    private function parseTerm($dom, &$tokens, &$i) {
        $sequence = $dom->createElement(self::NODE_TYPE_SEQUENCE);
        $factor = $this->parseFactor($dom, $tokens, $i);
        $sequence->appendChild($factor);
        $token = $tokens[$i];
        $mul = false;

        while ($token['value'] !== '.' && $token['value'] !== '=' && $token['value'] !== '|' &&
               $token['value'] !== ')' && $token['value'] !== ']' && $token['value'] !== '}') {
            $sequence->appendChild($this->parseFactor($dom, $tokens, $i));
            $token = $tokens[$i];
            $mul   = true;
        }

        if ($mul) {
            return $sequence;
        }

        return $sequence->removeChild($sequence->firstChild);
    }

    private function parseFactor(&$dom, &$tokens, &$i) {
        $token = $tokens[$i++];

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
            $expression = $this->parseExpression($dom, $tokens, $i);
            $token = $tokens[$i++];

            if (!$this->checkToken($token, Token::OPERATOR, ')')) {
                throw new Exception("Group must end with ')': {$token['pos']}");
            }

            return $expression;
        }

        if ($this->checkToken($token, Token::OPERATOR, '[')) {
            $option = $dom->createElement(self::NODE_TYPE_OPTION);
            $option->appendChild($this->parseExpression($dom, $tokens, $i));
            $token = $tokens[$i++];

            if (!$this->checkToken($token, Token::OPERATOR, ']')) {
                throw new Exception("Option must end with ']': {$token['pos']}");
            }

            return $option;
        }

        if ($this->checkToken($token, Token::OPERATOR, '{')) {
            $loop = $dom->createElement(self::NODE_TYPE_LOOP);
            $loop->appendChild($this->parseExpression($dom, $tokens, $i));
            $token = $tokens[$i++];

            if (!$this->checkToken($token, Token::OPERATOR, '}')) {
                throw new Exception("Loop must end with '}': {$token['pos']}");
            }

            return $loop;
        }

        throw new Exception("Factor expected: {$token['pos']}");
    }

    private function checkToken($token, $type, $value) {
        return $token['type'] === $type && $token['value'] === $value;
    }
}
