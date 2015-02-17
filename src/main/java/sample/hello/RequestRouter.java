package sample.hello;

import java.util.HashMap;

import messages.HistoricRouterMessage;
import messages.InitInfo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;


public class RequestRouter extends UntypedActor{
	
	
	public void onReceive(Object msg) {
		  
		if(msg instanceof HistoricRouterMessage){
			HistoricRouterMessage currentMessage = (HistoricRouterMessage) msg;
			  
			if (currentMessage.requestType == "getStatus"){
				getStatus(currentMessage);

			} else if(currentMessage.requestType == "setComment"){
				setComment(currentMessage);
				  
			} else if(currentMessage.requestType == "setInfo"){
				setInfo(currentMessage);
				  
			} else if(currentMessage.requestType == "setYay"){
				setYay(currentMessage, getSender());
				  
			} else if(currentMessage.requestType == "setNay"){
				setNay(currentMessage, getSender());
				  
			}
		}		  
		unhandled(msg);
	}
	
	//implement HTTP router part of this or receive message from central HTTP server!
	  
	private void getStatus(HistoricRouterMessage msg){
//		if (activeBallotsList.get(msg.ballotTimeStamp) == null){
//			//if not in active list the get tally from DB
//		} else {
//			activeBallotsList.get(msg.ballotTimeStamp).tell("getStatus", getSelf());
//		}
	}
	
	  
	private void setComment(HistoricRouterMessage msg){
		if (msg.ballotTimeStamp < (System.currentTimeMillis() - 300000l)){
			//send message back to HTTP router that its timed out
		} else {
			//took over 5 minutes to set message, denied
		}
	}
	
	  
	private void setInfo(HistoricRouterMessage msg){
		if (msg.ballotTimeStamp < (System.currentTimeMillis() - 300000l)){
			//send message back to HTTP router that its timed out
		} else {
			//took over 5 minutes to set message, denied
		}
	}
	
	
	private void setYay(HistoricRouterMessage msg, ActorRef actorRef){
//		if (activeBallotsList.get(msg.ballotTimeStamp) == null){
//			  //send message back to HTTP router that its timed out
//		} else {
//			activeBallotsList.get(msg.ballotTimeStamp).tell("setYay", getSelf());
//		}
	}
	

	private void setNay(HistoricRouterMessage msg, ActorRef actorRef){
//		if (activeBallotsList.get(msg.ballotTimeStamp) == null){
//			  //send message back to HTTP router that its timed out
//		} else {
//			activeBallotsList.get(msg.ballotTimeStamp).tell("setNay", getSelf());
//		}
	}
}
