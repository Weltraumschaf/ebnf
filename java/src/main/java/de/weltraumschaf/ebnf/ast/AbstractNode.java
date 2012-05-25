package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Abstract representation of AST nodes which are not the {@link nodes.Syntax "root node"}
 * but any other kind of {@link Node}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractNode implements Node {

    /**
     * The direct ancestor in the AST tree.
     */
    private final Node parent;
    private final NodeType type;

    /**
     * Initializes the node with it's parent. This is immutable.
     *
     * May the node itself change, it is not possible to set the
     * reference to the parent node.
     *
     * @param Node Ancestor node.
     */
    protected AbstractNode(final Node parent, final NodeType type) {
        this.parent = parent;
        this.type   = type;
    }

    /**
     * Returns the parent node.
     *
     * @return
     */
    public Node getParent() {
        return parent;
    }

    @Override
    public NodeType getType() {
        return type;
    }

    @Override
    public String getNodeName() {
        return getType().toString();
    }

    /**
     * Defines method to accept {@link Visitor visitors}.
     *
     * Implements <a href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>.
     *
     * @param visitor Object which visits the node.
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.beforeVisit(this);
        visitor.visit(this);
        visitor.afterVisit(this);
    }

}
