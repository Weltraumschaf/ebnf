package de.weltraumschaf.ebnf.ast;

/**
 * Rule node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Rule extends AbstractComposite {

    /**
     * Name literal of rule.
     *
     * @var
     */
    public String name = "";

    public Rule(Node parent) {
        super(parent);
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

}
