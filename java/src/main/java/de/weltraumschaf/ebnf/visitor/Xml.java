package de.weltraumschaf.ebnf.visitor;

import de.weltraumschaf.ebnf.ast.Composite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.nodes.Syntax;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link Visitor} which generates a XML string from the visited AST tree.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Xml implements Visitor {

    public static final String  DEFAULT_VERSION  = "1.0";
    public static final String  DEFAULT_ENCODING = "utf-8";
    public static final int DEFAULT_INDENTATION  = 4;

    /**
     * Buffers the constructed XML string.
     */
    private final StringBuilder xmlString = new StringBuilder();

    /**
     * Level to indent the tags.
     */
    private int indentationLevel = 0;

    public Xml() {
        this(DEFAULT_ENCODING);
    }

    public Xml(String encoding) {
        this(encoding, DEFAULT_VERSION);
    }

    /**
     * Initializes the {@link Visitor} with XML version and encoding.
     *
     * @param version  Optional XML version. Default is {@link DEFAULT_VERSION}.
     * @param encoding Optional XML encoding. Default is {@link DEFAULT_ENCODING}.
     */
    public Xml(String encoding, String version) {
        append(String.format("<?xml version=\"%s\" encoding=\"%s\"?>", version, encoding));
    }

    public static String createOpenTag(String name) {
        return createOpenTag(name, null);
    }

    public static String createOpenTag(String name, Map<String, String> attr) {
        return createOpenTag(name, attr, true);
    }
    /**
     * Creates a opening tag string by name.
     *
     * @param name       The tag name.
     * @param attributes Optional tag attributes.
     * @param block      Whether the tag is in line or block.
     *
     * @return
     */
    public static String createOpenTag(String name, Map<String, String> attributes, boolean block) {
        StringBuilder tag = new StringBuilder();
        tag.append('<').append(name);

        if (null != attributes && !attributes.isEmpty()) {
            for (Map.Entry<String, String> attribute : attributes.entrySet()) {
                tag.append(String.format(" %s=\"%s\"",
                           attribute.getKey(),
                           StringEscapeUtils.escapeXml(attribute.getValue())));
            }
        }

        if (!block) {
            tag.append('/');
        }

        tag.append('>');
        return tag.toString();
    }

    /**
     * Creates a closing tag.
     *
     * @param name The tag name.
     *
     * @return
     */
    public static String createCloseTag(String name) {
        return String.format("</%s>", name);
    }

    /**
     * Extracts all public properties of a {@link Node} object and returns them
     * as associative array.
     *
     * @param node Extracted node.
     *
     * @return
     */
    public static Map<String, String> extractAttributes(Node node) {
        Map<String, String> attributes = new HashMap<String, String>();
        Field[] properties = node.getClass().getFields();

        for (int i = 0; i < properties.length; ++i) {
            Field property = properties[i];

            if (property.getName().equals("DEFAULT_META")) {
                continue;
            }

            try {
                attributes.put(property.getName(), (String) property.get(node));
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return attributes;
    }

    /**
     * Generates an indentation string depending on the actual indentation level.
     *
     * @return
     */
    private String indent() {
        return StringUtils.repeat(' ', indentationLevel * DEFAULT_INDENTATION);
    }

    /**
     * Appends a string to the xml buffer string.
     *
     * @param string Generated XML string to append.
     *
     * @return
     */
    private void append(String string) {
        xmlString.append(string);
    }

    /**
     * {@link Visitor} method to visit a node.
     *
     * Generates opening tags from visited node.
     *
     * @param visitable Visited node.
     *
     * @return
     */
    @Override
    public void visit(Node visitable) {
        boolean block = false;

        if (visitable instanceof Composite && ((Composite)visitable).hasChildren()) {
            block = true;
        }

        append("\n");
        append(indent());
        append(createOpenTag(visitable.getNodeName(), extractAttributes(visitable), block));


        if (visitable instanceof Composite && ((Composite)visitable).hasChildren()) {
            indentationLevel++;
        }
    }

    /**
     * Not used.
     *
     * @param visitable Visited node.
     *
     * @return
     */
    @Override
    public void beforeVisit(Node visitable) {

    }

    /**
     * Generates closing tags for composite nodes.
     *
     * @param visitable Visited node.
     *
     * @return
     */
    @Override
    public void afterVisit(Node visitable) {
        if (visitable instanceof Composite && ((Composite)visitable).hasChildren()) {
            indentationLevel--;
            append("\n");
            append(indent());
            append(createCloseTag(visitable.getNodeName()));
        }
    }

    /**
     * Returns the actual buffered XML string.
     *
     * @return
     */
    public String getXmlString() {
        return xmlString.toString();
    }
}
