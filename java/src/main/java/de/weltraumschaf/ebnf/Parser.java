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
    private final Scanner scanner;
    /**
     * The abstract syntax tree.
     *
     * @var Syntax
     */
    private final Syntax ast;

    /**
     * Initialized with a scanner which produced the token stream.
     *
     * @param scanner Provides the token stream.
     */
    public Parser(final Scanner scanner) {
        this.scanner = scanner;
        ast = Syntax.newInstance();
    }

    /**
     * Parses the EBNF tokens and returns a syntax AST.
     *
     * On semantic syntax errors a SyntaxError will be thrown.
     *
     * @throws SyntaxError
     */
    public Syntax parse() throws SyntaxException, IOException {
        scanner.nextToken();

        if (scanner.getCurrentToken().isType(TokenType.LITERAL)) {
            ast.title = scanner.getCurrentToken().getValue(true);
            scanner.nextToken();
        }

        if (!assertToken(scanner.getCurrentToken(), TokenType.L_BRACE, "{")) {
            raiseError("Syntax must start with '{'");
        }

        scanner.nextToken();

        while (scanner.hasNextToken()) {
            if (scanner.getCurrentToken().isType(TokenType.IDENTIFIER)) {
                final Node rules = parseRule();
                ast.addChild(rules);
                scanner.nextToken();
            } else if (scanner.getCurrentToken().isType(TokenType.COMMENT)) {
                final Comment comment = Comment.newInstance(ast, scanner.getCurrentToken().getValue());
                ast.addChild(comment);
                scanner.nextToken();
            } else {
                break;
            }
        }

        if (!assertToken(scanner.getCurrentToken(), TokenType.R_BRACE, "}")) {
            raiseError(String.format("Syntax must end with '}' but saw '%s'", scanner.getCurrentToken()));
        }

        scanner.nextToken();

        if (scanner.hasNextToken()) {
            if (scanner.getCurrentToken().isType(TokenType.LITERAL)) {
                ast.meta = scanner.getCurrentToken().getValue(true);
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
    private Node parseRule() throws SyntaxException, IOException {
        if (!scanner.getCurrentToken().isType(TokenType.IDENTIFIER)) {
            raiseError("Production must start with an identifier");
        }

        final Rule rule = Rule.newInstance(ast, scanner.getCurrentToken().getValue());
        scanner.nextToken();

        if (scanner.getCurrentToken().isType(TokenType.COMMENT)) {
            final Comment comment = Comment.newInstance(rule, scanner.getCurrentToken().getValue());
            rule.addChild(comment);
            scanner.nextToken();
        }

        if (!assertTokens(scanner.getCurrentToken(), TokenType.ASIGN, Arrays.asList("=", ":", ":=="))) {
            raiseError("Identifier must be followed by '='");
        }

        scanner.nextToken();
        final Node expressions = parseExpression(rule);
        rule.addChild(expressions);

        if (scanner.getCurrentToken().isType(TokenType.COMMENT)) {
            final Comment comment = Comment.newInstance(rule, scanner.getCurrentToken().getValue());
            rule.addChild(comment);
            scanner.nextToken();
        }

        if (!assertTokens(scanner.getCurrentToken(), TokenType.END_OF_RULE, Arrays.asList(".", ";"))) {
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
    private Node parseExpression(final Node parent) throws SyntaxException, IOException {
        final Choice choiceNode = Choice.newInstance(parent);
        Node term = parseTerm(choiceNode);
        choiceNode.addChild(term);
        boolean multipleTerms = false;

        while (assertToken(scanner.getCurrentToken(), TokenType.CHOICE, "|")) {
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
    private Node parseTerm(final Node parent) throws SyntaxException, IOException {
        final Sequence sequenceNode = Sequence.newInstance(parent);
        Node factor = parseFactor(sequenceNode);
        sequenceNode.addChild(factor);
        scanner.nextToken();
        boolean multipleFactors = false;
        final String[] allowed = {".", "=", "|", ")", "]", "}"};

        while (scanner.getCurrentToken().isNotEquals(allowed)) {
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
    private Node parseFactor(final Node parent) throws SyntaxException, IOException {
        if (scanner.getCurrentToken().isType(TokenType.IDENTIFIER)) {
            return Identifier.newInstance(parent, scanner.getCurrentToken().getValue());
        }

        if (scanner.getCurrentToken().isType(TokenType.LITERAL)) {
            /*if (assertToken(scanner.peekToken(), TokenType.OPERATOR, ".")) {
                echo "range";
                range = dom.createElement(Type.RANGE);
                range.setAttribute("from", scanner.currentToken().getValue(true));
                scanner.nextToken(); // Omit ".." literal.
                scanner.nextToken();
                range.setAttribute("to", scanner.currentToken().getValue(true));
                return range;
            }*/

            return Terminal.newInstance(parent, scanner.getCurrentToken().getValue(true));
        }

        if (scanner.getCurrentToken().isType(TokenType.COMMENT)) {
            return Comment.newInstance(parent, scanner.getCurrentToken().getValue());
        }

        if (assertToken(scanner.getCurrentToken(), TokenType.L_PAREN, "(")) {
            scanner.nextToken();
            final Node expression = parseExpression(parent);

            if (!assertToken(scanner.getCurrentToken(), TokenType.R_PAREN, ")")) {
                raiseError("Group must end with ')'");
            }

            return expression;
        }

        if (assertToken(scanner.getCurrentToken(), TokenType.L_BRACK, "[")) {
            scanner.nextToken();
            final Node expression = parseExpression(parent);
            final Option option = Option.newInstance(parent);
            option.addChild(expression);

            if (!assertToken(scanner.getCurrentToken(), TokenType.R_BRACK, "]")) {
                raiseError("Option must end with ']'");
            }

            return option;
        }

        if (assertToken(scanner.getCurrentToken(), TokenType.L_BRACE, "{")) {
            scanner.nextToken();
            final Node expression = parseExpression(parent);
            final Loop loop       = Loop.newInstance(parent);
            loop.addChild(expression);

            if (!assertToken(scanner.getCurrentToken(), TokenType.R_BRACE, "}")) {
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
    protected boolean assertToken(final Token token, final TokenType type, final String value) {
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
    protected  boolean assertTokens(final Token token, final TokenType type, final List<String>values) {
        for (String value : values) {
            if (assertToken(token, type, value)) {
                return true;
            }
        }

        return false;
    }

    protected void raiseError(final String msg) throws SyntaxException {
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
    protected void raiseError(final String msg, final Position pos) throws SyntaxException {
        if (null == pos) {
            throw new SyntaxException(msg, scanner.getCurrentToken().getPosition());
        } else {
            throw new SyntaxException(msg, pos);
        }
    }
}
