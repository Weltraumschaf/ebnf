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

import de.weltraumschaf.ebnf.gfx.Point;
import java.awt.Dimension;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Shape extends Paintable {

    public static final int DEFAULT_WIDTH = 31;
    public static final int DEFAULT_HEIGHT = 31;

    public Point getPosition();
    public void setPosition(Point pos);
    public Dimension getSize();
    public void setSize(Dimension size);
    public void setDebug(boolean onnOff);
}
