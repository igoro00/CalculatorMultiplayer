package com.example.calculatormultiplayer;

public class IpToFillFormat {
    public String format(String input){
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
}
