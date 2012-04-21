package de.weltraumschaf.ebnf;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum TokenType {
    // Special type:
    LITERAL,
    COMMENT,
    IDENTIFIER,
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
