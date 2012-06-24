package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.ast.nodes.Null;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for DepthCalculator.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DepthCalculatorTest {

    static class AbstractCompositeImpl extends AbstractComposite {

        public AbstractCompositeImpl() {
            super(Null.getInstance(), null);
        }


    }

    private Node createNode() {
        return createNode(1);
    }

    /**
     * @param int depth
     * @return Node
     */
    private Node createNode(final int depth) {
        final Node node = mock(Node.class);
        when(node.depth()).thenReturn(depth);
        return node;
    }

    @Test public void testDepth() {
        AbstractComposite subject = new AbstractCompositeImpl();

        DepthCalculator calc = new DepthCalculator(subject);
        assertEquals(0, subject.countChildren());
        assertEquals(1, calc.depth());

        subject.addChild(createNode());
        assertEquals(1, subject.countChildren());
        calc = new DepthCalculator(subject);
        assertEquals(2, calc.depth());

        subject.addChild(createNode());
        assertEquals(2, subject.countChildren());
        calc = new DepthCalculator(subject);
        assertEquals(2, calc.depth());

        subject.addChild(createNode());
        assertEquals(3, subject.countChildren());
        calc = new DepthCalculator(subject);
        assertEquals(2, calc.depth());

        subject = new AbstractCompositeImpl();
        subject.addChild(createNode(2));
        calc = new DepthCalculator(subject);
        assertEquals(3, calc.depth());

        subject.addChild(createNode(5));
        calc = new DepthCalculator(subject);
        assertEquals(6, calc.depth());

        subject.addChild(createNode(1));
        calc = new DepthCalculator(subject);
        assertEquals(6, calc.depth());

        subject.addChild(createNode(8));
        calc = new DepthCalculator(subject);
        assertEquals(9, calc.depth());
    }
}
