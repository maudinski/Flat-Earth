package entities.characters;

import com.badlogic.gdx.physics.box2d.Body;

import utilities.CharacterState;
import utilities.GameState;

public abstract class Player extends Character 
{

	protected Player(CharacterType type, Body body, int id) 
	{
		super(type, body, id);
		// TODO Auto-generated constructor stub
	}
	
	@Override

	public void update(float delta, CharacterState cs)
	{
		//Animation
		frameTime += delta;
		if(frameTime > .05f)
		{
			frameIndex ++;
			frameTime = 0;
		}
		if(frameIndex > 3)
		{
			frameIndex = 0;
		}
		// all the applyForce stuff if moving the body (physics collision stuff)
		
		float direction = -1;
		
		if (!cs.charMovement()) {
			body.setLinearVelocity(0, 0);
			body.setAngularVelocity(0);
		} else {
			direction = cs.getCharDirection();
			this.body.applyForceToCenter(cs.getCharForceX(), cs.getCharForceY(), true); // can this always be true> or do i need to set it?
		}
		
		if(direction >= 0)
		{
			this.body.setTransform(body.getPosition(), (float) Math.toRadians(direction));
		}
		
		updateDirection();

		// this gets the sprite that will be drawn on screen and binds it to the body (physics bs) by setting it to the bodies position
		// body's position is the apply force stuff
		this.getFrame().setPosition(body.getPosition().x - getFrame().getWidth() / 2, body.getPosition().y);
	}
	
}
