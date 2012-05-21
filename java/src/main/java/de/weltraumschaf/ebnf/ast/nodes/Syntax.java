package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;
import de.weltraumschaf.ebnf.visitor.Visitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Syntax node.
 *
 * The root of the AST.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Syntax implements Node, Composite {

    public static final String DEFAULT_META = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";

    /**
     * Title literal of string.
     *
     * @var string
     */
    @Attribute public String title;
    /**
     * Meta literal of string.
     *
     * @var string
     */
    @Attribute public String meta;

    /**
     * Holds the child nodes.
     *
     * @var array
     */
    private final List<Node> nodes = new ArrayList<Node>();

    public static Syntax newInstance() {
        return newInstance("");
    }

    public static Syntax newInstance(final String title) {
        return newInstance(title, DEFAULT_META);
    }

    public static Syntax newInstance(final String title, final String meta) {
        return new Syntax(title, meta);
    }

    private Syntax(final String title, final String meta) {
        super();
        this.title = title;
        this.meta  = meta;
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.SYNTAX.toString();
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
     * @param Node Child node to add.
     *
     * @return
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
     * @param Node         Node to compare against.
     * @param Notification Object which collects all equivlanece violations.
     *
     * @return void
     */
    @Override
    public void probeEquivalence(final Node other, final Notification result) {
        try {
            final Syntax syntax = (Syntax) other;

            if (!title.equals(syntax.title)) {
                result.error("Titles of syntx differs: '%s' != '%s'!", title, syntax.title);
            }

            if (!meta.equals(syntax.meta)) {
                result.error("Meta of syntx differs: '%s' != '%s'!", meta, syntax.meta);
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
     * Defines method to accept {@link Visitors}.
     *
     * Implements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor visitor Object which visits te node.
     *
     * @return
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
        final StringBuilder str = new StringBuilder();
        str.append(String.format("<SYNTAX title=%s, meta=%s>", title, meta));

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
