/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.time.DateUtils;

import org.primefaces.context.RequestContext;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import uk.tryzub.entity.RowOfLeaderboard;
import uk.tryzub.entity.User;
import uk.tryzub.validators.ValidWords;

/**
 *
 * @author tszin
 */
@ManagedBean(eager = true)
@ApplicationScoped
public final class ViktorynaHelper implements Serializable {

    private Timer timer;
    private TimerTask timerStartGame;
    private TimerTask timerEndGame;

    private ValidWords validWords;

    private HashSet<User> participants;

    private LinkedList<RowOfLeaderboard> leaderboard;

    private LinkedHashMap<String, Integer> mapOfLeaders;
    private List<String> keyList;

    private String startingWord;
    private Date startingDate;
    private Date endDate;

    private String word;

    private boolean isGameRegister;
    private boolean isGameStart;
    private boolean isGameFinished;
    private boolean isSoundRingNeed;

    private int numberTimer;

    public void startRegister() {
        isGameRegister = true;
        participants = new HashSet<>();
        System.out.println("startRegister!!!!!!!!!!!!");

        endDate = DateUtils.addMinutes(startingDate, 10);
        numberTimer = (int) (endDate.getTime() - startingDate.getTime()) / 1000;

        timerStartGame = new TimerTask() {
            @Override
            public void run() {

                startGame();
            }
        };

        timerEndGame = new TimerTask() {
            @Override
            public void run() {
                System.out.println(numberTimer);
                if (endDate.getTime() > System.currentTimeMillis()) {
                    numberTimer = (int) ((endDate.getTime() - System.currentTimeMillis()) / 1000);
                    return;
                }
                endGame();
            }
        };

        timer = new Timer();
        timer.schedule(timerStartGame, startingDate);
        timer.schedule(timerEndGame, 1000, 1000);
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/reloadPage", "message");
    }

    public void startGame() {
        leaderboard = new LinkedList<>();
        isGameRegister = false;
        isSoundRingNeed = true;
        isGameStart = true;
        validWords = new ValidWords();

        /*
        for (int i = 0; i < 5; i++) {
            startingWord = startingWord + " " + validWords.h.toArray()[new Random().nextInt(validWords.h.size())].toString();
        }
         */
        System.out.print("Quantity of words: ");
        System.out.println(validWords.h.size());

        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/reloadPage", "message");
        eventBus = null;
        System.out.println("timerStartGame " + startingDate);
    }

