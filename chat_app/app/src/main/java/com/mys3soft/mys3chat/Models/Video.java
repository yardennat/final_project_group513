/**
 * this class represent a video
 */
package com.mys3soft.mys3chat.Models;

public class Video {
    // name of video all the names are video/ver+id of game+_+number of rounds+".mp4"
    public String name_video;
    // if true or false
    public boolean type;
    // if guess was true or not
    public int succed_to_guess;
    // ho sent the video
    public String from;
    // who suppose to get the video
    public String to;
    // not in use, for future versions
    public int answer;
    // global variebels
    public  int WAIT = 0;
    public  int TRUE = 1;
    public int FALSE= 2;


    public Video(String path, boolean type, String from, String to){
        this.name_video = path;
        this.type =type;
        this.succed_to_guess=0;
        this.from= from;
        this.to= to;
        this.answer = WAIT;
    }
    public Video(){}

}
