package de.weltraumschaf.ebnf.visitor;

import de.weltraumschaf.ebnf.ast.Node;

/**
 * Defines interface for an AST tree visitor.
 *
 * Interface for {@linkt http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Visitor {

    /**
     * Template method to hook in before specific node vsitor method
     * will be invoked.
     *
     * @param Node visitable Visited {@link Node}.
     *
     * @return void
     */
    public void beforeVisit(Node visitable);

    /**
     * Generic visitor method called by a visited {@link Node}.
     *
     * @param Node visitable Visited {@link Node}.
     *
     * @return void
     */
    public void visit(Node visitable);

    /**
     * Template method to hook in after specific node visitor method
     * will be invoked.
     *
     * @param Node visitable Visited {@link Node}.
     *
     * @return void
     */
    public void afterVisit(Node visitable);

}
