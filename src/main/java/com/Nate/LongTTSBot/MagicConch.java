package com.Nate.LongTTSBot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by iWasHere on 8/15/2016.
 */
public class MagicConch {

    public static String ask(String question, Random random){

        System.out.println(LongTTSBot.sender.getName() + " asked: " + question +"?");

        List<String> specials = new ArrayList<>();
        specials.add("can i have something to eat");
        specials.add("cant you say anything else but no");
        specials.add("could i have something to eat");
        specials.add("no mans sky is gonna b goty ill chop ur head off bithc how u like that");
        String reply = "";

        switch (random.nextInt(10)){
            default: reply = "No."; break;
            case 1: reply = "Try asking again."; break;
            case 2: reply = "Definitely not."; break;
            case 3: reply = "Maybe."; break;
            case 4: reply = "Probably not."; break;
            case 5: reply = "Probably."; break;
            case 6: reply = "Absolutely."; break;
            case 7: reply = "Yes."; break;
        }

        Iterator iterator = specials.iterator();
        while (iterator.hasNext()) {
            if (question.toLowerCase().contains((String)iterator.next())){
                reply = "No.";
            }
        }

        return reply;

    }

}
