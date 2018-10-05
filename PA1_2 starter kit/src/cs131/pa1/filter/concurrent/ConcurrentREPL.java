package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.Scanner;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(command.endsWith("&")){
				//put checks for background r
			} else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist;
				filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				while(filterlist != null) {
					filterlist.process();
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}
	
	//iterates through the list and checks each process for completion. Returns false if one is not finished
	public static boolean isDone(ConcurrentFilter filterlist){
		while (filterlist.isDone()){
			if(filterlist.getNext()!=null){
				filterlist = (ConcurrentFilter) filterlist.getNext();
			}
			return true;
		}
			
		return false;
	}

}
