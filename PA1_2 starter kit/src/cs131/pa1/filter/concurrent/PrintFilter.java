package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter implements Runnable {
	public PrintFilter() {
		super();
	}
	
	public void process() throws InterruptedException {
		while(!isDone()) {
			if(!input.isEmpty()){
				String newLine = input.take();
				if(newLine.equals("poison_pill")){
					done = true;
					break;
				} else{
					processLine(newLine);
				}
			}
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
		try {
			process();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
