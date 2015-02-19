package sample.hello;

import java.util.concurrent.TimeUnit;

import messages.comment;
import messages.makeBallot;
import messages.vote;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import akka.actor.UntypedActor;

public class InfluxCapacitor extends UntypedActor{
	InfluxDB influxDB = InfluxDBFactory.connect("http://178.62.74.225:8086", "root", "root");
	
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof makeBallot){	
			makeNewBallot((makeBallot) msg);
			//return to http the id
		} if (msg instanceof vote){
			setVote((vote) msg);
		} if (msg instanceof comment){
			setComment((comment) msg);
		} if (msg instanceof String){//getInfo
			getStatus((String) msg);
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
        	.columns("value")
        	.values(v.value)
        	.build();
		
		influxDB.write(v.id, TimeUnit.MILLISECONDS, serie);
	}
	
	void setComment(comment c){
		Serie serie = new Serie.Builder("comment")
    		.columns("value")
    		.values(c.value)
    		.build();
	
		influxDB.write(c.id, TimeUnit.MILLISECONDS, serie);		
	}
	
	void getStatus(String id){
		System.out.println("Method not finished yet");
		//get overall votes
		//get comment list
	}
	
	void getInfo(String id){
		System.out.println("Method not finished yet");
		//get name
		//get description
	}
}
