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
 * @package ast
 */

namespace de\weltraumschaf\ebnf\ast;

/**
 * Used to notify collected AST errors.
 *
 * @package ast
 * @version @@version@@
 */
class Notification {
    /**
     * Collected arrays.
     *
     * @var array
     */
    private $errors = array();

    /**
     * Collect an line of error.
     *
     * The first parameter is a sprintf style format string.
     *
     * Example:
     * error($format, $arg1, $arg2 .. $argN)
     *
     * @return void
     */
    public function error() {
        $this->errors[] = call_user_func_array("sprintf", func_get_args());
    }

    /**
     * Returns true if no error was collected.
     *
     * @return bool
     */
    public function isOk() {
        return empty($this->errors);
    }

    /**
     * Returns all errors concatenated as string.
     *
     * @return string
     */
    public function report() {
        if ($this->isOk()) {
            return "";
        }

        return implode(PHP_EOL, $this->errors);
    }
}