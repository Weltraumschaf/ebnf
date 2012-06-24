package de.weltraumschaf.ebnf.ast.visitor;

import com.google.common.collect.Maps;
import static de.weltraumschaf.ebnf.TestHelper.helper;
import static de.weltraumschaf.ebnf.ast.builder.SyntaxBuilder.syntax;
import de.weltraumschaf.ebnf.ast.nodes.Loop;
import de.weltraumschaf.ebnf.ast.nodes.Syntax;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for Xml.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class XmlTest {

    @Test public void testCreateOpenTag() {
        final Map<String, String> fix1 = Maps.newHashMap();
        fix1.put("bar", "1"); // NOPMD
        fix1.put("baz", "<\">&");
        final Map<String, String> fix2 = Maps.newHashMap();
        fix2.put("bar", "1");
        fix2.put("baz", "2");

        assertEquals("<foo>", Xml.createOpenTag("foo")); // NOPMD
        assertEquals("<foo baz=\"&lt;&quot;&gt;&amp;\" bar=\"1\">", Xml.createOpenTag("foo", fix1));
        assertEquals("<foo baz=\"2\" bar=\"1\">", Xml.createOpenTag("foo", fix2));
        assertEquals("<foo baz=\"2\" bar=\"1\"/>", Xml.createOpenTag("foo", fix2, false));
    }

    @Test public void testCloseOpenTag() {
        assertEquals("</foo>", Xml.createCloseTag("foo"));
        assertEquals("</fooBar>", Xml.createCloseTag("fooBar"));
    }

    @Test public void testExtractAttributes() {
        final Map<String, String> fix = Maps.newHashMap();
        fix.put("meta", "foo");
        fix.put("title", "bar");

        final Syntax syntax = Syntax.newInstance("bar", "foo");
        assertEquals(fix, Xml.extractAttributes(syntax));

        final Loop loop = Loop.newInstance();
        assertEquals(new HashMap<String, String>(), Xml.extractAttributes(loop));
    }

    @Test public void testGenerateXml() throws URISyntaxException, IOException {
        Xml visitor = new Xml();
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
            visitor.getXmlString()
        );

        Syntax syntax = Syntax.newInstance("xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3",
                                           "EBNF defined in itself.");
        visitor = new Xml();
        syntax.accept(visitor);
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                   + "<syntax title=\"xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ "
                   + "gpl3\" meta=\"EBNF defined in itself.\"/>",
            visitor.getXmlString()
        );

        syntax = syntax("EBNF defined in itself.", "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3")
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
                        .identifier("character") // NOPMD
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

        visitor = new Xml();
        syntax.accept(visitor);

        final String xml = helper().createStringFromFixture("ast/visitor/syntax.xml");
        assertEquals(xml, visitor.getXmlString());
    }

}
