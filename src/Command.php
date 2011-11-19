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
 * @author  Vincent Tscherter <tscherter@karmin.ch>
 * @author  Sven Strittmatter <ich@weltraumschaf.de>
 * @package ebnf
 */

namespace de\weltraumschaf\ebnf;

/**
 * @see Scanner
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Scanner.php';
/**
 * @see Parser
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Parser.php';
/**
 * @see Renderer
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'Renderer.php';
/**
 * @see ReferenceGrammar
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'ReferenceGrammar.php';

/**
 * Abstracts the CLI command.
 *
 * @package ebnf
 */
class Command {
    const EBNF_OK           = 0;
    const EBNF_READ_ERROR   = 1;
    const EBNF_NO_SYNTAX    = 2;
    const EBNF_SYNTAX_ERROR = 3;
    const EBNF_FATAL_ERROR  = 4;

    /**
     * Command line arguments.
     *
     * @var array
     */
    private $opts;

    /**
     * Base name of the invoking script.
     *
     * @var string
     */
    private $baseName;

    /**
     * Invokes the command.
     *
     * Needs opts like: getopt("s:f:o:hd").
     * Returns error codes defined as class constants.
     *
     * @param array  $opts     Command line arguments.
     * @param string $baseName Teh script which invokes the command, default is 'ebnf'.
     *
     * @return int
     */
    public static function main(array $opts, $baseName = "ebnf") {
        $debug = false;

        if (isset($opts["d"])) {
            $debug = true;
        }

        try {
            $self = new Command($opts, $baseName);
            return $self->execute();
        } catch (SyntaxtException $e) {
            echo "{$e}\n";

            if ($debug) {
                echo "{$e->getTraceAsString()}\n";
            }

            return self::EBNF_SYNTAX_ERROR;
        } catch (Exception $e) {
            echo "Error: {$e->getMessage()}\n";

            if ($debug) {
                echo "{$e->getTraceAsString()}\n";
            }

            return self::EBNF_FATAL_ERROR;
        }
    }

    /**
     * Initializes the options.
     *
     * @param array  $opts     Command line arguments.
     * @param string $baseName Teh script which invokes the command.
     *
     * @return int
     */
    private function __construct(array $opts, $baseName) {
        $this->opts     = $opts;
        $this->baseName = (string) $baseName;
    }

    /**
     * Returns usage string.
     *
     * @param string $baseName Teh script which invokes the command.
     *
     * @return string
     */
    private static function usage($baseName) {
        $example = new ReferenceGrammar();
        return "Usage: {$baseName} -s <file> [-o <file>] [-f png|jpg|gif|xml] [-h]" . PHP_EOL . PHP_EOL .

               "  -s <file>  File with EBNF grammar." . PHP_EOL .
               "  -o <file>  Output file. If omitted the input file name is used and the file extensions" . PHP_EOL .
               "             will be substituded with the format." . PHP_EOL .
               "  -f format  Format for generated image (png, jpg, gif, xml). Default is png." . PHP_EOL .
               "  -d         Enables debug output." . PHP_EOL .
               "  -h         This help." . PHP_EOL . PHP_EOL .

               "Example grammar:" . PHP_EOL .
               $example->__toString() . PHP_EOL .
               '}' . PHP_EOL . PHP_EOL;
    }

    /**
     * Runs the cummand and returns error code.
     *
     * @return int
     */
    private function execute() {
        $format  = Renderer::FORMAT_PNG;
        $outfile = "";

        if (isset($this->opts["h"])) {
            echo self::usage($this->baseName);
            return self::EBNF_OK;
        }

        if (isset($this->opts["s"]) && !empty($this->opts["s"])) {
            $infile = $this->opts["s"];

            if (!is_readable($infile)) {
                echo "Can't read EBNF file '{$infile}'!\n";
                return self::EBNF_READ_ERROR;
            }

            $input = file_get_contents($infile);

            if (false === $input || empty($input)) {
                echo "Can't read content from EBNF file '{$infile}'!\n";
                return self::EBNF_READ_ERROR;
            }
        } else {
            echo "Error: Please specify a syntax file!\n\n";
            echo self::usage($this->baseName);
            return self::EBNF_NO_SYNTAX;
        }

        if (isset($this->opts["f"]) && !empty($this->opts["f"])) {
            $format = $this->opts["f"];
        }

        if (isset($this->opts["o"]) && !empty($this->opts["o"])) {
            $outfile = $this->opts["o"];
        } else {
            $outfile  = basename($infile);
            $outfile  = substr($outfile, 0, strrpos($outfile, ".") + 1);
            $outfile .= $format;
        }

        $scanner  = new Scanner($input);
        $parser   = new Parser($scanner);
        $ast      = $parser->parse();
        $renderer = new Renderer($format, $outfile, $ast);
        $renderer->save();
        return self::EBNF_OK;
    }
}
