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
	public void process() {
		while(counter < flist.length) {
			output.add(processLine(""));
		}
	}
	
	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
	}
	
	/**
	 *Run is the method from the class Runnable, which allows this to be a filter
	 */
	public void run(){
		process();
	}
}
