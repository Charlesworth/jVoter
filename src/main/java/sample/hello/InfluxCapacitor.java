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
			setVote((vote) msg);
		} if (msg instanceof get){
			get request = (get) msg;
			if (request.type == "status"){
				getVote(request.id);
			} else if (request.type == "info"){
				getInfo(request.id);
			} else if (request.type == "both"){
				getVote(request.id);
				getInfo(request.id);
			}
		} else {
		unhandled(msg);
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
		int totalVotes = result.size();
		int v1Votes = resultv1.size();
		int v2Votes = resultv2.size();
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
