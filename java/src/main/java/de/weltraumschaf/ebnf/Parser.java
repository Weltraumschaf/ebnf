package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.nodes.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Parses a stream of EBNF tokens and generate a abstract syntax tree.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Parser {

    /**
     * Used to receive the tokens.
     */
    private Scanner scanner;
    /**
     * The abstract syntax tree.
     *
     * @var Syntax
     */
    private Syntax ast;

    /**
     * Initialized with a scanner which produced the token stream.
     *
     * @param scanner Provides the token stream.
     */
    public Parser(Scanner scanner) {
        this.scanner = scanner;
        ast     = Syntax.newInstance();
    }

    /**
     * Parses the EBNF tokens and returns a syntax tree as DOMDocument object on success.
     *
     * On semantic syntax errors a SyntaxError will be thrown.
     *
     * @throws SyntaxError
     */
    public Syntax parse() throws SyntaxError, IOException {
        scanner.nextToken();

        if (scanner.currentToken().isType(TokenType.LITERAL)) {
            ast.title = scanner.currentToken().getValue(true);
            scanner.nextToken();
        }

        if (!assertToken(scanner.currentToken(), TokenType.L_BRACE, "{")) {
            raiseError("Syntax must start with '{'");
        }

        scanner.nextToken();

        while (scanner.hasNextToken() && scanner.currentToken().isType(TokenType.IDENTIFIER)) {
            Node rules = parseRule();
            ast.addChild(rules);
            scanner.nextToken();
        }

        if (!assertToken(scanner.currentToken(), TokenType.R_BRACE, "}")) {
            raiseError(String.format("Syntax must end with '}' but saw '%s'", scanner.currentToken()));
        }

        scanner.nextToken();

        if (scanner.hasNextToken()) {
            if (scanner.currentToken().isType(TokenType.LITERAL)) {
                ast.meta = scanner.currentToken().getValue(true);
            } else {
                raiseError("Literal expected as syntax comment");
            }
        }

        return ast;
    }

    /**
     * Parses an EBNF production: rule = identifier ( "=" | ":==" | ":" ) expression ( "." | ";" ) .
     *
     * @throws SyntaxError
     * @return
     */
    private Node parseRule() throws SyntaxError, IOException {
        if (!scanner.currentToken().isType(TokenType.IDENTIFIER)) {
            raiseError("Production must start with an identifier");
        }

        Rule rule = Rule.newInstance(ast, scanner.currentToken().getValue());
        scanner.nextToken();

        if (!assertTokens(scanner.currentToken(), TokenType.ASIGN, Arrays.asList("=", ":", ":=="))) {
            raiseError("Identifier must be followed by '='");
        }

        scanner.nextToken();
        Node expressions = parseExpression(rule);
        rule.addChild(expressions);

        if (!assertTokens(scanner.currentToken(), TokenType.END_OF_RULE, Arrays.asList(".", ";"))) {
            raiseError("Rule must end with '.' or ';'", scanner.backtrackToken(2).getPosition(true));
        }

        return rule;
    }

    /**
     * Parses an EBNF expression: expression = term { "|" term } .
     *
     * @param Node parent Parent node.
     *
     * @throws SyntaxError
     * @return
     */
    private Node parseExpression(Node parent) throws SyntaxError, IOException {
        Choice choiceNode = Choice.newInstance(parent);
        Node term       = parseTerm(choiceNode);
        choiceNode.addChild(term);
        boolean multipleTerms = false;

        while (assertToken(scanner.currentToken(), TokenType.CHOICE, "|")) {
            scanner.nextToken();
            term = parseTerm(choiceNode);
            choiceNode.addChild(term);
            multipleTerms = true;
        }

        return multipleTerms ? choiceNode : term;
    }

    /**
     * Parses an EBNF term: term = factor { factor } .
     *
     * @param Node parent Parent node.
     *
     * @throws SyntaxError
     * @return
     */
    private Node parseTerm(Node parent) throws SyntaxError, IOException {
        Sequence sequenceNode = Sequence.newInstance(parent);
        Node factor           = parseFactor(sequenceNode);
        sequenceNode.addChild(factor);
        scanner.nextToken();
        boolean multipleFactors = false;
        String[] allowed = {".", "=", "|", ")", "]", "}"};

        while (scanner.currentToken().isNotEquals(allowed)) {
            factor = parseFactor(sequenceNode);
            sequenceNode.addChild(factor);
            scanner.nextToken();
            multipleFactors = true;
        }

        return multipleFactors ? sequenceNode : factor;
    }

    /**
     * Parses an EBNF factor:
     * factor = identifier
     *       | literal
     *       | "[" expression "]"
     *       | "(" expression ")"
     *       | "{" expression "}" .
     *
     * @param Node parent Parent node.
     *
     * @throws SyntaxError
     * @return
     */
    private Node parseFactor(Node parent) throws SyntaxError, IOException {
        if (scanner.currentToken().isType(TokenType.IDENTIFIER)) {
            Identifier identifier = Identifier.newInstance(parent, scanner.currentToken().getValue());
            return identifier;
        }

        if (scanner.currentToken().isType(TokenType.LITERAL)) {
            /*if (assertToken(scanner.peekToken(), TokenType.OPERATOR, ".")) {
                echo "range";
                range = dom.createElement(Type.RANGE);
                range.setAttribute("from", scanner.currentToken().getValue(true));
                scanner.nextToken(); // Omit ".." literal.
                scanner.nextToken();
                range.setAttribute("to", scanner.currentToken().getValue(true));
                return range;
            }*/

            Terminal literal = Terminal.newInstance(parent, scanner.currentToken().getValue(true));
            return literal;
        }

        if (scanner.currentToken().isType(TokenType.COMMENT)) {
            return Comment.newInstance(parent, scanner.currentToken().getValue());
        }

        if (assertToken(scanner.currentToken(), TokenType.L_PAREN, "(")) {
            scanner.nextToken();
            Node expression = parseExpression(parent);

            if (!assertToken(scanner.currentToken(), TokenType.R_PAREN, ")")) {
                raiseError("Group must end with ')'");
            }

            return expression;
        }

        if (assertToken(scanner.currentToken(), TokenType.L_BRACK, "[")) {
            scanner.nextToken();
            Node expression = parseExpression(parent);
            Option option   = Option.newInstance(parent);
            option.addChild(expression);

            if (!assertToken(scanner.currentToken(), TokenType.R_BRACK, "]")) {
                raiseError("Option must end with ']'");
            }

            return option;
        }

        if (assertToken(scanner.currentToken(), TokenType.L_BRACE, "{")) {
            scanner.nextToken();
            Node expression = parseExpression(parent);
            Loop loop       = Loop.newInstance(parent);
            loop.addChild(expression);

            if (!assertToken(scanner.currentToken(), TokenType.R_BRACE, "}")) {
                raiseError("Loop must end with '}'");
            }

            return loop;
        }

        raiseError("Factor expected");
        return null;
    }

    /**
     * Checks whether a token is of a type and is equal to a string literal or not.
     *
     * @param Token  token Token to assert.
     * @param int    type  Token type to assert against.
     * @param string value Token value to assert against.
     *
     * @return
     */
    protected boolean assertToken(Token token, TokenType type, String value) {
        return token.getType() == type && token.getValue().equals(value);
    }

    /**
     * Checks whether a token is of a type and is equal to a array of string literal or not.
     *
     * @param Token token  Token to assert.
     * @param int   type   type to assert against.
     * @param array values Array of strings.
     *
     * @return
     */
    protected  boolean assertTokens(Token token, TokenType type, List<String>values) {
        for (String value : values) {
            if (assertToken(token, type, value)) {
                return true;
            }
        }

        return false;
    }

    protected void raiseError(String msg) throws SyntaxError {
        raiseError(msg, null);
    }

    /**
     * Helper to raise syntax errors.
     *
     * If no position is passed the one of the current token is used.
     *
     * @param msg The error message.
     * @param pos The optional position of the error.
     *
     * @throws SyntaxError Throws always an exception.
     * @return void
     */
    protected void raiseError(String msg, Position pos) throws SyntaxError {
        if (null == pos) {
            pos = scanner.currentToken().getPosition();
        }

        throw new SyntaxError(msg, pos);
    }
}
