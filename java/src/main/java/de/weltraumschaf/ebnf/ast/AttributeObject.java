package de.weltraumschaf.ebnf.ast;

import com.google.common.base.Objects;

/**
 * A node attribute consists of a type and string value.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AttributeObject {

    /**
     * Type of the attribute.
     */
    private final AttributeType type;
    /**
     * Value of the attribute.
     */
    private final String value;

    /**
     * Initializes the immutable attribute.
     *
     * @param name
     * @param value
     */
    public AttributeObject(AttributeType name, String value) {
        this.type  = name;
        this.value = value;
    }

    /**
     * Returns the types name as string.
     *
     * @return
     */
    public String name() {
        return type.name();
    }

    /**
     * Returns the attribute type.
     *
     * @return
     */
    public AttributeType type() {
        return type;
    }

    /**
     * Returns the attribute value.
     * 
     * @return
     */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final AttributeObject other = (AttributeObject) obj;

        if (!Objects.equal(type, other.type)) {
            return false;
        }

        return Objects.equal(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, value);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", type)
                .add("value", value)
                .toString();
    }
}
