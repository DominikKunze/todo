package com.webserver.control;

import java.util.Random;

public class GenerateCookieHashCode {
    public String generateHash(int hashLength){
        Random rand = new Random();
        String hash = "";
        do{
            int c = rand.nextInt(123);
            if((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122)){
                hash += (char) c;
            }
        }while (hash.length() <= (hashLength-1));
        return hash;
    }
}
