package helloandroid.ut3.battlewhat.gameUtils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

import helloandroid.ut3.battlewhat.R;

public class SoundManager {

    MediaPlayer mediaPlayer; // For music, large sound don't work with SoundPool
    private static final int MAX_STREAMS = 5;
    private SoundPool soundPool;
    private boolean soundPoolIsLoaded = false;
    private Map<Integer, Integer> soundLoaded;
    private Context context;

    public SoundManager(Context context) {
        this.context = context;
        soundLoaded = new HashMap<>();
        mediaPlayer = MediaPlayer.create(context, R.raw.music);
        mediaPlayer.setVolume(0.5f, 0.5f);
        initSoundPool();
    }

    private void initSoundPool() {
        // AudioManager audio settings for adjusting the volume
        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        SoundPool.Builder builder= new SoundPool.Builder();
        builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
        soundPool = builder.build();
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> soundPoolIsLoaded = true);

        // Load sound
        soundLoaded.put(R.raw.laser_player, soundPool.load(context, R.raw.laser_player, 1));
        soundLoaded.put(R.raw.laser_god, soundPool.load(context, R.raw.laser_god, 1));
        soundLoaded.put(R.raw.laser_enemy, soundPool.load(context, R.raw.laser_enemy, 1));
        soundLoaded.put(R.raw.power_up, soundPool.load(context, R.raw.power_up, 1));
        soundLoaded.put(R.raw.player_hit, soundPool.load(context, R.raw.player_hit, 1));
        soundLoaded.put(R.raw.enemy_hit, soundPool.load(context, R.raw.enemy_hit, 1));
        soundLoaded.put(R.raw.power_up_god, soundPool.load(context, R.raw.power_up_god, 1));
    }

    public void playMusic() {
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    public void playPowerUp() {
        if(soundPoolIsLoaded && soundPool != null) {
            soundPool.play(soundLoaded.get(R.raw.power_up), 0.7f, 0.7f, 1, 0, 1f);
        }
    }

    public void playPowerUpGod() {
        if(soundPoolIsLoaded && soundPool != null) {
            soundPool.play(soundLoaded.get(R.raw.power_up_god), 0.7f, 0.7f, 1, 0, 1f);
        }
    }

    public void playPlayerHit() {
        if(soundPoolIsLoaded && soundPool != null) {
            soundPool.play(soundLoaded.get(R.raw.player_hit), 0.3f, 0.3f, 1, 0, 1f);
        }
    }

    public void playEnemyHit() {
        if(soundPoolIsLoaded && soundPool != null) {
            soundPool.play(soundLoaded.get(R.raw.enemy_hit), 0.5f, 0.5f, 1, 0, 1f);
        }
    }

    public void playSoundLaserPlayer(boolean isGodMod) {
        if(soundPoolIsLoaded && soundPool != null) {
            soundPool.play(isGodMod ?
                            soundLoaded.get(R.raw.laser_god) :
                            soundLoaded.get(R.raw.laser_player),
                    0.5f, 0.5f, 1, 0, 1f);
        }
    }
    public void playSoundLaserEnemy() {
        if(soundPoolIsLoaded && soundPool != null) {
            soundPool.play(soundLoaded.get(R.raw.laser_enemy),1f, 1f, 1, 0, 1f);
        }
    }

    public final void cleanUpSound() {
        soundLoaded = null;
        soundPool.release();
        soundPool = null;
        mediaPlayer.stop();
        mediaPlayer = null;
    }

}
