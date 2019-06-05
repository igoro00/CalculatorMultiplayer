package com.example.calculatormultiplayer;

public class Formatters {
    public String formatIP(String input){
        String output="";
        int dots = 0;
        for(int i=0; i<input.length(); i++){
            output += input.charAt(i);
            if(input.charAt(i)=='.'){
                dots++;
            }
            if(dots>=3){
                break;
            }
        }
        return output;
    }

    public String formatBackspace(String input){
        if(input.length()>0) {
            String output = "";
            int max = input.length() - 1;
            if (input.endsWith("\uD83D\uDE02")) {
                max--;
            }
            for (int i = 0; i < max; i++) {
                output += input.charAt(i);
            }
            return output;
        }
        else{
            return "";
        }
    }
}
