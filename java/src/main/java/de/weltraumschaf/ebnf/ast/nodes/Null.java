package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.Notification;
import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Null implements Node {

    private Null() {}

    public static Null newInstance() {
        return new Null();
    }
    
    @Override
    public String getNodeName() {
        return "null";
    }

    @Override
    public void accept(Visitor visitor) {

    }

    @Override
    public void probeEquivalence(Node other, Notification result) {
        if (other instanceof Null == false) {
            result.error("Other node is not of type Null!");
        }
    }

    @Override
    public int depth() {
        return 0;
    }

}
