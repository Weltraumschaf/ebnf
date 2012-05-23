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
 * Defines the interface for railroad shapes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Shape extends Paintable {

    /**
     * Default width for shapes.
     */
    int DEFAULT_WIDTH = 31;
    /**
     * Default height for shapes.
     */
    int DEFAULT_HEIGHT = 31;

    /**
     * Returns the position of the shape.
     *
     * @return Position object.
     */
    Point getPosition();
    /**
     * Sets the shapes position.
     *
     * @param pos The position the shape is painted.
     */
    void setPosition(Point pos);
    /**
     * Returns the size of the shape.
     *
     * @return The shapes size.
     */
    Dimension getSize();
    /**
     * Sets the size of the shape.
     *
     * @param size NEw size.
     */
    void setSize(Dimension size);
    /**
     * Enables/disable debug paintings.
     *
     * By default debugging should be off. Debug painting means that the shape paints some
     * additional helper pixels for debugging.
     *
     * @param onnOff
     */
    void setDebug(boolean onnOff);
}
