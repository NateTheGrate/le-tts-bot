package com.Nate.LongTTSBot;

import de.btobastian.javacord.entities.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by iWasHere on 8/15/2016.
 */
public class MagicConch {

    public int timesAsked;
    public String toBully;

    public MagicConch(){
        this.timesAsked = 0;
        this.toBully = "78698293193879552";
    }

    public String ask(String question, Random random){

        User sender = LongTTSBot.sender;

        System.out.println(sender.getName() + " asked: " + question +"?");

        List<String> specials = new ArrayList<>();
        specials.add("can i have something to eat");
        specials.add("cant you say anything else but no");
        specials.add("could i have something to eat");
        specials.add("no mans sky is gonna b goty ill chop ur head off bithc how u like that");
        specials.add("hype");
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

        if (sender.getId().equals(this.toBully)){ //no slares allowed
            this.timesAsked++;
            reply = "Try asking again.";

            if (this.timesAsked % 5 == 0){
                reply = "We don't serve your kind here.";
            }
        }

        return reply;

    }

}
