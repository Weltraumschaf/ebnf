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
 * @see TestDirHelper
 */
require_once 'TestDirHelper.php';

/**
 * Testcase for class {@link TestDirHelper}.
 *
 * @package tests
 */
class TestDirHelperTest extends \PHPUnit_Framework_TestCase {

    public function testGenerateUniqueName() {
        $dir1 = new TestDirHelper();
        $dir2 = new TestDirHelper();
        $dir3 = new TestDirHelper();

        $this->assertNotEquals($dir1->get(), $dir2->get());
        $this->assertNotEquals($dir1->get(), $dir3->get());
        $this->assertNotEquals($dir2->get(), $dir3->get());
    }

    public function testCreateAndRemove() {
        $dir = new TestDirHelper();
        $this->assertFalse(file_exists($dir->get()));
        $dir->create();
        $this->assertTrue(file_exists($dir->get()));
        $this->assertTrue(is_dir($dir->get()));
        $dir->remove();
        $this->assertFalse(file_exists($dir->get()));
    }
}
