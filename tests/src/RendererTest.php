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

namespace de\weltraumschaf\ebnf;

/**
 * @see Position
 */
require_once 'Renderer.php';

/**
 * Testcase for class {@link Renderer}.
 *
 * @package tests
 */
class RendererTest extends \PHPUnit_Framework_TestCase {
    private $fixtureDir;

    public function __construct($name = NULL, array $data = array(), $dataName = '') {
        parent::__construct($name, $data, $dataName);
        $this->fixtureDir = EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "Renderer";
    }

    public function testRenderXml() {
        $this->markTestIncomplete();
    }

    public function testRenderGif() {
        $this->markTestIncomplete();
    }
    public function testRenderJpg() {
        $this->markTestIncomplete();
    }
    public function testRenderPng() {
        $this->markTestIncomplete();
    }

}
