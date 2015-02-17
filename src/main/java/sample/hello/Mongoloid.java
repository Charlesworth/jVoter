package sample.hello;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClientURI;

import akka.actor.UntypedActor;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import messages.Ballots;

public class Mongoloid extends UntypedActor {
	DB db;
	MongoClient mongoClient = null;
	DBCollection collection;
	DBCursor cursor;
	
	
	public void preStart(){
		init();
	    setCollection("testData");
	    dropCollection();
	    getAllEntries();
	}
	
	

	@Override
	public void onReceive(Object msg) {

		if (msg instanceof Ballots){
			
			BasicDBObject doc = new BasicDBObject("name", "Test " + UUID.randomUUID())
            .append("yays", ((Ballots) msg).yays)
            .append("nays", ((Ballots) msg).nays);

			
			insertDoc(doc);
			getAllEntries();
		} else if(msg == "stop"){
			System.out.println("PoisonPill: Actor " + getSelf().toString() + " bows and leaves the stage");
			
			close();
			
			getSelf().tell(akka.actor.PoisonPill.getInstance(), null);
		}
		unhandled(msg);
	}


    //----------------------------------------------------Init Operations----------------------------------

	public void init(){
        try {mongoClient = new MongoClient();} catch (UnknownHostException e) {}
        db = mongoClient.getDB("datadb");
    }


    //For remote DBs
    public void init(MongoClientURI mongoURI) {
        try {mongoClient = new MongoClient(mongoURI);} catch (UnknownHostException e) {}
        db = mongoClient.getDB("datadb");
    }

    //----------------------------------------------------Collection Operations----------------------------
    public Set<String> getCollections(){
        // get a list of the collections in this database and print them out
        Set<String> collectionNames = db.getCollectionNames();
        for (final String s : collectionNames) {
            System.out.println(s);
        }
		return collectionNames;
    }

    public void setCollection(String Collection){
        // get a handle to the "test" collection
        collection = db.getCollection(Collection);
    }

    public long getCollectionRange(String Collection){
        // get a handle to the "test" collection
        return db.getCollection(Collection).getCount();
    }

    public void dropCollection(){   
        // drop all the data in a collection
        collection.drop();
    }

    //----------------------------------------------------Doc Operations----------------------------    
    public void insertTestDoc(){
        // make a document and insert it
        BasicDBObject doc = new BasicDBObject("name", "Test " + UUID.randomUUID())
                            .append("type", "test")
                            .append("nested", new BasicDBObject("test1", 1).append("test2", 2));

        collection.insert(doc);
    }

    public void insertDoc(BasicDBObject doc){
        collection.insert(doc);
    }

    public void insertDocs(List<DBObject> documents){
        collection.insert(documents);
    }

    //----------------------------------------------------cursor Operations----------------------------
    public DBObject getEntry(){
        // get it (since it's the only one in there since we dropped the rest earlier on)
        DBObject myDoc = collection.findOne();
        System.out.println(myDoc);
        return myDoc;
    }

	public List<DBObject> getAllEntries(){
		cursor = collection.find();
		ArrayList<DBObject> Entries = new ArrayList<DBObject>();
		while(cursor.hasNext()) {
			DBObject obj = cursor.next();
			System.out.println(obj);
			Entries.add(obj);
		}
		return Entries;
	}	

    //----------------------------------------------------Shutdown----------------------------        
    void close(){
        // release resources
    	cursor.close();
        mongoClient.close();

    }
    
    //----------------------------------------------------Junk----------------------------
    public void sysOutCol(){
        // lets get all the documents in the collection and print them out
        cursor = collection.find();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    public void setCursor(){
        // now use a query to get 1 document out
        cursor = collection.find(new BasicDBObject("i", 71));
    }

    void fuckoff(){
        // now use a range query to get a larger subset
        cursor = collection.find(new BasicDBObject("i", new BasicDBObject("$gt", 50)));

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }

        // range query with multiple constraints
        cursor = collection.find(new BasicDBObject("i", new BasicDBObject("$gt", 20).append("$lte", 30)));

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }

        // create an ascending index on the "i" field
        collection.createIndex(new BasicDBObject("i", 1));

        // list the indexes on the collection
        List<DBObject> list = collection.getIndexInfo();
        for (final DBObject o : list) {
            System.out.println(o);
        }
    }
    
    
}
