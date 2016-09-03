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
                if( System.currentTimeMillis() % 5000 == 0){ //every 5 mins
                    //working on nadeku flower twitch thingy
                }

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

                                //broken
                                //set the game timer
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
                        if(text.contains(":") ) {
                            //get name
                            String emote = text.toLowerCase().substring(text.indexOf(":") + 1, text.indexOf(":", text.indexOf(":") + 1));
                            File[] emotePool = Utility.getFilesInFolder( new File("src\\main\\resources\\images\\emotes\\twitch") );
                            System.out.println(authorLog + " is looking for " + emote + " emote");
                            for( File f : emotePool){
                                //loop through every emote to see which is the one
                                String filePath = f.getPath();
                                if(emote.equals(filePath.toLowerCase().substring(filePath.lastIndexOf("\\") + 1, filePath.toLowerCase().lastIndexOf(".") ) ) ) {
                                    sleep(10);
                                    message.replyFile(f);
                                    break;
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
                            while (text.contains(" ")){
                                String claps = ":clap:";
                                if (rand.nextInt(10) == 1){
                                    claps = claps+":clap:";
                                }
                                text = text.replace(" ",claps);
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
                        ///////////////////////////////////////
                        ///////////EXPAND LENNY///////////////
                        /////////////////////////////////////
                        String lennycheck = text.toLowerCase();
                        if (lennycheck.contains("lenny") &&!isBot){

                            String chosenLenny = (!lennycheck.contains("salt") ? lenny : pLenny);

                            if (!lennycheck.contains("/ltts")){ //lennies in non-ltts message

                                sleep(100);
                                message.delete();
                                String lennified = authorMention + ": "+ Utility.replace(text,"lenny",chosenLenny); //oh god that lenny
                                message.reply(lennified);

                            }else{ //lennies in ltts messages
                                text = Utility.replace(text,"lenny",chosenLenny);
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

                        ///////////////////////////////////////
                        ////////LONG TTS MESSAGES/////////////
                        /////////////////////////////////////
                        if (text.contains("/ltts ")) {
                            if (!text.startsWith("/ltts ")) {
                                System.out.println("message sent by " + authorLog + " did not go through because ltts needs to be at the beginning");
                            } else {
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
