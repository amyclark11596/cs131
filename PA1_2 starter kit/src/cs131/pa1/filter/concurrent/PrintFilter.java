package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter implements Runnable {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		while(!isDone()) {
			String newLine = input.poll();
			if(newLine.equals("poison_pill")){
				break;
			}
			processLine(newLine);
		}
	}
	
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}
	
	/**
	 *Run is the method from the class Runnable, which allows this to be a filter
	 */
	public void run(){
		process();
	}
}
