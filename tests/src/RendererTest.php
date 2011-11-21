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
 * @see Parser
 */
require_once 'Parser.php';
/**
 * @see Scanner
 */
require_once 'Scanner.php';

/**
 * Testcase for class {@link Renderer}.
 *
 * @package tests
 */
class RendererTest extends \PHPUnit_Framework_TestCase {
    private $fixtureDir;
    private static $testDir;
    private static $isGdInstalled;

    public function __construct($name = NULL, array $data = array(), $dataName = '') {
        parent::__construct($name, $data, $dataName);
        $this->fixtureDir = EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "Renderer";
    }

    public static function setUpBeforeClass() {
        parent::setUpBeforeClass();
        self::$isGdInstalled = function_exists("imagedestroy");
        self::$testDir = "ebnf_test_files_" . time();

        if (self::$isGdInstalled) {
            system("mkdir -p /tmp/" . self::$testDir);
        }
    }

    public static function tearDownAfterClass() {
        if (self::$isGdInstalled) {
            system("rm -rf /tmp/" . self::$testDir);
        }

        parent::tearDownAfterClass();
    }

    /**
     * @return DOMDocument
     */
    private function createAst() {
        $input    = file_get_contents($this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.ebnf");
        $scanner  = new Scanner($input);
        $parser   = new Parser($scanner);
        return $parser->parse();
    }

    public function testRenderXml() {
        \vfsStream::setup(self::$testDir);
        $fileName = self::$testDir . "/out.xml";
        $outUrl   = \vfsStream::url($fileName);
        $renderer = new Renderer(Renderer::FORMAT_XML, $outUrl, $this->createAst());
        $renderer->save();
        $this->assertEquals(
            file_get_contents("{$this->fixtureDir}/test_grammar.xml"),
            file_get_contents($outUrl)
        );
    }

    public function testRenderGif() {
        if ( ! self::$isGdInstalled) {
            $this->markTestSkipped("No GD lib installed!");
        }

        $fileName = "/tmp/" . self::$testDir . "/out.gif";
        $renderer = new Renderer(Renderer::FORMAT_GIF, $fileName, $this->createAst());
        $renderer->save();
        $this->assertTrue(
            file_get_contents("{$this->fixtureDir}/test_grammar.gif") === file_get_contents($fileName)
        );
    }

    public function testRenderJpg() {
        if ( ! self::$isGdInstalled) {
            $this->markTestSkipped("No GD lib installed!");
        }

        $fileName = "/tmp/" . self::$testDir . "/out.jpg";
        $renderer = new Renderer(Renderer::FORMAT_JPG, $fileName, $this->createAst());
        $renderer->save();
        $this->assertTrue(
            file_get_contents("{$this->fixtureDir}/test_grammar.jpg") === file_get_contents($fileName)
        );
    }

    public function testRenderPng() {
        if ( ! self::$isGdInstalled) {
            $this->markTestSkipped("No GD lib installed!");
        }

        $fileName = "/tmp/" . self::$testDir . "/out.png";
        $renderer = new Renderer(Renderer::FORMAT_PNG, $fileName, $this->createAst());
        $renderer->save();
        $this->assertTrue(
            file_get_contents("{$this->fixtureDir}/test_grammar.png") === file_get_contents($fileName)
        );
    }

    /**
     * @expectedException        \RuntimeException
     * @expectedExceptionMessage Can't write output to '/foo/bar.xml'!
     */
    public function testThrowExceptionOnWriteToNotExistingDir() {
        $renderer = new Renderer(Renderer::FORMAT_XML, "/foo/bar.xml", $this->createAst());
        $renderer->save();
    }

    /**
     * @expectedException        \InvalidArgumentException
     * @expectedExceptionMessage Unsupported format: 'foo'!
     */
    public function testThrowExceptionOnInvalidFormat() {
        $renderer = new Renderer("foo", "/tmp/" . self::$testDir . "/out.foo", $this->createAst());
        $renderer->save();
    }
}
