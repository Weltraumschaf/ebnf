package de.weltraumschaf.ebnf.ast;

/**
 * Types of node attributes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum AttributeType {

    // For Syntax nodes.
    TITLE, META;

    /**
     * Returns the type according to a given name.
     *
     * @param name
     * @return
     */
    public static AttributeType getFor(String name) {
        if (name.equalsIgnoreCase("title")) {
            return TITLE;
        } else if (name.equalsIgnoreCase("meta")) {
            return META;
        } else {
            return null;
        }
    }
}
