package cmpe202.umlparser;

class App{
	
	public static void main(String[] args){
		parser myUmlParser = new parser(args[1],args[2]);
		myUmlParser.startParsing();
		
		
	}
	
	
	
}