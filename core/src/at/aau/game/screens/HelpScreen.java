package at.aau.game.screens;

import at.aau.game.GameConstants;
import at.aau.game.PixieSmack;
import at.aau.game.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HelpScreen extends ScreenAdapter {
	private PixieSmack parentGame;
	Texture backgroundImage, goodPixie, badPixie, pixie;
	BitmapFont menuFont;
	BitmapFont describe;
	private final OrthographicCamera cam;
	private final SpriteBatch batch;
	String helpString = "Help";
	String collect = "Hurry up\n and catch fairy dust!";

	
	
	float offsetLeft = PixieSmack.MENU_GAME_WIDTH / 6,
			offsetTop = PixieSmack.MENU_GAME_WIDTH / 20,
			offsetY = PixieSmack.MENU_GAME_HEIGHT / 8;
	
	public HelpScreen(PixieSmack game) {
		this.parentGame = game;

		backgroundImage = parentGame.getAssetManager().get("menu/menu_background_empty.png");
		menuFont = parentGame.getAssetManager().get("menu/Ravie_72.fnt");
		menuFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		menuFont.setColor(GameConstants.COLOR_PINK);
		
		describe = parentGame.getAssetManager().get("menu/Ravie_42.fnt");
		describe.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		describe.setColor(GameConstants.COLOR_PINK);
		
		cam = new OrthographicCamera(PixieSmack.MENU_GAME_WIDTH, PixieSmack.MENU_GAME_HEIGHT);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();
		
		batch = new SpriteBatch();
		
		badPixie= new Texture(Gdx.files.internal("gameplay/bad_pixie.png"));
		goodPixie= new Texture(Gdx.files.internal("gameplay/spec_pixie.png"));
		pixie= new Texture(Gdx.files.internal("gameplay/pixie.png"));
		
	}
	
	@Override
	public void render(float delta){
		handleInput();
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// draw bgImage ...
		batch.draw(backgroundImage, 0, 0, PixieSmack.MENU_GAME_WIDTH, PixieSmack.MENU_GAME_HEIGHT);
		menuFont.draw(batch, helpString, offsetLeft, PixieSmack.MENU_GAME_HEIGHT - offsetTop);
		
		describe.draw(batch, collect, offsetLeft, PixieSmack.MENU_GAME_HEIGHT - offsetTop -0 * offsetY -120);

		batch.draw(pixie, 150,390);
		batch.draw(goodPixie, 150,250);
		batch.draw(badPixie, 150,110);
		
		
		
		batch.end();
	}
	
	private void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
			parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.Menu);
		}
	}

}
