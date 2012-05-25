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

package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.gfx.CreatorHelper;
import de.weltraumschaf.ebnf.gfx.RailroadDiagramImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliApp {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final PrintStream stdOut;
    private final CreatorHelper helper = new CreatorHelper();

    public CliApp(final PrintStream stdOut) {
        this.stdOut = stdOut;
    }

    public void execute() {
        final RailroadDiagramImage img = new RailroadDiagramImage(WIDTH, HEIGHT, new File("./test.png"));
        helper.debug = false;
        img.setDiagram(helper.createDiagram(img.getGraphics()));
        img.paint();

        try {
            img.save();
        } catch (IOException ex) {
            stdOut.println("Can't write file!");
        }
    }

}
