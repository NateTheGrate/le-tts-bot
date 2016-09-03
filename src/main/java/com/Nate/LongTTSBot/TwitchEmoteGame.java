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

    private boolean caseSensitive = true;

    public boolean stopGame = true;
    public boolean timerset = false;

    private File pokemonBackground = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\who's-that-pokemon.jpg");
    private File combined = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\combined.jpg");

    private File statsFile = new File("src\\main\\resources\\text\\twitch-game-stats.txt");


    private long startTime;

    private long timerLength = 1; //in minutes

    public TwitchEmoteGame(){
        emotePool = Utility.getFilesInFolder( new File("src\\main\\resources\\images\\emotes\\twitch") );

    }

    public void startGame(double timer){
        timerLength = (long)timer;
        startTimer();
        stopGame = false;
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
        //not case-sensitive
        if(!caseSensitive){
            if(reply.getContent().toLowerCase().equals(answerPath.toLowerCase().substring(answerPath.lastIndexOf("\\") + 1, answerPath.lastIndexOf(".") ) ) ) {
                //award points
                Utility.overwriteUserVariable(statsFile, reply.getAuthor().getId(), "twitchgame", "1", true);
                return true;
            }
        }
        //case-sensitive
        else if(reply.getContent().equals(answerPath.substring(answerPath.lastIndexOf("\\") + 1, answerPath.lastIndexOf(".") ) ) ) {
            //award points
            Utility.overwriteUserVariable(statsFile, reply.getAuthor().getId(), "twitchgame", "1", true);
            return true;
        }

        return false;
    }

    public void startTimer(){

        //record the time the call started
        startTime = System.currentTimeMillis();
        timerset = true;

    }

    public void startTimer(double timer){

        //record the time the call started
        startTime = System.currentTimeMillis();
        timerset = true;
        timerLength = (long)timer;
    }

    public boolean checkTimer(){

        if(timerset) {

            long elapsedTime = System.currentTimeMillis() - startTime;

            if (elapsedTime > (timerLength * 60 * 1000)) {
                timerset = false;
                return true;
            }
        }
        return false;
    }

    public long timeLeft(){
        return ( (timerLength * 60 * 1000) - (System.currentTimeMillis() - startTime) ) / 1000;
    }
    public String getAnswer(){

        if(answer == null){
            System.out.println("attempt to retrieve answer when not available");
            return "";
        }

        String answerPath = answer.getPath();


        return answerPath.substring(answerPath.lastIndexOf("\\") + 1, answerPath.lastIndexOf(".") );
    }

    public void setTimerLength(double length){
        timerLength = (long)length;
    }

    public double getTimerLength(){
        return timerLength;
    }
    //just to make more sense
    public void stopGame(){
        stopGame = true;
    }

    public void setCaseSensitive(boolean tf){
        caseSensitive = tf;
    }


}
