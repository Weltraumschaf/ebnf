package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractComposite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;

/**
 * Lop node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Loop extends AbstractComposite {

    private Loop(final Node parent) {
        super(parent);
    }

    /**
     * Creates new loop node with a {@link Null} as parent.
     *
     * @return New instance.
     */
    public static Loop newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates new loop node.
     *
     * @param parent The parent node.
     * @return        New instance.
     */
    public static Loop newInstance(final Node parent) {
        return new Loop(parent);
    }

    /**
     * Returns the name of a node.
     *
     * @return string
     */
    @Override
    public String getNodeName() {
        return NodeType.LOOP.toString();
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("<LOOP>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
