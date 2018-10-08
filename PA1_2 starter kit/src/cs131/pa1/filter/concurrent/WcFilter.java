package cs131.pa1.filter.concurrent;

public class WcFilter extends ConcurrentFilter implements Runnable {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() throws InterruptedException {
		while (!isDone()){
			String line = input.take();
			if(line.equals("poison_pill")){
				done = true;
			}
			String processedLine = processLine(line);
			if (processedLine != null){
				output.put(processedLine);
			}
		}
		output.put(processLine(null));
		output.put("poison_pill");
	}
	
	public String processLine(String line) {
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		}
		
		if(isDone()) {
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return ++linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
		}
	}
	/**
	 * Run is the method to make the class runnable, and thus able to be a filter
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
