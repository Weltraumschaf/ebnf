package de.weltraumschaf.ebnf.ast;

/**
 * Choice node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Choice extends AbstractComposite {

    public Choice(Node parent) {
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
