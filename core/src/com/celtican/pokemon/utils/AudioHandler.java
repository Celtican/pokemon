package com.celtican.pokemon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;

public class AudioHandler {

    private static final float FADE_OUT_AMOUNT = 0.5f / Game.TARGET_FRAME_RATE; // 2 seconds to fade out
    private static final float FADE_IN_AMOUNT = 2f / Game.TARGET_FRAME_RATE; // half a second to fade in

    private Array<Music> fadingOut = new Array<>();
    private Music curPlaying;
    private boolean soundMuted = false;
    private boolean musicMuted = true;

    public float soundVolume = 0.25f;
    public float musicVolume = 0.25f;

    public void playSound(String fileLocation) {
        playSound(Game.game.assets.get(fileLocation, Sound.class));
    }
    public void playSound(Sound sound) {
        if (sound == null || soundMuted) return;
        sound.play(soundVolume);
    }
    public void stopSound(String fileLocation) {
        stopSound(Game.game.assets.get(fileLocation, Sound.class));
    }
    public void stopSound(Sound sound) {
        if (sound == null || soundMuted) return;
        sound.stop();
    }
    public void toggleSoundMuted() {
        soundMuted = !soundMuted;
    }
    public boolean isSoundMuted() {
        return soundMuted;
    }

    public void playMusic(String fileLocation) {
        playMusic(Game.game.assets.get(fileLocation, Music.class));
    }
    public void playMusic(Music music) {
        if (curPlaying != null) {
            if (curPlaying == music) return;
            fadingOut.add(curPlaying);
            curPlaying = null;
        }
        if (music != null) {
            fadingOut.removeValue(music, true);
            music.setLooping(true);
            music.play();
            music.setVolume(musicMuted ? 0 : FADE_IN_AMOUNT * musicVolume);
            curPlaying = music;
        }
    }
    public void stopMusic() {
        playMusic((Music)null);
    }
    public void toggleMusicMuted() {
        musicMuted = !musicMuted;
        if (musicMuted) {
            fadingOut.forEach(music -> {
                music.stop();
                fadingOut.removeValue(music, true);
            });
            curPlaying.setVolume(0);
        } else {
            curPlaying.setVolume(musicVolume);
        }
    }
    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) toggleMusicMuted();
        if (musicMuted) return;
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
