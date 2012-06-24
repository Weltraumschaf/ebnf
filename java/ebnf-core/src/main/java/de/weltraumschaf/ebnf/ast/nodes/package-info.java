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

/**
 * All available AST nodes.
 *
 * <p>
 * This package is a part of the open-source
 * <a href="https://github.com/Weltraumschaf/ebnf">EBNF library</a>
 *
 * <p>
 * This package contains all available nodes an EBNF AST may have.
 *
 * <p>
 * The nodes are dived in three types:
 *
 * <ol>
 *  <li>Leafs: The nodes {@link de.weltraumschaf.ebnf.ast.nodes.Identifier Identifier},
 *      {@link de.weltraumschaf.ebnf.ast.nodes.Terminal Terminal}, and
 *      {@link de.weltraumschaf.ebnf.ast.nodes.Null Null}
 *      are leafs. They can not contain other nodes as children.
 *  <li>Composites: All other nodes are composite nodes and may have one or more child nodes.
 *  <li>Special: The leaf node {@link de.weltraumschaf.ebnf.ast.nodes.Null Null} is a special
 *      purpose node mostly used in the unit tests. It is used as default parent node for all
 *      composite nodes.
 */
package de.weltraumschaf.ebnf.ast.nodes;
