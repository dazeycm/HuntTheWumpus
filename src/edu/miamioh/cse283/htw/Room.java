package edu.miamioh.cse283.htw;

import java.util.*;

public class Room {
	
	public static final int NONE = 0;
	public static final int WUMPUS = 1;
	public static final int HOLE = 2;
	public static final int BATS = 3;
	
	/** Danger in the room */
	protected int danger;
	
	/** Players currently in this room. */
	protected ArrayList<ClientProxy> players;

	/** Rooms that this room is connected to. */
	protected HashSet<Room> connected;

	/** ID number of this room. */
	protected int roomId;
	
	/** Constructor. */
	public Room(int danger) {
		players = new ArrayList<ClientProxy>();
		connected = new HashSet<Room>();
		this.danger = danger;
		
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
		connected.add(r);
		r.connected.add(this);
	}
	
	/** Called when a player enters this room. */
	public synchronized void enterRoom(ClientProxy c) {
		ArrayList<String> entryMessage = new ArrayList<String>();
		switch(danger)	{
		case NONE:
			players.add(c);
			break;
		case WUMPUS:
			entryMessage.add("Kyle emerges from the shadows and slowly devours you!");
			c.sendNotifications(entryMessage);
			c.died();
			break;
		case HOLE:
			entryMessage.add("You fell down into a pit and broke both of your legs.");
			entryMessage.add("You're trapped, son. RIP");
			c.sendNotifications(entryMessage);
			c.died();
			break;
		case BATS:
			entryMessage.add("Kyle's bat minions swoop down and carry you to another room!");
			c.sendNotifications(entryMessage);
			this.players.remove(c);
		}
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
		return msg;
	}
}
