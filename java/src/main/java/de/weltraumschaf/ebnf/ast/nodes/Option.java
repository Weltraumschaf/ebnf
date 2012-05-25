package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractComposite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;

/**
 * Option node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Option extends AbstractComposite {

    private Option(final Node parent) {
        super(parent, NodeType.OPTION);
    }

    /**
     * Creates an new option node with a {@link Null} parent node.
     *
     * @return New instance.
     */
    public static Option newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates new option node.
     *
     * @param parent The parent node.
     * @return        New instance.
     */
    public static Option newInstance(final Node parent) {
        return new Option(parent);
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(String.format("<%s>", getNodeName().toUpperCase()));

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
