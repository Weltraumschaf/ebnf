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
 * @package tests
 */
class TestDirHelper {
    const DEFAULT_BASE_DIR = "/tmp";

    private $dirName;

    public function __construct($baseDir = self::DEFAULT_BASE_DIR) {
        $this->dirName = $baseDir . DIRECTORY_SEPARATOR . "ebnf_test_files_" . time();
    }

    public function create() {
        system("mkdir -p {$this->dirName}");
    }

    public function remove() {
        system("rm -rf {$this->dirName}");
    }

    public function get() {
        return $this->dirName;
    }
}
