/***
 * this class represent a Game Object
 */
package com.mys3soft.mys3chat.Models;

public class Game {
    // mail of player 0
    private String player0;
    //mail of player 1
    private String player1;
    //number of rounds for onr game- round is counting every time video is sent
    private int numOfRounds;
    // not in use today but for futuer versions
    private int numOfSuccessfulFooling;
    // the Catrgory of the Game
    private String category;
    // noy in use today but for future versions for play in multiple language
    private String lang;
    // status of the Game
    private boolean active;
    // id of game
    private int id;

    //global variable to decide after 2 rounds
    public int WAIT_ANS = 0;
    public int WANT_CONT =1;
    public int WANT_GIVE_UP=2;

    // player 0 decision what he want to do after 2 rounds
    private int wantContP0;
    // player 1 decision what he want to do after 2 rounds
    private int wantContP1;
    // which screen player 0 is
    private int screen_player_0;
    // which screen player 1 is
    private int screen_player_1;
    //score payer 0
    private int score_player0;
    // score player 1
    private int score_player1;
    // saving important information for player 0
    private  String m0;
    // saving important information for player 0
    private  String m1;


    /**
     * initial construction
     */
    public Game(String player0, String player1, int numOfRounds,int numOfSuccessfulFooling,String category,String lang, boolean active,int id) {
        this.player0=player0;
        this.player1= player1;
        this.numOfRounds= numOfRounds;
        this.numOfSuccessfulFooling= numOfSuccessfulFooling;
        this.category= category;
        this.lang= lang;
        this.active =active;
        this.id= id;
        this.wantContP0= WAIT_ANS;
        this.wantContP1= WAIT_ANS;
        this.screen_player_0 = 0;
        this.screen_player_1 = 0;
        this.score_player0 =0;
        this.score_player1 =0;
        this.m0="";
        this.m1="";
    }

    /**
     *constructor with all the data
     */
    public Game(String player0, String player1, int numOfRounds,int numOfSuccessfulFooling,String category,String lang, boolean active,int id,int s0, int s1, String ms0, String ms1 ) {
        this.player0=player0;
        this.player1= player1;
        this.numOfRounds= numOfRounds;
        this.numOfSuccessfulFooling= numOfSuccessfulFooling;
        this.category= category;
        this.lang= lang;
        this.active =active;
        this.id= id;
        this.wantContP0= WAIT_ANS;
        this.wantContP1= WAIT_ANS;
        this.screen_player_0 = 0;
        this.screen_player_1 = 0;
        this.score_player0 =s0;
        this.score_player1 =s1;
        if(ms0 == null){
            this.m0="";
        }else{
            this.m0=ms0;
        }
        if(ms1 == null){
            this.m1="";
        }else{
            this.m1=ms1;
        }

    }

    public Game clone(){

        Game game= null;
        try{
            game= (Game)super.clone();
        }catch (CloneNotSupportedException e){
            game= new Game(this.getPlayer0(), this.getPlayer1(),this.getNumOfRounds(),this.getNumOfSuccessfulFooling(),this.getCategory(),this.getLang(),this.active, this.id);

        }
        return game;
    }

    public Game(){

    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPlayer0() {
        return player0;
    }

    public String getPlayer1() {
        return player1;
    }

    public int getNumOfRounds() {
        return numOfRounds;
    }

    public int getNumOfSuccessfulFooling() {
        return numOfSuccessfulFooling;
    }

    public String getCategory() {
        return category;
    }

    public void setNumOfSuccessfulFooling(int numOfSuccessfulFooling) {
        this.numOfSuccessfulFooling = numOfSuccessfulFooling;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getScreen_player_0() {
        return screen_player_0;
    }

    public int getScreen_player_1(){
        return screen_player_1;
    }

    public void setScreen_player_0(int screen_player_0) {
        this.screen_player_0 = screen_player_0;
    }

    public void setScreen_player_1(int screen_player_1) {
        this.screen_player_1 = screen_player_1;
    }

    public int getScore_player0() {
        return score_player0;
    }

    public void setPlayer0(String player0) {
        this.player0 = player0;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setNumOfRounds(int numOfRounds) {
        this.numOfRounds = numOfRounds;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWantContP0() {
        return wantContP0;
    }

    public void setWantContP0(int wantContP0) {
        this.wantContP0 = wantContP0;
    }

    public int getWantContP1() {
        return wantContP1;
    }

    public void setWantContP1(int wantContP1) {
        this.wantContP1 = wantContP1;
    }

    public void setScore_player0(int score_player0) {
        this.score_player0 = score_player0;
    }

    public int getScore_player1() {
        return score_player1;
    }

    public void setScore_player1(int score_player1) {
        this.score_player1 = score_player1;
    }

    public String getM0() {
        return m0;
    }

    public void setM0(String m0) {
        this.m0 = m0;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }
}
