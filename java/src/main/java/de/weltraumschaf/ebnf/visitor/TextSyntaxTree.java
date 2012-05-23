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

package de.weltraumschaf.ebnf.visitor;

import com.google.common.collect.Lists;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.nodes.*;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Generates an ASCII formatted tree of the visited AST {@link Syntax} node.
 *
 * Example:
 *
 * The file <kbd>rules_with_literals.ebnf</kbd> will produce
 *
 * <pre>
 * [syntax]
 *  +--[rule='literal']
 *      +--[choice]
 *          +--[sequence]
 *          |   +--[terminal=''']
 *          |   +--[identifier='character']
 *          |   +--[loop]
 *          |   |   +--[identifier='character']
 *          |   +--[terminal=''']
 *          +--[sequence]
 *              +--[terminal='"']
 *              +--[identifier='character']
 *              +--[loop]
 *              |   +--[identifier='character']
 *              +--[terminal='"']
 * </pre>
 *
 * For generating the tree lines a two dimensional array is used as
 * "render" matrix. The text is lazy computed by computing the array field
 * column by column and row by row.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TextSyntaxTree implements Visitor {

    private static final String BRANCH = " +--";
    private static final String PIPE   = " |  ";
    private static final String BLANK  = "    ";

    /**
     * The formatted ASCII text.
     *
     * Lazy computed.
     */
    private String text = null;
    /**
     * Depth of the visited tree.
     *
     * Asked in {@link Syntax#beforeBisit} node.
     */
    private int depth = 0;
    /**
     * The indention level in the matrix.
     */
    private int level = 0;
    /**
     * The matrix.
     *
     * Two dimensional array. Initialized on visiting a {@link Syntax} node.
     * So it is important that the syntax node is the root node of the tree.
     * The matrix grows row by row by visiting each child node. A child node
     * represents a row.
     *
     * @var array
     */
    private final List<List<String>> matrix = Lists.newArrayList();

    /**
     * Returns the two dimensional matrix.
     *
     * @return array
     */
    public List<List<String>> getMatrix() {
        return matrix;
    }

    /**
     * Returns the depth.
     *
     * @return
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Formats nodes two strings.
     *
     * {@link Rule}, {@link Terminal} and {@link Identifier} nodes will be
     * rendered with their attributes name or value.
     *
     * @param node Node t format.
     * @return      Formatted node.
     */
    public static String formatNode(final Node node) {
        final StringBuilder text = new StringBuilder();
        text.append('[').append(node.getNodeName());
        String value = "";

        if (node instanceof Rule) {
            value = ((Rule) node).name;
        } else if (node instanceof Terminal) {
            value = ((Terminal) node).value; //NOPMD
        } else if (node instanceof Identifier) {
            value = ((Identifier) node).value; //NOPMD
        } else if (node instanceof Comment) {
            value = ((Comment) node).value; //NOPMD

            if (value.length() > 20) {
                value = value.substring(0, 20) + "...";
            }
        }

        if (!value.isEmpty()) {
            text.append("='").append(value).append('\'');
        }

        text.append(']');
        return text.toString();
    }

    /**
     * Returns an array of colCount empty strings as elements.
     *
     * @param colCount Count of columns with empty strings.
     * @return
     */
    public static List<String> createRow(final int colCount) {
        if (colCount < 0) {
            throw new IllegalArgumentException(
                String.format("Coll count msut be greater equal 0! Given value '%d'.", colCount));
        }

        final List<String> row = Lists.newArrayList();

        for (int i = 0; i < colCount; i++) {
            row.add("");
        }

        return row;
    }

    /**
     * If as {@link Syntax} node comes around the visitor will be initializez.
     * Which means that the depth property is read, the matrix and level properties
     * will be initialized. All other {@link Node} types increment the level property.
     *
     * @param visitable Visited node.
     */
    @Override
    public void beforeVisit(final Node visitable) {
        if (visitable instanceof Syntax) {
            depth  = visitable.depth();
            level  = 0;
            matrix.clear();
        } else {
            level++;
        }

        // While we're visiting the output will change anyway.
        text = null;
    }

    /**
     * Generates the string contents  in the row of the visited node.
     *
     * @param visitable Visited node.
     */
    @Override
    public void visit(final Node visitable) {
        final List<String> row = createRow(depth);

        if (level > 0) {
            for (int i = 0; i < level - 1; ++i) {
                row.set(i, BLANK);
            }

            row.set(level - 1, BRANCH);
            row.set(level, formatNode(visitable));
        }

        row.set(level, formatNode(visitable));
        matrix.add(row);
    }

    /**
     * Also "climbs" all rows in the current level and sets a "|" to parent nodes
     * id appropriate.
     *
     * Decrements the level until it reaches 0.
     *
     * @param visitable visited node.
     */
    @Override
    public void afterVisit(final Node visitable) {
        final int rowCnt = matrix.size();

        for (int i = rowCnt - 1; i > -1; i--) {
            if ((matrix.get(i).get(level).equals(BRANCH) || matrix.get(i).get(level).equals(PIPE))
                && (matrix.get(i - 1).get(level).equals(BLANK))) {
                matrix.get(i - 1).set(level, PIPE);
            }
        }

        level = level - 1;

        if (level < 0) {
            level = 0;
        }
    }

    /**
     * Concatenates the matrix columns and rows adn returns the ASCII formatted text.
     *
     * After all visiting is done this method only generates the string once and memoizes
     * the result.
     *
     * @return string
     */
    public String getText() {
        if (null == text) {
            final StringBuilder buffer = new StringBuilder();

            for (List<String> row : matrix) {
                buffer.append(StringUtils.join(row, ""))
                      .append(System.getProperty("line.separator"));
            }

            text = buffer.toString();
        }

        return text;
    }
}
