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

import java.awt.Font;

/**
 * Collection of used fonts with default size.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum Fonts {
    MONOSPACED("Monospaced", Font.PLAIN), // used ofr terminals
    SANSERIF("Sanserif", Font.PLAIN),     // used for rule names
    SANSERIFIT("Sanserif", Font.ITALIC);  // used for identifiers

    /**
     * Font name.
     */
    private final String name;
    /**
     * Font style.
     */
    private final int style;
    /**
     * Font size.
     */
    private int size;
    /**
     * Caches font object for particular font size.
     */
    private Font font = null;

    public static final int DEFAULT_SIZE = 6;

    /**
     * Font with default size.
     *
     * @param name  Font name.
     * @param style Font style.
     */
    Fonts(final String name, final int style) {
        this(name, style, DEFAULT_SIZE);
    }

    /**
     * Font with custom size.
     *
     * @param name  Font name.
     * @param style Font style.
     * @param size  Font size.
     */
    Fonts(final String name, final int style, final int size) {
        this.name  = name;
        this.style = style;
        this.size  = size;
    }

    /**
     * Creates an AWT font object.
     *
     * Returns always same object until a different size is creates.
     *
     * @return
     */
    public Font create() {
        if (null == font) {
            font = new Font(name, style, size);
        }
        return font;
    }

    /**
     * Creates a font object with a new size.
     * 
     * @param size New font size.
     * @return
     */
    public Font create(final int size) {
        this.size = size;
        font = null;
        return create();
    }

}
