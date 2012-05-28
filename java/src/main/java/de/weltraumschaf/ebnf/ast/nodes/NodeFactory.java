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

package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class NodeFactory {

    private NodeFactory() { }

    public static Node newNode(final NodeType type, final Node parent) {
        switch (type) {
            case CHOICE:
                return Choice.newInstance(parent);
            case COMMENT:
                return Comment.newInstance(parent);
            case IDENTIFIER:
                return Identifier.newInstance(parent);
            case LOOP:
                return Loop.newInstance(parent);
            case OPTION:
                return Option.newInstance(parent);
            case RULE:
                return Rule.newInstance(parent);
            case SEQUENCE:
                return Sequence.newInstance(parent);
            case TERMINAL:
                return Terminal.newInstance(parent);
            default:
                // This may happen if someone adds new NodeType enum w/o adding here.
                throw new IllegalArgumentException(String.format("Unsupported node type '%s'!", type));
        }
    }

    public static Node newNode(final NodeType type) {
        if (NodeType.SYNTAX == type) {
            return Syntax.newInstance();
        } else if (NodeType.NULL == type) {
            return Null.getInstance();
        } else {
            return newNode(type, Null.getInstance());
        }
    }
}
