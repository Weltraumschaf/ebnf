
<?php

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license http://www.gnu.org/licenses/ GNU General Public License
 * @author  Sven Strittmatter <ich@weltraumschaf.de>
 * @package visitor
 */

namespace de\weltraumschaf\ebnf\visitor;

/**
 * @see Visitor
 */
require_once __DIR__. DIRECTORY_SEPARATOR . "Visitor.php";

use de\weltraumschaf\ebnf\ast\Node        as Node;
use de\weltraumschaf\ebnf\ast\Composite   as Composite;
use de\weltraumschaf\ebnf\ast\Identifier  as Identifier;
use de\weltraumschaf\ebnf\ast\Loop        as Loop;
use de\weltraumschaf\ebnf\ast\Option      as Option;
use de\weltraumschaf\ebnf\ast\Rule        as Rule;
use de\weltraumschaf\ebnf\ast\Sequence    as Sequence;
use de\weltraumschaf\ebnf\ast\Syntax      as Syntax;
use de\weltraumschaf\ebnf\ast\Terminal    as Terminal;
use \ReflectionObject                     as ReflectionObject;
use \ReflectionProperty                   as ReflectionProperty;

/**
 * {@link Visitor} which generates a XML string from the visited AST tree.
 * 
 * @package visitor
 */
class Xml implements Visitor {
    
    const DEFAULT_VERSION     = "1.0";
    const DEFAULT_ENCODING    = "utf-8";
    const DEFAULT_INDENTATION = 4;
    
    /**
     * Level of indent hte tags.
     * 
     * @var int
     */
    private $indentationLevel = 0;
    
    /**
     * Buffers the constructed XML string.
     * 
     * @var string
     */
    private $xmlString = "";
    
    /**
     * Initializes the {@link Visitor} with XML version and endcoding.
     * 
     * @param string $version  Optional XML version. Default is {DEFAULT_VERSION}.
     * @param string $encoding Optional XML encoding. Default is {DEFAULT_ENCODING}.
     */
    public function __construct($version = self::DEFAULT_VERSION, $encoding = self::DEFAULT_ENCODING) {
        $this->append("<?xml version=\"{$version}\" encoding=\"{$encoding}\"?>");
    }

    /**
     * Creates a opening tag string by name.
     *
     * @param string $name  The tag name.
     * @param array  $attr  Optional tag attributes.
     * @param bool   $block Whether the tag is inline or block.
     * 
     * @return string 
     */
    public static function createOpenTag($name, array $attr = null, $block = true) {
        $tag = "<{$name}";
        
        if (null !== $attr && !empty($attr)) {
            foreach ($attr as $attrName => $value) {
                $value = htmlspecialchars($value);
                $tag  .= " {$attrName}=\"{$value}\"";
            }
        }
        
        if (!$block) {
            $tag .= "/";
        }
        
        $tag .= ">";
        return $tag;
    }
    
    /**
     * Creates a closing tag.
     * 
     * @param string $name The tag name.
     * 
     * @return string
     */
    public static function createCloseTag($name) {
        return "</{$name}>";
    }
    
    /**
     * Extracts all public properties of a {@link Node} object and returns them
     * as associatove array.
     * 
     * @param Node $node Extracted node.
     * 
     * @return array
     */
    public static function extractAttributes(Node $node) {
        $attributes = array();
        $object     = new ReflectionObject($node);
        $properties = $object->getProperties(ReflectionProperty::IS_PUBLIC);
        
        if (!empty($properties)) {
            foreach ($properties as $property) {
                /* @var $property ReflectionProperty */
                $attributes[$property->getName()] = $node->{$property->getName()};
            }
        }
        return $attributes;
    }
    
    /**
     * Generates an indentation string depending on the acutal indentation
     * level.
     * 
     * @return string
     */
    private function indent() {
        return str_repeat(" ", $this->indentationLevel * self::DEFAULT_INDENTATION);
    }
    
    /**
     * Appends a string to the xml buffer string.
     *
     * @param string $string Generated XML string to append.
     * 
     * @return void
     */
    private function append($string) {
        $this->xmlString .= (string) $string;
    }
    
    /**
     * {@link Visitor} method to visit a node.
     * 
     * Generates opening tags from visited node.
     *
     * @param Node $visitable Visited node.
     * 
     * @return void
     */
    public function visit(Node $visitable) {
        $block = false;
        
        if ($visitable instanceof Composite && $visitable->hasChildren()) {
            $block = true;
        }
        
        $this->append(
            "\n" .
            $this->indent() .
            self::createOpenTag(
                $visitable->getNodeName(),
                self::extractAttributes($visitable),
                $block
            )
        );
        
        if ($visitable instanceof Composite && $visitable->hasChildren()) {
            $this->indentationLevel++;
        }
    }

    /**
     * Not used.
     * 
     * @param Node $visitable Visited node.
     * 
     * @return void
     */
    public function beforeVisit(Node $visitable) { 
        return null;
    }
    
    /**
     * Generates closing tags for composite nodes.
     *
     * @param Node $visitable Visited node.
     * 
     * @return void
     */
    public function afterVisit(Node $visitable) {
        if ($visitable instanceof Composite && $visitable->hasChildren()) {
            $this->indentationLevel--;
            $this->append(
                "\n" .
                $this->indent() .
                self::createCloseTag($visitable->getNodeName())
            );
        }
    }

    /**
     * Retruns the actual buffered XML string.
     *
     * @return string
     */
    public function getXmlString() {
        return $this->xmlString;
    }
}
