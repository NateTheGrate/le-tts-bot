package com.Nate.LongTTSBot;

import com.sun.media.jfxmedia.logging.Logger;
import de.btobastian.javacord.entities.message.Message;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

import static jdk.nashorn.internal.objects.NativeArray.lastIndexOf;

/**
 * Created by Nathanael on 8/18/2016.
 */
public class TwitchEmoteGame {

    private File[] emotePool;
    private File answer = null;
    public boolean timerUp = false;

    public boolean stopGame = false;

    private File pokemonBackground = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\who's-that-pokemon.jpg");
    private File combined = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\combined.jpg");

    private File statsFile = new File("src\\main\\resources\\text\\twitch-game-stats.txt");

    public TwitchEmoteGame(){
        emotePool = Utility.getFilesInFolder( new File("src\\main\\resources\\images\\emotes\\twitch") );
    }

    public void newRound(){
        answer = emotePool[new Random().nextInt(emotePool.length)];
        try {
            Utility.combineImages(combined, pokemonBackground, answer, 48, 41);
            stopGame = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean guess(Message reply){
        if(answer == null){
            return false;
        }

        String answerPath = answer.getPath();
        //case-sensitive
        if(reply.getContent().equals(answerPath.substring(answerPath.lastIndexOf("\\") + 1, answerPath.lastIndexOf(".") ) ) ) {
            //award points
            return true;
        }

        return false;
    }

    public void startTimer(double length){

        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        while (elapsedTime < length*60*1000) {
            //perform db poll/check
            elapsedTime = (new Date()).getTime() - startTime;
        }

        timerUp = true;
    }

    public void stopGame(){
        stopGame = true;
    }



}
