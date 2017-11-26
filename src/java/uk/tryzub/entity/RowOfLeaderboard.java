/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.entity;

/**
 *
 * @author tszin
 */
public class RowOfLeaderboard implements java.io.Serializable {

    private String playerName;
    private String word;
    private int score;

    public RowOfLeaderboard(String playerName, String word, int score) {
        this.playerName = playerName;
        this.word = word;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
