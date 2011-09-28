<?php

namespace Weltraumschaf\Ebnf;

require_once __DIR__ . DIRECTORY_SEPARATOR . 'Token.php';

/**
 * Description of Scanner
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @license http://www.weltraumschaf.de/the-beer-ware-license.txt THE BEER-WARE LICENSE
 */
class Scanner {
    private $lexemes = array(
        array('type' => Token::OPERATOR,   'expr' => '[={}()|.;[\]]'),
        array('type' => Token::LITERAL,    'expr' => "\"[^\"]*\""),
        array('type' => Token::LITERAL,    'expr' => "'[^']*'"),
        array('type' => Token::IDENTIFIER, 'expr' => "[a-zA-Z0-9_-]+"),
        array('type' => Token::WHITESPACE, 'expr' => "\\s+")
    );

    /**
     *
     * @var string
     */
    private $input;

    /**
     * @param string $input
     */
    public function __construct($input) {
        $this->input = (string)$input;
    }

    /**
     *
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
                throw new Exception("Invalid token at position {$i}: " . substr($this->input, $i, 10) . "...");
            }
        }

        return $tokens;
    }
}
