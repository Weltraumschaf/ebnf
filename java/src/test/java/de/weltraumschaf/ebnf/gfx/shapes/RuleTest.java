/*
 * LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a beer in return.
 *
 */

package de.weltraumschaf.ebnf.gfx.shapes;

import de.weltraumschaf.ebnf.gfx.StringPainter;
import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.rule;
import java.awt.Dimension;
import java.awt.Graphics2D;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class RuleTest {

    static class RuleStub extends Rule {

        Dimension calcTextSize = new Dimension();

        public RuleStub(final String text) {
            super(text);
        }

        @Override
        protected Dimension calculateTextSize(final Graphics2D graphic) {
            return calcTextSize;
        }

        void setCalculatedTextSize(final Dimension calcTextSize) {
            this.calcTextSize = calcTextSize;
        }

    }

    @Test public void font() {
        final AbstractTextShape rule = rule("foobar");
        assertEquals("foobar", rule.getText());
        assertEquals(StringPainter.SANSERIF, rule.getFont());
    }

    @Test public void adjust() {
        final RuleStub rule = new RuleStub("foobar");
        rule.adjust(null);
        assertEquals(new Dimension(31, 31), rule.getSize());

        rule.setCalculatedTextSize(new Dimension(100, 16));
        rule.adjust(null);
        assertEquals(new Dimension(124, 31), rule.getSize());
    }

    @Ignore("Not ready yet.")
    @Test public void paint() {

    }
}
