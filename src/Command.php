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

require_once __DIR__ . DIRECTORY_SEPARATOR . "Scanner.php";
require_once __DIR__ . DIRECTORY_SEPARATOR . "Parser.php";
require_once __DIR__ . DIRECTORY_SEPARATOR . "Renderer.php";

/**
 * Abstracts the CLI command.
 */
class Command {
    const EBNF_OK           = 0;
    const EBNF_READ_ERROR   = 1;
    const EBNF_NO_SYNTAX    = 2;
    const EBNF_SYNTAX_ERROR = 3;
    const EBNF_FATAL_ERROR  = 4;

    /**
     * @var array
     */
    private $opts;

    private static $exampleInput =
'"EBNF defined in itself" {
  syntax     = [ title ] "{" { rule } "}" [ comment ].
  rule       = identifier "=" expression ( "." | ";" ) .
  expression = term { "|" term } .
  term       = factor { factor } .
  factor     = identifier
             | literal
             | "[" expression "]"
             | "(" expression ")"
             | "{" expression "}" .
  identifier = character { character } .
  title      = literal .
  comment    = literal .
  literal    = "\'" character { character } "\'"
             | \'"\' character { character } \'"\' .
}';

    public static function main(array $opts) {
        $self = new Command($opts);
        return $self->execute();
    }

    private function __construct(array $opts) {
        $this->opts = $opts;
    }

    private static function usage() {
        return "Usage: ebnf -s <file> [-o <file>] [-f png|jpg|gif|xml] [-h]" . PHP_EOL . PHP_EOL .
               "  -s <file>  File with EBNF grammar." . PHP_EOL .
               "  -o <file>  Output file. If omitted the input file name is used and the file extensions" . PHP_EOL .
               "             will be substituded with the format." . PHP_EOL .
               "  -f format  Format for generated image (png, jpg, gif, xml). Default is png." . PHP_EOL .
               "  -d         Enables debug output." . PHP_EOL .
               "  -h         This help." . PHP_EOL . PHP_EOL .
               "Example grammar:" . PHP_EOL .
               self::$exampleInput . PHP_EOL . PHP_EOL;
    }

    private function execute() {
        $format  = Renderer::FORMAT_PNG;
        $outfile = "";
        $debug   = false;

        if (isset($this->opts["h"])) {
            echo self::usage();
            return self::EBNF_OK;
        }

        if (isset($this->opts["d"])) {
            $debug = true;
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
            echo self::usage();
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

        try {
            $scanner  = new Scanner($input);
            $parser   = new Parser($scanner);
            $ast      = $parser->parse();
            $renderer = new Renderer($format, $outfile, $ast);
            $renderer->save();
            return self::EBNF_OK;
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
}
