package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractComposite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;

/**
 * Choice node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Choice extends AbstractComposite {

    /**
     * Creates a new choice node instance with a {@link Null} parent node.
     *
     * @return
     */
    public static Choice newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates a new choice node instance with custom parent node.
     *
     * @param parent The associated parent node.
     * @return        The choice node.
     */
    public static Choice newInstance(final Node parent) {
        return new Choice(parent);
    }

    private Choice(final Node parent) {
        super(parent);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.CHOICE.toString();
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("<CHOICE>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
