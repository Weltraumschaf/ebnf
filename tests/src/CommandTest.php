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

    public function testGenerateXml() {
        $fixturesDir = EBNF_TESTS_FIXTURS . DIRECTORY_SEPARATOR . "Renderer";
        $syntaxFile  = $fixturesDir . DIRECTORY_SEPARATOR . "test_grammar.ebnf";
        \vfsStream::setup("testdir");
        $this->assertEquals(Command::EBNF_OK, Command::main(array(
            "s" => $syntaxFile,
            "o" => \vfsStream::url("testdir/out.xml"),
            "f" => "xml"
        )));
        $this->assertEquals(
            file_get_contents($fixturesDir . DIRECTORY_SEPARATOR . "test_grammar.xml"),
            file_get_contents(\vfsStream::url("testdir/out.xml"))
        );

    }

}
