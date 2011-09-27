<?php

namespace Weltraumschaf;

use \DOMDocument as DOMDocument;
use \DOMElement  as DOMElement;
use \Exception   as Exception;

/**
 * Inspired by Vincent Tscherter (http://karmin.ch/ebnf/index).
 */
class Ebnf {
    const META = "xis/ebnf v0.2 http://wiki.karmin.ch/ebnf/ gpl3";

    // parser
    const OPERATOR_TOKEN   = 1;
    const LITERAL_TOKEN    = 2;
    const WHITESPACE_TOKEN = 3;
    const IDENTIFIER_TOKEN = 4;

    // rendering
    const FONT = 3;
    const UNIT = 10;

    const FORMAT_PNG = "png";
    const FORMAT_XML = "xml";

    const NODE_TYPE_IDENTIFIER = "identifier";
    const NODE_TYPE_TERMINAL   = "terminal";
    const NODE_TYPE_OPTION     = "option";
    const NODE_TYPE_LOOP       = "loop";
    const NODE_TYPE_SEQUENCE   = "sequence";
    const NODE_TYPE_CHOISE     = "choise";
    const NODE_TYPE_SYNTAX     = "syntax";

    // lexemes
    private $lexemes = array(
        array('type' => self::OPERATOR_TOKEN,   'expr' => '[={}()|.;[\]]'),
        array('type' => self::LITERAL_TOKEN,    'expr' => "\"[^\"]*\""),
        array('type' => self::LITERAL_TOKEN,    'expr' => "'[^']*'"),
        array('type' => self::IDENTIFIER_TOKEN, 'expr' => "[a-zA-Z0-9_-]+"),
        array('type' => self::WHITESPACE_TOKEN, 'expr' => "\\s+")
    );

    private $white;
    private $black;
    private $blue;
    private $red;
    private $green;
    private $silver;

    /**
     * @var string
     */
    private $input;
    /**
     * @var string
     */
    private $format;

    public function __construct($input, $format = self::FORMAT_PNG) {
        $this->input  = (string)$input;
        $this->format = (string)$format;
    }

    public function create() {
        $tokens = $this->ebnfScan($this->input, true);
        $dom    = $this->ebnfParseSyntax($tokens);

        if (self::FORMAT_XML === $this->format) {
            return $dom->saveXML();
        }

        return $this->renderNode($dom->firstChild, true);
    }

    private function rr($im, $x1, $y1, $x2, $y2, $r, $black) {
        imageline($im, $x1 + $r, $y1, $x2 - $r, $y1, $black);
        imageline($im, $x1 + $r, $y2, $x2 - $r, $y2, $black);
        imageline($im, $x1, $y1 + $r, $x1, $y2 - $r, $black);
        imageline($im, $x2, $y1 + $r, $x2, $y2 - $r, $black);
        imagearc($im, $x1 + $r, $y1 + $r, 2 * $r, 2 * $r, 180, 270, $black);
        imagearc($im, $x2 - $r, $y1 + $r, 2 * $r, 2 * $r, 270, 360, $black);
        imagearc($im, $x1 + $r, $y2 - $r, 2 * $r, 2 * $r, 90, 180, $black);
        imagearc($im, $x2 - $r, $y2 - $r, 2 * $r, 2 * $r, 0, 90, $black);
    }

    /**
     *
     * @param int $width
     * @param int $height
     * @return resource
     */
    private function createImage($width, $height) {
        $im = imagecreatetruecolor($width, $height);
        imageantialias($im, true);
        $this->white  = imagecolorallocate($im, 255, 255, 255);
        $this->black  = imagecolorallocate($im, 0, 0, 0);
        $this->blue   = imagecolorallocate($im, 0, 0, 255);
        $this->red    = imagecolorallocate($im, 255, 0, 0);
        $this->green  = imagecolorallocate($im, 0, 200, 0);
        $this->silver = imagecolorallocate($im, 127, 127, 127);
        imagefilledrectangle($im, 0, 0, $width, $height, $this->white);

        return $im;
    }

    private function arrow($image, $x, $y, $lefttoright) {
        if ($lefttoright) {
            $points = array(
                $x - self::UNIT,
                $y - self::UNIT / 3,
                $x,
                $y,
                $x - self::UNIT,
                $y + self::UNIT / 3
            );
        } else {
            $points = array(
                $x,
                $y - self::UNIT / 3,
                $x - self::UNIT,
                $y,
                $x,
                $y + self::UNIT / 3);
        }

        imagefilledpolygon($image, $points, 3, $this->black);
    }

