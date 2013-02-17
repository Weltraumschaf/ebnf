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
 * @see Command
 */
require_once 'Command.php';

/**
 * Systemtest for {@link Command}.
 *
 * @package tests
 */
class CommandTest extends \PHPUnit_Framework_TestCase {

    private $fixtureDir;

    /**
     * @var TestDirHelper
     */
    private static $testDir;

    public function __construct($name = NULL, array $data = array(), $dataName = '') {
        parent::__construct($name, $data, $dataName);
        $this->fixtureDir = EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "Renderer";
    }

    public static function setUpBeforeClass() {
        parent::setUpBeforeClass();
        self::$testDir = new TestDirHelper();
        self::$testDir->create();
    }

    public static function tearDownAfterClass() {
        self::$testDir->remove();
        parent::tearDownAfterClass();
    }

    /**
     * @large
     */
    public function testGenerateXml() {
        $syntaxFile = $this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.ebnf";
        \vfsStream::setup("testdir");
        $this->assertEquals(Command::EBNF_OK, Command::main(array(
            "s" => $syntaxFile,
            "o" => \vfsStream::url("testdir/out.xml"),
            "f" => "xml"
        )));
        $this->assertEquals(
            file_get_contents($this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.xml"),
            file_get_contents(\vfsStream::url("testdir/out.xml"))
        );

    }

    /**
     * @large
     */
    public function testGeneratePng() {
        $syntaxFile = $this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.ebnf";
        $outFile    = self::$testDir->get() . DIRECTORY_SEPARATOR . "out.png";
        $this->assertEquals(Command::EBNF_OK, Command::main(array(
            "s" => $syntaxFile,
            "o" => $outFile,
            "f" => "png"
        )));
        $this->assertTrue(
            file_get_contents($this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.png") ===
            file_get_contents($outFile)
        );
    }

    /**
     * @large
     */
    public function testGenerateJpg() {
        $syntaxFile = $this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.ebnf";
        $outFile    = self::$testDir->get() . DIRECTORY_SEPARATOR . "out.jpg";
        $this->assertEquals(Command::EBNF_OK, Command::main(array(
            "s" => $syntaxFile,
            "o" => $outFile,
            "f" => "jpg"
        )));

        $fixture = $this->fixtureDir . "/test_grammar";

        if (EBNF_TESTS_HOST_OS === EBNF_TESTS_HOST_OS_LINUX) {
            $fixture .= "_linux";
        }

        $fixture .= ".jpg";

        $this->assertTrue(
            file_get_contents($fixture) ===
            file_get_contents($outFile)
        );
    }

    /**
     * @large
     */
    public function testGenerateGif() {
        $syntaxFile = $this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.ebnf";
        $outFile    = self::$testDir->get() . DIRECTORY_SEPARATOR . "out.gif";
        $this->assertEquals(Command::EBNF_OK, Command::main(array(
            "s" => $syntaxFile,
            "o" => $outFile,
            "f" => "gif"
        )));
        $this->assertTrue(
            file_get_contents($this->fixtureDir . DIRECTORY_SEPARATOR . "test_grammar.gif") ===
            file_get_contents($outFile)
        );
    }

}
