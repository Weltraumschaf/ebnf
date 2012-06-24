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

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HForkNW extends AbstractFork {

    public HForkNW() {
        super(new StraightWE(), new CurveNW());
    }

}