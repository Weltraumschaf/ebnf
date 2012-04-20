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

namespace de\weltraumschaf\ebnf\ast;

/**
 * @see Type
 */
require_once 'ast/Type.php';

/**
 * Tests for {@link Type}.
 * 
 * @package tests
 */
class TypeTest  extends \PHPUnit_Framework_TestCase {
    public function testIsAndToString() {
        foreach (array(
            Type:: CHOICE,
            Type::IDENTIFIER,
            Type::LOOP,    
            Type::OPTION,  
            Type::RANGE,   
            Type::RULE,    
            Type::SEQUENCE,
            Type::SYNTAX,
            Type::TERMINAL
        ) as $typeName) {
            $t = new Type($typeName);
            $this->assertTrue($t->is($typeName));
            $this->assertEquals($typeName, $t->__toString());
        }
    }
}
