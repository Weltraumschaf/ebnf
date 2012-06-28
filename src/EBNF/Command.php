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
 * @see Xml
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'visitor' . DIRECTORY_SEPARATOR . 'Xml.php';
/**
 * @see TextSyntaxTree
 */
require_once __DIR__ . DIRECTORY_SEPARATOR . 'visitor' . DIRECTORY_SEPARATOR . 'TextSyntaxTree.php';

use \Exception as Exception;
use de\weltraumschaf\ebnf\visitor\TextSyntaxTree;
use de\weltraumschaf\ebnf\visitor\Xml;

/**
 * Abstracts the CLI command.
 *
 * @package ebnf
 * @version @@version@@
 * @group   slow
 */
class Command {
    const EBNF_OK           = 0;
    const EBNF_READ_ERROR   = 1;
    const EBNF_NO_SYNTAX    = 2;
    const EBNF_SYNTAX_ERROR = 3;
    const EBNF_FATAL_ERROR  = 4;

    const OPTS = "s:f:o:hdt";

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

    private $format;
    private $outfile;
    private $infile;

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
        $this->format   = Renderer::FORMAT_PNG;
        $this->outfile  = "";
        $this->infile   = "";
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
               "  -t         Prints textual representation of the syntax tree to stdout." . PHP_EOL .
               "  -d         Enables debug output." . PHP_EOL .
               "  -h         This help." . PHP_EOL . PHP_EOL .

               "Example grammar:" . PHP_EOL .
               $example->__toString() . PHP_EOL .
               '}' . PHP_EOL . PHP_EOL;
    }

    /**
     * Colects all necessary CLI parameters.
     *
     * Returns != {self::EBNF_OK} on error.
     *
     * @return int
     */
    private function findOptions() {
        if (isset($this->opts["h"])) {
            echo self::usage($this->baseName);
            return self::EBNF_OK;
        }

        if (isset($this->opts["s"]) && !empty($this->opts["s"])) {
            $this->infile = $this->opts["s"];
        } else {
            echo "Error: Please specify a syntax file!\n\n";
            echo self::usage($this->baseName);
            return self::EBNF_NO_SYNTAX;
        }

        if (isset($this->opts["f"]) && !empty($this->opts["f"])) {
            $this->format = strtolower(trim($this->opts["f"]));
        }

        if (isset($this->opts["t"])) {
            $this->format = "texttree";
        }

        if (isset($this->opts["o"]) && !empty($this->opts["o"])) {
            $this->outfile = $this->opts["o"];
        } else {
            $this->outfile  = basename($this->infile);
            $this->outfile  = substr($this->outfile, 0, strrpos($this->outfile, ".") + 1);
            $this->outfile .= $this->format;
        }

        return self::EBNF_OK;
    }

    /**
     * Runs the cummand and returns error code.
     *
     * @return int
     */
    public function execute() {
        $returnCode = $this->findOptions();

        if (self::EBNF_OK !== $returnCode) {
            return $returnCode;
        }

        if (!is_readable($this->infile)) {
            echo "Can't read EBNF file '{$this->infile}'!\n";
            return self::EBNF_READ_ERROR;
        }

        $input = file_get_contents($this->infile);

        if (false === $input || empty($input)) {
            echo "Can't read content from EBNF file '{$infile}'!\n";
            return self::EBNF_READ_ERROR;
        }

        $this->generateOutput($input);
        return self::EBNF_OK;
    }

    /**
     * Generates either CLI or file output depending on -f option.
     *
     * @param string $input The input grammar.
     *
     * @return void
     */
    private function generateOutput($input) {
        $scanner = new Scanner($input);
        $parser  = new Parser($scanner);
        $ast     = $parser->parse();

        if ("texttree" === $this->format) {
            $visitor = new TextSyntaxTree();
            $parser->getAst()->accept($visitor);
            echo $visitor->getText();
        } else if ("xml" === $this->format) {
            $visitor = new Xml();
            $parser->getAst()->accept($visitor);
            file_put_contents($this->outfile, $visitor->getXmlString());
        } else {
            $renderer = new Renderer($this->format, $this->outfile, $ast);
            $renderer->save();
        }
    }

}
