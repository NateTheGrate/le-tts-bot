package com.Nate.LongTTSBot;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Boolean.parseBoolean;

/**
 * Created by Nathanael on 8/12/2016.
 */
public class LongTTSBot {

    public MagicConch conch = new MagicConch();

    private String lenny = "( ͡° ͜ʖ ͡°)";
    private String pLenny = "( ͡° ͜> ͡°)";

    private File secretPogChamp = new File("src\\main\\resources\\images\\emotes\\twitch\\SecretPogChamp.png");
    private File pogChamp = new File("src\\main\\resources\\images\\emotes\\twitch\\PogChamp.jpg");
    private File squidDab = new File("src\\main\\resources\\images\\emotes\\Dab.png");
    private File dab = new File("src\\main\\resources\\images\\emotes\\SquidDab.png");

    protected File memeFolder = new File("src\\main\\resources\\images\\memes");
    protected File memeQuips = new File("src\\main\\resources\\text\\memes.txt");

    private File beemovie = new File("src\\main\\resources\\text\\beemovie.txt");
    private File beecover = new File("src\\main\\resources\\images\\beemovie\\barry.jpg");
    public boolean stopPasta;

    private File combined = new File("src\\main\\resources\\images\\Twitch-Emote-Game\\combined.jpg");


    protected File godFolder = new File("src\\main\\resources\\images\\extinct");

    public static User sender = null;

