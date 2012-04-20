#!/usr/bin/env php
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

namespace de\weltraumschaf\ebnf;

if (strpos('@php_bin@', '@php_bin') === 0) {
    // In uninstalled ev enviroment include from repositories src/ first.
    set_include_path(implode(PATH_SEPARATOR, array(
        dirname(__DIR__) . DIRECTORY_SEPARATOR . "src",
        get_include_path()
    )));
}

require_once "EBNF/Command.php";

exit(Command::main(getopt(Command::OPTS), basename(__FILE__)));