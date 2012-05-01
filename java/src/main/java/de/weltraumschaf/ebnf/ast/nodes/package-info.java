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
 * <p>This package is a part of the open-source
 * <a href="https://github.com/Weltraumschaf/ebnf">EBNF library</a></p>
 *
 * <p>This package contains all available nodes an EBNF AST may have</p>.
 *
 * <p>The nodes are dived in three types:</p>
 *
 * <ol>
 * <li>Leafs: The nodes {@link Identifer}, {@link Terminal}, and {@link Null} are leafs. They can not
 * contain other nodes as children.</li>
 * <li>Composites: All other nodes are composite nodes and may have one or more child nodes.</li>
 * <li>Special: The leaf node {@link Null} is a special purpose node mostly used in the unit tests.
 * It is used as default parent node for all composite nodes.</li>
 * </ol>
 */
package de.weltraumschaf.ebnf.ast.nodes;