    /**
     *
     * @param DOMElement $node
     * @param bool $leftToRight
     * @return resource
     */
    public function renderNode(DOMElement $node, $leftToRight) {
        if ($node->nodeName === self::NODE_TYPE_IDENTIFIER || $node->nodeName === self::NODE_TYPE_TERMINAL) {
            $text = $node->getAttribute('value');
            $w = imagefontwidth(self::FONT) * (strlen($text)) + 4 * self::UNIT;
            $h = 2 * self::UNIT;
            $im = $this->createImage($w, $h);

            if ($node->nodeName != 'terminal') {
                imagerectangle($im, self::UNIT, 0, $w - self::UNIT - 1, $h - 1, $this->black);
                imagestring($im, self::FONT, 2 * self::UNIT, ($h - imagefontheight(self::FONT)) / 2, $text, $this->red);
            } else {
                if ($text != "...") {
                    $this->rr($im, self::UNIT, 0, $w - self::UNIT - 1, $h - 1, self::UNIT / 2, $this->black);
                }

                imagestring($im, self::FONT, 2 * self::UNIT, ($h - imagefontheight(self::FONT)) / 2, $text, $text != "..." ? $this->blue : $this->black);
            }

            imageline($im, 0, self::UNIT, self::UNIT, self::UNIT, $this->black);
            imageline($im, $w - self::UNIT, self::UNIT, $w + 1, self::UNIT, $this->black);

            return $im;

        } else if ($node->nodeName === self::NODE_TYPE_LOOP || $node->nodeName === self::NODE_TYPE_LOOP) {
            if ($node->nodeName === self::NODE_TYPE_LOOP) {
                $leftToRight = !$leftToRight;
            }

            $inner = $this->renderNode($node->firstChild, $leftToRight);
            $w = imagesx($inner) + 6 * self::UNIT;
            $h = imagesy($inner) + 2 * self::UNIT;
            $im = $this->createImage($w, $h);
            imagecopy($im, $inner, 3 * self::UNIT, 2 * self::UNIT, 0, 0, imagesx($inner), imagesy($inner));
            imageline($im, 0, self::UNIT, $w, self::UNIT, $this->black);

            if ($node->nodeName === self::NODE_TYPE_LOOP) {
                $this->arrow($im, $w / 2 + self::UNIT / 2, self::UNIT, !$leftToRight);
            } else {
                $this->arrow($im, $w / 2 + self::UNIT / 2, self::UNIT, $leftToRight);
            }

            $this->arrow($im, 3 * self::UNIT, 3 * self::UNIT, $leftToRight);
            $this->arrow($im, $w - 2 * self::UNIT, 3 * self::UNIT, $leftToRight);
            imageline($im, self::UNIT, self::UNIT, self::UNIT, 3 * self::UNIT, $this->black);
            imageline($im, self::UNIT, 3 * self::UNIT, 2 * self::UNIT, 3 * self::UNIT, $this->black);
            imageline($im, $w - self::UNIT, self::UNIT, $w - self::UNIT, 3 * self::UNIT, $this->black);
            imageline($im, $w - 3 * self::UNIT - 1, 3 * self::UNIT, $w - self::UNIT, 3 * self::UNIT, $this->black);

            return $im;

        } else if ($node->nodeName === self::NODE_TYPE_SEQUENCE) {
            $inner = $this->renderChilds($node, $leftToRight);

            if (!$leftToRight) {
                $inner = array_reverse($inner);
            }

            $w = count($inner) * self::UNIT - self::UNIT;
            $h = 0;

            for ($i = 0; $i < count($inner); $i++) {
                $w += imagesx($inner[$i]);
                $h = max($h, imagesy($inner[$i]));
            }

            $im = $this->createImage($w, $h);
            imagecopy($im, $inner[0], 0, 0, 0, 0, imagesx($inner[0]), imagesy($inner[0]));
            $x = imagesx($inner[0]) + self::UNIT;

            for ($i = 1; $i < count($inner); $i++) {
                imageline($im, $x - self::UNIT - 1, self::UNIT, $x, self::UNIT, $this->black);
                $this->arrow($im, $x, self::UNIT, $leftToRight);
                imagecopy($im, $inner[$i], $x, 0, 0, 0, imagesx($inner[$i]), imagesy($inner[$i]));
                $x += imagesx($inner[$i]) + self::UNIT;
            }

            return $im;

        } else if ($node->nodeName === self::NODE_TYPE_CHOISE) {
            $inner = $this->renderChilds($node, $leftToRight);
            $h = (count($inner) - 1) * self::UNIT;
            $w = 0;

            for ($i = 0; $i < count($inner); $i++) {
                $h += imagesy($inner[$i]);
                $w = max($w, imagesx($inner[$i]));
            }

            $w += 6 * self::UNIT;
            $im = $this->createImage($w, $h);
            $y = 0;
            imageline($im, 0, self::UNIT, self::UNIT, self::UNIT, $this->black);
            imageline($im, $w - self::UNIT, self::UNIT, $w, self::UNIT, $this->black);

            for ($i = 0; $i < count($inner); $i++) {
                imageline($im, self::UNIT, $y + self::UNIT, $w - self::UNIT, $y + self::UNIT, $this->black);
                imagecopy($im, $inner[$i], 3 * self::UNIT, $y, 0, 0, imagesx($inner[$i]), imagesy($inner[$i]));
                $this->arrow($im, 3 * self::UNIT, $y + self::UNIT, $leftToRight);
                $this->arrow($im, $w - 2 * self::UNIT, $y + self::UNIT, $leftToRight);
                $top = $y + self::UNIT;
                $y += imagesy($inner[$i]) + self::UNIT;
            }

            imageline($im, self::UNIT, self::UNIT, self::UNIT, $top, $this->black);
            imageline($im, $w - self::UNIT, self::UNIT, $w - self::UNIT, $top, $this->black);

            return $im;

        } else if ($node->nodeName === self::NODE_TYPE_SYNTAX) {
            $title  = $node->getAttribute('title');
            $meta   = $node->getAttribute('meta');
            $node   = $node->firstChild;
            $names  = array();
            $images = array();

            while ($node != null) {
                $names[]  = $node->getAttribute('name');
                $im       = $this->renderNode($node->firstChild, $leftToRight);
                $images[] = $im;
                $node     = $node->nextSibling;
            }

            $wn = 0;
            $wr = 0;
            $h  = 5 * self::UNIT;

            for ($i = 0; $i < count($images); $i++) {
                $wn = max($wn, imagefontwidth(self::FONT) * strlen($names[$i]));
                $wr = max($wr, imagesx($images[$i]));
                $h += imagesy($images[$i]) + 2 * self::UNIT;
            }

            if ($title == '') {
                $h -= 2 * self::UNIT;
            }

            if ($meta == '') {
                $h -= 2 * self::UNIT;
            }

            $w  = max($wr + $wn + 3 * self::UNIT, imagefontwidth(1) * strlen($meta) + 2 * self::UNIT);
            $im = $this->createImage($w, $h);
            $y  = 2 * self::UNIT;

            if ($title != '') {
                imagestring($im, self::FONT, self::UNIT, (2 * self::UNIT - imagefontheight(self::FONT)) / 2, $title, $this->green);
                imageline($im, 0, 2 * self::UNIT, $w, 2 * self::UNIT, $this->green);
                $y += 2 * self::UNIT;
            }

            for ($i = 0; $i < count($images); $i++) {
                imagestring($im, self::FONT, self::UNIT, $y - self::UNIT + (2 * self::UNIT - imagefontheight(self::FONT)) / 2, $names[$i], $this->red);
                imagecopy($im, $images[$i], $wn + 2 * self::UNIT, $y, 0, 0, imagesx($images[$i]), imagesy($images[$i]));
                imageline($im, self::UNIT, $y + self::UNIT, $wn + 2 * self::UNIT, $y + self::UNIT, $this->black);
                imageline($im, $wn + 2 * self::UNIT + imagesx($images[$i]) - 1, $y + self::UNIT, $w - self::UNIT, $y + self::UNIT, $this->black);
                imageline($im, $w - self::UNIT, $y + self::UNIT / 2, $w - self::UNIT, $y + 1.5 * self::UNIT, $this->black);
                $y += 2 * self::UNIT + imagesy($images[$i]);
            }

            imagestring($im, 1, self::UNIT, $h - 2 * self::UNIT + (2 * self::UNIT - imagefontheight(1)) / 2, $meta, $this->silver);
            $this->rr($im, 0, 0, $w - 1, $h - 1, self::UNIT / 2, $this->green);

            return $im;
        }
    }

