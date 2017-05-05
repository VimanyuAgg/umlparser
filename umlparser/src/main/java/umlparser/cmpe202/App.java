package umlparser.cmpe202;

public class App {

    public static void main(String[] args) throws Exception {
    	
    	
        if (args[0].equals("umlparser")) {
            Umlparser parser = new Umlparser(args[1], args[2]);
            parser.startParsing();
        } else {
            System.out.println("Invalid keyword " + args[0]+ " in the input string");
        }

    }

}