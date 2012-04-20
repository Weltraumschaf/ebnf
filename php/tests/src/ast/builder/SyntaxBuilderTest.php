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

namespace de\weltraumschaf\ebnf\ast\builder;

/**
 * @see SyntaxBuilder
 */
require_once 'ast/builder/SyntaxBuilder.php';
/**
 * @see Xml
 */
require_once 'visitor/Xml.php';

use de\weltraumschaf\ebnf\visitor\Xml;

/**
 * Tests for all syntax builder classes: {@link SyntaxBuilder}, {@link RuleBuilder} and {@link Builder}.
 *
 * Tests generating AST by using the internal DSL interface of the builder.
 *
 * @package tests
 * @version @@version@@
 */
class SyntaxBuilderTest extends \PHPUnit_Framework_TestCase {
    public function testbuilder() {
        $builder = new SyntaxBuilder();
        $builder->syntax("EBNF defined in itself.", "xis/ebnf v2.0 http://wiki.karmin.ch/ebnf/ gpl3")
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
