package com.handheld.upsizeuhf.util;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class MediaUtils {
    private static boolean enableAppInventorySound = true;
    /**
     * Play one beep sound by not use MediaPlayer
     */
    public static void playOneBeepSoundNoMediaPlayer()  {
        if(enableAppInventorySound) {
//        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
//            AudioTrack tone = generateTone(440, 150);
//            AudioTrack tone = generateTone(220, 150);
//            AudioTrack tone = generateTone(880, 100);
            try {
                AudioTrack tone = generateTone(880, 50);
                tone.play();
                tone.release();
            }catch (Exception e)    {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Android Tone Generator
     *  Usage:
     *    AudioTrack tone = generateTone(440, 250);
     *    tone.play();
     * @param freqHz
     * @param durationMs
     * @return
     */
    private static AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }


}
