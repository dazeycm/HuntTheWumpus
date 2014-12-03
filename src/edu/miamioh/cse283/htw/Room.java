package edu.miamioh.cse283.htw;

import java.util.*;

public class Room {
	
	public static final int NONE = 0;
	public static final int WUMPUS = 1;
	public static final int HOLE = 2;
	public static final int BATS = 3;
	public static final int LADDER = 4;
	protected int gold = 0;
	protected int arrows = 0;
	
	/** Danger in the room */
	protected int danger;
	
	/** Players currently in this room. */
	protected ArrayList<ClientProxy> players;

	/** Rooms that this room is connected to. */
	protected ArrayList<Room> connected;

	/** ID number of this room. */
	protected int roomId;
	
	/** Constructor. */
	public Room(int danger, int gold) {
		players = new ArrayList<ClientProxy>();
		connected = new ArrayList<Room>();
		this.danger = danger;
		this.gold += gold;
		arrows = 0;
	}
	
	/** Set this room's id number. */
	public void setIdNumber(int n) {
		roomId = n;
	}

	/** Get this room's id number. */
	public int getIdNumber() {
		return roomId;
	}
	
	/** Connect room r to this room (bidirectional). */
	public void connectRoom(Room r) {
		if(r != this && !connected.contains(r))
			connected.add(r);
			r.connected.add(this);
	}
	
	/** Called when a player enters this room. */
	public synchronized void enterRoom(ClientProxy c) {
		players.add(c);
	}
	
	/** Called when a player leaves this room. */
	public synchronized void leaveRoom(ClientProxy c) {
		players.remove(c);
	}

	/** Returns a connected Room (if room is valid), otherwise returns null. */
	public Room getRoom(int room) {
		for(Room r: connected) {
			if(r.getIdNumber() == room) {
				return r;
			}
		}
		return null;
	}
	
	/** Returns a string describing what a player sees in this room. */
	public synchronized ArrayList<String> getSensed() {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("You are in room: " + getIdNumber());
		String t = "You see tunnels to rooms ";
		int c = 0;
		for(Room r : connected) {
			++c;
			if(c == connected.size()) {
				t = t.concat("and " + r.getIdNumber() + ".");
 			} else {
 				t = t.concat("" + r.getIdNumber() + ", ");
 			}
		}
		msg.add(t);
		
		if(this.gold > 0){
			msg.add("There's something shiny in the corner! Better pick it up!");
		}
		if(this.arrows > 0)	{
			msg.add("WOAH! IS THAT AN ARROW ON THE GROUND?!");
		}
		
		for(Room r : connected)	{
			switch(r.danger)	{
			case WUMPUS:
				msg.add("You smell Kyle in a nearby room.");
				break;
			case HOLE:
				msg.add("You feel a draft. There's probably a hole or something nearby.");
				break;
			case BATS:
				msg.add("You hear Kyle's minions screeching in a nearby room.");
				break;
			case LADDER:
				msg.add("You smell wood nearby... ");
			}
		}
		
		return msg;
	}
}
