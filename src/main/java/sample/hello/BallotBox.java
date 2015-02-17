package sample.hello;

import java.util.concurrent.TimeUnit;

import messages.Ballots;
import messages.InitInfo;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;

public class BallotBox extends UntypedActor {
	Ballots rename = new Ballots();
	ActorRef db = null;
			
	public static enum Msg {
		voteYes, voteNo, tally, done, stop;
	}

	@Override
	public void onReceive(Object msg) {

		if (msg instanceof InitInfo){


			
			final Cancellable tick = getContext().system().scheduler().scheduleOnce(
					Duration.create(((InitInfo) msg).millisecToEnd, TimeUnit.MILLISECONDS),
					getSelf(), Msg.stop, getContext().dispatcher(), null);


	  	} else if (msg == Msg.voteYes || msg == "setYay") {
			System.out.println("Yes Vote");
			rename.yays++;
			getSender().tell(Msg.done, getSelf());


		} else if(msg == Msg.voteNo || msg == "setNay"){
			System.out.println("No Vote");
			rename.nays++;
			getSender().tell(Msg.done, getSelf());


		} else if(msg == Msg.tally || msg == "getStatus"){
			System.out.println("Nays: " + rename.nays + ", Yays: " + rename.yays);
			if (rename.nays > rename.yays){
				System.out.println("The Nays have it!");
			} else if (rename.yays > rename.nays){
				System.out.println("The Yays have it!");
			} else {
				System.out.println("Its a tie!");
			}  
			getSender().tell(Msg.done, getSelf());


		} else if(msg == Msg.stop){
			System.out.println("PoisonPill: " + getSelf().toString() + " bows and leaves the stage");
			getContext().parent().tell(rename.startTime, null);
			
			//db.tell(rename, getSelf());//need to get constant db reference that isn't passed
			//db.tell("stop", getSelf());
			
			getSelf().tell(akka.actor.PoisonPill.getInstance(), null);
		}
		unhandled(msg);
	}
}