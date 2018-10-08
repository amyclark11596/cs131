package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.LinkedList;
import java.util.Scanner;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		int numjobs = 0;
		LinkedList<Integer> intjobs = new LinkedList<Integer>();
		LinkedList<String> jobs = new LinkedList<String>();
		LinkedList<LinkedList<Thread>> joblist = new LinkedList<LinkedList<Thread>>();
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(command.endsWith("&")){
				//put checks for background r
				numjobs++;
				jobs.add("\t"+ numjobs+". "+command+"\n");
				intjobs.add(numjobs);
				command = command.substring(0, command.length()-1);
				//building the filters list from the command
				ConcurrentFilter filterlist;
				filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				//adds filter to a list of currently running jobs
				LinkedList<Thread> thisjob = filterListBackground(filterlist);
				joblist.add(thisjob);
				
			} else if (command.equals("repl_jobs")){ 
				if (!(jobs.isEmpty())){
					jobs = getJobs(jobs);
				}
			} else if (command.startsWith("kill")){
				//calls method to kill a job, which takes in an integer of the job it wants to kill
				String[] killcmd = command.split(" ");
				if (killcmd.length == 1 ){
					System.out.println(Message.REQUIRES_PARAMETER);
				} else if (!(killcmd[killcmd.length-1].matches(".*\\d+.*"))){
					System.out.println(Message.INVALID_PARAMETER);
				} else {
					int toKill = Integer.parseInt(killcmd[killcmd.length-1]);
					int killing = intjobs.indexOf(toKill);
					if ((toKill > joblist.size() || toKill < 1)){
						System.out.println(Message.INVALID_PARAMETER);
					}
					
					kill(killing, joblist);
				}
			}else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist;
				filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				ConcurrentFilter head = filterlist;
				LinkedList<Thread> currentThreads = new LinkedList<Thread>();
				while(filterlist != null) {
					//filterlist.process();
					Thread t = new Thread(filterlist);
					t.start();
					currentThreads.add(t);
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
				isDone(currentThreads);
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}
	
	public static boolean isDone(LinkedList<Thread> threads){

		//waits for the threads to complete. When they're all finished, method returns
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
	
	public static LinkedList<String> getJobs(LinkedList<String> jobs){
		LinkedList<String> accountedFor = new LinkedList<String>();
		while (!(jobs.isEmpty())){
			String job = jobs.poll();
			System.out.print(job);
			accountedFor.add(job);
		}
		return accountedFor;
	}
	
	public static LinkedList<LinkedList<Thread>> kill (int job, LinkedList<LinkedList<Thread>> joblist){
		LinkedList<Thread> toKill = joblist.get(job);
		joblist.remove(job);
		while (!(toKill.isEmpty())){
			Thread link = toKill.poll();
			link.interrupt();
		}
		return joblist;
	}
}
