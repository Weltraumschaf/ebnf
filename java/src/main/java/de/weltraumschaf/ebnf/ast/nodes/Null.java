package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;
import de.weltraumschaf.ebnf.ast.Notification;
import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Null implements Node {

    private static final Null INSTANCE = new Null();
    private static final NodeType TYPE = NodeType.NULL;

    private Null() { }

    /**
     * Returns always the same null instance.
     *
     * @return Shared instance.
     */
    public static Null getInstance() {
        return INSTANCE;
    }

    @Override
    public void accept(final Visitor visitor) {
        // Do nothing here.
    }

    @Override
    public void probeEquivalence(final Node other, final Notification result) {
        if (!(other instanceof Null)) {
            result.error("Other node is not of type Null!");
        }
    }

    @Override
    public int depth() {
        return 0;
    }


    @Override
    public String toString() {
        return String.format("<%s value=%s>", getNodeName().toUpperCase());
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
