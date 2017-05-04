package umlparser.cmpe202;

public class App {

    public static void main(String[] args) throws Exception {
    	
    	
        if (args[0].equals("umlparser")) {
            Umlparser parser = new Umlparser(args[1], args[2]);
            parser.startParsing();
        
           if (args.length > 3){
        	   if(args[3] != null){
               	ParserSeqDiagram seqParser = new ParserSeqDiagram(args[3], "Main", "main", args[4]);
               	seqParser.startParsing();
           
               }
           }
        }
        else {
            System.out.println("bad keyword " + args[0]+ " in the input string");
        }

    }

}