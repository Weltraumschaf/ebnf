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
 * @see SyntaxBuilder
 */
require_once 'ast/builder/SyntaxBuilder.php';
/**
 * @see Node
 */
require_once 'ast/Node.php';
/**
 * @see Notification
 */
require_once 'ast/Notification.php';
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

use de\weltraumschaf\ebnf\visitor\Visitor;
use de\weltraumschaf\ebnf\ast\builder\SyntaxBuilder;

/**
 * Test for integrating all AST node types.
 *
 * @package tests
 */
class IntegrationTest extends \PHPUnit_Framework_TestCase {

    public function testThatNodeIsNotComposite() {
        $this->assertNotInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Terminal($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $this->assertNotInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Identifier($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
    }

    public function testThatNodeIsComposite() {
        $this->assertInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Choice($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $this->assertInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Loop($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $this->assertInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Option($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $this->assertInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Rule($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $this->assertInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Sequence($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
        $this->assertInstanceOf(
            'de\weltraumschaf\ebnf\ast\Composite',
            new Syntax($this->getMock('de\weltraumschaf\ebnf\ast\Node'))
        );
    }

    public function testIntegration() {
        $syntax = new Syntax();
        $syntax->meta = "foo";
        $syntax->title = "bar";
        $it = $syntax->getIterator();
        $this->assertEquals(0, $it->count());
        $this->assertFalse($it->offsetExists(0));

        $rule1 = new Rule($syntax);
        $this->assertTrue($rule1->hasParent());
        $this->assertSame($syntax, $rule1->getParent());
        $rule1->name = "first";
        $syntax->addChild($rule1);

        $it = $syntax->getIterator();
        $this->assertEquals(1, $it->count());
        $this->assertTrue($it->offsetExists(0));
        $this->assertSame($rule1, $it->offsetGet(0));

        $rule2 = new Rule($syntax);
        $rule2->name = "second";
        $this->assertTrue($rule2->hasParent());
        $this->assertSame($syntax, $rule1->getParent());
        $syntax->addChild($rule2);

        $it = $syntax->getIterator();
        $this->assertEquals(2, $it->count());
        $this->assertTrue($it->offsetExists(0));
        $this->assertSame($rule1, $it->offsetGet(0));
        $this->assertTrue($it->offsetExists(1));
        $this->assertSame($rule2, $it->offsetGet(1));
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

        $n = new Notification();
        $syntax1->probeEquivalence($syntax1, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());
        $n = new Notification();
        $syntax1->probeEquivalence($syntax2, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = new Notification();
        $syntax2->probeEquivalence($syntax2, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());
        $n = new Notification();
        $syntax2->probeEquivalence($syntax1, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = new Notification();
        $syntax3->probeEquivalence($syntax3, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $errors  = "Titles of syntx differs: 'foo' != 'bla'!" . PHP_EOL;
        $errors .= "Meta of syntx differs: 'bar' != 'blub'!";
        $n = new Notification();
        $syntax1->probeEquivalence($syntax3, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
        $n = new Notification();
        $syntax2->probeEquivalence($syntax3, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
        $errors  = "Titles of syntx differs: 'bla' != 'foo'!" . PHP_EOL;
        $errors .= "Meta of syntx differs: 'blub' != 'bar'!";
        $n = new Notification();
        $syntax3->probeEquivalence($syntax1, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());
        $n = new Notification();
        $syntax3->probeEquivalence($syntax2, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($errors, $n->report());

        $errors = "Probed node types mismatch: 'de\weltraumschaf\ebnf\ast\Syntax' != 'BadNode'!";
        $stub = new Syntax();
        $mock = $this->getMock('de\weltraumschaf\ebnf\ast\Node', array(), array(), 'BadNode');
        $this->assertFalse($mock instanceof Syntax);
        $n = new Notification();
        $stub->probeEquivalence($mock, $n);
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
        $rule1 = new Rule($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $rule1->name = "rule1";
        $syntax1->addChild($rule1);
        $syntax2->addChild($rule1);

        $n = new Notification();
        $syntax1->probeEquivalence($syntax2, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = new Notification();
        $syntax2->probeEquivalence($syntax1, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $rule2 = new Rule($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $rule2->name = "rule2";
        $syntax1->addChild($rule2);
        $error  = "Node syntax has different child count than other: 2 != 1!\n";
        $error .= "Other node has not the expected subnode!";
        $n = new Notification();
        $syntax1->probeEquivalence($syntax2, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($error, $n->report());

        $error  = "Node syntax has different child count than other: 1 != 2!";
        $n = new Notification();
        $syntax2->probeEquivalence($syntax1, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals($error, $n->report());

        $syntax2->addChild($rule2);
        $n = new Notification();
        $syntax1->probeEquivalence($syntax2, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = new Notification();
        $syntax2->probeEquivalence($syntax1, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $rule3 = new Rule($this->getMock('de\weltraumschaf\ebnf\ast\Node'));
        $rule3->name = "rule3";
        $syntax3->addChild($rule1);
        $syntax3->addChild($rule3);
        $n = new Notification();
        $syntax1->probeEquivalence($syntax3, $n);
        $this->assertFalse($n->isOk());
        $this->assertEquals("Names of rule differs: 'rule2' != 'rule3'!", $n->report());
    }

    public function testProbeEquivalenceSyntaxWithRulesAndSubnodes() {
        $builder = new SyntaxBuilder();
        $builder->syntax("foo", "bar")
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
                    ->end();
        $syntax1 = $builder->getAst();
        $builder->clear()
                ->syntax("foo", "bar")
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
                    ->end();
        $syntax2 = $builder->getAst();

        $n = new Notification();
        $syntax1->probeEquivalence($syntax2, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $n = new Notification();
        $syntax2->probeEquivalence($syntax1, $n);
        $this->assertTrue($n->isOk(), $n->report());
        $this->assertEquals("", $n->report());

        $builder->clear()
                ->syntax("foo", "bar")
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
                    ->end();
        $syntax1 = $builder->getAst();
        $builder->clear()
                ->syntax("snafu", "bar")
                ->rule("syntax")
                    ->sequence()
                        ->option()
                            ->identifier("bla")
                        ->end()
                        ->terminal("{")
                        ->loop()
                            ->identifier("snafu")
                        ->end()
                        ->terminal("}")
                        ->option()
                            ->identifier("blub")
                        ->end()
                    ->end();
        $syntax2 = $builder->getAst();

        $n = new Notification();
        $syntax1->probeEquivalence($syntax2, $n);
        $this->assertFalse($n->isOk(), $n->report());
        $this->assertEquals(
            "Titles of syntx differs: 'foo' != 'snafu'!\n" .
            "Identifier value mismatch: 'title' != 'bla'!\n" .
            "Identifier value mismatch: 'rule' != 'snafu'!\n" .
            "Identifier value mismatch: 'comment' != 'blub'!",
            $n->report()
        );

        $n = new Notification();
        $syntax2->probeEquivalence($syntax1, $n);
        $this->assertFalse($n->isOk(), $n->report());
        $this->assertEquals(
            "Titles of syntx differs: 'snafu' != 'foo'!\n" .
            "Identifier value mismatch: 'bla' != 'title'!\n" .
            "Identifier value mismatch: 'snafu' != 'rule'!\n" .
            "Identifier value mismatch: 'blub' != 'comment'!",
            $n->report()
        );

    }
}