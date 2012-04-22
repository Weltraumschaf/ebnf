package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.visitor.Visitor;
import java.util.ArrayList;
import java.util.List;

/**
 *Abstract base class for nodes which are not leaves and have subnodes.
 *
 * Provides interface for iterate and add child nodes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractComposite extends AbstractNode implements Composite {

    /**
     * Holds the child nodes.
     *
     * @var array
     */
    private final List<Node> nodes = new ArrayList<Node>();

    public AbstractComposite() {
        this(null);
    }

    /**
     * Initializes object with empty child node array and parent node.
     *
     * @param Node parent The parent node.
     */
    public AbstractComposite(Node parent) {
        super(parent);
    }

    /**
     * Implements IteratorAggregate for retrieving an interator.
     *
     * @return
     */
    @Override
    public List<Node> getChildren() {
        return nodes;
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
    public void addChild(Node child) {
        nodes.add(child);
    }

    /**
     * Defines method to accept {@link Visitors}.
     *
     * Implements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor Object which visits te node.
     *
     * @return void
     */
    @Override
    public void accept(Visitor visitor) {
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
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         Node to compare against.
     * @param Notification Object which collects all equivalence violations.
     *
     * @return
     */
    @Override
    public void probeEquivalence(Node other, Notification result) {
        try {
            Composite comp = (Composite) other;

            if (!getClass().equals(other.getClass())) {
                result.error(
                    "Probed node types mismatch: '%s' != '%s'!",
                    getClass(),
                    other.getClass()
                );
                return;
            }

            if (countChildren() != comp.countChildren()) {
                result.error(
                    "Node %s has different child count than other: %d != %d!",
                    getNodeName(),
                    countChildren(),
                    comp.countChildren()
                );
            }

            List<Node> subnodes      = getChildren();
            List<Node> otherSubnodes = comp.getChildren();

            int i = 0;
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
                "Probed node is not a composite node: '%s'!",
                other.getClass()
            );
        }
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

}
