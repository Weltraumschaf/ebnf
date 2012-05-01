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

    private Sequence(Node parent) {
        super(parent);
    }

    public static Sequence newInstance() {
        return newInstance(Null.newInstance());
    }

    public static Sequence newInstance(Node parent) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("<SEQUENCE>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                sb.append('\n').append(child.toString());
            }
        }

        return sb.toString();
    }

}
