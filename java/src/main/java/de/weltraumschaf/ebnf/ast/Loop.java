package de.weltraumschaf.ebnf.ast;

/**
 * Lop node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Loop extends AbstractComposite {

    public Loop(Node parent) {
        super(parent);
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
