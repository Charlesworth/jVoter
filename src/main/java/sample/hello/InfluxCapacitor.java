package sample.hello;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import akka.actor.UntypedActor;

public class InfluxCapacitor extends UntypedActor{

	
	void dbtest(){
		System.out.println("DB test");
		
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");

		//influxDB.createDatabase("testerDB");

		Serie serie1 = new Serie.Builder("farty6")
		            .columns("testcolumn1", "testcolumn2")
		            .values(System.currentTimeMillis(), 1)
		            .values(System.currentTimeMillis(), 2)
		            .build();
		try {
		influxDB.write("testerDB", TimeUnit.MILLISECONDS, serie1);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg == "dbtest"){
			dbtest();
		}
		
	}
	
	
	
	
}
