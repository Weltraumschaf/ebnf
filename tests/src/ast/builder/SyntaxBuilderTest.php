<?php
namespace de\weltraumschaf\ebnf\ast\builder;

require_once 'ast/builder/SyntaxBuilder.php';
require_once 'visitor/Xml.php';

use de\weltraumschaf\ebnf\visitor\Xml;

class SyntaxBuilderTest extends \PHPUnit_Framework_TestCase {
    public function testbuilder() {
        $builder = new SyntaxBuilder();
        $builder->syntaxt("EBNF defined in itself.", "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3")
                ->rule("syntax")
                    ->sequence()
                        ->option()
                            ->identifier("title")
                        ->end()
                        ->terminal("{")
                        ->loop()
                            ->identifier("rule")
                        ->end()
                        ->terminal("}")
                        ->option()
                            ->identifier("comment")
                        ->end()
                    ->end()
                ->rule("rule")
                    ->sequence()
                        ->identifier("identifier")
                        ->choice()
                            ->terminal("=")
                            ->terminal(":")
                            ->terminal(":==")
                        ->end()
                        ->identifier("expression")
                        ->choice()
                            ->terminal(".")
                            ->terminal(";")
                        ->end()
                    ->end()
                ->rule("literal")
                    ->choice()
                        ->sequence()
                            ->terminal("'")
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal("'")
                        ->end()
                        ->sequence()
                            ->terminal('"')
                            ->identifier("character")
                            ->loop()
                                ->identifier("character")
                            ->end()
                            ->terminal('"')
                        ->end()
                    ->end();

        $syntax  = $builder->getAst();
        $xml     = file_get_contents(EBNF_TESTS_FIXTURS . "/visitor/syntax.xml");
        $visitor = new Xml();
        $syntax->accept($visitor);
        $this->assertEquals($xml, $visitor->getXmlString());
    }
}
