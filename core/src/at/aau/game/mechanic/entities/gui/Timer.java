package at.aau.game.mechanic.entities.gui;

import at.aau.game.GameConstants;
import at.aau.game.mechanic.World;
import at.aau.game.mechanic.entities.AbstractGameObject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Timer extends AbstractGameObject {
	private final Animation timer;

	public Timer(Vector2 position, World world, Vector2 size) {
		super(position, world, size);
		timer = AbstractGameObject.getAnimationManager().loadAnimation("gameplay/timer-pink.png", GameConstants.TIMEOUT / 15f, (int) size.x, (int) size.y);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(timer.getKeyFrame(animTime, true), this.position.x, this.position.y);
	}

	@Override
	public void update(float delta) {
		this.animTime += delta;
		if (this.animTime < 0) {
			this.animTime = 0;
		}
		if (this.animTime > GameConstants.TIMEOUT * 1000) {
			this.animTime = GameConstants.TIMEOUT * 1000;
		}
	}

	public void addAnimTime(float add) {
		this.animTime += add;
		if (this.animTime < 0) {
			this.animTime = 0;
		}
		if (this.animTime > GameConstants.TIMEOUT * 1000) {
			this.animTime = GameConstants.TIMEOUT * 1000;
		}
	}
}