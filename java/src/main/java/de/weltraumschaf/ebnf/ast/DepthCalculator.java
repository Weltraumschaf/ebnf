package de.weltraumschaf.ebnf.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the algorithm to calculate the depth of an {@link Composite} node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DepthCalculator {

    /**
     * The subject to calculate for.
     *
     * @var
     */
    private final Composite node;

    /**
     * Initializes the immutable object.
     *
     * @param Composite Calculation subject.
     */
    public DepthCalculator(final Composite node) {
        this.node = node;
    }

    /**
     * Calculates the depth on each call.
     *
     * It will return at least 1 if the subject node as no children.
     *
     * @return
     */
    public int depth() {
        if (node.hasChildren()) {
            final List<Integer> depths = new ArrayList<Integer>();

            for (Node n : node.getChildren()) {
                depths.add(n.depth());
            }

            return Collections.max(depths) + 1;
        }

        return 1;
    }

}
