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
 * @package util
 */

namespace de\weltraumschaf\ebnf\util;

/**
 * Simple string template engine.
 *
 * You can use a template string to render it with assigned variables.
 *
 * Example:
 * <code>
 * <?php
 * $t = new StringTemplate('A @var1@ template string');
 * $t->assign('var1', 'simple');
 * $t->render() // will return 'A simple template string'
 * </code>
 *
 * @package util
 */
class StringTemplate {
    /**
     * Character which delimits the variable name, e.g. @var1@.
     */
    const DELIM = '@';

    /**
     * The template string to render.
     *
     * @var string
     */
    private $template;
    /**
     * Assigned template variables.
     *
     * @var array
     */
    private $vars;

    /**
     * Sets the template string ans initializes empty template vars.
     *
     * @param string $template The template string.
     */
    public function __construct($template) {
        $this->template = (string) $template;
        $this->vars     = array();
    }

    /**
     * Assigns a template var.
     *
     * The passed value is substituded with all occurencies of @$name@ in the
     * template string.
     *
     * @param string $name  Name of the variable. Declared as @$name@ in template string.
     * @param string $value The value which will be rendered into the output string.
     *
     * @return void
     */
    public function assign($name, $value) {
        $this->vars[$name] = (string) $value;
    }

    /**
     * Renders the template by substitude all variable names with theassigned vlues.
     *
     * If a vlaue is not assigned the var name deklaration is still presente in the
     * output string.
     *
     * @return string
     */
    public function render() {
        $output = $this->template;

        if (!empty($this->vars)) {
            foreach ($this->vars as $name => $value) {
                $output = str_replace(self::DELIM . $name . self::DELIM, $value, $output);
            }
        }

        return $output;
    }

}