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
 * @author Vincent Tscherter <tscherter@karmin.ch>
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf\ast;

require_once "ast/Composite.php";
require_once "ast/Terminal.php";
require_once "ast/Identifier.php";
require_once "ast/Choice.php";
require_once "ast/Loop.php";
require_once "ast/Option.php";
require_once "ast/Rule.php";
require_once "ast/Sequence.php";
require_once "ast/Syntax.php";

class IntegrationTest extends \PHPUnit_Framework_TestCase {
    
    public function testThatNodeIsNotComposite() {
        $this->assertNotInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Terminal());
        $this->assertNotInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Identifier());
    }
    
    public function testThatNodeIsComposite() {
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Choice());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Loop());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Option());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Rule());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Sequence());
        $this->assertInstanceOf("de\weltraumschaf\ebnf\ast\Composite", new Syntax());
        $composite = $this->getMockForAbstractClass("de\weltraumschaf\ebnf\ast\Composite");
        $this->assertInstanceOf("\IteratorAggregate", $composite);
    }
    
    public function testIntegratiob() {
        $this->markTestIncomplete();
    }
    
}