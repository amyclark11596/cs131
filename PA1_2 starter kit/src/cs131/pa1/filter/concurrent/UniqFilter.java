package cs131.pa1.filter.concurrent;

import java.util.HashSet;

public class UniqFilter extends ConcurrentFilter implements Runnable{
	private HashSet<String> existingStringSet;
	//This set will record what strings are existing
	
	public UniqFilter () throws Exception {
		existingStringSet = new HashSet<String> ();
	}

	
	public void process() throws InterruptedException{
		while (!isDone()){
			String line = input.take();
			if(line.equals("poison_pill")){
				done = true;
				break;
			}
			String processedLine = processLine(line);
			if (processedLine != null){
				output.put(processedLine);
			}
		}
		output.put("poison_pill");
	}
	
	public String processLine(String line) {
		if(existingStringSet.contains(line)) {
			return null;
		}else {
			existingStringSet.add(line);
			return line;
		}
	}
	
	/**
	 *Run is the method from the class Runnable, which allows this to be a filter
	 */
	public void run(){
		try {
			process();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
