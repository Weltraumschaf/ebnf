package de.weltraumschaf.ebnf.ast;

import java.util.List;

/**
 * Represents an object in the AST model which can have
 * some child {@link Node nodes}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Composite {

    /**
     * Count of direct children nodes.
     *
     * @return
     */
    public int countChildren();

    /**
     * Whether the node has direct child nodes or not.
     *
     * @return
     */
    public boolean hasChildren();

    /**
     * Append a child {@link Node} to the list of children.
     *
     * @param Node Child node to add.
     *
     * @return
     */
    public void addChild(Node child);

    /**
     * Returns an iterator for the child nodes.
     *
     * @return
     */
    public List<Node> getChildren();

}
