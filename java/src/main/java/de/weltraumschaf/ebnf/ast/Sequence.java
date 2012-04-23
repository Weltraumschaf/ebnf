package de.weltraumschaf.ebnf.ast;

/**
 * Sequence node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Sequence extends AbstractComposite {

    public Sequence(Node parent) {
        super(parent);
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
