package com.Nate.LongTTSBot;

import de.btobastian.javacord.entities.message.Message;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Nathanael on 8/18/2016.
 */
public class TwitchEmoteGame {

    private File[] emotePool;
    private File answer = null;

    public boolean stopGame = true;
    public boolean messagePause = true;

    private File pokemonBackground = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\who's-that-pokemon.jpg");
    private File combined = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\combined.jpg");

    private File statsFile = new File("src\\main\\resources\\text\\twitch-game-stats.txt");

    private long timer;
    private long startTime;

    private long timerLength = 1;

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
            Utility.overwriteUserVariable(statsFile, reply.getAuthor().getId(), "twitchgame", "1", true);
            return true;
        }

        return false;
    }

    public void startTimer(){

        //record the time the call started
        startTime = System.currentTimeMillis();

    }

    public boolean checkTimer(){

        long elapsedTime = System.currentTimeMillis() - startTime ;

        if( elapsedTime > (timer * 60 * 1000) ){
            return true;
        }

        return false;
    }

    public void setTimerLength(double length){
        timerLength = (long)length;
    }

    public double getTimerLength(){
        return timerLength;
    }


}
