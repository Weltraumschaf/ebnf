package de.weltraumschaf.ebnf.ast;

import com.google.common.collect.Maps;
import de.weltraumschaf.ebnf.visitor.Visitor;
import java.util.Map;

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
    private final Map<String, String> attributes;

    /**
     * Initializes the node with it's parent. This is immutable.
     *
     * May the node itself change, it is not possible to set the
     * reference to the parent node.
     *
     * @param Node Ancestor node.
     */
    protected AbstractNode(final Node parent, final NodeType type) {
        this.parent     = parent;
        this.type       = type;
        this.attributes = Maps.newHashMap();
    }

    @Override
    public boolean hasParent() {
        return parent != null && parent.getType() != NodeType.NULL;
    }

    /**
     * Returns the parent node.
     *
     * @return
     */
    @Override
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

    @Override
    public boolean hasAttributes() {
        return !attributes.isEmpty();
    }

    @Override
    public boolean hasAttribute(final String name) {
        return attributes.containsKey(name);
    }

    @Override
    public String getAttribute(final String name) {
        if (!hasAttribute(name)) {
            throw new IllegalArgumentException(String.format("Does not have attribute with name '%s'!", name));
        }

        return attributes.get(name);
    }

    @Override
    public void setAttribute(final String name, final String value) {
        attributes.put(name, value);
    }

}
