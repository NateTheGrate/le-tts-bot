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

    protected File godFolder = new File("src\\main\\resources\\images\\extinct");

    public static User sender = null;

    public LongTTSBot(String token){

        DiscordAPI api = Javacord.getApi(token, true);
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

                        // check the content of the message
                        String text = message.getContent();

                        //Check if the message is sent by a bot
                        boolean isBot = message.getAuthor().isBot();

                        //////////////////////////////////////
                        ////////TUMBLRIZE ME, CAPN///////////
                        ////////////////////////////////////
                        if (text.toLowerCase().startsWith("/tumblrize ") && !isBot){
                            System.out.println("Request for tumblrization");
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
                            System.out.println("Request for a meme.");
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
                        ////////////////////////////////////////
                        ////////TWITCH EMOTES?/////////////////
                        ///////////////////////////////////////
                        //pogchamp
                        if(text.toLowerCase().contains("pogchamp") && !isBot ){
                            int randomPog = rand.nextInt(69);
                            sleep(10);
                            if( randomPog == 30){
                                message.replyFile(secretPogChamp);
                            }else{
                                message.replyFile(pogChamp);
                            }
                        }
                        //dab
                        if(text.toLowerCase().contains(("dab")) && !isBot) {
                            if (!text.toLowerCase().contains("boi")) {
                                int randomDab = rand.nextInt(2);
                                sleep(10);
                                if (randomDab == 1) {
                                    message.replyFile(squidDab);
                                } else {

                                    message.replyFile(dab);
                                }
                            }else{
                                sleep(10);
                                //its dab boi!!!
                                message.replyFile(new File("src\\main\\resources\\images\\emotes\\secret\\dabboi.jpg"));
                                //o shit waddup
                            }
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
                            System.out.println("Set bully target to: '"+conch.toBully+"'");
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
                                System.out.println("message sent by " + author + " did not go through because ltts needs to be at the begginning");
                            } else {
                                if (text.length() > 140 && !isBot) {
                                    System.out.println("/ltts message sent by " + author); //logging
                                    sleep(100); //have to wait to make sure the messages get sent inorder
                                    message.delete();//reduce spam and deleting the message here prevents any accidental repeating

                                    message.reply("-------------------------------------------------------------------");
                                    sleep(500);
                                    message.reply(authorMention + " made me do this;" + text.substring(text.indexOf("/ltts") + 5, text.indexOf(" ", 140)), true);
                                    //spitting out the split up strings
                                    ArrayList<String> splits = Utility.split(text.substring(text.indexOf(" ", 140)), 140);
                                    for (String s : splits) {
                                        sleep(500);
                                        message.reply(s, true);
                                    }
                                    sleep(500);
                                    message.reply("-------------------------------------------------------------------");

                                    //short ltts calls
                                } else {
                                    System.out.println("/ltts message under 140 characters was sent by " + message.getAuthor().getName());//just some logging
                                    sleep(100);
                                    message.delete();
                                    message.reply("-------------------------------------------------------------------");
                                    sleep(100);
                                    message.reply("The ltts command is used for messages over 140 characters, use the tts command for shorter messages");
                                    sleep(100);
                                    message.reply("-------------------------------------------------------------------");
                                }
                            }
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
