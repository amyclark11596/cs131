package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter implements Runnable{
	public PwdFilter() {
		super();
	}
	
	public void process() {
		output.add(processLine(""));
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
	/**
	 *Run is the method from the class Runnable, which allows this to be a filter
	 */
	public void run(){
		process();
	}
}
