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

import de.weltraumschaf.ebnf.gfx.ShapeFactory;
import de.weltraumschaf.ebnf.gfx.StringPainter;
import java.awt.Dimension;
import java.awt.Graphics2D;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IdentifierTest {

    class IdentifierStub extends Identifier {

        Dimension calcTextSize = new Dimension();

        public IdentifierStub(final String text) {
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
        final AbstractTextShape term = ShapeFactory.getInstance().identifier("foobar");
        assertEquals("foobar", term.getText());
        assertEquals(StringPainter.SANSERIFIT, term.getFont());
    }

    @Test public void calcBoxSize() {
        final IdentifierStub ident = new IdentifierStub("foobar");
        assertEquals(new Dimension(10, 0), ident.calcBoxSize(null));
        ident.setCalculatedTextSize(new Dimension(100, 16));
        assertEquals(new Dimension(110, 16), ident.calcBoxSize(null));
    }

    @Test public void adjust() {
        final IdentifierStub ident = new IdentifierStub("foobar");
        ident.adjust(null);
        assertEquals(new Dimension(31, 31), ident.getSize());

        ident.setCalculatedTextSize(new Dimension(100, 16));
        ident.adjust(null);
        assertEquals(new Dimension(124, 31), ident.getSize());
    }

    @Ignore("Not ready yet.")
    @Test public void paint() {

    }
}
