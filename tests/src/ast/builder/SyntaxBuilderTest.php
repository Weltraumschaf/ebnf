<?php
namespace de\weltraumschaf\ebnf\ast\builder;

require_once 'ast/Choice.php';
require_once 'ast/Composite.php';
require_once 'ast/Identifier.php';
require_once 'ast/Loop.php';
require_once 'ast/Node.php';
require_once 'ast/Option.php';
require_once 'ast/Rule.php';
require_once 'ast/Sequence.php';
require_once 'ast/Syntax.php';
require_once 'ast/Terminal.php';
require_once 'visitor/Xml.php';

use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Composite;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Node;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Terminal;
use de\weltraumschaf\ebnf\visitor\Xml;

abstract class Builder {

    /**
     * @var Composite
     */
    protected $node;

    /**
     * @var Builder
     */
    private $parent;

    public function __construct(Composite $seq, Builder $parent) {
        $this->node   = $seq;
        $this->parent = $parent;
    }

    public function end() {
        return $this->parent;
    }
}

class SyntaxBuilder {
    /**
     * @var Syntax
     */
    private $syntax;

    public function syntaxt($title, $meta) {
        $this->syntax = new Syntax();
        $this->syntax->title = (string)$title;
        $this->syntax->meta = (string)$meta;
        return $this;
    }

    public function rule($name) {
        $rule = new Rule();
        $rule->name = (string) $name;
        $this->syntax->addChild($rule);
        return new RuleBuilder($rule, $this);
    }

    public function getAst() {
        return $this->syntax;
    }
}

class RuleBuilder extends Builder {

    public function __construct(Rule $rule, SyntaxBuilder $parent) {
        $this->node = $rule;
        $this->parent = $parent;
    }

    public function rule($name) {
        return $this->parent->rule($name);
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->node->addChild($t);
        return $this;
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->node->addChild($i);
        return $this;
    }

    public function sequence() {
        $seq = new Sequence();
        $this->node->addChild($seq);
        return new SequenceBuilder($seq, $this);
    }

    public function choice() {
        $choice = new Choice();
        $this->node->addChild($choice);
        return new ChoiceBuilder($choice, $this);
    }

    public function loop() {
        $loop = new Loop();
        $this->node->addChild($loop);
        return new LoopBuilder($loop, $this);
    }
}

class SequenceBuilder extends Builder {

    public function option() {
        $option = new Option();
        $this->node->addChild($option);
        return new OptionBuilder($option, $this);
    }

    public function loop() {
        $loop = new Loop();
        $this->node->addChild($loop);
        return new LoopBuilder($loop, $this);
    }

    public function choice() {
        $choice = new Choice();
        $this->node->addChild($choice);
        return new ChoiceBuilder($choice, $this);
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->node->addChild($i);
        return $this;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->node->addChild($t);
        return $this;
    }

}

class ChoiceBuilder extends Builder {

    public function sequence() {
        $seq = new Sequence();
        $this->node->addChild($seq);
        return new SequenceBuilder($seq, $this);
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->node->addChild($i);
        return $this;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->node->addChild($t);
        return $this;
    }

}

class OptionBuilder extends Builder {

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->node->addChild($i);
        return $this;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->node->addChild($t);
        return $this;
    }

    public function loop() {
        $loop = new Loop();
        $this->node->addChild($loop);
        return new LoopBuilder($loop, $this);
    }
}

class LoopBuilder extends Builder {

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->node->addChild($i);
        return $this;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->node->addChild($t);
        return $this;
    }

    public function option() {
        $option = new Option();
        $this->node->addChild($option);
        return new OptionBuilder($option, $this);
    }

}

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
