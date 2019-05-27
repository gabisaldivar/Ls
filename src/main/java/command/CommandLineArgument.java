package command;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class CommandLineArgument {

    @Option(name = "-l", usage = "switch the output to a long format")
    boolean longFormat;
    @Option(name = "-h", usage = " switch output to human-readable format", depends = "-l")
    boolean humanReadable;
    @Option(name = "-r", usage = "change the output order to the opposite")
    boolean reverse;
    @Option(name = "-o", usage = "specify the name of the file to output the result to", metaVar = "OUTPUT")
    String out;
    @Argument(required = true)
    String pathFile;

    CommandLineArgument(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.print("invalid input: ");
            System.err.println(e.getMessage());
            throw e;
        }
    }
}