    public void endGame() {
        timer.cancel();
        timer = null;
        timerStartGame = null;
        timerEndGame = null;
        isSoundRingNeed = false;

        if (!leaderboard.isEmpty()) {
            mapOfLeaders = new LinkedHashMap<>();
            for (int i = 0; i < leaderboard.size(); i++) {
                if (mapOfLeaders.containsKey(leaderboard.get(i).getPlayerName())) {
                    mapOfLeaders.put(leaderboard.get(i).getPlayerName(), mapOfLeaders.get(leaderboard.get(i).getPlayerName()) + leaderboard.get(i).getScore());
                } else {
                    mapOfLeaders.put(leaderboard.get(i).getPlayerName(), leaderboard.get(i).getScore());
                }
            }

            //sorting map by value
            mapOfLeaders = mapOfLeaders.entrySet().stream()
                    .sorted(Entry.comparingByValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));

            keyList = new ArrayList<String>(mapOfLeaders.keySet());

            //making List decreasing
            Collections.reverse(keyList);

            //итеровать эту мапу, если лист больше 2-х - сравнить по доп. показателям и изменить keyList
            Map<Integer, ArrayList<String>> reverseMap = new HashMap<>(
                    mapOfLeaders.entrySet().stream()
                            .collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream()
                            .collect(Collectors.toMap(
                                    item -> item.get(0).getValue(),
                                    item -> new ArrayList<>(
                                            item.stream()
                                                    .map(Map.Entry::getKey)
                                                    .collect(Collectors.toList())
                                    ))
                            ));

            for (Map.Entry entry : reverseMap.entrySet()) {
                if (((ArrayList<String>) entry.getValue()).size() >= 2) {

                    ArrayList<String> sortList = new ArrayList<>();

                    for (int i = 0; i < leaderboard.size(); i++) {
                        for (int j = 0; j < ((ArrayList<String>) entry.getValue()).size(); j++) {
                            if (leaderboard.get(i).getPlayerName().equals(((ArrayList<String>) entry.getValue()).get(j))) {
                                sortList.add(leaderboard.get(i).getPlayerName());
                                ((ArrayList<String>) entry.getValue()).remove(j);
                                break;
                            }

                        }
                    }

                    mainLoop:
                    for (int i = 0; i < keyList.size(); i++) {
                        for (int j = 0; j < sortList.size(); j++) {
                            if (keyList.get(i).equals(sortList.get(j))) {
                                while (sortList.size() > 0) {
                                    keyList.set(i, sortList.get(0));
                                    i++;
                                    sortList.remove(0);
                                }
                                break mainLoop;
                            }

                        }

                    }

                }

            }

            isGameFinished = true;

        }
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/reloadPage", "message");
    }

    public void closeGame() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timerStartGame = null;
        timerEndGame = null;

        leaderboard = null;
        isGameStart = false;
        isGameFinished = false;
        isSoundRingNeed = false;
        startingWord = null;
        startingDate = null;
        participants = null;
        validWords = null;

        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/reloadPage", "message");
        eventBus = null;

    }

    public void register(User user) {
        if (!participants.contains(user) && isGameStart == false) {
            participants.add(user);
        }
    }

    public void setWordNull() {
        word = "";
        RequestContext.getCurrentInstance().update("gameField:word");
    }

    public synchronized boolean addWordToLeaderboard(String playerName) {
        try {
            //checking whether word is valid
            int score = submitWord(playerName, word);

            if (score == 0) {
                return false;
            }

            //checking whether leaderboard already contains this word
            for (RowOfLeaderboard row : leaderboard) {
                if (word.equals(row.getWord())) {

                    FacesMessage message1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Упсс", "Дане слово уже є на дошці :-(");
                    RequestContext.getCurrentInstance().showMessageInDialog(message1);
                    return false;
                }
            }

            //creating a row of leaderboard
            RowOfLeaderboard rowOfLeaderboard = new RowOfLeaderboard(playerName, word, score);

            // checking whether word has score enough for leaderboard.
            // if yes - adding to appropriate position
            for (int i = 0; i < 10; i++) {
                if (i == leaderboard.size() || score > leaderboard.get(i).getScore()) {
                    /*jvm do not check second condition if first is true, so Index out of bound Exception will never happened*/
                    leaderboard.add(i, rowOfLeaderboard);
                    // keeping only 10 positions in leaderboard
                    if (leaderboard.size() > 10) {
                        leaderboard.remove(10);
                    }
                    EventBus eventBus = EventBusFactory.getDefault().eventBus();
                    eventBus.publish("/updateGame", "message");
                    eventBus = null;
                    return true;
                }
            }

            //if word was not added
            FacesMessage message1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Упсс", "На дошці вже немає місця для цього слова :-(");
            RequestContext.getCurrentInstance().showMessageInDialog(message1);
            return false;
        } finally {
            setWordNull();
        }

    }

    public synchronized int submitWord(String playerName, String word) {
       
        //1)checking whether all letters in "word" are contained in "wordForGame"
        char[] availableLetters = startingWord.toLowerCase().toCharArray();
        char[] proposedLetters = word.toLowerCase().toCharArray();

        int count = 0;

        for (char x : proposedLetters) {
            for (int i = 0; i < availableLetters.length; i++) {
                if (x == availableLetters[i]) {
                    count++;
                    /*task says that words do not contain whitespace.
                  So I am using it for "deleting" already used letters  */
                    availableLetters[i] = ' ';
                    break;
                }
            }
        }

        if (count != word.length()) {

            FacesMessage message1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Упсс", "Слово містить літери, яких немає у реченні :-(");
            RequestContext.getCurrentInstance().showMessageInDialog(message1);
            return 0;
        }

        //2)checking whether "word" is valid
        if (!validWords.contains(word)) {
            FacesMessage message1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Упсс", "Такого слова немає у словнику :-(");
            RequestContext.getCurrentInstance().showMessageInDialog(message1);
            return 0;
        }

        return count;
    }

    public LinkedList<RowOfLeaderboard> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(LinkedList<RowOfLeaderboard> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public String getStartingWord() {
        return startingWord;
    }

    public void setStartingWord(String startingWord) {
        this.startingWord = startingWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public HashSet<User> getParticipants() {
        return participants;
    }

    public void setPaticipants(HashSet<User> participants) {
        this.participants = participants;
    }

    public boolean isIsGameStart() {
        return isGameStart;
    }

    public void setIsGameStart(boolean isGameStart) {
        this.isGameStart = isGameStart;
    }

    public boolean isIsGameFinished() {
        return isGameFinished;
    }

    public void setIsGameFinished(boolean isGameFinished) {
        this.isGameFinished = isGameFinished;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public boolean isIsGameRegister() {
        return isGameRegister;
    }

    public void setIsGameRegister(boolean isGameRegister) {
        this.isGameRegister = isGameRegister;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public TimerTask getTimerStartGame() {
        return timerStartGame;
    }

    public void setTimerTask(TimerTask timerTask1) {
        this.timerStartGame = timerTask1;
    }

    public TimerTask getTimerEndGame() {
        return timerEndGame;
    }

    public void setTimerEndGame(TimerTask timerEndGame) {
        this.timerEndGame = timerEndGame;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LinkedHashMap<String, Integer> getMapOfLeaders() {
        return mapOfLeaders;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public ValidWords getValidWords() {
        return validWords;
    }

    public void setValidWords(ValidWords validWords) {
        this.validWords = validWords;
    }

    public int getNumberTimer() {
        return numberTimer;
    }

    public void setNumberTimer(int numberTimer) {
        this.numberTimer = numberTimer;
    }

    public boolean isIsSoundRingNeed() {
        return isSoundRingNeed;
    }

    public void setIsSoundRingNeed(boolean isSoundRingNeed) {
        this.isSoundRingNeed = isSoundRingNeed;
    }

}
