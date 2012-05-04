package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;

/**
 * Rule node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Rule extends AbstractComposite {

    /**
     * Name literal of rule.
     *
     * @var
     */
    @Attribute public String name;

    private Rule(Node parent, String name) {
        super(parent);
        this.name = name;
    }

    public static Rule newInstance() {
        return newInstance(Null.newInstance());
    }

    public static Rule newInstance(String name) {
        return newInstance(Null.newInstance(), name);
    }

    public static Rule newInstance(Node parent) {
        return newInstance(parent, "");
    }

    public static Rule newInstance(Node parent, String name) {
        return new Rule(parent, name);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.RULE.toString();
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         other  Node to compare against.
     * @param Notification result Object which collects all equivalence violations.
     *
     * @return
     */
    @Override
    public void probeEquivalence(Node other, Notification result) {
        super.probeEquivalence(other, result);

        Rule rule = (Rule) other;

        if (!name.equals(rule.name)) {
            result.error("Names of rule differs: '%s' != '%s'!", name, rule.name);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<RULE name=%s>", name));

        if (hasChildren()) {
            for (Node child : getChildren()) {
                sb.append('\n').append(child.toString());
            }
        }

        return sb.toString();
    }
}
