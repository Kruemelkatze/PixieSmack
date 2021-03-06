package at.aau.game.basic.managers;

import java.util.HashMap;

import at.aau.game.GameConstants;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager extends AbstractManager {
	private final HashMap<String, String> event2sound;
	private static Music currentMusic;
	private static String currentMusicString = "";

	public SoundManager(AssetManager gdxAssetManager) {
		super(gdxAssetManager);
		// this.parentGame = parentGame;

		// register the available events.
		event2sound = new HashMap<String, String>(20);
		event2sound.put("blip", "sfx/blip.wav");
		event2sound.put("explode", "sfx/explosion.wav");
		event2sound.put("hit", "sfx/hit.wav");
		event2sound.put("jump", "sfx/jump.wav");
		event2sound.put("laser", "sfx/laser.wav");
		event2sound.put("pickup", "sfx/pickup.wav");
		event2sound.put("powerup", "sfx/powerup.wav");

		event2sound.put(GameConstants.GAME_MUSIC, GameConstants.MUSIC_INGAME);
		event2sound.put(GameConstants.INTRO_MUSIC, GameConstants.MUSIC_INTRO);
		event2sound.put("dead", GameConstants.SOUND_DEAD_BIG_FAIRY);
		event2sound.put("bad_collect", GameConstants.SOUND_BAD_COLLECT);

		for (int i = 1; i <= GameConstants.SmackSoundsCount; i++) {
			event2sound.put("smack" + i, "sfx/smack" + i + ".wav");
		}

		for (int i = 1; i <= GameConstants.CollectSoundsCount; i++) {
			event2sound.put("collect" + i, "sfx/collect" + i + ".wav");
		}

	}

	/**
	 * Plays an event registered in the constructor. Make sure that (i) the event is known and (ii) the asset is loaded in the constructor of Game.
	 *
	 * @param event
	 */
	public void playEvent(String event) {
		if (event2sound.get(event) != null) {
			final String soundName = event2sound.get(event).toString();
			if (soundName.equals(GameConstants.MUSIC_INTRO) || soundName.equals(GameConstants.MUSIC_INGAME)) {
				final Music music = gdxAssetManager.get(event2sound.get(event), Music.class);
				music.setLooping(true);
				if ((currentMusic == null || !currentMusic.isPlaying()) || (!currentMusicString.equals(event))) {
					if (currentMusic != null) {
						currentMusic.stop();
					}
					music.play();
					SoundManager.currentMusic = music;
					currentMusicString = event;
				}
			} else {
				final Sound sound = gdxAssetManager.get(event2sound.get(event), Sound.class);
				final long id = sound.play();
				if (event.equals("dead")) {
					sound.setVolume(id, 1.0f);
				} else if (event.equals("smack4")) {
					sound.setVolume(id, 0.1f);
				} else {
					sound.setVolume(id, 0.3f);
				}
			}
		} else {
			System.err.println("playEvent unknown: " + event);
		}
	}

	public static void stopMusic() {
		if (SoundManager.currentMusic != null) {
			currentMusic.stop();
			currentMusic = null;
		}
	}

	public void pauseEvent(String event) {
		if (event2sound.get(event) != null) {
			final String soundName = event2sound.get(event).toString();
			if (soundName.equals(GameConstants.MUSIC_INTRO) || soundName.equals(GameConstants.MUSIC_INGAME)) {
				gdxAssetManager.get(event2sound.get(event), Music.class).pause();
			}
		} else {
			System.err.println("pauseEvent unknown: " + event);
		}
	}

	public void resumeEvent(String event) {
		if (event2sound.get(event) != null) {
			final String soundName = event2sound.get(event).toString();
			if (soundName.equals(GameConstants.MUSIC_INTRO) || soundName.equals(GameConstants.MUSIC_INGAME)) {
				final Music music = gdxAssetManager.get(event2sound.get(event), Music.class);
				if ((currentMusic == null || !currentMusic.isPlaying()) || (!currentMusicString.equals(event))) {
					if (currentMusic != null) {
						currentMusic.stop();
					}
					music.setVolume(1f);
					music.play();
					currentMusic = music;
					currentMusicString = event;
				}
			}
		} else {
			System.err.println("resumeEvent unknown: " + event);
		}
	}
}
