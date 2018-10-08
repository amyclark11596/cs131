package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter implements Runnable{
	public PwdFilter() {
		super();
	}
	
	public void process() throws InterruptedException {
		output.put(processLine(""));
		output.put("poison_pill");
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
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
