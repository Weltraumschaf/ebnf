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

    public static Choice newInstance() {
        return newInstance(Null.newInstance());
    }

    public static Choice newInstance(Node parent) {
        return new Choice(parent);
    }

    private Choice(Node parent) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("<CHOICE>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                sb.append('\n').append(child.toString());
            }
        }

        return sb.toString();
    }
}
