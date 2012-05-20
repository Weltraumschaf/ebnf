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

import de.weltraumschaf.ebnf.gfx.shapes.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class ShapeFactory {

    private static final ShapeFactory INSTANCE = new ShapeFactory();

    private ShapeFactory() {}

    public static ShapeFactory getInstance() { return INSTANCE; }

    public Shape empty() {
        return new Empty();
    }

    public Shape start() {
        return new Start();
    }

    public Shape end() {
        return new End();
    }

    public Shape straightNorthSouth() {
        return new StraightNS();
    }

    public Shape straightWestEast() {
        return new StraightWE();
    }

    public GridLayout grid() {
        return new GridLayout();
    }

    public ColumnLayout column() {
        return new ColumnLayout();
    }

    public Sequence sequence() {
        return new Sequence();
    }

    public Sequence sequence(final Shape... shapes) {
        final Sequence sequence = sequence();
        sequence.append(shapes);
        return sequence;
    }

    public AbstractTextShape rule(final String name) {
        return new Rule(name);
    }

    public AbstractTextShape terminal(final String value) {
        return new Terminal(value);
    }

    public AbstractTextShape identifier(final String value) {
        return new Identifier(value);
    }

    public Choice choice() {
        return new Choice();
    }

    public Choice choice(final Shape... shapes) {
        final Choice choice = choice();
        for (Shape shape : shapes) {
            choice.addChoice(shape);
        }
        return choice;
    }

    public Option option() {
        return new Option();
    }

    public Option option(final Shape optional) {
        final Option option = option();
        option.setOptional(optional);
        return option;
    }

    public Loop loop() {
        return new Loop();
    }

    public Loop loop(final Shape looped) {
        final Loop loop = loop();
        loop.setLooped(looped);
        return loop;
    }

    public Loop loop(final Shape looped, final Shape additional) {
        final Loop loop = loop(looped);
        loop.setAdditional(additional);
        return loop;
    }

}
