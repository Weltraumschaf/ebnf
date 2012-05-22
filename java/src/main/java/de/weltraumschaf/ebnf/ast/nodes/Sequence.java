package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractComposite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;

/**
 * Sequence node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Sequence extends AbstractComposite {

    private Sequence(final Node parent) {
        super(parent);
    }

    /**
     * Creates an new sequence node with a {@link Null} parent node.
     *
     * @return New instance.
     */
    public static Sequence newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates an new sequence node.
     *
     * @param parent The parent node.
     * @return        New instance.
     */
    public static Sequence newInstance(final Node parent) {
        return new Sequence(parent);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.SEQUENCE.toString();
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("<SEQUENCE>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }

}
