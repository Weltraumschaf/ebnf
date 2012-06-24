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

package de.weltraumschaf.ebnf.parser;

import de.weltraumschaf.ebnf.ast.nodes.Syntax;
import java.io.IOException;

/**
 * Interface for parser.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Parser {

    Syntax parse() throws SyntaxException, IOException;
}
