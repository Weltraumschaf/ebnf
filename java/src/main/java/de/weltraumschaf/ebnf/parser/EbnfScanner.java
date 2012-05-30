package de.weltraumschaf.ebnf.parser;

import static de.weltraumschaf.ebnf.parser.CharacterHelper.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
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
 *         doSomething(token);
 *     }
 * } catch (SyntaxtError ex) {
 *      logError(ex);
 * }
 * </code>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class EbnfScanner implements Scanner {

    /**
     * End of file character.
     */
    private static final char EOF = (char) 0;

    /**
     * The input stream to read from.
     */
    private final Reader input;
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
     * @param inputStream The input stream to scan.
     */
    public EbnfScanner(final Reader inputStream) {
        this(inputStream, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param inputStream The input stream to scan.
     * @param fileName    The file name associated with the scanned source.
     */
    public EbnfScanner(final Reader inputStream, final String fileName) {
        this.input = inputStream;
        this.file  = fileName;
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
     * @return The associated file name or null if not scanning from a file.
     */
    @Override
    public final String getFile() {
        return file;
    }

    /**
     * Returns if there is a next character in the input stream.
     *
     * @return If there is one more character.
     */
    boolean hasNextCharacter() {
        return !atEof;
    }

    /**
     * Increments the character cursor.
     *
     * @throws IOException On IO errors caused by the input reader.
     */
    void nextCharacter() throws IOException {
        if (currentCharacter > -1 && EOF == getCurrentCharacter()) {
            return;
        }

        if (currentCharacter + 1 >= buffer.length()) {
            final int chr = input.read();

            if (-1 == chr) {
                atEof = true;
            } else {
                buffer.append((char) chr);
            }
        }

        currentCharacter++;
        column++;
    }

    /**
     * Returns the character at the current cursor from the input stream.
     *
     * @return The current character.
     */
    char getCurrentCharacter() {
        try {
            return buffer.charAt(currentCharacter);
        } catch (StringIndexOutOfBoundsException ex) {
            if (atEof) {
                return EOF;
            } else {
                throw new IllegalStateException("Invoke Scanner#nextCharacter() first!", ex);
            }
        }
    }

    /**
     * Decrements the character cursor.
     *
     * @return
     */
    void backupCharacter() {
        currentCharacter--;
        column--;
    }

    /**
     * Returns next character without advancing the cursor.
     *
     * @return
     */
    char peekCharacter() throws IOException {
        char character = EOF;

        if (hasNextCharacter()) {
            nextCharacter();
            character = getCurrentCharacter();
            backupCharacter();
        }

        return character;
    }

    /**
     * Throws a {SyntaxException} with the current {Position} in the input stream.
     * @param msg Error message string.
     *
     * @throws SyntaxException On syntax errors.
     */
    void raiseError(final String msg) throws SyntaxException {
        throw new SyntaxException(msg, createPosition());
    }

    /**
     * Creates a {Position} from the current line and column in the input stream.
     *
     * @return
     */
    Position createPosition() {
        return new Position(line, column, file);
    }

    /**
     * Returns the current scanned token.
     *
     * May be null if never {@link Scanner#nextToken()} was called.
     *
     * @return
     */
    @Override
    public Token getCurrentToken() {
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
    @Override
    public Token backtrackToken() {
        return backtrackToken(1);
    }

    /**
     * Returns the nth token backwards from the actual token.
     *
     * @param count How many tokens to backtrack.
     *
     * @return The backtracked token.
     */
    @Override
    public Token backtrackToken(final int count) {
        final int index = currentToken - count;

        try {
            return tokens.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(
                String.format("Can't backup token on positon -%d! There are only %d tokens.",
                              count, tokens.size()),
                ex);
        }
    }

    /**
     * Returns if there are more tokens.
     *
     * This is always true if never {@link Scanner#nextToken()}
     * was called. No more tokens are indicated if the current token is
     * of type {@link TokenType#EOF}.
     *
     * @return
     */
    @Override
    public boolean hasNextToken() {
        final Token token = getCurrentToken();

        return null == token
               ? true
               : !token.isType(TokenType.EOF);
    }

    /**
     * Returns the next token without advancing the internal pointer (aka. lookahead).
     *
     * A call to {@link Scanner#nextToken()} will return this token ahead.
     *
     * @return
     * @throws SyntaxException On syntax errors.
     * @throws IOException     On input stream IO errors.
     */
    @Override
    public Token peekToken() throws SyntaxException, IOException {
        nextToken();
        final Token token = getCurrentToken();
        currentToken--;

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
     * @throws SyntaxException On syntax errors.
     * @throws IOException     On input stream IO errors.
     */
    @Override
    public void nextToken() throws SyntaxException, IOException {
        if (!hasNextToken()) {
            return;
        }

        if (currentToken > -1 && currentToken < (tokens.size() - 1)) {
            // recover backtracked tokens.
            currentToken++;
            return;
        }

        processToken();
    }

    private void processToken() throws SyntaxException, IOException {
        while (hasNextCharacter()) {
            nextCharacter();

            if (isAlpha(getCurrentCharacter())) {
                tokens.add(EbnfScannerHelper.scanIdentifier(this));
                currentToken++;
                return;
            } else if (isQuote(getCurrentCharacter())) {
                tokens.add(EbnfScannerHelper.scanLiteral(this));
                currentToken++;
                return;
            } else if (isOperator(getCurrentCharacter())) {
                if ('(' == getCurrentCharacter() && '*' == peekCharacter()) {
                    tokens.add(EbnfScannerHelper.scanComment(this));
                } else {
                    tokens.add(EbnfScannerHelper.scanOperator(this));
                }
                currentToken++;
                return;
            } else if (isWhiteSpace(getCurrentCharacter())) { // NOPMD
                // Ignore white spaces.
            } else {
                raiseError(String.format("Invalid character '%s' at %s",
                                        getCurrentCharacter(),
                                        createPosition()));
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
    void checkNewline() {
        if ('\n' == getCurrentCharacter() || '\r' == getCurrentCharacter()) {
            line++;
            column = 0;
        }
    }

    /**
     * Closes the {@link BufferedReader}.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        input.close();
    }
}
