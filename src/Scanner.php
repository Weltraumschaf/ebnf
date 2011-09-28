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

namespace Weltraumschaf\Ebnf;

/**
 * @see {Token}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Token.php';
/**
 * @see {SyntaxtException}
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'SyntaxtException.php';

/**
 * Scanns an input string for EBNF syntax tokens.
 *
 * This class provides only one public method which returns
 * the scanned tokens as an array of strings.
 *
 * On lexiacl syntax errors a SyntaxException will be thrown.
 *
 * @todo Return an array of Token objects with according position objects.
 * @todo Throw SyntaxException with according position of syntax error.
 */
class Scanner {

    /**
     * Definiton of PREGs for tokens.
     *
     * @var array
     */
    private $lexemes = array(
        array('type' => Token::OPERATOR,   'expr' => '[={}()|.;[\]]'),
        array('type' => Token::LITERAL,    'expr' => "\"[^\"]*\""),
        array('type' => Token::LITERAL,    'expr' => "'[^']*'"),
        array('type' => Token::IDENTIFIER, 'expr' => "[a-zA-Z0-9_-]+"),
        array('type' => Token::WHITESPACE, 'expr' => "\\s+")
    );

    /**
     * Grammar string.
     *
     * @var string
     */
    private $input;

    /**
     * Initializes the scanner with a grammar string.
     *
     * @param string $input
     */
    public function __construct($input) {
        $this->input = (string)$input;
    }

    /**
     * Returns an array of tokens.
     *
     * @throws SyntaxtException
     * @return array
     */
    public function scan() {
        $i = 0;
        $n = strlen($this->input);
        $m = count($this->lexemes);
        $tokens = array();

        while ($i < $n) {
            $j = 0;

            while ($j < $m && preg_match("/^{$this->lexemes[$j]['expr']}/", substr($this->input, $i), $matches) === 0) {
                $j++;
            }

            if ($j < $m) {
                if ($this->lexemes[$j]['type'] !== Token::WHITESPACE) {
                    $tokens[] = array(
                        'type'  => $this->lexemes[$j]['type'],
                        'value' => $matches[0],
                        'pos'   => $i
                    );
                }

                $i += strlen($matches[0]);
            } else {
                throw new SyntaxtException("Invalid token at position {$i}: " . substr($this->input, $i, 10) . "...", null);
            }
        }

        return $tokens;
    }
}
