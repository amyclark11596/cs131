package cs131.pa1.filter.concurrent;

import java.util.LinkedList;

public class Jobs {
	
	int jobNum;
	LinkedList<Thread> pieces;
	String cmd;
	
	public Jobs(int jobNum, LinkedList<Thread> pieces, String cmd){
		this.jobNum = jobNum;
		this.pieces = pieces;
		this.cmd = cmd;
	}
	
	public String toString(){
		return jobNum+". "+cmd;
	}

}
