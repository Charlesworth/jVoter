package sample.hello;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import akka.actor.UntypedActor;

public class InfluxCapacitor extends UntypedActor{
	InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");
	
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg == "dbtest"){
			dbtest();
		} if (msg == "setVote"){
			
		} if (msg == "setComment"){
			
		} if (msg == "setDescription"){
			
		} if (msg == "getStatus"){
		
		} else {
		unhandled(msg);
		}
	}
	
	void dbtest(){
		System.out.println(TimeUnit.MILLISECONDS);
		
		

		//influxDB.createDatabase("testerDB");

		Serie serie1 = new Serie.Builder("farty6")
		            .columns("testcolumn1", "testcolumn2")
		            .values(System.currentTimeMillis(), 1)
		            .build();
		try {
		influxDB.write("testerDB", TimeUnit.MILLISECONDS, serie1);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	void setVote(String id, boolean vote){
		Serie serie = new Serie.Builder("vote")
        	.columns("vote")
        	.values(vote)
        	.build();
		
		influxDB.write(id, TimeUnit.MILLISECONDS, serie);
	}
	
	void setComment(String id, String comment){
		Serie serie = new Serie.Builder("comment")
    		.columns("comment")
    		.values(comment)
    		.build();
	
		influxDB.write(id, TimeUnit.MILLISECONDS, serie);		
	}
	
	void setDescription(String id, String description){
		Serie serie = new Serie.Builder("description")
			.columns("description")
			.values(description)
			.build();

		influxDB.write(id, TimeUnit.MILLISECONDS, serie);		
	}

	void getStatus(String id){
		System.out.println("Not set yet");
	}
		
}
