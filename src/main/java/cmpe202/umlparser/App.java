package cmpe202.umlparser;

class App{

	public static void main(String[] args){
		if(args.length !=2){
			System.out.println("Error: please enter valid arguments");
			System.exit(0);
		}
		parser myUmlParser = new parser(args[1],args[2]);
		myUmlParser.startParsing();


	}



}