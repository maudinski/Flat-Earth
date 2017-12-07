package server.skills;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import communicators.serverToClient.SkillState;
import server.ServerGameMap;
import server.entities.PosAndDir;
import server.entities.ServerCharacter;
import server.entities.ServerSpawner;

public class ServerShootArrow extends PerformableSkill
{
    private Body body;
    private float size;
    private ServerGameMap gameMap;
    private float xInitial;
    private float yInitial;
    private short category = ServerSpawner.ATTACK_PHYSICS;
    private short mask = ServerSpawner.FOOT_PHYSICS;
    private float speed;
    private float range;
    private float distanceCovered;
    private PosAndDir caster;

    //private boolean done;
    
    public ServerShootArrow(PosAndDir caster, ServerGameMap gameMap)
    {
        super(caster, 3);
        this.caster = caster;
        this.speed = .02f;
        this.size = .5f;
        this.range = 15;
        this.gameMap = gameMap;
        this.xInitial = this.position.x;
        this.yInitial = this.position.y;
        this.category = ServerSpawner.ATTACK_PHYSICS;
        this.mask = ServerSpawner.BOUNDING_BODY_PHYSICS | ServerSpawner.FOOT_PHYSICS;
        this.distanceCovered = 0;
        //this.done = false;
        this.power = 4;
    }

    @Override
    public void perform(PosAndDir initial)
    {
//        if(done)
//            return;
        this.gameMap.getEntityManager().updateables.add(this);
        this.gameMap.getEntityManager().activeSkills.add(this);
        this.body = ServerSpawner.createWeaponBody(this.gameMap.getWorld(), this.xInitial, this.yInitial, this.category, this.mask, this.size, new Vector2(-this.size / 2, -this.size),
                new Vector2(this.size / 2, -this.size),
                new Vector2(this.size, -this.size / 2),
                new Vector2(this.size, this.size / 2),
                new Vector2(this.size / 2, this.size),
                new Vector2(-this.size / 2, this.size),
                new Vector2(-this.size, this.size / 2),
                new Vector2(-this.size, -this.size / 2));
        this.body.setUserData(this);

        //this.done = true;
    }

    @Override
    public void update(float delta)
    {

    	if(this.distanceCovered >= this.range)
    	{
    	    this.gameMap.getEntityManager().updateables.removeValue(this, false);
    	    this.gameMap.getEntityManager().activeSkills.removeValue(this, false);
    	    gameMap.getEntityManager().bodiesToDestroy.add(this.body);
    	    return;
    	}
    	
    	this.position.x = (float) (this.body.getPosition().x + Math.cos(Math.toRadians(this.direction)) * (delta * this.speed));
    	this.position.y = (float) (this.body.getPosition().y + Math.sin(Math.toRadians(this.direction)) * (delta * this.speed));
    	
    	this.distanceCovered = (float) Math.sqrt(Math.pow(Math.abs(this.xInitial - this.position.x), 2) + Math.pow(Math.abs(this.yInitial - this.position.y), 2));
    	
    	this.body.setTransform(this.position, this.direction);
    }

	@Override
	public ServerSkill copy() {
		return new ServerShootArrow(this.caster, this.gameMap);
	}

	@Override
	public SkillState getState() {
		return new SkillState(this.position.x, this.position.y, this.direction, SkillType.ARROW);
	}
	
	@Override 
	public void setPerformer(ServerCharacter c) {
		this.performer = c;
	}
	
}



















