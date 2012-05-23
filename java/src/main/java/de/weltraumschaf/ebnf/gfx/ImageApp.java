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

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ImageApp {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    boolean debug = false;
    private final CreatorHelper helper = new CreatorHelper();
    private final String[] args;

    public ImageApp(final String[] args) {
        this.args = args.clone();
    }

    public int execute() {
        String type = "";

        for (String arg : args) {
            if (arg.equalsIgnoreCase("swing") || arg.equalsIgnoreCase("img")) {
                type = arg.toLowerCase(); //NOPMD
            } else if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("--debug")) {
                debug = true;
            }
        }

        if (type.equalsIgnoreCase("swing")) {
//            createAndShowGui();
        } else if (type.equalsIgnoreCase("img")) {
            createAndSaveImage();
        } else {
            System.out.println(String.format("Give either 'swing' or 'img'! Given was '%s'.",
                                             type));
            return -1;
        }

        return 0;
    }

    private void createAndSaveImage() {
        final RailroadDiagramImage img = new RailroadDiagramImage(WIDTH, HEIGHT,
                                                                  new File("./test.png"));
        helper.debug = debug;
        img.setDiagram(helper.createDiagram(img.getGraphics()));
        img.paint();
        try {
            img.save();
        } catch (IOException ex) {
            System.out.println("Can't write file!");
        }
    }
}
