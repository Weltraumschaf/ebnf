package de.weltraumschaf.ebnf;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans an input string for EBNF syntax tokens.
 *
 * This class implements a standard lexical scanner pattern with one
 * character lookahead and iterator interface for receiving token by token.
 * On lexical syntax errors a SyntaxException will be thrown.
 *
 * Example:
 * <code>
 * BufferedReader grammar = new BufferedReader(new StringReader("...")); // the EBNF grammar
 *
 * try {
 *     Scanner scanner = new Scanner(grammar);
 *
 *     while (scanner->hasNextToken()) {
 *         scanner->nextToken();
 *         Token token = scanner->currentToken();
 *         System.out.println(token);
 *     }
 * } catch (SyntaxtError ex) {
 *      System.out.println(ex);
 * }
 * </code>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Scanner {

    private final BufferedReader input;
    private final String file;
    private final List<Token> tokens;
    private int currentCharacter;
    private int currentToken;
    private int column;
    private int line;

    public Scanner(BufferedReader input) {
        this(input, null);
    }

    public Scanner(BufferedReader input, String file) {
        this.input = input;
        this.file  = file;
        currentCharacter = -1;
        currentToken     = -1;
        column = 0;
        line   = 1;
        tokens = new ArrayList<Token>();
    }

    /**
     * Returns the file from where the input stream comes.
     *
     * May be null.
     *
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns if there is a next character in the input stream.
     *
     * @return
     */
    private boolean hasNextCharacter() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Increments the character cursor.
     *
     * @return
     */
    private void nextCharacter() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Returns the character at the current cursor from the input stream.
     *
     * @return
     */
    private char currentCharacter() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Decrements the character cursor.
     *
     * @return
     */
    private void backupCharacter() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Returns next character without advancing the cursor.
     *
     * @return
     */
    private char peekCharacter() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Throws a {SyntaxException} with the current {Position} in the input stream.
     *
     * @param string Error message string.
     *
     * @throws SyntaxError
     */
    public void raiseError(String msg) throws SyntaxError {
        throw new SyntaxError(msg, createPosition());
    }

    /**
     * Creates a {Position} from the current line and column in the input stream.
     *
     * @return
     */
    protected Position createPosition() {
        return new Position(line, column, file);
    }

    /**
     * Returns the current scanned token.
     *
     * May be null if never {@link nextToken()} was called.
     *
     * @return
     */
    public Token currentToken() {
        throw new RuntimeException("Not implemented yet!");

//        return null;
    }

    /**
     * Returns one token backwards from the actual token.
     *
     * @return
     */
    public Token backtrackToken() {
        return backtrackToken(1);
    }

    /**
     * Returns the nth token backwards from the actual token.
     *
     * @param int How many tokens to backtrack.
     *
     * @return Token
     */
    public Token backtrackToken(int count) {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Returns if there are more tokens.
     *
     * This is always true if never {@link nextToken()}
     * was called. No more tokens are indicated if the current token is
     * of type {@link TokenType.EOF}.
     *
     * @return
     */
    public boolean hasNextToken() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Returns the next token without advancing the internal pointer (aka. lookahead).
     *
     * A call to {nextToken()} will rturn ths token ahead.
     *
     * @return
     */
    public Token peekToken() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Start the scanning of the next token.
     *
     * This method should be called until {hasNextToken()} returns false.
     *
     * @return
     */
    public void nextToken() throws SyntaxError {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Checks if the current character is a new line character (\n or \r)
     * and if it is increments the line counter and resets the column counter to 0.
     *
     * @return
     */
    private void checkNewline() {
        if ('\n' == currentCharacter() || '\r' == currentCharacter()) {
            line++;
            column = 0;
        }
    }

    /**
     * Scans an identifier [a-zA-Z\-_].
     *
     * @return
     */
    private Token scannIdentifier() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Scans a literal (any character inside single or double quotes.
     *
     * @return
     */
    private Token scanLiteral() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Scans a comment (any character inside '(*' and '*)'.
     *
     * @return
     */
    private Token scanComment() {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
     * Scans an operator.
     *
     * @return
     */
    private Token scanOperator() {
        throw new RuntimeException("Not implemented yet!");
    }
}
