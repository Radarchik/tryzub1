/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import com.sun.faces.util.LRUMap;
import com.sun.xml.wss.impl.misc.NonceCache;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.descriptor.java.DataHelper;
import org.primefaces.context.RequestContext;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Organization;
import org.primefaces.event.CellEditEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import uk.tryzub.beans.LoginView;
import uk.tryzub.entity.Habitation;
import uk.tryzub.entity.Review;
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

    private boolean isGameStart;
    private boolean isGameFinished;

    public void startRegister() {
        participants = new HashSet<>();
        System.out.println("startRegister!!!!!!!!!!!!");

        endDate = DateUtils.addMinutes(startingDate, 3);

        timerStartGame = new TimerTask() {
            @Override
            public void run() {

                startGame();
            }
        };

        timerEndGame = new TimerTask() {
            @Override
            public void run() {
                System.out.println("timerEndGame " + endDate);
                endGame();
            }
        };

        timer = new Timer();
        timer.schedule(timerStartGame, startingDate);
        timer.schedule(timerEndGame, endDate);
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/reloadPage", "message");
    }

    public void startGame() {
        leaderboard = new LinkedList<>();
        isGameStart = true;
        validWords = new ValidWords();

        System.out.print("Quantity of words: ");
        System.out.println(validWords.h.size());

        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/reloadPage", "message");
        eventBus = null;
        System.out.println("timerStartGame " + startingDate);
    }

    public void endGame() {
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

        leaderboard = null;
        isGameStart = false;
        isGameFinished = false;
        startingWord = "";
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
            return false;
        } finally {
            setWordNull();
        }

    }

    public synchronized int submitWord(String playerName, String word) {

        System.out.println(1111111);
        //1)checking whether all letters in "word" are contained in "wordForGame"
        char[] availableLetters = startingWord.toCharArray();
        char[] proposedLetters = word.toCharArray();

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
            return 0;
        }

        //2)checking whether "word" is valid
        if (!validWords.contains(word)) {
            System.out.println("Такого слова нет!!!");
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

}
