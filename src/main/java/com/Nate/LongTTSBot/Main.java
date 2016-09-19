package com.Nate.LongTTSBot;

import de.btobastian.javacord.utils.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * Created by Nathanael on 8/12/2016.
 */
public class Main {

    private static File token = new File("token.txt");

    public static void main(String[] args){
        try {
            LongTTSBot test = new LongTTSBot(Utility.getTextinTextFile(token));
            System.out.println("Bot booted up");
        }catch(IOException e){
            //text file probably doesn't exists
            System.out.println("token file not found please add a text file called 'token.txt' with the token from discord in the root folder of the bot");
        }
    }
}
