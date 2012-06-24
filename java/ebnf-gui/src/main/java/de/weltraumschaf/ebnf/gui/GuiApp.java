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

package de.weltraumschaf.ebnf.gui;

import de.weltraumschaf.ebnf.cli.CliOptions;
import de.weltraumschaf.ebnf.gfx.CreatorHelper;
import de.weltraumschaf.ebnf.gfx.RailroadDiagram;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GuiApp implements Runnable {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final JFrame frame = new JFrame();
    private final CreatorHelper helper = new CreatorHelper();
    private final CliOptions options;

    public GuiApp(final CliOptions options) {
        this.options = options;
    }

    public static void main(final CliOptions options) {
        final GuiApp app = new GuiApp(options);
        SwingUtilities.invokeLater(app);
    }

    @Override
    public void run() {
        frame.setTitle("Railroad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        final RailroadDiagram diagram = helper.createDiagram(frame.getGraphics());
        diagram.setDebug(options.isDebug());
        final RailroadDiagramPanel panel = new RailroadDiagramPanel(diagram);
        frame.add(panel);
        frame.validate();
    }
}
