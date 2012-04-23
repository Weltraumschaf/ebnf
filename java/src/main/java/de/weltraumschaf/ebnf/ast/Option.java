package de.weltraumschaf.ebnf.ast;

/**
 * Option node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Option extends AbstractComposite {

    public Option(Node parent) {
        super(parent);
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
