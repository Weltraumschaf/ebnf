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

package de.weltraumschaf.ebnf.ide;

import de.weltraumschaf.ebnf.gfx.CreatorHelper;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IdeApp implements Runnable {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final JFrame frame = new JFrame();
    private final CreatorHelper helper = new CreatorHelper();

    public static void main() {
        final IdeApp app = new IdeApp();
        app.helper.debug = false;
        SwingUtilities.invokeLater(app);
    }

    @Override
    public void run() {
        frame.setTitle("Railroad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        final RailroadDiagramPanel panel = new RailroadDiagramPanel(
                helper.createDiagram(frame.getGraphics()));
        frame.add(panel);
        frame.validate();
    }
}
