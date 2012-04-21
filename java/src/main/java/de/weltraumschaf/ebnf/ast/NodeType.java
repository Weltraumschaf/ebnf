package de.weltraumschaf.ebnf.ast;

/**
 * Represents the type of an EBNF node type.
 * 
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum NodeType {
    CHOICE,
    IDENTIFIER,
    LOOP,
    OPTION,
    RANGE,
    RULE,
    SEQUENCE,
    SYNTAX,
    TERMINAL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
