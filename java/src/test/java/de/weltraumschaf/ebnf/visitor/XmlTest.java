package de.weltraumschaf.ebnf.visitor;

import com.google.common.collect.Maps;
import de.weltraumschaf.ebnf.ast.nodes.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for Xml.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class XmlTest {

    @Test public void testCreateOpenTag() {
        Map<String, String> fix1 = Maps.newHashMap();
        fix1.put("bar", "1");
        fix1.put("baz", "<\">&");
        Map<String, String> fix2 = Maps.newHashMap();
        fix2.put("bar", "1");
        fix2.put("baz", "2");

        assertEquals("<foo>", Xml.createOpenTag("foo"));
        assertEquals("<foo baz=\"&lt;&quot;&gt;&amp;\" bar=\"1\">", Xml.createOpenTag("foo", fix1));
        assertEquals("<foo baz=\"2\" bar=\"1\">", Xml.createOpenTag("foo", fix2));
        assertEquals("<foo baz=\"2\" bar=\"1\"/>", Xml.createOpenTag("foo", fix2, false));
    }

    @Test public void testCloseOpenTag() {
        assertEquals("</foo>", Xml.createCloseTag("foo"));
        assertEquals("</fooBar>", Xml.createCloseTag("fooBar"));
    }

    @Test public void testExtractAttributes() {
        Map<String, String> fix = Maps.newHashMap();
        fix.put("meta", "foo");
        fix.put("title", "bar");

        Syntax syntax = Syntax.newInstance("bar", "foo");
        assertEquals(fix, Xml.extractAttributes(syntax));

        Loop loop = Loop.newInstance();
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

        Syntax syntax = Syntax.newInstance("xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3", "EBNF defined in itself.");
        visitor = new Xml();
        syntax.accept(visitor);
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<syntax title=\"xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3\" meta=\"EBNF defined in itself.\"/>",
            visitor.getXmlString()
        );

        syntax = Syntax.newInstance("EBNF defined in itself.", "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3");

        Rule syntaxRule = Rule.newInstance();
        syntaxRule.name = "syntax";
        Sequence seq   = Sequence.newInstance();
        Option opt   = Option.newInstance();
        Identifier ident = Identifier.newInstance();
        ident.value = "title";
        opt.addChild(ident);
        seq.addChild(opt);
        Terminal terminal = Terminal.newInstance();
        terminal.value = "{";
        seq.addChild(terminal);
        Loop loop = Loop.newInstance();
        ident = Identifier.newInstance();
        ident.value = "rule";
        loop.addChild(ident);
        seq.addChild(loop);
        terminal = Terminal.newInstance();
        terminal.value = "}";
        seq.addChild(terminal);
        opt   = Option.newInstance();
        ident = Identifier.newInstance();
        ident.value = "comment";
        opt.addChild(ident);
        seq.addChild(opt);
        syntaxRule.addChild(seq);
        syntax.addChild(syntaxRule);

        Rule ruleRule = Rule.newInstance();
        ruleRule.name = "rule";
        seq   = Sequence.newInstance();
        ident = Identifier.newInstance();
        ident.value = "identifier";
        seq.addChild(ident);
        Choice choice = Choice.newInstance();

        for (String literal : Arrays.asList("=", ":", ":==" )) {
            terminal = Terminal.newInstance();
            terminal.value = literal;
            choice.addChild(terminal);
        }

        seq.addChild(choice);
        ident = Identifier.newInstance();
        ident.value = "expression";
        seq.addChild(ident);
        choice = Choice.newInstance();

        for (String literal : Arrays.asList(".", ";")) {
            terminal = Terminal.newInstance();
            terminal.value = literal;
            choice.addChild(terminal);
        }

        seq.addChild(choice);
        ruleRule.addChild(seq);
        syntax.addChild(ruleRule);

        Rule literalRule = Rule.newInstance();
        literalRule.name = "literal";
        choice   = Choice.newInstance();
        seq      = Sequence.newInstance();
        terminal = Terminal.newInstance();
        terminal.value = "'";
        seq.addChild(terminal);
        ident = Identifier.newInstance();
        ident.value = "character";
        seq.addChild(ident);
        loop  = Loop.newInstance();
        ident = Identifier.newInstance();
        ident.value = "character";
        loop.addChild(ident);
        seq.addChild(loop);
        seq.addChild(terminal);
        choice.addChild(seq);
        seq      = Sequence.newInstance();
        terminal = Terminal.newInstance();
        terminal.value = "\"";
        seq.addChild(terminal);
        ident = Identifier.newInstance();
        ident.value = "character";
        seq.addChild(ident);
        loop  = Loop.newInstance();
        ident = Identifier.newInstance();
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
