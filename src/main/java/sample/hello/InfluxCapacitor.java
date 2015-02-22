package sample.hello;

import java.util.List;
import java.util.concurrent.TimeUnit;

import messages.get;
import messages.makeBallot;
import messages.vote;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import akka.actor.UntypedActor;

public class InfluxCapacitor extends UntypedActor{
	InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");
	String ConstID;

	@Override
	public void onReceive(Object msg) throws Exception {

		if (msg instanceof makeBallot){	
			ConstID = makeNewBallot((makeBallot) msg);
			//return to http the id
		} if (msg instanceof vote){
			vote request = (vote) msg;
			if (checkID(request.id)){
				setVote(request);
			} else {
				System.out.println("Ballot has been closed, vote not counted");
			}
		} if (msg instanceof get){
			get request = (get) msg;
			if (request.type == "status"){
				getVote(request.id);
			} else if (request.type == "info"){
				getInfo(request.id);
			} else if (request.type == "both"){
				checkID(request.id);
				getInfo(request.id);
				getVote(request.id);
			}
		} else {
		unhandled(msg);
		}
	}
	
	boolean checkID(String id){
		Long startTime = Long.parseLong(id.substring(0, 13), 10);
		Long endTime = null;
		switch (Integer.parseInt(id.substring(13))) {
			case 1:	endTime = startTime + 300000L; //5 mins
				break;
			case 2:	endTime = startTime + 600000L; //10 mins
				break;
			case 3:	endTime = startTime + 1800000L; //30 mins
				break;
			case 4:	endTime = startTime + 3600000L; //1 hour
				break;
			case 5: endTime = startTime + 21600000L; //6 hours
				break;
			case 6: endTime = startTime + 43200000L; //12 hours
				break;
			case 7: endTime = startTime + 85400000L; //1 day
				break;
		}
		if (endTime > System.currentTimeMillis()){
			return true;
		} else {
			return false;
		}
	}
	
	private String makeNewBallot(makeBallot info){
		
		String id = (Long.toString(System.currentTimeMillis())) + info.timeOut;
		
		while (influxDB.describeDatabases().contains(id)){
			id = (Long.toString(System.currentTimeMillis())) + info.timeOut;
		}
		
		influxDB.createDatabase(id);
		
		Serie serie = new Serie.Builder("info")
			.columns("name", "description", "vote1", "vote2")
			.values(info.name, info.description, info.voteFeilds[0], info.voteFeilds[1])
			.build();
		
		influxDB.write(id, TimeUnit.MILLISECONDS, serie);
		return id;
	}

	void deleteDB(String id){
		influxDB.deleteDatabase(id);
	}

	void setVote(vote v){
		Serie serie = new Serie.Builder("vote")
        	.columns("value", "comment")
        	.values(v.value, v.comment)
        	.build();

		influxDB.write(v.id, TimeUnit.MILLISECONDS, serie);
	}

	void getVote(String id){
		List<Serie> info = this.influxDB.query(id, "select vote1, vote2 from info", TimeUnit.MILLISECONDS);
 		String v1 = (String) info.get(0).getRows().get(0).get("vote1");
 		String v2 = (String) info.get(0).getRows().get(0).get("vote2");
		List<Serie> result = this.influxDB.query(id, "select count(value) from vote", TimeUnit.MILLISECONDS);
		List<Serie> resultv1 = this.influxDB.query(id, "select count(value) from vote where value = '" + v1 + "'", TimeUnit.MILLISECONDS);
		List<Serie> resultv2 = this.influxDB.query(id, "select count(value) from vote where value = '" + v2 + "'", TimeUnit.MILLISECONDS);
		System.out.println( "Total votes: " + result.get(0).getRows().get(0).get("count"));
		if (resultv1.isEmpty()){
			System.out.println("no votes for " + v1);
		} else {
			System.out.println( v1 + " votes " + resultv1.get(0).getRows().get(0).get("count"));
		}
		if (resultv2.isEmpty()){
			System.out.println("no votes for " + v2);
		} else {
			System.out.println( v2 + " votes " + resultv2.get(0).getRows().get(0).get("count"));
		}
	}

	void getInfo(String id){
		System.out.println("Hey You:");
 		List<Serie> result = this.influxDB.query(id, "select name, description, vote1, vote2 from info", TimeUnit.MILLISECONDS);
 		System.out.println("Name: " + (String) result.get(0).getRows().get(0).get("name"));
 		System.out.println("Description: " + (String) result.get(0).getRows().get(0).get("description"));
 		System.out.println("vote variable 1: " + (String) result.get(0).getRows().get(0).get("vote1"));
 		System.out.println("vote variable 2: " + (String) result.get(0).getRows().get(0).get("vote2"));
	}
}