package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JTabbedPane;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		int numjobs = 0;
		//LinkedList<Integer> intjobs = new LinkedList<Integer>();
		//LinkedList<String> jobs = new LinkedList<String>();
		//LinkedList<LinkedList<Thread>> joblist = new LinkedList<LinkedList<Thread>>();
		LinkedList<Jobs> jobList = new LinkedList<Jobs>();
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(command.endsWith("&")){
				//put checks for background r
				numjobs++;
				//jobs.add("\t"+ numjobs+". "+command+"\n");
				//intjobs.add(numjobs);
				command = command.substring(0, command.length()-1);
				//building the filters list from the command
				ConcurrentFilter filterlist;
				filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command.substring(0, (command.length()-1)));
				//adds filter to a list of currently running jobs
				LinkedList<Thread> thisjob = filterListBackground(filterlist);
				//joblist.add(thisjob);
				Jobs newJob = new Jobs(numjobs, thisjob, command);
				jobList.add(newJob);
				
			} else if (command.equals("repl_jobs")){ 
				if (!(jobList.isEmpty())){
					getJobs(jobList);
				}
			} else if (command.startsWith("kill")){
				//calls method to kill a job, which takes in an integer of the job it wants to kill
				String[] killcmd = command.split(" ");
				if (killcmd.length == 1 ){
					System.out.printf(Message.REQUIRES_PARAMETER.with_parameter("kill"));
				} else if (!(killcmd[killcmd.length-1].matches(".*\\d+.*"))){
					System.out.printf(Message.INVALID_PARAMETER.with_parameter(command));
				} else {
					String[] cmds = command.split(" ");
					String toKill = cmds[cmds.length-1];
					int numCheck = Integer.parseInt(toKill);
					if (( numCheck > numjobs | numCheck < 1)){
						System.out.printf(Message.INVALID_PARAMETER.with_parameter(command));
					}
					
					jobList= kill(toKill, jobList);
				}
			}else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist;
				filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				LinkedList<Thread> currentThreads = new LinkedList<Thread>();
				while(filterlist != null) {
					//filterlist.process();
					Thread t = new Thread(filterlist);
					t.start();
					currentThreads.add(t);
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
				try {
					currentThreads.getLast().join();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}
	
	public static boolean closeThreads(LinkedList<Thread> threads){
//		waits for the threads to complete. When they're all finished, method returns
		for(Thread t: threads){
			try {
				t.join();
				
				//System.out.println(t.getName());
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static LinkedList<Thread> filterListBackground(ConcurrentFilter filterlist){
		//the loop is its own method so multiple filterlists can call it at once
		LinkedList<Thread> threads = new LinkedList<Thread>();
		while(filterlist != null) {
			//filterlist.process();
			Thread t = new Thread(filterlist);
			threads.add(t);
			t.start();
			filterlist = (ConcurrentFilter) filterlist.getNext();
		}
		return threads;
	}
	
	public static void getJobs(LinkedList<Jobs> curr){
		Iterator<Jobs> it = curr.iterator();
		while (it.hasNext()){
			System.out.println(it.next().toString());
		}
		//for (Jobs j: curr){
			//System.out.println(j.toString());
		//}
	}
	
	public static LinkedList<Jobs> kill (String toKill, LinkedList<Jobs> alljobs){
		boolean done = false;
		Iterator<Jobs> it = alljobs.iterator();
		while ((!done) && it.hasNext()){
			Jobs curr = it.next();
			if (curr.jobNum == Integer.parseInt(toKill)){
				for (Thread t : curr.pieces){
					t.interrupt();
				}
				alljobs.remove(curr);
				done = true;
			}
		}
		return alljobs;
	}
}
