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

import de.weltraumschaf.ebnf.ast.AbstractCompositeTest;
import de.weltraumschaf.ebnf.ast.DepthCalculatorTest;
import de.weltraumschaf.ebnf.ast.IntegrationTest;
import de.weltraumschaf.ebnf.ast.NotificationTest;
import de.weltraumschaf.ebnf.ast.builder.SyntaxBuilderTest;
import de.weltraumschaf.ebnf.ast.nodes.CommentTest;
import de.weltraumschaf.ebnf.ast.nodes.IdentifierTest;
import de.weltraumschaf.ebnf.ast.nodes.TerminalTest;
import de.weltraumschaf.ebnf.util.ScannerHelperTest;
import de.weltraumschaf.ebnf.visitor.TextSyntaxTreeTest;
import de.weltraumschaf.ebnf.visitor.XmlTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ParserTest.class,
    PositionTest.class,
    ScannerTest.class,
    SyntaxExceptionTest.class,
    TokenTest.class,
    AbstractCompositeTest.class,
    DepthCalculatorTest.class,
    IntegrationTest.class,
    NotificationTest.class,
    SyntaxBuilderTest.class,
    CommentTest.class,
    IdentifierTest.class,
    TerminalTest.class,
    ScannerHelperTest.class,
    TextSyntaxTreeTest.class,
    XmlTest.class
})
public class TestSuite {

}