    public LongTTSBot(String token){

        DiscordAPI api = Javacord.getApi(token, true);
        TwitchEmoteGame teg = new TwitchEmoteGame();
        // connect
        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate(DiscordAPI api, Message message) {

                        List<String> admins = new ArrayList();
                        admins.add("140614999809720321");
                        admins.add("68232539621298176");



                        Random rand = new Random();

                        sender = message.getAuthor();
                        //Get the message's author
                        String author = message.getAuthor().getName();
                        String authorMention = message.getAuthor().getMentionTag();
                        String authorLog = author + authorMention; //used specificly for logging
                        // check the content of the message
                        String text = message.getContent();

                        //Check if the message is sent by a bot
                        boolean isBot = message.getAuthor().isBot();

                        ///////////////////////////////////////
                        ////////TWITCH GAME////////////////////
                        //////////////////////////////////////

                        if(!isBot) {

                            //start game
                            if (text.startsWith("/game start")) {

                                if (!teg.stopGame) {

                                    message.reply("a game is already in progress :/");

                                } else {

                                    System.out.println(authorLog + " has started the game");
                                    message.reply(authorMention + " has started the Twitch Emote Game!", true);
                                    teg.startGame((long)5.0); //minutes

                                }

                            }

                            else if (!teg.stopGame) {

                                //stop game
                                if (text.startsWith("/game stop") ) {

                                    System.out.println(author + " has stopped the game");
                                    message.reply(authorMention + " has ended the Twitch Emote Game. ;-;", true);
                                    teg.stopGame();

                                }


                                //begin a new round
                                else if (text.startsWith("/game newround")) {

                                    System.out.println(authorLog + " has started a new round");
                                    message.reply(authorMention + " has started a new round.", true);
                                    teg.newRound();
                                    message.reply("Who's that Twitch Emote?");
                                    message.replyFile(combined);
                                    teg.timerset = false;

                                    System.out.println("the answer is " + teg.getAnswer() );

                                }

                                //repeat current emote
                                else if (text.startsWith("/game repeat")) {

                                    System.out.println(authorLog + " has asked for the twitch emote be repeated");
                                    message.reply("Who's that Twitch Emote?");
                                    sleep(10);
                                    message.replyFile(combined);

                                }

                               else if (text.startsWith("/game setTime")) {

                                    int newTime = Integer.parseInt(text.substring(14));
                                    System.out.println("Game timer has been set for " + newTime + " minute(s) " + "by: " + authorLog);
                                    message.reply("A new timer has been set by" + authorMention, true);
                                    teg.setTimerLength((long)newTime);

                                }

                                //give the answer
                                else if(text.startsWith("/game igiveup")){
                                    if(teg.getAnswer().length() > 0) {
                                        message.reply("The answer is '" + teg.getAnswer() + "'", true);
                                        sleep(10);
                                        message.reply("A new round will be started.", true);
                                        teg.newRound();
                                        teg.startTimer();
                                    }else{
                                        message.reply("No answer available.");
                                    }
                                }

                                //sends the time left on the timer
                                else if(text.startsWith("/game time")){
                                    System.out.println(authorLog + " has requested the time left on the timer");
                                    message.reply(teg.timeLeft() + " seconds left until next emote drop");
                                }

                                else if(text.startsWith("/game caseSensitive")){
                                    // from the start of the next word to the end of it sense true and false both end with e
                                    boolean tf = Boolean.parseBoolean( text.substring(20, text.lastIndexOf("e") + 1 ) );
                                    teg.setCaseSensitive(tf);
                                    System.out.println( "case sensitive is set to " + tf + " by " + authorLog);
                                    if(tf){
                                        message.reply("The game is now case-sensitive", true);
                                    }else{
                                        message.reply("The game is not case-sensitive anymore", true);
                                    }
                                }
                                //timer up
                               else if (teg.timerset) {

                                    if (teg.checkTimer()) {
                                        //alert server of drop
                                        System.out.println("time of " + teg.getTimerLength() + " is up" );
                                        message.reply("A twitch emote dropped", true);
                                        sleep(10);
                                        message.reply(Reference.SPACER);
                                        sleep(10);
                                        //start guessing
                                        teg.newRound();
                                        message.reply("Who's that Twitch Emote?");
                                        message.replyFile(combined);

                                        System.out.println("the answer is " + teg.getAnswer() );
                                    }else{
                                        System.out.println(teg.timeLeft() + " seconds are left on the timer");
                                    }

                                } else {

                                    //guessing
                                    if (teg.guess(message)) {
                                        //messager server about the winner
                                        System.out.println(authorLog + " has won the round with the answer of " + "'" + text + "'");
                                        message.reply(message.getAuthor().getMentionTag() + " has won the round!", true);
                                        //start new round
                                        teg.startTimer();

                                    }
                                }

                            }else if(text.startsWith("/game")){
                                message.reply("a game has not been started yet, use the '/game help' command for more information");
                            }
                        }

                        ////////////////////////////////////////
                        ////////TWITCH EMOTES?/////////////////
                        ///////////////////////////////////////
                        if(text.contains(":") && !isBot) {
                            if(text.indexOf(":", text.indexOf(":") + 1) >= 0 ) { //checks for a second : for a complete emote call
                                //get name
                                String emote = text.toLowerCase().substring(text.indexOf(":") + 1, text.indexOf(":", text.indexOf(":") + 1));
                                File[] emotePool = Utility.getFilesInFolder(new File("src\\main\\resources\\images\\emotes\\twitch"));
                                System.out.println(authorLog + " is looking for " + emote + " emote");
                                for (File f : emotePool) {
                                    //loop through every emote to see which is the one
                                    String filePath = f.getPath();
                                    if (emote.equals(filePath.toLowerCase().substring(filePath.lastIndexOf("\\") + 1, filePath.toLowerCase().lastIndexOf(".")))) {
                                        message.replyFile(f);
                                    }
                                }
                            }
                            //pogchamp
                            /**if (text.toLowerCase().contains("pogchamp") && !isBot) {
                                 int randomPog = rand.nextInt(69);
                                    sleep(10);
                                 if (randomPog == 30) {
                                     message.replyFile(secretPogChamp);
                                 } else {
                                    message.replyFile(pogChamp);
                                 }
                             }**/

                        }
                        //dab
                        if (text.toLowerCase().contains(("dab")) && !isBot) {
                            if (!text.toLowerCase().contains("boi")) {
                                int randomDab = rand.nextInt(2);
                                sleep(10);
                                if (randomDab == 1) {
                                    message.replyFile(squidDab);
                                } else {

                                    message.replyFile(dab);
                                }
                            } else {
                                sleep(10);
                                //its dab boi!!!
                                message.replyFile(new File("src\\main\\resources\\images\\emotes\\secret\\dabboi.jpg"), "o shit waddup");
                                //o shit waddup
                            }
                        }

                        //////////////////////////////////////
                        ////////TUMBLRIZE ME, CAPN///////////
                        ////////////////////////////////////
                        if (text.toLowerCase().startsWith("/tumblrize ") && !isBot){
                            System.out.println("Request for tumblrization by:" + authorLog);
                            text = text.replace("/tumblrize ","");
                           while (text.contains(" ") ){
                                String claps = ":clap:";
                                if (rand.nextInt(10) == 1){
                                    claps = claps+":clap:";
                                }
                                text = text.replace(" ",":clap:");
                            }

                            message.reply(message.getAuthor().getMentionTag()+": "+text);
                            message.delete();
                        }

                        //////////////////////////////////////
                        ////////STUPID MEME CRAP/////////////
                        ////////////////////////////////////
                        if(text.toLowerCase().contains(">meme") && !isBot ){
                            System.out.println("Request for a meme by:" + authorLog);
                            sleep(100);

                            //Get the image
                            File[] memes = Utility.getFilesInFolder(memeFolder);
                            File file = memes[rand.nextInt(memes.length)];

                            //Get the string
                            String quip;
                            try {
                                List<String> strings = Utility.getLinesInTextFile(memeQuips);
                                quip = strings.get(rand.nextInt(strings.size()));
                            }catch (IOException e){
                                System.out.println("Error while reading text file. Defaulting...");
                                quip = "happy to serve you :)";
                            }
                            System.out.println("Using: '"+quip+"'");

                            //Post the message
                            message.replyFile(file, quip);
                        }
                        //triggered
                        if(text.toLowerCase().contains("triggered") && !isBot){
                            System.out.println(authorLog + " is triggered");
                            sleep(10);
                            message.reply("Sombody triggered?");
                            sleep(5);
                            message.reply("https://media.giphy.com/media/ZEVc9uplCUJFu/giphy.gif");
                        }
                        ///////////////////////////////////////
                        ///////////EXPAND LENNY///////////////
                        /////////////////////////////////////
                        String lennycheck = text.toLowerCase();
                        if(lennycheck.contains("lennypede")){
                            message.reply(Reference.LENNYPEDE);
                        }
                        else if (lennycheck.contains("lenny") &&!isBot){

                            String chosenLenny = (!lennycheck.contains("salt") ? Reference.LENNY : Reference.PLENNY);

                            if (!lennycheck.contains("/ltts")){ //lennies in non-ltts message

                                sleep(100);
                                message.delete();
                                String lennified = authorMention + ": "+ Utility.replace(text,"lenny",chosenLenny); //oh god that lenny
                                message.reply(lennified);

                            }else{ //lennies in ltts messages
                                text = Utility.replace(text,"lenny",chosenLenny);
                            }
                        }//////////////////////////////////
                        /////////COPY PASTAS/////////////////
                        ////////////////////////////////////
                        if(text.startsWith("/pasta") && !isBot) {

                            stopPasta = false;
                            if (text.toLowerCase().contains("stop")) {
                                stopPasta = true;
                            }
                            if (text.toLowerCase().contains("bee movie"))

                            {
                                System.out.println(authorLog + " has wrecked hell with the bee movie script");
                                message.reply(authorMention + " has started the Bee Movie");
                                sleep(100);
                                message.replyFile(beecover);
                                sleep(1000);
                                Thread t = new Thread() {  //run on a seperate thread so this doesn't interupt everything else
                                    public void run() {
                                        try {
                                            //get text
                                            String script = Utility.getTextinTextFile(beemovie);
                                            //split into tts-sized chunks
                                            ArrayList<String> lines = Utility.split(script, 140);

                                            for (String line : lines) { //loop through each chunk of the text file
                                                //stop, because this would probably ruin a whole chat server
                                                if (stopPasta) {
                                                    message.reply(authorMention + " has ended the Bee Movie");
                                                    return;
                                                }
                                                try {
                                                    Thread.sleep(2000); //make sure messages get sent inorder
                                                }catch(InterruptedException e){
                                                    //interrupted
                                                }
                                                message.reply(line, true); //send tts-sized chunk
                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            System.out.println("Incorrect file or something went wrong when fetching the Bee Movie script text file.");
                                        }
                                    }
                                };
                                t.start(); //start thread

                            }
                        }
                        ///////////////////////////////////////
                        ///////////AVENGING THE///////////////
                        //////////////FALLEN/////////////////
                        if (text.toLowerCase().contains("dicks out") && !isBot){
                            File[] gods = Utility.getFilesInFolder(godFolder);
                            File god = gods[rand.nextInt(gods.length)];
                            message.replyFile(god,"#DicksOutForHarambe");
                        }

                        ///////////////////////////////////////
                        ///////////MAGIC CONCH////////////////
                        /////////////////////////////////////
                        if (text.regionMatches(false, 0, "/conch", 0, 6) && !isBot){
                            text = text.replace("/conch ","");
                            text = text.replace("/conch","");
                            message.reply(authorMention + " " + conch.ask(text,rand));
                        }
                        if (text.toLowerCase().startsWith("/bully ") && !isBot && (!message.getAuthor().getId().equals(conch.toBully) || admins.contains(message.getAuthor().getId()))){
                            conch.toBully = text.replace("/bully ","");
                            System.out.println("Set bully target to: '"+conch.toBully+"' by:" + authorLog);
                        }

                        ////////////////////////////////////
                        /////////no man's sky prank////////
                        //////////////////////////////////
                        if(text.toLowerCase().contains("no mans sky") || text.toLowerCase().contains("no man's sky") ){
                            sleep(100);
                            message.reply("no mans sky, more like NO GUY BUY", true);
                        }

                        ////////////////////////////////
                        ////////BLOCK LETTERS//////////
                        //////////////////////////////
                        if(text.startsWith("/blockify") && !isBot){
                            //get message
                            String mes = text.toLowerCase().substring(9);
                            //remove command from string
                            //reply
                            String reply = "";
                            //loop through each character in the original message
                            for(int i = 0; i < mes.length(); i++){
                                //char at each character of the string
                                char letter = mes.substring(i, i+1).charAt(0);
                                //add spaces
                                if(letter == ' '){
                                    reply += letter + "";
                                }
                                //make sure that the character is a letter
                                for(char alphabet = 'a'; alphabet <= 'z'; alphabet++){
                                    if(letter == alphabet){
                                        //add block emote for each letter
                                        reply += ":regional_indicator_" + letter + ":";
                                    }
                                }
                                //try to see if there are any numbers
                                try {
                                    //for the numbers out there
                                    for (int j = 0; j <= 9; j++) {
                                        if (Integer.parseInt(letter + "") == j) {
                                            //
                                            reply += ":clock" + j + ":";
                                        }
                                    }
                                }catch (Exception e){
                                    //is not number
                                }

                            }
                            //send reply
                            message.reply(reply);
                        }

                        ///////////////////////////////////////
                        ////////LONG TTS MESSAGES/////////////
                        /////////////////////////////////////
                        if (text.startsWith("/ltts") && !isBot) {

                            if (text.length() > 140 && !isBot) {
                                System.out.println("/ltts message sent by " + authorLog); //logging
                                sleep(100); //have to wait to make sure the messages get sent inorder
                                message.delete();//reduce spam and deleting the message here prevents any accidental repeating

                                message.reply(Reference.SPACER);
                                sleep(500);
                                message.reply(authorMention + " made me do this;" + text.substring(text.indexOf("/ltts") + 5, text.indexOf(" ", 140)), true);
                                //spitting out the split up strings
                                ArrayList<String> splits = Utility.split(text.substring(text.indexOf(" ", 140)), 140);
                                for (String s : splits) {
                                    sleep(500);
                                    message.reply(s, true);
                                }
                                sleep(500);
                                message.reply(Reference.SPACER);

                                //short ltts calls
                            } else {
                                System.out.println("/ltts message under 140 characters was sent by " + authorLog);//just some logging
                                sleep(100);
                                message.delete();
                                message.reply(Reference.SPACER);
                                sleep(100);
                                message.reply("The ltts command is used for messages over 140 characters, use the tts command for shorter messages");
                                sleep(100);
                                message.reply(Reference.SPACER);
                            }
                        }
                        ///////////////////////////////////////
                        ////////anti-anthony//////////////////
                        /////////////////////////////////////
                        if( text.contains("Ỏ̷͖͈̞̩͎̻̫̫̜͉̠̫͕̭̭̫̫̹̗̹͈̼̠̖͍͚̥͈̮̼͕̠̤̯̻̥̬̗̼̳̤̳̬̪̹͚̞̼̠͕̼̠̦͚̫͔̯̹͉͉̘͎͕̼̣̝͙̱̟̹̩̟̳̦̭͉̮̖̭̣̣̞̙̗̜̺̭̻̥͚͙̝̦̲̱͉͖͉̰̦͎̫̣̼͎͍̠̮͓̹̹͉̤̰̗̙͕͇͔̱͕̭͈̳̗̭͔̘̖̺̮̜̠͖̘͓̳͕̟̠̱̫̤͓͔̘̰̲͙͍͇̙͎̣̼̗̖͙̯͉̠̟͈͍͕̪͓̝̩̦̖̹̼̠̘̮͚̟͉̺̜͍͓̯̳̱̻͕̣̳͉̻̭̭̱͍̪̩̭̺͕̺̼̥̪͖̦̟͎̻̰")){
                            message.delete();
                            message.reply("¯\\_(ツ)_/¯");
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

    }


    public void sleep(int milliseconds){
        try{
            Thread.sleep(1000);
        }catch( Exception e){
            System.out.println("something went wrong with the delay between messages: " + e.getMessage());
        }
    }

}
