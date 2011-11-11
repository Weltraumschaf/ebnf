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
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf\visitor;

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

/**
 * 
 */
class Xml implements Visitor {
    
    private $indentationLevel = 0;
    private $xmlString = "";
    
    public function __construct($version = "1.0", $encoding = "utf-8") {
        $this->append("<?xml version=\"{$version}\" encoding=\"{$encoding}\"?>");
    }

    public static function createOpenTag($name, array $attr = null, $block = true) {
        $tag = "<{$name}";
        
        if (null !== $attr && !empty($attr)) {
            foreach ($attr as $attrName => $value) {
                $value = htmlspecialchars($value);
                $tag .= " {$attrName}=\"{$value}\"";
            }
        }
        
        if (!$block) {
            $tag .= "/";
        }
        
        $tag .= ">";
        return $tag;
    }
    
    public static function createCloseTag($name) {
        return "</{$name}>";
    }
    
    public static function extractAttributes(Node $node) {
        $attr = array();
        
        $object = new \ReflectionObject($node);
        $properties = $object->getProperties(\ReflectionProperty::IS_PUBLIC);
        
        if (!empty($properties)) {
            foreach ($properties as $property) {
                /* @var $property ReflectionProperty */
                $attr[$property->getName()] = $node->{$property->getName()};
            }
        }
        return $attr;
    }
    
    private function indent() {
        return str_repeat(" ", $this->indentationLevel * 4);
    }
    
    private function append($string) {
        $this->xmlString .= (string)$string;
    }
    
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

    public function beforeVisit(Node $visitable) {}
    
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

    public function getXmlString() {
        return $this->xmlString;
    }
}
