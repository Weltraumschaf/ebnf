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

package de.weltraumschaf.ebnf.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Helper to create some AWT stuff.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Helper {

    /**
     * Pure factory.
     */
    private Helper() {}

    /**
     * Creates a Buffered image of size 1 x 1.
     *
     * @return
     */
    public static BufferedImage newBufferedImage() {
        return newBufferedImage(1, 1);
    }

    /**
     * Creates a buffered image with custom size.
     *
     * @param width  Image width.
     * @param height Image height.
     * @return
     */
    public static BufferedImage newBufferedImage(final int width, final int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Creates a graphics object from a default buffered image.
     * 
     * @return
     */
    public static Graphics2D newGraphics() {
        return newBufferedImage().createGraphics();
    }
}
