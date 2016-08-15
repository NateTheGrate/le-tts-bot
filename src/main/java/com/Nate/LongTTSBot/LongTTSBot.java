package com.Nate.LongTTSBot;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
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


    private File secretPogChamp = new File("src\\main\\resources\\images\\SecretPogChamp.png");
    private File pogChamp = new File("src\\main\\resources\\images\\PogChamp.jpg");

    protected File memeFolder = new File("src\\main\\resources\\images\\memes");
    protected File memeQuips = new File("src\\main\\resources\\text\\memes.txt");


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

                        Random rand = new Random();

                        //Get the message's author
                        String author = message.getAuthor().getName();

                        // check the content of the message
                        String text = message.getContent();

                        //Check if the message is sent by a bot
                        boolean isBot = message.getAuthor().isBot();

                        //////////////////////////////////////
                        ////////STUPID MEME CRAP/////////////
                        ////////////////////////////////////
                        if(text.toLowerCase().contains(">meme") && !isBot ){
                            System.out.println("Request for a meme.");
                            sleep(100);

                            //Get the image
                            File[] memes = FileHelper.getFilesInFolder(memeFolder);
                            File file = memes[rand.nextInt(memes.length)];

                            //Get the string
                            String quip;
                            try {
                                List<String> strings = FileHelper.getLinesInTextFile(memeQuips);
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
                        if(text.toLowerCase().contains("pogchamp") && !isBot ){
                            int randomPog = new Random().nextInt(69);
                            sleep(100);
                            if( randomPog == 30){
                                message.replyFile(secretPogChamp);
                            }else{
                                message.replyFile(pogChamp);
                            }
                        }
                        ///////////////////////////////////////
                        ////////LONG TTS MESSAGES/////////////
                        /////////////////////////////////////
                        if (text.contains("/ltts ")) {

                            if(text.length() > 140 && !isBot ){
                                System.out.println("/ltts message sent by " + author ); //logging
                                sleep(100); //have to wait to make sure the messages get sent inorder
                                message.delete();//reduce spam and deleting the message here prevents any accidental repeating

                                message.reply("-------------------------------------------------------------------");
                                sleep(1000);
                                message.reply("@"+author + " made me do this;" + text.substring(text.indexOf("/ltts") + 5, text.indexOf(" ", 140) ), true);

                                String result = "";
                                //splitting up the message
                                for(int i = text.indexOf(" ", 140); i < text.length(); i++) {

                                    result += text.substring(i, i+1); //char by char to avoid index out of bounds

                                    if(result.length() >= 140 && (result.endsWith(" ")) ) {

                                        sleep(1000);
                                        message.reply(result, true);
                                        result = "";
                                    }

                                }
                                //send the remainder of the message
                                if(result.length() != 0) {
                                    sleep(1000);
                                    message.reply(result, true);
                                    sleep(100);
                                    message.reply("-------------------------------------------------------------------");
                                }

                            //short ltts calls
                            }else {
                                System.out.println("/ltts message under 140 characters was sent by " + message.getAuthor().getName() );//just some logging
                                message.delete();
                                message.reply("-------------------------------------------------------------------");
                                sleep(100);
                                message.reply("The ltts command is used for messages over 140 characters, use the tts command for shorter messages");
                                sleep(100);
                                message.reply("-------------------------------------------------------------------");
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
