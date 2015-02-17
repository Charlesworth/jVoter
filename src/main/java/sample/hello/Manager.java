package sample.hello;

import java.util.HashMap;

import messages.InitInfo;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.ActorRef;

public class Manager extends UntypedActor {
	ActorRef requestRouter = null;
	HashMap<Long, ActorRef> activeBallotsList = new HashMap<Long, ActorRef>();
	
  @Override
  public void preStart() {
    // create the greeter actor
//    final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
//    // tell it to perform the greeting
//    greeter.tell(Greeter.Msg.FART, getSelf());
//    
//MongoMofo db = new MongoMofo();
//db.setCollection("testData");
//db.dropCollection();
//    db.getEntry();
//    System.out.println("Hello World!");
    
	  //final ActorRef mongoloid = getContext().actorOf(Props.create(Mongoloid.class));
	  final ActorRef idb = getContext().actorOf(Props.create(InfluxCapacitor.class));
	  idb.tell("dbtest", getSelf());
	  //*****************************************all that needs to be pre started is the overall HTTP server and mongo manager*********************************
	  
	  
	  
	  
	  //InitInfo init = new InitInfo(10000l, mongoloid);
	  //mongoloid.tell(, getSelf());
	  //activeBallotsList;
	  //init the actor type BallotBox with ref ballotBox
	  //final ActorRef ballotBox = getContext().actorOf(Props.create(BallotBox.class));
	  //requestRouter = getContext().actorOf(Props.create(RequestRouter.class));
	  //requestRouter.tell(10000l, getSelf());
//	  HistoricRouterMessage poo = new HistoricRouterMessage();
//	  poo.ballotTimeStamp = 10l;
//	  historicRouter.tell(poo, getSelf());
//	  
//	  //tell (don't expect reply) that actor long 10
//	  ballotBox.tell(init, getSelf());
//	  
//	  //tell a series of messages to set the votes
//	  ballotBox.tell(BallotBox.Msg.voteYes, getSelf());
//	  ballotBox.tell(BallotBox.Msg.voteNo, getSelf());
//	  ballotBox.tell(BallotBox.Msg.voteYes, getSelf());
//	  
//	  //change this to an ask
//	  ballotBox.tell(BallotBox.Msg.tally, getSelf());

	  
	  //db.getAllEntries();
  }

  @Override
  public void onReceive(Object msg) {
	  
	if(msg instanceof Long){
		//its either a create or destroy
		if ((Long) msg < 86400001l){
			//if its under 1 day in milliseconds, then its a make request from the Manager
			System.out.println("Make request, casting new actor");
			
			ActorRef actorRef = getContext().actorOf(Props.create(BallotBox.class));
			Long t = System.currentTimeMillis();
			activeBallotsList.put(t, actorRef);
			InitInfo init = new InitInfo((Long) msg, t); //to do this better just give it the ballet object created from here
			actorRef.tell(init, null);
		} else {
			//in this case as the message is the start time of a ballot, its a destroy message so remove hashmap entry
			activeBallotsList.remove((Long) msg);
			System.out.println("Manager forgets about an actor, Bravo");
		}
	  }
      unhandled(msg);
  }
  

}