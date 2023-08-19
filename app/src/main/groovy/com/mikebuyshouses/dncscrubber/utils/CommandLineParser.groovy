package com.mikebuyshouses.dncscrubber.utils

import groovy.cli.picocli.CliBuilder

// SOURCE: Phind AI
class CommandLineParser {
    String inputFilename
    String outputFilename

    void parseArgs(String[] args) {
        CliBuilder cli = new CliBuilder()
        cli.with {
            usage: 'groovy App.groovy -i <input_filename> -o <output_filename>'
            i(longOpt: 'input', args: 1, required: true, 'Input filename')
            o(longOpt: 'output', args: 1, required: true, 'Output filename')
            h(longOpt: 'help', 'Prints usage information')
        }

        def options = cli.parse(args)
        if (!options || options.h) {
            println(cli.usage)
            System.exit(1)
        }

        inputFilename = options.i
        outputFilename = options.o
    }
}