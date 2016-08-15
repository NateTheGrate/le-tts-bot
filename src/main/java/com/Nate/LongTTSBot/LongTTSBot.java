package com.Nate.LongTTSBot;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import java.io.File;
import java.net.URL;
import java.util.Random;

/**
 * Created by Nathanael on 8/12/2016.
 */
public class LongTTSBot {

    private File meme1 = new File("C:\\Users\\Nathanael\\IdeaProjects\\le tts bot\\src\\main\\resources\\images\\meme1.jpg");
    private File meme2 = new File("C:\\Users\\Nathanael\\IdeaProjects\\le tts bot\\src\\main\\resources\\images\\meme2.png");
    private File meme3 = new File("C:\\Users\\Nathanael\\IdeaProjects\\le tts bot\\src\\main\\resources\\images\\meme3.jpg");

    private File secretPogChamp = new File("C:\\Users\\Nathanael\\IdeaProjects\\le tts bot\\src\\main\\resources\\images\\SecretPogChamp.png");
    private File pogChamp = new File("C:\\Users\\Nathanael\\IdeaProjects\\le tts bot\\src\\main\\resources\\images\\PogChamp.jpg");
    private File test = new File("..\\..\\..\\resources\\images\\PogChamp.jpg");


    public LongTTSBot(String token){

        DiscordAPI api = Javacord.getApi(token, true);
        // connect
        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                if( System.currentTimeMillis() % 300000 == 0){ //every 5 mins
                    //working on nadeku flower twitch thingy
                }
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate(DiscordAPI api, Message message) {
                        // check the content of the message
                        String text = message.getContent();
                        //////////////////////////////////////
                        ////////STUPID MEME CRAP/////////////
                        ////////////////////////////////////
                        if(text.toLowerCase().contains(">meme") && !message.getAuthor().isBot() ){
                            int random = new Random().nextInt(3);
                            sleep(100);
                            if( random == 1){
                                message.replyFile(test, "say no more ;)");
                            }else if(random == 2){
                                message.replyFile(test, "this is what you wanted, right?");
                            }else{
                                message.replyFile(test, "look if you wanted something else you should have said something :/");
                            }
                        }
                        ////////////////////////////////////////
                        ////////TWITCH EMOTES?/////////////////
                        ///////////////////////////////////////
                        if(text.toLowerCase().contains("pogchamp") && !message.getAuthor().isBot() ){
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

                            if(text.length() > 140 && !message.getAuthor().isBot() ){
                                System.out.println("/ltts message sent by " + message.getAuthor().getName() ); //logging
                                sleep(100); //have to wait to make sure the messages get sent inorder
                                message.delete();//reduce spam and deleting the messaage here prevents any accidental repeating
                                message.reply("-------------------------------------------------------------------");
                                sleep(1000);
                                message.reply(message.getAuthor().getName() + " made me do this;" + text.substring(text.indexOf("/ltts") + 5, text.indexOf(" ", 140) ), true);

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
                                System.out.println("/ltts message under 140 characters was sent by" + message.getAuthor().getName() );//just some logging
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
            System.out.println("something went wrong with the delay between messages");
        }
    }

}
