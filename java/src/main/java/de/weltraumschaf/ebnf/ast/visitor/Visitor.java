package de.weltraumschaf.ebnf.ast.visitor;

import de.weltraumschaf.ebnf.ast.Node;

/**
 * Defines interface for an AST tree visitor.
 *
 * Interface for <a href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Visitor {

    /**
     * Template method to hook in before specific node vsitor method
     * will be invoked.
     *
     * @param visitable Visited {@link Node}.
     */
    void beforeVisit(Node visitable);

    /**
     * Generic visitor method called by a visited {@link Node}.
     *
     * @param visitable Visited {@link Node}.
     */
    void visit(Node visitable);

    /**
     * Template method to hook in after specific node visitor method
     * will be invoked.
     *
     * @param visitable Visited {@link Node}.
     */
    void afterVisit(Node visitable);

}
