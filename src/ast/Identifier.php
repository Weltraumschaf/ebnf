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
 * @author Sven Strittmatter <ich@weltraumschaf.de>
 */

namespace de\weltraumschaf\ebnf\ast;

require_once __DIR__ . DIRECTORY_SEPARATOR . 'Node.php';
require_once dirname(__DIR__) . DIRECTORY_SEPARATOR . "visitor" . DIRECTORY_SEPARATOR .  "Visitable.php";

use de\weltraumschaf\ebnf\visitor\Visitable as Visitable;
use de\weltraumschaf\ebnf\visitor\Visitor as Visitor;

/**
 * Has no subnodes.
 */
class Identifier implements Node, Visitable {
    
    public $value = "";
    
    public function getNodeName() {
        return "identifier";
    }

    public function accept(Visitor $visitor) {
        $visitor->visit($this);
    }

}