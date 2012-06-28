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
 * @package ebnf
 */

namespace de\weltraumschaf\ebnf;

/**
 * Helper class for generating random temp dir names and deletes them.
 * Useful for unittests which can not use vfsStream files.
 *
 * This class generates on construction time a dir name prefixed with basepath
 * provided to the constructor, the string "ebnf_test_files_" and a random
 * number. This object is in that way imutable that the directory name is
 * not mutable. Each instance reprsents a iwn unique direcoty.
 *
 * @package ebnf
 */
class TestDirHelper {

    /**
     * Default base dir is unix style tmp dir.
     */
    const DEFAULT_BASE_DIR = "/tmp";

    /**
     * Unique and imutable dir name.
     *
     * @var string
     */
    private $dirName;

    /**
     * Initializes the object with the unique dir name prefixed by $baseDir.
     *
     * @param string $baseDir Direcotry prefix.
     */
    public function __construct($baseDir = self::DEFAULT_BASE_DIR) {
        $this->dirName = $baseDir . DIRECTORY_SEPARATOR . "ebnf_test_files_" . mt_rand();
    }

    /**
     * Creates the directory physcally.
     *
     * @return void
     */
    public function create() {
        system("mkdir -p {$this->dirName}");
    }

    /**
     * removes the directory physcally.
     *
     * @return void
     */
    public function remove() {
        system("rm -rf {$this->dirName}");
    }

    /**
     * Retunrs the unique directory name string.
     *
     * @return string
     */
    public function get() {
        return $this->dirName;
    }
}
