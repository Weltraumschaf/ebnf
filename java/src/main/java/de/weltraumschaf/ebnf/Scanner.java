package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.util.ScannerHelper;
import java.io.BufferedReader;
import java.io.IOException;
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

    /**
     * End of file character.
     */
    public static final char EOF = (char) 0;

    /**
     * The input stream to read from.
     */
    private final BufferedReader input;
    /**
     * The file name associated with the input.
     *
     * This piece of information is only used to generated better error messages.
     * This value is passed to all {@link Position} objects created during scanning.
     *
     * This may be null, if a StringReader buffer is supplied!
     */
    private final String file;
    /**
     * Holds all scanned tokens.
     */
    private final List<Token> tokens;
    /**
     * Holds all read characters.
     */
    private final StringBuilder buffer;
    /**
     * Current scanner position in buffer string.
     */
    private int currentCharacter;
    /**
     * Index of current token in token list.
     */
    private int currentToken;
    /**
     * Current column in source.
     */
    private int column;
    /**
     * Current line in source.
     */
    private int line;
    /**
     * True if end of file reached.
     */
    private boolean atEof;

    /**
     * Use this constructor if your input buffer has no file name associated.
     *
     * @param input
     */
    public Scanner(BufferedReader input) {
        this(input, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param input The input stream to scan.
     * @param file  The file name associated with the scanned source.
     */
    public Scanner(BufferedReader input, String file) {
        this.input = input;
        this.file  = file;
        currentCharacter = -1;
        currentToken     = -1;
        column = 0;
        line   = 1;
        tokens = new ArrayList<Token>();
        buffer = new StringBuilder();
        atEof  = false;
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
        return !atEof;
    }

    /**
     * Increments the character cursor.
     *
     * @return
     */
    private void nextCharacter() throws IOException {
        if (currentCharacter > -1 && EOF == currentCharacter()) {
            return;
        }

        if (currentCharacter + 1 >= buffer.length()) {
            int chr = input.read();

            if (-1 == chr) {
                atEof = true;
            } else {
                buffer.append((char)chr);
            }
        }

        currentCharacter++;
        column++;
    }

    /**
     * Returns the character at the current cursor from the input stream.
     *
     * @return
     */
    private char currentCharacter() {
        try {
            return buffer.charAt(currentCharacter);
        } catch (StringIndexOutOfBoundsException ex) {
            if (atEof) {
                return EOF;
            } else {
                throw new RuntimeException("Invoke Scanner#nextCharacter() first!", ex);
            }
        }
    }

    /**
     * Decrements the character cursor.
     *
     * @return
     */
    private void backupCharacter() {
        currentCharacter--;
        column--;
    }

    /**
     * Returns next character without advancing the cursor.
     *
     * @return
     */
    private char peekCharacter() throws IOException {
        char c = EOF;

        if (hasNextCharacter()) {
            nextCharacter();
            c = currentCharacter();
            backupCharacter();
        }

        return c;
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
        try {
            return tokens.get(currentToken);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
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
        int index = currentToken - count;

        try {
            return tokens.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(String.format("Can't backup token on positon -%d! There are only %d tokens.", count, tokens.size()));
        }
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
        Token token = currentToken();

        return null == token
               ? true
               : !token.isType(TokenType.EOF);
    }

    /**
     * Returns the next token without advancing the internal pointer (aka. lookahead).
     *
     * A call to {nextToken()} will return this token ahead.
     *
     * @return
     */
    public Token peekToken() throws SyntaxError, IOException {
        nextToken();
        Token token = currentToken();
        currentToken--;

        // @todo May be this is not necessary.
        if (currentToken < 0) {
            currentToken = 0;
        }

        return token;
    }

    /**
     * Start the scanning of the next token.
     *
     * This method should be called until {hasNextToken()} returns false.
     *
     * @return
     */
    public void nextToken() throws SyntaxError, IOException {
        if (currentToken > -1 && currentToken < (tokens.size() - 1)) {
            // recover backtracked tokens.
            currentToken++;
            return;
        }

        while (hasNextCharacter()) {
            nextCharacter();

            if (ScannerHelper.isAlpha(currentCharacter())) {
                tokens.add(scanIdentifier());
                currentToken++;
                return;
            } else if(ScannerHelper.isQuote(currentCharacter())) {
                tokens.add(scanLiteral());
                currentToken++;
                return;
            } else if (ScannerHelper.isOperator(currentCharacter())) {
                if ('(' == currentCharacter() && '*' == peekCharacter()) {
                    tokens.add(scanComment());
                } else {
                    tokens.add(scanOperator());
                }

                currentToken++;
                return;
            } else if (ScannerHelper.isWhiteSpace(currentCharacter())) {
                // Ignore white spaces.
            } else {
                raiseError(String.format("Invalid character '%s' as %s", currentCharacter(), createPosition()));
            }

            checkNewline();
        }

        tokens.add(new Token(TokenType.EOF, file, createPosition()));
        currentToken++;
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

    private static final char[] IDENTIFIER_SPECIAL_CHARS =  {'-', '_'};
    /**
     * Scans an identifier [a-zA-Z\-_].
     *
     * @return
     */
    private Token scanIdentifier() throws IOException {
        Position position = createPosition();
        StringBuilder value = new StringBuilder();
        value.append(currentCharacter());

        while (hasNextCharacter()) {
            nextCharacter();

            if (ScannerHelper.isAlphaNum(currentCharacter()) ||
                ScannerHelper.isEquals(currentCharacter(), IDENTIFIER_SPECIAL_CHARS)) {
                value.append(currentCharacter());
            } else {
                backupCharacter();
                break;
            }
        }

        return new Token(TokenType.IDENTIFIER, value.toString(), position);
    }

    /**
     * Scans a literal (any character inside single or double quotes.
     *
     * @return
     */
    private Token scanLiteral() throws IOException {
        Position position = createPosition();
        char start = currentCharacter();
        StringBuilder value = new StringBuilder();
        value.append(start);

        while (hasNextCharacter()) {
            nextCharacter();
            value.append(currentCharacter());

            // Ensure that a lieral opened with " is not temrinated by ' and vice versa.
            if (ScannerHelper.isQuote(currentCharacter()) && currentCharacter() == start) {
                break;
            }
        }

        return new Token(TokenType.LITERAL, value.toString(), position);
    }

    /**
     * Scans a comment (any character inside '(*' and '*)'.
     *
     * @return
     */
    private Token scanComment() throws IOException {
        Position postition = createPosition();
        StringBuilder value = new StringBuilder();
        value.append(currentCharacter());

        while (hasNextCharacter()) {
            nextCharacter();
            value.append(currentCharacter());

            if ('*' == currentCharacter() && ')' == peekCharacter()) {
                nextCharacter();
                value.append(currentCharacter());
                break;
            }

            checkNewline(); // // Comments cann be multiline.
        }

        return new Token(TokenType.COMMENT, value.toString(), postition);
    }

    /**
     * Scans an operator.
     *
     * @return
     */
    private Token scanOperator() throws SyntaxError, IOException {
        Position position   = createPosition();
        StringBuilder value = new StringBuilder();
        value.append(currentCharacter());

        char peek      = peekCharacter();
        TokenType type = null;

        switch (currentCharacter()) {
            case ':': {
                if ('=' == peek) {
                    nextCharacter();
                    value.append(currentCharacter());
                    nextCharacter();

                    if (currentCharacter() != '=') {
                        raiseError(String.format("Expecting '=' but seen '%s'", currentCharacter()));
                    }

                    value.append(currentCharacter());
                    type = TokenType.ASIGN;
                } else {
                    type = TokenType.ASIGN;
                }

                break;
            }
            case '=': type = TokenType.ASIGN; break;
            case '.': { // range or end of rule
                if ('.' == peek) {
                    nextCharacter();
                    value.append(currentCharacter());
                    type = TokenType.RANGE;
                } else {
                    type = TokenType.END_OF_RULE;
                }

                break;
            }
            case ';': type = TokenType.END_OF_RULE; break;
            case '(': type = TokenType.L_PAREN; break;
            case '[': type = TokenType.L_BRACK; break;
            case '{': type = TokenType.L_BRACE; break;
            case ')': type = TokenType.R_PAREN; break;
            case ']': type = TokenType.R_BRACK; break;
            case '}': type = TokenType.R_BRACE; break;
            case '|': type = TokenType.CHOICE; break;
            default: {
                raiseError(String.format("Unexpected operator character '%s'", currentCharacter()));
            }
        }

        return new Token(type, value.toString(), position);
    }

    /**
     * Closes the {@link BufferedReader}.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        input.close();
    }
}
