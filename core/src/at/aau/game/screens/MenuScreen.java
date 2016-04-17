package at.aau.game.screens;

import at.aau.game.GameConstants;
import at.aau.game.PixieSmack;
import at.aau.game.ScreenManager;
import at.aau.game.SoundManager;
import at.aau.game.Mechanics.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen extends ScreenAdapter {
	private final SpriteBatch batch;
	private final OrthographicCamera cam;
	private PixieSmack parentGame;
	Texture backgroundImage, helpButton, helpButtonClicked;
	BitmapFont menuFont;
	// Music menuMusic;
	// ImageButton imageButton;
	// ImageButtonStyle imageButtonStyle;
	private boolean helpClicked;

	String[] menuStrings = { GameConstants.NEW_GAME, GameConstants.RESUME_GAME, "Hall Of Fame", "Credits", "Exit" };
	int currentMenuItem = 0;

	private float offsetLeft = PixieSmack.MENU_GAME_WIDTH / 3.8f, offsetTop = PixieSmack.MENU_GAME_WIDTH / 4, offsetY = PixieSmack.MENU_GAME_HEIGHT / 8;

	public MenuScreen(PixieSmack game) {
		this.parentGame = game;
		helpClicked = false;

		backgroundImage = parentGame.getAssetManager().get("menu/menu_background.png");
		helpButton = parentGame.getAssetManager().get("menu/helpButton.png");
		helpButtonClicked = parentGame.getAssetManager().get("menu/helpButton_pressed.png");
		menuFont = parentGame.getAssetManager().get("menu/Ravie_42.fnt");
		menuFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		menuFont.setColor(GameConstants.COLOR_PINK);
		// Create camera that projects the desktop onto the actual screen size.
		cam = new OrthographicCamera(PixieSmack.MENU_GAME_WIDTH, PixieSmack.MENU_GAME_HEIGHT);

		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		batch = new SpriteBatch();

		Gdx.input.setCursorCatched(false);
		this.parentGame.getSoundManager().playEvent(GameConstants.INTRO_MUSIC);
		// menuMusic = Gdx.audio.newMusic(Gdx.files.internal(GameConstants.MUSIC_INTRO));
		// menuMusic.setLooping(true);
		// menuMusic.play();
		// imageButtonStyle = new ImageButtonStyle();
		// imageButtonStyle.imageUp =
		// imageButton = new ImageButton();

	}

	@Override
	public void render(float delta) {
		handleInput();
		// camera:
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// draw bgImage ...
		batch.draw(backgroundImage, 0, 0, PixieSmack.MENU_GAME_WIDTH, PixieSmack.MENU_GAME_HEIGHT);
		// draw Strings ...
		int offsetFactor = 0;
		for (int i = 0; i < menuStrings.length; i++) {
			if (i == currentMenuItem)
				menuFont.setColor(0.8f, 0.0f, 0.7f, 1f);
			else
				menuFont.setColor(GameConstants.COLOR_PINK);
			if (menuStrings[i].equals(GameConstants.RESUME_GAME) && !this.parentGame.alreadyIngame) {
				menuFont.setColor(0.3f, 0.3f, 0.3f, 1f);
				menuFont.draw(batch, menuStrings[i], offsetLeft, PixieSmack.MENU_GAME_HEIGHT - offsetTop - offsetFactor * offsetY);
				offsetFactor++;
			} else if (menuStrings[i].equals(GameConstants.RESUME_GAME) && this.parentGame.alreadyIngame) {
				menuFont.draw(batch, menuStrings[i], offsetLeft, PixieSmack.MENU_GAME_HEIGHT - offsetTop - offsetFactor * offsetY);
				offsetFactor++;
			} else if (!menuStrings[i].equals(GameConstants.RESUME_GAME)) {
				menuFont.draw(batch, menuStrings[i], offsetLeft, PixieSmack.MENU_GAME_HEIGHT - offsetTop - offsetFactor * offsetY);
				offsetFactor++;
			}
		}
		//draw FAQ Button
		if (!helpClicked) {
			batch.draw(helpButton,PixieSmack.MENU_GAME_WIDTH-90,20,90,90);
		} else {
			batch.draw(helpButtonClicked,PixieSmack.MENU_GAME_WIDTH-90,20,90,90);
		}
		batch.end();
	}

	private void handleInput() {
		// keys ...
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && this.parentGame.alreadyIngame) { // JUST
			this.parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.ResumeGame);
			// SoundManager.stopMusic();
			parentGame.getSoundManager().playEvent("blip");
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			currentMenuItem = (currentMenuItem + 1) % menuStrings.length;
			if (currentMenuItem == 1 && !this.parentGame.alreadyIngame) {
				currentMenuItem++;
			}
			parentGame.getSoundManager().playEvent("blip");
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			currentMenuItem = (currentMenuItem - 1) % menuStrings.length;
			if (currentMenuItem == 1 && !this.parentGame.alreadyIngame) {
				currentMenuItem--;
			}
			if (currentMenuItem < 0) {
				currentMenuItem = 0;
			} else {
				parentGame.getSoundManager().playEvent("blip");
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if (menuStrings[currentMenuItem].equals("Exit")) {
				Gdx.app.exit();
				parentGame.getSoundManager().playEvent("explode");
			} else if (menuStrings[currentMenuItem].equals("Credits")) {
				// SoundManager.stopMusic();
				parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.Credits);
			} else if (menuStrings[currentMenuItem].equals(GameConstants.NEW_GAME)) {
				parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.NewGame);
				// SoundManager.stopMusic();
			} else if (menuStrings[currentMenuItem].equals(GameConstants.RESUME_GAME) && this.parentGame.alreadyIngame) {
				parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.ResumeGame);
				// SoundManager.stopMusic();
			} else if (menuStrings[currentMenuItem].equals("Hall Of Fame")) {
				parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.Highscore);
				// SoundManager.stopMusic();
			}
		}
		// touch
		if (Gdx.input.justTouched()) {
			Vector3 touchWorldCoords = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1));
			// find the menu item ..
			for (int i = 0; i < menuStrings.length; i++) {
				if (touchWorldCoords.x > offsetLeft &&
						touchWorldCoords.x < PixieSmack.MENU_GAME_WIDTH - offsetLeft) {
					float pos = PixieSmack.MENU_GAME_HEIGHT - offsetTop - i * offsetY;
					if (touchWorldCoords.y < pos &&
							touchWorldCoords.y > pos - menuFont.getLineHeight()) {
						// it's there
						if (menuStrings[i].equals("Exit")) {
							Gdx.app.exit();
						} else if (menuStrings[i].equals(GameConstants.NEW_GAME)) {
							// SoundManager.stopMusic();
							parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.NewGame);
						} else if (menuStrings[i].equals(GameConstants.RESUME_GAME) && this.parentGame.alreadyIngame) {
							// SoundManager.stopMusic();
							parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.ResumeGame);
						} else if (menuStrings[i].equals("Credits")) {
							// SoundManager.stopMusic();
							parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.Credits);
						} else if (menuStrings[i].equals("Hall Of Fame")) {
							// SoundManager.stopMusic();
							parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.Highscore);
						}
					}
				}

			}
			if (touchWorldCoords.x > PixieSmack.MENU_GAME_WIDTH - 90 &&
						touchWorldCoords.x < PixieSmack.MENU_GAME_WIDTH &&
						touchWorldCoords.y < 110 &&
						touchWorldCoords.y > 20) {
				helpClicked = true;
				parentGame.getScreenManager().setCurrentState(ScreenManager.ScreenState.Help);
				helpClicked = false;
			}
		}
		Vector3 worldCoords = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1));
		if (worldCoords.x > offsetLeft && worldCoords.x < PixieSmack.MENU_GAME_WIDTH - offsetLeft && worldCoords.y < PixieSmack.MENU_GAME_HEIGHT - offsetTop) {
			for (int i = 0; i < menuStrings.length; i++) {
				float pos = PixieSmack.MENU_GAME_HEIGHT - offsetTop - i * offsetY;
				if (worldCoords.y > pos - menuFont.getLineHeight() && worldCoords.y < pos) {
					currentMenuItem = i;
				}
			}

		}
		if (worldCoords.x > PixieSmack.MENU_GAME_WIDTH - 90 &&
				worldCoords.x < PixieSmack.MENU_GAME_WIDTH &&
				worldCoords.y < 110 &&
				worldCoords.y > 20) {
		helpClicked = true;
	} else {
			helpClicked = false;
		}
	}

}
