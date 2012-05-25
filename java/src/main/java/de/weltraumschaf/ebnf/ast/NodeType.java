package de.weltraumschaf.ebnf.ast;

/**
 * Represents the type of an EBNF node type.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum NodeType {
    CHOICE,
    COMMENT,
    IDENTIFIER,
    LOOP,
    OPTION,
    RULE,
    SEQUENCE,
    SYNTAX,
    TERMINAL,
    NULL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
