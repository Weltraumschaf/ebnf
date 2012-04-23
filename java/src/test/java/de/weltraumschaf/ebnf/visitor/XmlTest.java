package de.weltraumschaf.ebnf.visitor;

import de.weltraumschaf.ebnf.ast.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Unit test for Xml.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class XmlTest {

    @Test public void testCreateOpenTag() {
        assertEquals("<foo>", Xml.createOpenTag("foo"));
        assertEquals(
            "<foo baz=\"&lt;&quot;&gt;&amp;\" bar=\"1\">",
            Xml.createOpenTag("foo", new HashMap<String, String>() {{
                put("bar", "1");
                put("baz", "<\">&");
            }})
        );
        assertEquals(
            "<foo baz=\"2\" bar=\"1\">",
            Xml.createOpenTag("foo", new HashMap<String, String>() {{
                put("bar", "1");
                put("baz", "2");
            }})
        );
        assertEquals(
            "<foo baz=\"2\" bar=\"1\"/>",
            Xml.createOpenTag("foo", new HashMap<String, String>() {{
                    put("bar", "1");
                    put("baz", "2");
                }},
            false
        ));
    }

    @Test public void testCloseOpenTag() {
        assertEquals("</foo>", Xml.createCloseTag("foo"));
        assertEquals("</fooBar>", Xml.createCloseTag("fooBar"));
    }

    @Test public void testExtractAttributes() {
        Syntax syntax = new Syntax();
        syntax.meta  = "foo";
        syntax.title = "bar";
        assertEquals(
            new HashMap<String, String>() {{
                put("meta", "foo");
                put("title", "bar");
            }},
            Xml.extractAttributes(syntax)
        );

        Loop loop = new Loop(mock(Node.class));
        assertEquals(new HashMap<String, String>(), Xml.extractAttributes(loop));
    }

    /**
     * @todo use AST builder
     */
    @Test public void testGenerateXml() throws URISyntaxException, IOException {
        Xml visitor = new Xml();
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
            visitor.getXmlString()
        );

        Syntax syntax = new Syntax();
        syntax.meta  = "EBNF defined in itself.";
        syntax.title = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";
        visitor = new Xml();
        syntax.accept(visitor);
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<syntax title=\"xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3\" meta=\"EBNF defined in itself.\"/>",
            visitor.getXmlString()
        );

        syntax = new Syntax();
        syntax.title = "EBNF defined in itself.";
        syntax.meta  = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";

        Rule syntaxRule = new Rule(mock(Node.class));
        syntaxRule.name = "syntax";
        Sequence seq   = new Sequence(mock(Node.class));
        Option opt   = new Option(mock(Node.class));
        Identifier ident = new Identifier(mock(Node.class));
        ident.value = "title";
        opt.addChild(ident);
        seq.addChild(opt);
        Terminal terminal = new Terminal(mock(Node.class));
        terminal.value = "{";
        seq.addChild(terminal);
        Loop loop = new Loop(mock(Node.class));
        ident = new Identifier(mock(Node.class));
        ident.value = "rule";
        loop.addChild(ident);
        seq.addChild(loop);
        terminal = new Terminal(mock(Node.class));
        terminal.value = "}";
        seq.addChild(terminal);
        opt   = new Option(mock(Node.class));
        ident = new Identifier(mock(Node.class));
        ident.value = "comment";
        opt.addChild(ident);
        seq.addChild(opt);
        syntaxRule.addChild(seq);
        syntax.addChild(syntaxRule);

        Rule ruleRule = new Rule(mock(Node.class));
        ruleRule.name = "rule";
        seq   = new Sequence(mock(Node.class));
        ident = new Identifier(mock(Node.class));
        ident.value = "identifier";
        seq.addChild(ident);
        Choice choice = new Choice(mock(Node.class));

        for (String literal : Arrays.asList("=", ":", ":==" )) {
            terminal = new Terminal(mock(Node.class));
            terminal.value = literal;
            choice.addChild(terminal);
        }

        seq.addChild(choice);
        ident = new Identifier(mock(Node.class));
        ident.value = "expression";
        seq.addChild(ident);
        choice = new Choice(mock(Node.class));

        for (String literal : Arrays.asList(".", ";")) {
            terminal = new Terminal(mock(Node.class));
            terminal.value = literal;
            choice.addChild(terminal);
        }

        seq.addChild(choice);
        ruleRule.addChild(seq);
        syntax.addChild(ruleRule);

        Rule literalRule = new Rule(mock(Node.class));
        literalRule.name = "literal";
        choice   = new Choice(mock(Node.class));
        seq      = new Sequence(mock(Node.class));
        terminal = new Terminal(mock(Node.class));
        terminal.value = "'";
        seq.addChild(terminal);
        ident = new Identifier(mock(Node.class));
        ident.value = "character";
        seq.addChild(ident);
        loop  = new Loop(mock(Node.class));
        ident = new Identifier(mock(Node.class));
        ident.value = "character";
        loop.addChild(ident);
        seq.addChild(loop);
        seq.addChild(terminal);
        choice.addChild(seq);
        seq      = new Sequence(mock(Node.class));
        terminal = new Terminal(mock(Node.class));
        terminal.value = "\"";
        seq.addChild(terminal);
        ident = new Identifier(mock(Node.class));
        ident.value = "character";
        seq.addChild(ident);
        loop  = new Loop(mock(Node.class));
        ident = new Identifier(mock(Node.class));
        ident.value = "character";
        loop.addChild(ident);
        seq.addChild(loop);
        seq.addChild(terminal);
        choice.addChild(seq);
        literalRule.addChild(choice);
        syntax.addChild(literalRule);

        visitor = new Xml();
        syntax.accept(visitor);

        URL resource = getClass().getResource("/de/weltraumschaf/ebnf/visitor/syntax.xml");
        String xml = FileUtils.readFileToString(new File(resource.toURI()));
        assertEquals(xml, visitor.getXmlString());
    }

}
