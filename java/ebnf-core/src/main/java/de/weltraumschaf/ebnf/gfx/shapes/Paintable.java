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

import java.awt.Graphics2D;

/**
 * Implementors can paint them selves on a {@link java.awt.Graphics2D graphics context}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Paintable {

    /**
     * PAints the implementor.
     *
     * @param graphic Context to paint on.
     */
    void paint(Graphics2D graphic);

}
