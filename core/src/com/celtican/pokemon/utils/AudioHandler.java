package com.celtican.pokemon.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;

public class AudioHandler {

    private static final float FADE_OUT_AMOUNT = 0.5f / Game.TARGET_FRAME_RATE; // 2 seconds to fade out
    private static final float FADE_IN_AMOUNT = 2f / Game.TARGET_FRAME_RATE; // half a second to fade in

    private Array<Music> fadingOut = new Array<>();
    private Music curPlaying;

    public float soundVolume = 0.25f;
    public float musicVolume = 0.25f;

    public void playSound(String fileLocation) {
        playSound(Game.game.assets.get(fileLocation, Sound.class));
    }
    public void playSound(Sound sound) {
        if (sound == null)
            return;
        sound.play(soundVolume);
    }

    public void playMusic(String fileLocation) {
        playMusic(Game.game.assets.get(fileLocation, Music.class));
    }
    public void playMusic(Music music) {
        if (curPlaying != null) {
            fadingOut.add(curPlaying);
            curPlaying = null;
        }
        if (music != null) {
            music.setLooping(true);
            music.play();
            music.setVolume(FADE_IN_AMOUNT * musicVolume);
            curPlaying = music;
        }
    }

    public void update() {
        fadingOut.forEach(music -> {
            music.setVolume(Math.max(music.getVolume() - FADE_OUT_AMOUNT*musicVolume, 0));
            if (music.getVolume() == 0) {
                music.stop();
                fadingOut.removeValue(music, true);
            }
        });
        if (curPlaying != null && curPlaying.getVolume() < musicVolume)
            curPlaying.setVolume(Math.min(curPlaying.getVolume() + FADE_IN_AMOUNT*musicVolume, musicVolume));
    }
}
