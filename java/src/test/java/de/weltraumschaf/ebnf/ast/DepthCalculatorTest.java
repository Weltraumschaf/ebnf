package de.weltraumschaf.ebnf.ast;

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

    class AbstractCompositeImpl extends AbstractComposite {

        @Override
        public String getNodeName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    private Node createNode() {
        return createNode(1);
    }

    /**
     * @param int depth
     * @return Node
     */
    private Node createNode(int depth) {
        Node n = mock(Node.class);
        when(n.depth()).thenReturn(depth);
        return n;
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
