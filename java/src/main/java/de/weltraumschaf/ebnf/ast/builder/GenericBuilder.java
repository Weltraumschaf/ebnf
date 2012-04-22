package de.weltraumschaf.ebnf.ast.builder;

import de.weltraumschaf.ebnf.ast.*;

/**
 * Generic builder provides interface to construct sub nodes for rule nodes.
 *
 * This class is parameterized with the type of the parent builder.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenericBuilder<P> {

    /**
     * The parent builder which created the builder.
     */
    private final P parentBuilder;
    /**
     * The node built by the parent builder.
     */
    private final AbstractComposite parentNode;

    /**
     * Initializes the builder with its parent builder and node.
     *
     * The constructor is not intended to be called from outside the package.
     *
     * @param parentBuilder Builder which created the builder.
     * @param node          To this node all by this builder created nodes will be add.
     */
    GenericBuilder(P parentBuilder, AbstractComposite node) {
        this.parentBuilder = parentBuilder;
        this.parentNode = node;
    }

    /**
     * Returns to the parent builder.
     *
     * @return
     */
    public P end() {
        return parentBuilder;
    }

    /**
     * Creates an {@link Identifier} node and returns the same builder.
     *
     * @param value
     * @return
     */
    public GenericBuilder<P> identifier(String value) {
        Identifier ident = new Identifier(parentNode);
        ident.value = value;
        parentNode.addChild(ident);
        return this;
    }

    /**
     * Creates a {@link Terminal} node and returns the same builder.
     *
     * @param value
     * @return
     */
    public GenericBuilder<P> terminal(String value) {
        Terminal term = new Terminal(parentNode);
        term.value = value;
        parentNode.addChild(term);
        return this;
    }

    /**
     * Creates a {@link Choice} node and returns a sub builder for adding nodes to the {@link Choice} node.
     *
     * @return
     */
    public GenericBuilder<GenericBuilder<P>> choice() {
        Choice choice = new Choice(parentNode);
        parentNode.addChild(choice);
        return new GenericBuilder<GenericBuilder<P>>(this, choice);
    }

    /**
     * Creates a {@link Loop} node and returns a sub builder for adding nodes to the {@link Loop} node.
     *
     * @return
     */
    public GenericBuilder<GenericBuilder<P>> loop() {
        Loop loop = new Loop(parentNode);
        parentNode.addChild(loop);
        return new GenericBuilder<GenericBuilder<P>>(this, loop);
    }

    /**
     * Creates a {@link Option} node and returns a sub builder for adding nodes to the {@link Option} node.
     *
     * @return
     */
    public GenericBuilder<GenericBuilder<P>> option() {
        Option option = new Option(parentNode);
        parentNode.addChild(option);
        return new GenericBuilder<GenericBuilder<P>>(this, option);
    }

    /**
     * Creates a {@link Sequence} node and returns a sub builder for adding nodes to the {@link Sequence} node.
     *
     * @return
     */
    public GenericBuilder<GenericBuilder<P>> sequence() {
        Sequence seq = new Sequence(parentNode);
        parentNode.addChild(seq);
        return new GenericBuilder<GenericBuilder<P>>(this, seq);
    }
}
