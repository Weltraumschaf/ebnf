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
 * @see Token
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Token.php';
/**
 * @see SyntaxtException
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'SyntaxtException.php';
/**
 * @see Position
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Position.php';
/**
 * @see Choice
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Choice.php';
/**
 * @see Identifier
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Identifier.php';
/**
 * @see Loop
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Loop.php';
/**
 * @see Option
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Option.php';
/**
 * @see Rule
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Rule.php';
/**
 * @see Sequence
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Sequence.php';
/**
 * @see Syntax
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Syntax.php';
/**
 * @see Terminal
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Terminal.php';
/**
 * @see Type
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ast/Type.php';

use \DOMDocument;
use \DOMElement;
use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;
use de\weltraumschaf\ebnf\ast\Type;

/**
 * Parses a stream of EBNF tokens and generate a XML DOM or abstract syntax tree.
 *
 * @package ebnf
 * @version @@version@@
 */
class Parser {

    const DEFAULT_META = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";

    /**
     * Used to receive the tokens.
     *
     * @var Scanner
     */
    private $scanner;
    /**
     * Abstract syntax tree in XML.
     *
     * @deprecated
     * @var DOMDocument
     */
    private $dom;
    /**
     * The abstract syntax tree.
     *
     * @var Syntax
     */
    private $ast;

    /**
     * Initialized with a scanner which produced the token stream.
     *
     * @param Scanner $scanner Provides the token stream.
     */
    public function __construct(Scanner $scanner) {
        $this->scanner = $scanner;
        $this->dom     = new DOMDocument();
        $this->ast     = new Syntax();
    }

    /**
     * Used to get new AST model until parse will return it.
     *
     * @return Syntax
     */
    public function getAst() {
        return $this->ast;
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
        $syntax = $this->dom->createElement(Type::SYNTAX);
        $this->scanner->nextToken();

        if ($this->scanner->currentToken()->isType(Token::LITERAL)) {
            $syntax->setAttribute('title', $this->scanner->currentToken()->getValue(true));
            $this->ast->title = $this->scanner->currentToken()->getValue(true);
            $this->scanner->nextToken();
        }

        if (!$this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '{')) {
            $this->raiseError("Syntax must start with '{'");
        }

        $this->scanner->nextToken();

        while ($this->scanner->hasNextToken() && $this->scanner->currentToken()->isType(Token::IDENTIFIER)) {
            $rules = $this->parseRule();
            $syntax->appendChild($rules[0]);
            $this->ast->addChild($rules[1]);
            $this->scanner->nextToken();
        }

