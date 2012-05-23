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
import javax.swing.JPanel;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class RailroadDiagramPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final RailroadDiagram diagram;

    public RailroadDiagramPanel(final RailroadDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void paintComponent(final Graphics graphic) {
        super.paintComponent(graphic);
        diagram.paint((Graphics2D) graphic);
    }

}
