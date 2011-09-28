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

namespace Weltraumschaf\Ebnf;

use \DOMDocument              as DOMDocument;
use \DOMElement               as DOMElement;
use \RuntimeException         as RuntimeException;
use \InvalidArgumentException as InvalidArgumentException;

/**
 * Description of Renderer
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @license http://www.weltraumschaf.de/the-beer-ware-license.txt THE BEER-WARE LICENSE
 */
class Renderer {
    const FONT = 4;
    const UNIT = 10;

    const FORMAT_PNG = "png";
    const FORMAT_JPG = "jpg";
    const FORMAT_GIF = "gif";
    const FORMAT_XML = "xml";

    private $white;
    private $black;
    private $blue;
    private $red;
    private $green;
    private $silver;

    private $format;
    private $file;
    /**
     * @var DOMDocument
     */
    private $dom;

    public function __construct($format, $file, DOMDocument $dom) {
        $this->format = (string)$format;
        $this->file   = (string)$file;
        $this->dom    = $dom;
    }

    public function save() {
        if (self::FORMAT_XML === $this->format) {
            $out = $this->dom->saveXML();

            if (false === file_put_contents($this->file, $out)) {
                throw new \RuntimeException("Can't write output to '{$this->file}'!");
            }
        } else {
            $this->saveImage();
        }
    }

    private function saveImage() {
        $out = $this->renderNode($this->dom->firstChild, true);

        switch ($this->format) {
            case self::FORMAT_PNG:
                imagepng($out, $this->file);
                break;
            case self::FORMAT_JPG:
                imagejpeg($out, $this->file);;
                break;
            case self::FORMAT_GIF:
                imagegif($out, $this->file);
                break;
            default:
                throw new \InvalidArgumentException("Unsupported format: '{$this->format}'!");
        }

        imagedestroy($out);
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
    private function renderNode(DOMElement $node, $leftToRight) {
        if ($node->nodeName === Parser::NODE_TYPE_IDENTIFIER || $node->nodeName === Parser::NODE_TYPE_TERMINAL) {
            $text = $node->getAttribute('value');
            $w = imagefontwidth(self::FONT) * (strlen($text)) + 4 * self::UNIT;
            $h = 2 * self::UNIT;
            $im = $this->createImage($w, $h);

            if ($node->nodeName !== Parser::NODE_TYPE_TERMINAL) {
                imagerectangle($im, self::UNIT, 0, $w - self::UNIT - 1, $h - 1, $this->black);
                imagestring($im, self::FONT, 2 * self::UNIT, ($h - imagefontheight(self::FONT)) / 2, $text, $this->red);
            } else {
                if ($text !== "...") {
                    $this->rr($im, self::UNIT, 0, $w - self::UNIT - 1, $h - 1, self::UNIT / 2, $this->black);
                }

                if ($text !== "...") {
                    $color = $this->blue;
                } else {
                    $color = $this->black;
                }

                imagestring($im, self::FONT, 2 * self::UNIT, ($h - imagefontheight(self::FONT)) / 2, $text, $color);
            }

            imageline($im, 0, self::UNIT, self::UNIT, self::UNIT, $this->black);
            imageline($im, $w - self::UNIT, self::UNIT, $w + 1, self::UNIT, $this->black);

            return $im;

        } else if ($node->nodeName === Parser::NODE_TYPE_LOOP || $node->nodeName === Parser::NODE_TYPE_LOOP) {
            if ($node->nodeName === Parser::NODE_TYPE_LOOP) {
                $leftToRight = !$leftToRight;
            }

            $inner = $this->renderNode($node->firstChild, $leftToRight);
            $w = imagesx($inner) + 6 * self::UNIT;
            $h = imagesy($inner) + 2 * self::UNIT;
            $im = $this->createImage($w, $h);
            imagecopy($im, $inner, 3 * self::UNIT, 2 * self::UNIT, 0, 0, imagesx($inner), imagesy($inner));
            imageline($im, 0, self::UNIT, $w, self::UNIT, $this->black);

            if ($node->nodeName === Parser::NODE_TYPE_LOOP) {
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

        } else if ($node->nodeName === Parser::NODE_TYPE_SEQUENCE) {
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

        } else if ($node->nodeName === Parser::NODE_TYPE_CHOISE) {
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

        } else if ($node->nodeName === Parser::NODE_TYPE_SYNTAX) {
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
}
