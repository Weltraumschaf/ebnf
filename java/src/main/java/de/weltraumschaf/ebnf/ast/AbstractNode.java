package de.weltraumschaf.ebnf.ast;

/**
 * Abstract representation of AST nodes which are not the root
 * node {@link Syntax} but any other kind of {@link Node}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractNode implements Node {

    /**
     * The direct ancestor in the AST tree.
     */
    private final Node parent;

    /**
     * Initializes the node with it's parent. This is immutable.
     *
     * May the node itself change, it is not possible to set the
     * reference to the parent node.
     *
     * @param Node Ancestor node.
     */
    protected AbstractNode(final Node parent) {
        this.parent = parent;
    }

    /**
     * Returns the parent node.
     *
     * @return
     */
    public Node getParent() {
        return parent;
    }

}
