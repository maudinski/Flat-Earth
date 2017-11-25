package server.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.badlogic.gdx.utils.Array;
import communicators.clientToServer.KeysPressed;
import communicators.serverToClient.CharacterState;
import communicators.serverToClient.SkillState;
import server.ServerGameMap;
import server.Updateable;
import server.skills.ServerSkill;
import utilities.ActionTrigger;
import utilities.Sys;

public class ServerEntityManager {

	public ArrayList<ServerCharacter> characters; // TODO this is not optimized
	public Array<Body> bodiesToDestroy;
	ArrayList<Body> holes;
	private ServerGameMap gameMap;

	public Array<Updateable> updateables;
	public Array<ServerSkill> activeSkills;
	
	
	public ServerEntityManager(ServerGameMap gameMap) {
		this.gameMap = gameMap;
		this.bodiesToDestroy = new Array<Body>();
		this.updateables = new Array<Updateable>();
		this.activeSkills = new Array<ServerSkill>();
		characters = new ArrayList<ServerCharacter>();
		holes = new ArrayList<Body>();
	}
	
	// if the id is even needed
	public void add(ServerCharacter character) {
		characters.add(character);
	}
	
	/**
	 * pressed.length will also be the amount of players, so loop through the first n in
	 * the array list (where n is the amount of players), update them, then update the 
	 * rest based on the coordinates (Vector2's) of those players
	 * @param pressed
	 */
	public void updateAll(KeysPressed pressed[], float delta) {
		
		int i;
		//Sys.print("sshahahah" + pressed.length);
		Vector2 v[] = new Vector2[pressed.length];
		
		for (i = 0; i < pressed.length; i++) {
			ServerCharacter c = characters.get(i);
			((ServerPlayer)c).update(pressed[i], delta);
			v[i] = c.getPosition();
			//Sys.print(v[i].toString());
		}
		
		for (i = pressed.length; i < characters.size(); i++)
			((ServerNpc)characters.get(i)).update(v, delta);

		for(i = 0; i < updateables.size; i++)
			updateables.get(i).update(delta);

		for(i = 0; i < bodiesToDestroy.size; i++)
			this.gameMap.getWorld().destroyBody(bodiesToDestroy.get(i));
		bodiesToDestroy.clear();
	}
	
	/**
	 * returns a list of all the character states. used for sending to the client 
	 * there might be ways to optimize this, cause this is a lot of iterations TODO
	 * @return
	 */
	public CharacterState[] getCharacterStates() {
		CharacterState css[] = new CharacterState[this.characters.size()];
		for (int i = 0; i < characters.size(); i++) {
			css[i] = this.characters.get(i).getState();
		}
		return css;
	}
	
	public SkillState[] getSkillStates() {
		SkillState[] ss = new SkillState[this.activeSkills.size];
		for (int i = 0; i < activeSkills.size; i++)
			ss[i] = this.activeSkills.get(i).getState();
		return ss;
	}
	
	public void addHole(Body body) {
		holes.add(body);
	}
	
}
















