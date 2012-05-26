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

package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.ast.nodes.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class NodeFactoryTest {

    @Test public void newNode() {
        assertTrue(NodeFactory.newNode(NodeType.SYNTAX) instanceof Syntax);
        assertTrue(NodeFactory.newNode(NodeType.NULL) instanceof Null);
        assertTrue(NodeFactory.newNode(NodeType.CHOICE) instanceof Choice);
        assertTrue(NodeFactory.newNode(NodeType.COMMENT) instanceof Comment);
        assertTrue(NodeFactory.newNode(NodeType.IDENTIFIER) instanceof Identifier);
        assertTrue(NodeFactory.newNode(NodeType.LOOP) instanceof Loop);
        assertTrue(NodeFactory.newNode(NodeType.OPTION) instanceof Option);
        assertTrue(NodeFactory.newNode(NodeType.RULE) instanceof Rule);
        assertTrue(NodeFactory.newNode(NodeType.SEQUENCE) instanceof Sequence);
        assertTrue(NodeFactory.newNode(NodeType.TERMINAL) instanceof Terminal);
    }
}
