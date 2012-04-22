package de.weltraumschaf.ebnf.ast.builder;

import de.weltraumschaf.ebnf.ast.Syntax;
import static de.weltraumschaf.ebnf.ast.builder.SyntaxBuilder.syntax;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for all syntax builder classes: {@link SyntaxBuilder}, {@link RuleBuilder} and {@link Builder}.
 *
 * Tests generating AST by using the internal DSL interface of the builder.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SyntaxBuilderTest {

    @Ignore
    @Test public void testBuilder() {
        Syntax syntax = syntax("EBNF defined in itself.")
            .rule("syntax")
                .sequence()
                    .option()
                        .identifier("title")
                    .end()
                    .terminal("{")
                    .loop()
                        .identifier("rule")
                    .end()
                    .terminal("}")
                    .option()
                        .identifier("comment")
                    .end()
                .end()
            .end()
            .rule("rule")
                .sequence()
                    .identifier("identifier")
                    .choice()
                        .terminal("=")
                        .terminal(":")
                        .terminal(":==")
                    .end()
                    .identifier("expression")
                    .choice()
                        .terminal(".")
                        .terminal(";")
                    .end()
                .end()
            .end()
            .rule("literal")
                .choice()
                    .sequence()
                        .terminal("'")
                        .identifier("character")
                        .loop()
                            .identifier("character")
                        .end()
                        .terminal("'")
                    .end()
                    .sequence()
                        .terminal("\"")
                        .identifier("character")
                        .loop()
                            .identifier("character")
                        .end()
                        .terminal("\"")
                    .end()
                .end()
            .end()
        .build();

//        xml     = file_get_contents(EBNF_TESTS_FIXTURS . "/visitor/syntax.xml");
//        visitor = new Xml();
//        syntax.accept(visitor);
//        this.assertEquals(xml, visitor.getXmlString());
    }

}
