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

    private Option(Node parent) {
        super(parent);
    }

    public static Option newInstance() {
        return newInstance(Null.newInstance());
    }

    public static Option newInstance(Node parent) {
        return new Option(parent);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.OPTION.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<OPTION>");

        if (hasChildren()) {
            for (Node child : getChildren()) {
                sb.append('\n').append(child.toString());
            }
        }

        return sb.toString();
    }
}
