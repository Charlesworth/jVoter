package sample.hello;

import java.util.HashMap;

import messages.makeBallot;
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
	  makeBallot mkBal = new makeBallot("A", "a vote for a test", "testVote", "Pizza", "Pasta");
	  idb.tell(mkBal, getSelf());
	  //System.out.println(System.currentTimeMillis());
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
	  

      unhandled(msg);
  }
  

}