    private function renderChilds($node, $lefttoright) {
        $childs = array();
        $node = $node->firstChild;

        while ($node !== null) {
            $childs[] = $this->renderNode($node, $lefttoright);
            $node = $node->nextSibling;
        }

        return $childs;
    }

    public function ebnfScan($input) {
        $i = 0;
        $n = strlen($input);
        $m = count($this->lexemes);
        $tokens = array();

        while ($i < $n) {
            $j = 0;

            while ($j < $m && preg_match("/^{$this->lexemes[$j]['expr']}/", substr($input, $i), $matches) === 0) {
                $j++;
            }

            if ($j < $m) {
                if ($this->lexemes[$j]['type'] !== self::WHITESPACE_TOKEN) {
                    $tokens[] = array(
                        'type'  => $this->lexemes[$j]['type'],
                        'value' => $matches[0],
                        'pos'   => $i
                    );
                }

                $i += strlen($matches[0]);
            } else {
                throw new Exception("Invalid token at position {$i}: " . substr($input, $i, 10) . "...");
            }
        }

        return $tokens;
    }

    private function ebnfCheckToken($token, $type, $value) {
        return $token['type'] === $type && $token['value'] === $value;
    }

    public function ebnfParseSyntax(&$tokens) {
        $dom = new DOMDocument();
        $syntax = $dom->createElement("syntax");
        $syntax->setAttribute('meta', self::META);
        $dom->appendChild($syntax);
        $i = 0;
        $token = $tokens[$i++];

        if ($token['type'] === self::LITERAL_TOKEN) {
            $syntax->setAttribute('title', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));
            $token = $tokens[$i++];
        }

