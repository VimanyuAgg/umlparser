package umlparser.cmpe202;

public class App {

    public static void main(String[] args) throws Exception {
    	
    	
        if (args[0].equals("umlparser")) {
            ParserSeqDiagram seqParser = new ParserSeqDiagram(args[1], "Main", "main", args[2]);
            seqParser.startParsing();
        } else {
            System.out.println("Invalid keyword " + args[0]+ " in the input string");
        }

    }

}
