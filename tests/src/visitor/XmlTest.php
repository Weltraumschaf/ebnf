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

require_once 'visitor/Validator.php';
require_once 'visitor/Xml.php';
require_once 'ast/Choice.php';
require_once 'ast/Identifier.php';
require_once 'ast/Loop.php';
require_once 'ast/Option.php';
require_once 'ast/Rule.php';
require_once 'ast/Sequence.php';
require_once 'ast/Syntax.php';
require_once 'ast/Terminal.php';

use de\weltraumschaf\ebnf\ast\Identifier  as Identifier;
use de\weltraumschaf\ebnf\ast\Loop        as Loop;
use de\weltraumschaf\ebnf\ast\Option      as Option;
use de\weltraumschaf\ebnf\ast\Rule        as Rule;
use de\weltraumschaf\ebnf\ast\Sequence    as Sequence;
use de\weltraumschaf\ebnf\ast\Syntax      as Syntax;
use de\weltraumschaf\ebnf\ast\Terminal    as Terminal;

/**
 * Testcases for {Xml}.
 */
class XmlTest extends \PHPUnit_Framework_TestCase {
    
    public function testCreateOpenTag() {
        $this->assertEquals('<foo>', Xml::createOpenTag("foo"));
        $this->assertEquals(
            '<foo bar="1">', 
            Xml::createOpenTag("foo", array("bar" => "1"))
        );
        $this->assertEquals(
            '<foo bar="1" baz="2">', 
            Xml::createOpenTag("foo", array("bar" => "1", "baz" => 2))
        );
        $this->assertEquals(
            '<foo bar="1" baz="2"/>', 
            Xml::createOpenTag("foo", array("bar" => "1", "baz" => 2), false)
        );
    }
    
    public function testCloseOpenTag() {
        $this->assertEquals("</foo>", Xml::createCloseTag("foo"));
        $this->assertEquals("</fooBar>", Xml::createCloseTag("fooBar"));
    }
    
    public function testExtractAttributes() {
        $syntax = new Syntax();
        $syntax->meta  = "foo";
        $syntax->title = "bar";
        $this->assertEquals(
            array("meta" => "foo", "title" => "bar"), 
            Xml::extractAttributes($syntax)
        );
        $loop = new Loop();
        $this->assertEquals(array(), Xml::extractAttributes($loop));
    }

    public function testGenerateXml() {
        $visitor = new Xml();
        $this->assertEquals('<?xml version="1.0" encoding="utf-8"?>', $visitor->getXmlString());
        
        $syntax = new Syntax();
        $syntax->meta  = "EBNF defined in itself.";
        $syntax->title = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";
        $visitor = new Xml();
        $syntax->accept($visitor);
        $xml = file_get_contents(EBNF_TESTS_FIXTURS . "/validator/syntax.xml");
        $this->assertEquals(
            '<?xml version="1.0" encoding="utf-8"?>' . 
            "\n" .
            '<syntax title="xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3" meta="EBNF defined in itself."/>', 
            $visitor->getXmlString()
        );
        
        $syntax = new Syntax();
        $syntax->title = "EBNF defined in itself.";
        $syntax->meta  = "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3";
        
        $syntaxRule = new Rule();
        $syntaxRule->name = "syntax";
        $syntax->addChild($syntaxRule);
        
        $ruleRule = new Rule();
        $ruleRule->name = "rule";
        $syntax->addChild($ruleRule);
        
        $expressionRule = new Rule();
        $expressionRule->name = "expression";
        $syntax->addChild($expressionRule);
        
        $termRule = new Rule();
        $termRule->name = "term";
        $syntax->addChild($termRule);
        
        $literalRule = new Rule();
        $literalRule->name = "literal";
        $syntax->addChild($literalRule);
        
        $visitor = new Xml();
        $syntax->accept($visitor);
        $xml = file_get_contents(EBNF_TESTS_FIXTURS . "/validator/syntax.xml");
        $this->assertEquals($xml, $visitor->getXmlString());
        
    }
}