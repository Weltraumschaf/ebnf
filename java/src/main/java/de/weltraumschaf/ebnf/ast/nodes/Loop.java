package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractComposite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;

/**
 * Lop node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Loop extends AbstractComposite {

    private Loop(Node parent) {
        super(parent);
    }

    public static Loop newInstance() {
        return newInstance(Null.newInstance());
    }

    public static Loop newInstance(Node parent) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("<LOOP>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                sb.append('\n').append(child.toString());
            }
        }

        return sb.toString();
    }
}
