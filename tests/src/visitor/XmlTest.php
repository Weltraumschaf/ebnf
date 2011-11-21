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
 * @package tests
 */

namespace de\weltraumschaf\ebnf\visitor;

/**
 * @see Validator
 */
require_once 'visitor/Validator.php';
/**
 * @see Xml
 */
require_once 'visitor/Xml.php';
/**
 * @see Choice
 */
require_once 'ast/Choice.php';
/**
 * @see Identifier
 */
require_once 'ast/Identifier.php';
/**
 * @see Loop
 */
require_once 'ast/Loop.php';
/**
 * @see Option
 */
require_once 'ast/Option.php';
/**
 * @see Rule
 */
require_once 'ast/Rule.php';
/**
 * @see Sequence
 */
require_once 'ast/Sequence.php';
/**
 * @see Syntax
 */
require_once 'ast/Syntax.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';

use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Terminal;

/**
 * Testcases for {@link Xml}.
 *
 * @package tests
 */
class XmlTest extends \PHPUnit_Framework_TestCase {

    public function testCreateOpenTag() {
        $this->assertEquals('<foo>', Xml::createOpenTag("foo"));
        $this->assertEquals(
            '<foo bar="1" baz="&lt;&quot;&gt;&amp;">',
            Xml::createOpenTag("foo", array("bar" => "1", "baz" => "<\">&"))
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
        $this->assertEquals(
            '<?xml version="1.0" encoding="utf-8"?>',
            $visitor->getXmlString()
        );

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
        $seq   = new Sequence();
        $opt   = new Option();
        $ident = new Identifier();
        $ident->value = "title";
        $opt->addChild($ident);
        $seq->addChild($opt);
        $terminal = new Terminal();
        $terminal->value = "{";
        $seq->addChild($terminal);
        $loop = new Loop();
        $ident = new Identifier();
        $ident->value = "rule";
        $loop->addChild($ident);
        $seq->addChild($loop);
        $terminal = new Terminal();
        $terminal->value = "}";
        $seq->addChild($terminal);
        $opt   = new Option();
        $ident = new Identifier();
        $ident->value = "comment";
        $opt->addChild($ident);
        $seq->addChild($opt);
        $syntaxRule->addChild($seq);
        $syntax->addChild($syntaxRule);

        $ruleRule = new Rule();
        $ruleRule->name = "rule";
        $seq   = new Sequence();
        $ident = new Identifier();
        $ident->value = "identifier";
        $seq->addChild($ident);
        $choice = new Choice();
        foreach (array("=", ":", ":==") as $literal) {
            $terminal = new Terminal();
            $terminal->value = $literal;
            $choice->addChild($terminal);
        }
        $seq->addChild($choice);
        $ident = new Identifier();
        $ident->value = "expression";
        $seq->addChild($ident);
        $choice = new Choice();
        foreach (array(".", ";") as $literal) {
            $terminal = new Terminal();
            $terminal->value = $literal;
            $choice->addChild($terminal);
        }
        $seq->addChild($choice);
        $ruleRule->addChild($seq);
        $syntax->addChild($ruleRule);

        $literalRule = new Rule();
        $literalRule->name = "literal";
        $choice   = new Choice();
        $seq      = new Sequence();
        $terminal = new Terminal();
        $terminal->value = "'";
        $seq->addChild($terminal);
        $ident = new Identifier();
        $ident->value = "character";
        $seq->addChild($ident);
        $loop  = new Loop();
        $ident = new Identifier();
        $ident->value = "character";
        $loop->addChild($ident);
        $seq->addChild($loop);
        $seq->addChild($terminal);
        $choice->addChild($seq);
        $seq      = new Sequence();
        $terminal = new Terminal();
        $terminal->value = '"';
        $seq->addChild($terminal);
        $ident = new Identifier();
        $ident->value = "character";
        $seq->addChild($ident);
        $loop  = new Loop();
        $ident = new Identifier();
        $ident->value = "character";
        $loop->addChild($ident);
        $seq->addChild($loop);
        $seq->addChild($terminal);
        $choice->addChild($seq);
        $literalRule->addChild($choice);
        $syntax->addChild($literalRule);

        $visitor = new Xml();
        $syntax->accept($visitor);
        $xml = file_get_contents(EBNF_TESTS_FIXTURS . "/validator/syntax.xml");
        $this->assertEquals($xml, $visitor->getXmlString());

    }
}