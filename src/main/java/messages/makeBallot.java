package messages;

public class makeBallot {
	public String timeOut;
	public String description;
	public String name;
	public String[] voteFeilds = new String[2];
	
	public makeBallot(String t, String d, String n, String v0, String v1){
		timeOut = t;
		description = d;
		name = n;
		voteFeilds[0] = v0;
		voteFeilds[1] = v1;
	}
}