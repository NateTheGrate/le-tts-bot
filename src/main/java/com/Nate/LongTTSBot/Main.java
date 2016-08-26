package com.Nate.LongTTSBot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Nathanael on 8/12/2016.
 */
public class Main {
    public static void main(String[] args){
        String token = "MjEzNTU2Mzc4MDUxMDE4NzUz.Co8Pbw.AGzY-ZE-22KDil3IGsBJWePgkPw";
        LongTTSBot test = new LongTTSBot(token);

        System.out.println("Bots booted up");
    }
}
