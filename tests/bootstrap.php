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

error_reporting(E_ALL | E_STRICT);
set_include_path(implode(PATH_SEPARATOR, array(
    dirname(__DIR__) . DIRECTORY_SEPARATOR . "src" . DIRECTORY_SEPARATOR . "EBNF",
    PATH_SEPARATOR . get_include_path()
)));

define("EBNF_TESTS_FIXTURS", __DIR__ . DIRECTORY_SEPARATOR . "fixtures");
define("EBNF_TESTS_HOST_OS_UNKNOWN", 0);
define("EBNF_TESTS_HOST_OS_DARWIN", 1);
define("EBNF_TESTS_HOST_OS_LINUX", 2);

$hostOhsFile = dirname(__DIR__) . DIRECTORY_SEPARATOR . "build" . DIRECTORY_SEPARATOR . "host_os";

if (is_readable($hostOhsFile)) {
    $hostOs = file_get_contents($hostOhsFile);

    switch (strtolower($hostOs)) {
        case "darwin":
            define("EBNF_TESTS_HOST_OS", EBNF_TESTS_HOST_OS_DARWIN);
            break;
        case "linux":
            define("EBNF_TESTS_HOST_OS", EBNF_TESTS_HOST_OS_LINUX);
            break;
        default:
            define("EBNF_TESTS_HOST_OS", EBNF_TESTS_HOST_OS_UNKNOWN);
            break;
    }
} else {
    define("EBNF_TESTS_HOST_OS", EBNF_TESTS_HOST_OS_UNKNOWN);
}

unset($hostOhsFile, $hostOs);
require_once 'vfsStream/vfsStream.php';
require_once 'TestDirHelper.php';
