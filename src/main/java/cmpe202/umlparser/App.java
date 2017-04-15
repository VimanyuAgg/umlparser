package cmpe202.umlparser;
/*
 * App.java contains the main method which drives the program
 * This takes input as two arguments as per the specifications in readme
 *
 */
class App{

	public static void main(String[] args) throws Exception{
		if(args.length !=2){
			System.out.println("Error: please enter valid arguments");
			System.exit(0);
		}

		/*
		 * Below condition takes care of making class diagrams
		 */
		else if(args[0].equals("cls")){
			parser myUmlParser = new parser(args[1],args[2]);
			myUmlParser.startParsing();
		}
		/*
		 * Below condition takes care of making sequence diagrams
		 */
		else if (args[0].equals("sqnc"))
		{SeqDiagramGenerator myUmlParser = new SeqDiagramGenerator(args[1],args[2],args[3], args[4]);
			try {
				myUmlParser.startParsing();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Invalid parameter in arguments");
		}
	}




}