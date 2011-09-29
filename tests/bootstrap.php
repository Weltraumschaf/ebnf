<?php

error_reporting(E_ALL | E_STRICT);
set_include_path(
    dirname(__DIR__) . DIRECTORY_SEPARATOR . "src" .
    PATH_SEPARATOR . get_include_path()
);