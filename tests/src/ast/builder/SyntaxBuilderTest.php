<?php
namespace de\weltraumschaf\ebnf\ast\builder;

require_once 'ast/Choice.php';
require_once 'ast/Identifier.php';
require_once 'ast/Loop.php';
require_once 'ast/Option.php';
require_once 'ast/Rule.php';
require_once 'ast/Sequence.php';
require_once 'ast/Syntax.php';
require_once 'ast/Terminal.php';

use de\weltraumschaf\ebnf\ast\Choice;
use de\weltraumschaf\ebnf\ast\Identifier;
use de\weltraumschaf\ebnf\ast\Loop;
use de\weltraumschaf\ebnf\ast\Option;
use de\weltraumschaf\ebnf\ast\Rule;
use de\weltraumschaf\ebnf\ast\Syntax;
use de\weltraumschaf\ebnf\ast\Sequence;
use de\weltraumschaf\ebnf\ast\Terminal;

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
}

class RuleBuilder {
    /**
     * @var Rule
     */
    private $rule;
    /**
     * @var SyntaxBuilder
     */
    private $parent;

    public function __construct(Rule $rule, SyntaxBuilder $parent) {
        $this->rule = $rule;
        $this->parent = $parent;
    }

    public function rule($name) {
        return $this->parent->rule($name);
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->rule->addChild($t);
        return $this;
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->rule->addChild($i);
        return $this;
    }

    public function sequence() {
        $seq = new Sequence();
        $this->rule->addChild($seq);
        return new SequenceBuilder($seq, $this);
    }

    public function choice() {
        $choice = new Choice();
        $this->rule->addChild($choice);
        return new ChoiceBuilder($choice, $this);
    }

    public function loop() {
        $loop = new Loop();
        $this->rule->addChild($loop);
        return new LoopBuilder($loop, $this);
    }
}

class SequenceBuilder {
    /**
     * @var Sequence
     */
    private $seq;
    /**
     * @var RuleBuilder
     */
    private $parent;

    public function __construct(Sequence $seq, RuleBuilder $parent) {
        $this->seq = $seq;
        $this->parent = $parent;
    }

    public function option() {
        $option = new Option();
        $this->seq->addChild($option);
        return new OptionBuilder($option, $this);
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->seq->addChild($i);
        return $this;
    }

    public function terminal() {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->seq->addChild($t);
        return $ths;
    }

    public function rule($name) {
        return $this->parent->rule($name);
    }
}

class ChoiceBuilder {
    /**
     * @var Choice
     */
    private $choice;
    /**
     * @var RuleBuilder
     */
    private $parent;

    function __construct(Choice $choice, RuleBuilder $parent) {
        $this->choice = $choice;
        $this->parent = $parent;
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->choice->addChild($i);
        return $this;
    }

    public function terminal() {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->choice->addChild($t);
        return $ths;
    }

    public function rule($name) {
        return $this->parent->rule($name);
    }
}

class OptionBuilder {

    private $option;
    private $parent;

    function __construct(Option $option, $parent) {
        $this->option = $option;
        $this->parent = $parent;
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->option->addChild($i);
        return $this;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->option->addChild($t);
        return $this;
    }

    public function loop() {
        $loop = new Loop();
        $this->option->addChild($loop);
        return new LoopBuilder($loop, $this);
    }

    public function rule($name) {
        return $this->parent->rule($name);
    }
}

class LoopBuilder {

    private $loop;
    private $parent;

    function __construct(Loop $loop, $parent) {
        $this->loop = $loop;
        $this->parent = $parent;
    }

    public function identifier($value) {
        $i = new Identifier();
        $i->value = (string) $value;
        $this->loop->addChild($i);
        return $this;
    }

    public function terminal($value) {
        $t = new Terminal();
        $t->value = (string) $value;
        $this->loop->addChild($t);
        return $this;
    }

    public function option() {
        $option = new Option();
        $this->loop->addChild($option);
        return new OptionBuilder($option, $this);
    }

    public function rule($name) {
        return $this->parent->rule($name);
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
                        ->terminal("{")
                        ->loop()
                            ->identifier("rule")
                        ->terminal("}")
                        ->option()
                            ->identifier("comment")
                ->rule("second-rule")
                    ->choice()
                ->rule("third-rule")
                    ->terminal("foo")
                ->rule("fourth-rule")
                    ->identifier("bar");
    }
}
