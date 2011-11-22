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

namespace de\weltraumschaf\ebnf\ast;

/**
 * @see Composite
 */
require_once 'ast/Composite.php';
/**
 * @see Node
 */
require_once 'ast/Node.php';
/**
 * @see Terminal
 */
require_once 'ast/Terminal.php';
/**
 * @see Identifier
 */
require_once 'ast/Identifier.php';
/**
 * @see Choice
 */
require_once 'ast/Choice.php';
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

class SyntaxStub extends Syntax {
    public function exposedProbeEquivalenceInternal(Node $other, Notification $result) {
        parent::probeEquivalenceInternal($other, $result);
    }
}

/**
 * Test for integrating all AST node types.
 *
 * @package tests
 */
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

    public function testIntegration() {
        $syntax = new Syntax();
        $syntax->meta = "foo";
        $syntax->title = "bar";

        $firstRule = new Rule();
        $firstRule->name = "first";
        $syntax->addChild($firstRule);

        $secondRule = new Rule();
        $secondRule->name = "second";
        $syntax->addChild($secondRule);

        $this->markTestIncomplete();
    }

    public function testProbeEquivalenceSyntax() {
        $syntax1 = new Syntax();
        $syntax1->title = "foo";
        $syntax1->meta  = "bar";
        $syntax2 = new Syntax();
        $syntax2->title = "foo";
        $syntax2->meta  = "bar";
        $syntax3 = new Syntax();
        $syntax3->title = "bla";
        $syntax3->meta  = "blub";

        $n = $syntax1->probeEquivalence($syntax1);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());
        $n = $syntax1->probeEquivalence($syntax2);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = $syntax2->probeEquivalence($syntax2);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());
        $n = $syntax2->probeEquivalence($syntax1);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = $syntax3->probeEquivalence($syntax3);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $errors  = "Titles of syntx differs: 'foo' != 'bla'!" . PHP_EOL;
        $errors .= "Meta of syntx differs: 'bar' != 'blub'!";
        $n = $syntax1->probeEquivalence($syntax3);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
        $n = $syntax2->probeEquivalence($syntax3);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
        $errors  = "Titles of syntx differs: 'bla' != 'foo'!" . PHP_EOL;
        $errors .= "Meta of syntx differs: 'blub' != 'bar'!";
        $n = $syntax3->probeEquivalence($syntax1);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
        $n = $syntax3->probeEquivalence($syntax2);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());

        $errors = "Probed node types mismatch: 'de\weltraumschaf\ebnf\ast\SyntaxStub' != 'BadNode'!";
        $stub = new SyntaxStub();
        $mock = $this->getMock('de\weltraumschaf\ebnf\ast\Node', array(), array(), 'BadNode');
        $this->assertFalse($mock instanceof Syntax);
        $n = new Notification();
        $stub->exposedProbeEquivalenceInternal($mock, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
    }

    public function testProbeEquivalenceSyntaxWithRules() {
        $syntax1 = new Syntax();
        $syntax1->title = "foo";
        $syntax1->meta  = "bar";
        $syntax2 = new Syntax();
        $syntax2->title = "foo";
        $syntax2->meta  = "bar";
        $syntax3 = new Syntax();
        $syntax3->title = "foo";
        $syntax3->meta  = "bar";
        $rule1 = new Rule();
        $rule1->name = "rule1";
        $syntax1->addChild($rule1);
        $syntax2->addChild($rule1);

        $n = $syntax1->probeEquivalence($syntax2);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = $syntax2->probeEquivalence($syntax1);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $rule2 = new Rule();
        $rule2->name = "rule2";
        $syntax1->addChild($rule2);
        $error  = "Node syntax has different child count than other: 2 != 1!\n";
        $error .= "Other node has not the expected subnode!";
        $n = $syntax1->probeEquivalence($syntax2);
        $this->assertFalse($n->isOk());
        $this->assertEquals($error, $n->report());

        $error  = "Node syntax has different child count than other: 1 != 2!";
        $n = $syntax2->probeEquivalence($syntax1);
        $this->assertFalse($n->isOk());
        $this->assertEquals($error, $n->report());

        $syntax2->addChild($rule2);
        $n = $syntax1->probeEquivalence($syntax2);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = $syntax2->probeEquivalence($syntax1);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $rule3 = new Rule();
        $rule3->name = "rule3";
        $syntax3->addChild($rule1);
        $syntax3->addChild($rule3);
        $n = $syntax1->probeEquivalence($syntax3);
        $this->assertFalse($n->isOk());
        $this->assertEquals("Names of rule differs: 'rule2' != 'rule3'!", $n->report());
    }

}