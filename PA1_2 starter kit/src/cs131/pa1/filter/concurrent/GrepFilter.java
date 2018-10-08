package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

public class GrepFilter extends ConcurrentFilter implements Runnable {
	private String toFind;
	
	public GrepFilter(String line) {
		String[] param = line.split(" ");
		if(param.length > 1) {
			toFind = param[1];
		} else {
			System.out.printf(Message.REQUIRES_PARAMETER.toString(), line);
		}
	}
	
	
	public void process() throws InterruptedException{
		while (!isDone()){
			String inputLine = input.take();
			if(inputLine.equals("poison_pill")){
				output.put(inputLine);
				done = true;
			}else{
				String processedLine = processLine(inputLine);
				if (processedLine != null){
					output.put(processedLine);
				}
			}
		}
	}
		
	public String processLine(String line) {
		if(line.contains(toFind)) {
			return line;
		} else {
			return null;
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
