package de.weltraumschaf.ebnf.ast;

import java.lang.annotation.*;

/**
 * Indicates an AST {@link Node} attribute.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Attribute { }
