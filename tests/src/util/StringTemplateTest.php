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

namespace de\weltraumschaf\ebnf\util;

require_once 'util/StringTemplate.php';

/**
 * Tests for {@link StringTemplate}.
 *
 * @package tests
 */
class StringTemplateTest extends \PHPUnit_Framework_TestCase {

    public function testRender() {
        $t = new StringTemplate("Template @foo@ string @bar@!");
        $this->assertEquals("Template @foo@ string @bar@!", $t->render());
        $t->assign('foo', 'a vlaue');
        $this->assertEquals("Template a vlaue string @bar@!", $t->render());
        $t->assign('bar', 42);
        $this->assertEquals("Template a vlaue string 42!", $t->render());

        $t = new StringTemplate("Template @foo@ string @foo@!");
        $t->assign('foo', 'a vlaue');
        $this->assertEquals("Template a vlaue string a vlaue!", $t->render());
    }

}