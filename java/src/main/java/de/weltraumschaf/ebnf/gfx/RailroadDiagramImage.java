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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class RailroadDiagramImage {

    public static enum Type {
        PNG("png"), GIF("gif"), JPG("jpg");

        private final String text;

        private Type(final String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

    }

    private RailroadDiagram diagram;
    private final BufferedImage image;
    private final File file;
    private final Type type;

    public RailroadDiagramImage(final int width, final int height, final File file) {
        this(width, height, file, Type.PNG);
    }

    public RailroadDiagramImage(final int width, final int height, final File file, final Type type) {
        this.image = Helper.newBufferedImage(width, height);
        this.file  = file;
        this.type  = type;
    }

    public void setDiagram(final RailroadDiagram diagram) {
        this.diagram = diagram;
    }

    public Graphics2D getGraphics() {
        return image.createGraphics();
    }

    public void paint() {
        paint(getGraphics());
    }

    public void paint(final Graphics graphics) {
        paint((Graphics2D)graphics);
    }

    public void paint(final Graphics2D graphics) {
        diagram.paint(graphics);
    }

    public void save() throws IOException {
        ImageIO.write(image, type.getText(), file);
    }
}
