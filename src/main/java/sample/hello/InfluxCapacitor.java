package sample.hello;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import akka.actor.UntypedActor;

public class InfluxCapacitor extends UntypedActor{

	
	void dbtest(){
		InfluxDB influxDB = InfluxDBFactory.connect("http://178.62.74.225:8083/", "root", "root");

		//influxDB.createDatabase("poo");

		Serie serie1 = new Serie.Builder("serie2Name")
		            .columns("column1", "column2")
		            .values(System.currentTimeMillis(), 1)
		            .values(System.currentTimeMillis(), 2)
		            .build();
		Serie serie2 = new Serie.Builder("serie2Name")
		            .columns("column1", "column2")
		            .values(System.currentTimeMillis(), 1)
		            .values(System.currentTimeMillis(), 2)
		            .build();
		influxDB.write("poo", TimeUnit.MILLISECONDS, serie1, serie2);
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg == "dbtest"){
			dbtest();
		}
		
	}
	
	
	
	
}
