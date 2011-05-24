package net.sdiz.bitcoin;

public class Work {
	public Work() {
	}

	public Work(String data, String hash1, String target, String midstate) {
		this.data = data;
		this.hash1 = hash1;
		this.target = target;
		this.midstate = midstate;
	}

	public String data;
	public String hash1;
	public String target;
	public String midstate;
}
