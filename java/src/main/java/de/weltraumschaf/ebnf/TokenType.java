package de.weltraumschaf.ebnf;

/**
 * Defines all token types.
 * 
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum TokenType {
    /**
     * Type for literal tokens.
     */
    LITERAL,
    /**
     * Type for comment tokens.
     */
    COMMENT,
    /**
     * Type for identifier tokens.
     */
    IDENTIFIER,
    /**
     * Type for end of file token.
     */
    EOF,
    // Operator types:
    /**
     * :== or : or =
     */
    ASIGN,
    /**
     * . or ;
     */
    END_OF_RULE,
    /**
     * (
     */
    L_PAREN,
    /**
     * [
     */
    L_BRACK,
    /**
     * {
     */
    L_BRACE,
    /**
     * )
     */
    R_PAREN,
    /**
     * ]
     */
    R_BRACK,
    /**
     * }
     */
    R_BRACE,
    /**
     * ..
     */
    RANGE,
    /**
     * |
     */
    CHOICE;
}
