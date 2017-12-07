package server;

import java.util.ArrayList;


import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.World;

import communicators.clientToServer.KeysPressed;
import server.entities.ServerArcher;
import server.entities.ServerEntityManager;
import server.entities.ServerKnight;
import server.entities.ServerNpcArcher;
import server.entities.ServerNpcKnight;
import server.entities.ServerNpcWizard;
import server.entities.ServerSpawner;
import server.entities.ServerWizard;
import utilities.Settings;
import utilities.Sys;
import utilities.ParseMap.Beacon;
import utilities.ParseMap.MapDetails;
import utilities.ParseMap.PolygonBody;

public class ServerGameMap {

	public float length;
	public float width;

	private int playerCount;
	
	private ServerEntityManager entityManager;
	
	//private float accumulator;
	private float dt = 0.0133f;
	
	MapDetails details;
	
	private World world;

	//private ServerShootArrow arrow;
	
	
	public ServerGameMap(MapDetails details, int playerCount) {
		this.world = new World(new Vector2(0, 0), false);
		world.setContactListener(new CollisionListener());
		
		this.playerCount = playerCount;
		this.details = details;
		
		entityManager = new ServerEntityManager(this);
	}
	
	public void initialize(Settings settings) {		
		
		int npcSpawnCount = 0;
		
		// spawn the first few beacons as players. spawn the rest as npcs/enemies
		// hard coding just spawning a Knight for now
		ArrayList<Beacon> beacons = details.getBeacons();
		int i;
		Sys.print("Currently sets all players in the player count to whatever the player on this computer picks");
		for (i = 0; i < this.playerCount; i++) {
			switch(settings.characterType) {
			case 0:
				entityManager.add(ServerSpawner.spawnPlayer(ServerKnight.class, beacons.get(i).getX()*MapDetails.SCALE, beacons.get(i).getY()*MapDetails.SCALE, this));
				break;
			case 1:
				entityManager.add(ServerSpawner.spawnPlayer(ServerArcher.class, beacons.get(i).getX()*MapDetails.SCALE, beacons.get(i).getY()*MapDetails.SCALE, this));
				break;
			case 2:
				entityManager.add(ServerSpawner.spawnPlayer(ServerWizard.class, beacons.get(i).getX()*MapDetails.SCALE, beacons.get(i).getY()*MapDetails.SCALE, this));
				break;
			}
		}
		for(i = playerCount; i < details.getBeacons().size(); i++) { // will eventuall need to spawn the approriate type of npc, that will
			Beacon b = details.getBeacons().get(i);
			if(npcSpawnCount % 3 == 0)
				entityManager.add(ServerSpawner.spawnNpc(ServerNpcKnight.class, b.getX()*MapDetails.SCALE, b.getY()*MapDetails.SCALE, this));
			else if(npcSpawnCount % 3 == 1) 
				entityManager.add(ServerSpawner.spawnNpc(ServerNpcWizard.class, b.getX()*MapDetails.SCALE, b.getY()*MapDetails.SCALE, this));
			else if(npcSpawnCount % 3 == 2) 
				entityManager.add(ServerSpawner.spawnNpc(ServerNpcArcher.class, b.getX()*MapDetails.SCALE, b.getY()*MapDetails.SCALE, this));
			npcSpawnCount++;
		}
		// TODO now initialize polygons]
		for (PolygonBody pb : details.getPolygonBodies()) {
			if(pb.getVectors().size() > 8)
				Sys.print("ServerGameMap: initializing game holes: More than 8 vectors, wont work with libgdx");
			else 
				entityManager.addHole(ServerSpawner.spawnHole(pb.getScaledArray(), world));
		}
	}
	
	public ServerEntityManager getEntityManager() {
		return this.entityManager;
	}

	public World getWorld() { return this.world; }
	
	public void update(KeysPressed pressed[], float delta) {
		//System.out.println("serverGameMap: this uopdate is called---------");
		//accumulator += delta;
		//while (accumulator >= dt)
		//{
		//Sys.print("hahahahah");
		world.step(dt, 6, 2);
		entityManager.updateAll(pressed, delta);
        //accumulator -= dt;
		//}

	}
	
}
























