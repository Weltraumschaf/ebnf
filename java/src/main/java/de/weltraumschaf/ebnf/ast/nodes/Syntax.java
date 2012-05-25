package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;
import de.weltraumschaf.ebnf.ast.visitor.Visitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Syntax node.
 *
 * The root of the AST.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Syntax extends AbstractNode implements Composite {

    /**
     * Default meta string.
     */
    public static final String DEFAULT_META = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";

    private static final String ATTR_TITLE = "title";
    private static final String ATTR_META = "meta";
    private static final NodeType TYPE = NodeType.SYNTAX;

    /**
     * Holds the child nodes.
     *
     * @var array
     */
    private final List<Node> nodes = new ArrayList<Node>();

    private Syntax(final String title, final String meta) {
        super(Null.getInstance(), NodeType.SYNTAX);
        setAttribute(ATTR_TITLE, title);
        setAttribute(ATTR_META, meta);
    }

    /**
     * Creates a new syntax node with default meta and empty title.
     *
     * @return New instance.
     */
    public static Syntax newInstance() {
        return newInstance("");
    }

    /**
     * Creates a new syntax node with default meta.
     *
     * @param title Title of the syntax.
     * @return       New instance.
     */
    public static Syntax newInstance(final String title) {
        return newInstance(title, DEFAULT_META);
    }

    /**
     * Creates a new syntax node.
     *
     * @param title Title of the syntax.
     * @param meta  Meta of the syntax.
     * @return       New instance.
     */
    public static Syntax newInstance(final String title, final String meta) {
        return new Syntax(title, meta);
    }

    /**
     * Count of direct children nodes.
     *
     * @return
     */
    @Override
    public int countChildren() {
        return nodes.size();
    }

    /**
     * Whether the node has direct child nodes or not.
     *
     * @return
     */
    @Override
    public boolean hasChildren() {
        return 0 < countChildren();
    }

    /**
     * Append a child {@link Node} to the list of children.
     *
     * @param child Child node to add.
     */
    @Override
    public void addChild(final Node child) {
        nodes.add(child);
    }

    /**
     * Implements IteratorAggregate for retrieving an interaotr.
     *
     * @return
     */
    @Override
    public List<Node> getChildren() {
        return nodes;
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param other  Node to compare against.
     * @param result Object which collects all equivalence violations.
     */
    @Override
    public void probeEquivalence(final Node other, final Notification result) {
        try {
            final Syntax syntax = (Syntax) other;

            if (!getAttribute(ATTR_TITLE).equals(syntax.getAttribute(ATTR_TITLE))) {
                result.error("Titles of syntx differs: '%s' != '%s'!", getAttribute(ATTR_TITLE),
                                                                       syntax.getAttribute(ATTR_TITLE));
            }

            if (!getAttribute(ATTR_META).equals(syntax.getAttribute(ATTR_META))) {
                result.error("Meta of syntx differs: '%s' != '%s'!", getAttribute(ATTR_META),
                                                                     syntax.getAttribute(ATTR_META));
            }

            if (countChildren() != syntax.countChildren()) {
                result.error(
                    "Node %s has different child count than other: %d != %d!",
                    getNodeName(),
                    countChildren(),
                    syntax.countChildren()
                );
            }

            final List<Node> subnodes      = getChildren();
            final List<Node> otherSubnodes = syntax.getChildren();
            int i = 0; // NOPMD

            for (Node subnode : subnodes) {
                try {
                    subnode.probeEquivalence(otherSubnodes.get(i), result);
                } catch (IndexOutOfBoundsException ex) {
                    result.error("Other node has not the expected subnode!");
                }

                i++;
            }
        } catch (ClassCastException ex) {
            result.error(
                "Probed node types mismatch: '%s' != '%s'!",
                getClass(),
                other.getClass()
            );
        }
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

        if (hasChildren()) {
            for (Node subnode : getChildren()) {
                subnode.accept(visitor);
            }
        }

        visitor.afterVisit(this);
    }

    /**
     * Chooses the max depth of its direct children and returns it plus one.
     *
     * @return
     */
    @Override
    public int depth() {
        return new DepthCalculator(this).depth();
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder(super.toString());

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }

    @Override
    public String getNodeName() {
        return TYPE.toString();
    }

    @Override
    public NodeType getType() {
        return TYPE;
    }
}
