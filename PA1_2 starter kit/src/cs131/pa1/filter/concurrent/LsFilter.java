package cs131.pa1.filter.concurrent;
import java.io.File;

public class LsFilter extends ConcurrentFilter implements Runnable{
	int counter;
	File folder;
	File[] flist;
	
	public LsFilter() {
		super();
		counter = 0;
		folder = new File(ConcurrentREPL.currentWorkingDirectory);
		flist = folder.listFiles();
	}
	
	@Override
	public void process() throws InterruptedException {
		while(counter < flist.length) {
			output.put(processLine(""));
		}
		output.put("poison_pill");
	}
	
	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
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
