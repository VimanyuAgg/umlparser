package umlparser.umlparser;

public class Umlparser {

    public static void main(String[] args) throws Exception {
        if (args[0].equals("class")) {
            Parser parser = new Parser(args[1], args[2]);
            parser.startParsing();
        } else if (args[0].equals(("seq"))) {
            ParserSeqDiagram seqParser = new ParserSeqDiagram(args[1], args[2], args[3], args[4]);
            seqParser.startParsing();
        } else {
            System.out.println("Invalid keyword " + args[0]+ " in the input string");
        }

    }

}