        if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '{')) {
            throw new Exception("Syntax must start with '{': {$token['pos']}");
        }

        $token = $tokens[$i];

        while ($i < count($tokens) && $token['type'] === self::IDENTIFIER_TOKEN) {
            $syntax->appendChild($this->ebnfParseProduction($dom, $tokens, $i));

            if ($i < count($tokens)) {
                $token = $tokens[$i];
            }
        }

        $i++;

        if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '}')) {
            throw new Exception("Syntax must end with '}': " . $tokens[count($tokens) - 1]['pos']);
        }

        if ($i < count($tokens)) {
            $token = $tokens[$i];

            if ($token['type'] === self::LITERAL_TOKEN) {
                $syntax->setAttribute('meta', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));
            }
        }

        return $dom;
    }

    private function ebnfParseProduction(&$dom, &$tokens, &$i) {
        $token = $tokens[$i++];

        if ($token['type'] != self::IDENTIFIER_TOKEN) {
            throw new Exception("Production must start with an identifier'{': {$token['pos']}");
        }

        $production = $dom->createElement("rule");
        $production->setAttribute('name', $token['value']);
        $token = $tokens[$i++];

        if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, "=")) {
            throw new Exception("Identifier must be followed by '=': {$token['pos']}");
        }

        $production->appendChild($this->ebnfParseExpression($dom, $tokens, $i));
        $token = $tokens[$i++];

        if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '.') && !$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, ';')) {
            throw new Exception("Rule must end with '.' or ';' : {$token['pos']}");
        }

        return $production;
    }

    private function ebnfParseExpression(&$dom, &$tokens, &$i) {
        $choise = $dom->createElement("choise");
        $choise->appendChild($this->ebnfParseTerm($dom, $tokens, $i));
        $token = $tokens[$i];
        $mul = false;

        while ($this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '|')) {
            $i++;
            $choise->appendChild($this->ebnfParseTerm($dom, $tokens, $i));
            $token = $tokens[$i];
            $mul = true;
        }

        return $mul ? $choise : $choise->removeChild($choise->firstChild);
    }

    private function ebnfParseTerm(&$dom, &$tokens, &$i) {
        $sequence = $dom->createElement("sequence");
        $factor = $this->ebnfParseFactor($dom, $tokens, $i);
        $sequence->appendChild($factor);
        $token = $tokens[$i];
        $mul = false;

        while ($token['value'] != '.' && $token['value'] != '=' && $token['value'] != '|' && $token['value'] != ')' && $token['value'] != ']' && $token['value'] != '}') {
            $sequence->appendChild($this->ebnfParseFactor($dom, $tokens, $i));
            $token = $tokens[$i];
            $mul = true;
        }

        return $mul ? $sequence : $sequence->removeChild($sequence->firstChild);
    }

    private function ebnfParseFactor(&$dom, &$tokens, &$i) {
        $token = $tokens[$i++];

        if ($token['type'] == self::IDENTIFIER_TOKEN) {
            $identifier = $dom->createElement("identifier");
            $identifier->setAttribute('value', $token['value']);

            return $identifier;
        }

        if ($token['type'] == self::LITERAL_TOKEN) {
            $literal = $dom->createElement("terminal");
            $literal->setAttribute('value', stripcslashes(substr($token['value'], 1, strlen($token['value']) - 2)));

            return $literal;
        }

        if ($this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '(')) {
            $expression = $this->ebnfParseExpression($dom, $tokens, $i);
            $token = $tokens[$i++];

            if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, ')')) {
                throw new Exception("Group must end with ')': {$token['pos']}");
            }

            return $expression;
        }

        if ($this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '[')) {
            $option = $dom->createElement("option");
            $option->appendChild($this->ebnfParseExpression($dom, $tokens, $i));
            $token = $tokens[$i++];

            if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, ']')) {
                throw new Exception("Option must end with ']': {$token['pos']}");
            }

            return $option;
        }

        if ($this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '{')) {
            $loop = $dom->createElement("loop");
            $loop->appendChild($this->ebnfParseExpression($dom, $tokens, $i));
            $token = $tokens[$i++];

            if (!$this->ebnfCheckToken($token, self::OPERATOR_TOKEN, '}')) {
                throw new Exception("Loop must end with '}': {$token['pos']}");
            }

            return $loop;
        }

        throw new Exception("Factor expected: {$token['pos']}");
    }

}