        if (!$this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '}')) {
            $this->raiseError("Syntax must end with '}' but saw {$this->scanner->currentToken()}");
        }

        $this->scanner->nextToken();

        if ($this->scanner->hasNextToken()) {
            if ($this->scanner->currentToken()->isType(Token::LITERAL)) {
                $syntax->setAttribute('meta', $this->scanner->currentToken()->getValue(true));
                $this->ast->meta = $this->scanner->currentToken()->getValue(true);
            } else {
                $this->raiseError("Literal expected as syntax comment");
            }
        } else {
            $syntax->setAttribute('meta', self::DEFAULT_META);
            $this->ast->meta = self::DEFAULT_META;
        }

        $this->dom->appendChild($syntax);
        return $this->dom;
    }

    /**
     * Parses an EBNF production: rule = identifier ( "=" | ":==" | ":" ) expression ( "." | ";" ) .
     *
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseRule() {
        if (!$this->scanner->currentToken()->isType(Token::IDENTIFIER)) {
            $this->raiseError("Production must start with an identifier");
        }

        $production = $this->dom->createElement(Type::RULE);
        $production->setAttribute('name', $this->scanner->currentToken()->getValue());
        $rule       = new Rule($this->ast);
        $rule->name = $this->scanner->currentToken()->getValue();
        $this->scanner->nextToken();

        if (!$this->assertTokens($this->scanner->currentToken(), Token::OPERATOR, array("=", ":", ":=="))) {
            $this->raiseError("Identifier must be followed by '='");
        }

        $this->scanner->nextToken();
        $expressions = $this->parseExpression($rule);
        $production->appendChild($expressions[0]);
        $rule->addChild($expressions[1]);

        if (!$this->assertTokens($this->scanner->currentToken(), Token::OPERATOR, array(".", ";"))) {
            $this->raiseError("Rule must end with '.' or ';'", $this->scanner->backtrackToken(2)->getPosition(true));
        }

        return array($production, $rule);
    }

    /**
     * Parses an EBNF expression: expression = term { "|" term } .
     *
     * @param Node $parent Parent node.
     *
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseExpression(Node $parent) {
        $choice     = $this->dom->createElement(Type::CHOICE);
        $choiceNode = new Choice($parent);
        $term       = $this->parseTerm($choiceNode);
        $choice->appendChild($term[0]);
        $choiceNode->addChild($term[1]);
        $mul = false;

        while ($this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '|')) {
            $this->scanner->nextToken();
            $term = $this->parseTerm($choiceNode);
            $choice->appendChild($term[0]);
            $choiceNode->addChild($term[1]);
            $mul = true;
        }

        if ($mul) {
            return array($choice, $choiceNode);
        }

        return array($choice->removeChild($choice->firstChild), $choiceNode->getIterator()->offsetGet(0));
    }

    /**
     * Parses an EBNF term: term = factor { factor } .
     *
     * @param Node $parent Parent node.
     *
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseTerm(Node $parent) {
        $sequence     = $this->dom->createElement(Type::SEQUENCE);
        $sequenceNode = new Sequence($parent);
        $factor       = $this->parseFactor($sequenceNode);
        $sequence->appendChild($factor[0]);
        $sequenceNode->addChild($factor[1]);
        $this->scanner->nextToken();
        $mul = false;

        while ($this->scanner->currentToken()->isNotEquals(array('.', '=', '|', ')', ']', '}'))) {
            $factor = $this->parseFactor($sequenceNode);
            $sequence->appendChild($factor[0]);
            $sequenceNode->addChild($factor[1]);
            $this->scanner->nextToken();
            $mul = true;
        }

        if ($mul) {
            return array($sequence, $sequenceNode);
        }

        return array($sequence->removeChild($sequence->firstChild), $sequenceNode->getIterator()->offsetGet(0));
    }

    /**
     * Parses an EBNF factor:
     * factor = identifier
     *        | literal
     *        | "[" expression "]"
     *        | "(" expression ")"
     *        | "{" expression "}" .
     *
     * @param Node $parent Parent node.
     *
     * @throws SyntaxtException
     * @return DOMElement
     */
    private function parseFactor(Node $parent) {
        if ($this->scanner->currentToken()->isType(Token::IDENTIFIER)) {
            $identifier = $this->dom->createElement(Type::IDENTIFIER);
            $identifier->setAttribute('value', $this->scanner->currentToken()->getValue());
            $identifierNode        = new Identifier($parent);
            $identifierNode->value = $this->scanner->currentToken()->getValue();
            return array($identifier, $identifierNode);
        }

        if ($this->scanner->currentToken()->isType(Token::LITERAL)) {
            /*if ($this->assertToken($this->scanner->peekToken(), Token::OPERATOR, "::")) {
                echo "range";
                $range = $this->dom->createElement(Type::RANGE);
                $range->setAttribute("from", $this->scanner->currentToken()->getValue(true));
                $this->scanner->nextToken(); // Omit ".." literal.
                $this->scanner->nextToken();
                $range->setAttribute("to", $this->scanner->currentToken()->getValue(true));
                return $range;
            }*/

            $literal = $this->dom->createElement(Type::TERMINAL);
            $literal->setAttribute('value', $this->scanner->currentToken()->getValue(true));
            $literalNode        = new Terminal($parent);
            $literalNode->value = $this->scanner->currentToken()->getValue(true);
            return array($literal, $literalNode);
        }

        if ($this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '(')) {
            $this->scanner->nextToken();
            $expression = $this->parseExpression($parent);

            if (!$this->assertToken($this->scanner->currentToken(), Token::OPERATOR, ')')) {
                $this->raiseError("Group must end with ')'");
            }

            return $expression;
        }

        if ($this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '[')) {
            $option = $this->dom->createElement(Type::OPTION);
            $this->scanner->nextToken();
            $expression = $this->parseExpression($parent);
            $option->appendChild($expression[0]);
            $optionNode = new Option($parent);
            $optionNode->addChild($expression[1]);

            if (!$this->assertToken($this->scanner->currentToken(), Token::OPERATOR, ']')) {
                $this->raiseError("Option must end with ']'");
            }

            return array($option, $optionNode);
        }

        if ($this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '{')) {
            $loop = $this->dom->createElement(Type::LOOP);
            $this->scanner->nextToken();
            $expression = $this->parseExpression($parent);
            $loop->appendChild($expression[0]);
            $loopNode = new Loop($parent);
            $loopNode->addChild($expression[1]);

            if (!$this->assertToken($this->scanner->currentToken(), Token::OPERATOR, '}')) {
                $this->raiseError("Loop must end with '}'");
            }

            return array($loop, $loopNode);
        }

        $this->raiseError("Factor expected");
    }

    /**
     * Checks wheter a token is of a type and is equalt to a string literal or not.
     *
     * @param Token  $token Token to assert.
     * @param int    $type  Token type to assert against.
     * @param string $value Token value to assert against.
     *
     * @return bool
     */
    protected function assertToken(Token $token, $type, $value) {
        return $token->isType($type) && $token->isEqual($value);
    }

    /**
     * Checks wheter a token is of a type and is equalt to a array of string literasl or not.
     *
     * @param Token $token  Token to assert.
     * @param int   $type   type to assert against.
     * @param array $values Array of strings.
     *
     * @return bool
     */
    protected  function assertTokens(Token $token, $type, array $values) {
        foreach ($values as $value) {
            if ($this->assertToken($token, $type, $value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper to raise syntax errors.
     *
     * If no position is passed the one of the current token is used.
     *
     * @param string  $msg The error message.
     * @param Postion $pos The ptional position of the error.
     *
     * @throws SyntaxtException Throws always an exception.
     * @return void
     */
    protected function raiseError($msg, Postion $pos = null) {
        if (null === $pos) {
            $pos = $this->scanner->currentToken()->getPosition();
        }

        throw new SyntaxtException($msg, $pos);
    }
}
