/*
 * Copyright (C) 2016  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package be.kuleuven.drsolitaire.helper;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.Card;
import be.kuleuven.drsolitaire.classes.GamePlayed;
import be.kuleuven.drsolitaire.classes.Move;
import be.kuleuven.drsolitaire.classes.MoveType;
import be.kuleuven.drsolitaire.classes.Stack;
import be.kuleuven.drsolitaire.database.EntityMapper;
import be.kuleuven.drsolitaire.games.Klondike;
import be.kuleuven.drsolitaire.ui.GameManager;

import static be.kuleuven.drsolitaire.SharedData.DEFAULT_AUTO_START_NEW_GAME;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_FIRST_RUN;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_MOVED_FIRST_CARD;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_WON;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_WON_AND_RELOADED;
import static be.kuleuven.drsolitaire.SharedData.GAME_FIRST_RUN;
import static be.kuleuven.drsolitaire.SharedData.GAME_MOVED_FIRST_CARD;
import static be.kuleuven.drsolitaire.SharedData.GAME_NUMBER_OF_PLAYED_GAMES;
import static be.kuleuven.drsolitaire.SharedData.GAME_NUMBER_OF_WON_GAMES;
import static be.kuleuven.drsolitaire.SharedData.GAME_RANDOM_CARDS;
import static be.kuleuven.drsolitaire.SharedData.GAME_WON;
import static be.kuleuven.drsolitaire.SharedData.GAME_WON_AND_RELOADED;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_AUTO_START_NEW_GAME;
import static be.kuleuven.drsolitaire.SharedData.TIMER_END_TIME;
import static be.kuleuven.drsolitaire.SharedData.animate;
import static be.kuleuven.drsolitaire.SharedData.autoComplete;
import static be.kuleuven.drsolitaire.SharedData.cards;
import static be.kuleuven.drsolitaire.SharedData.currentGame;
import static be.kuleuven.drsolitaire.SharedData.gameLogic;
import static be.kuleuven.drsolitaire.SharedData.getBoolean;
import static be.kuleuven.drsolitaire.SharedData.getEntityMapper;
import static be.kuleuven.drsolitaire.SharedData.getInt;
import static be.kuleuven.drsolitaire.SharedData.getIntArray;
import static be.kuleuven.drsolitaire.SharedData.getIntList;
import static be.kuleuven.drsolitaire.SharedData.getLong;
import static be.kuleuven.drsolitaire.SharedData.getLongArrayList;
import static be.kuleuven.drsolitaire.SharedData.getSharedBoolean;
import static be.kuleuven.drsolitaire.SharedData.movingCards;
import static be.kuleuven.drsolitaire.SharedData.putBoolean;
import static be.kuleuven.drsolitaire.SharedData.putInt;
import static be.kuleuven.drsolitaire.SharedData.putIntArray;
import static be.kuleuven.drsolitaire.SharedData.putIntList;
import static be.kuleuven.drsolitaire.SharedData.putLong;
import static be.kuleuven.drsolitaire.SharedData.putLongArrayList;
import static be.kuleuven.drsolitaire.SharedData.recordList;
import static be.kuleuven.drsolitaire.SharedData.scores;
import static be.kuleuven.drsolitaire.SharedData.sounds;
import static be.kuleuven.drsolitaire.SharedData.stacks;
import static be.kuleuven.drsolitaire.SharedData.timer;
import static be.kuleuven.drsolitaire.SharedData.winnableGameList;

/**
 * Contains stuff for the game which i didn't know where i should put it.
 */

public class GameLogic {

    public Card[] randomCards;                                                                      //array to shuffle the cards
    private int numberWonGames;                                                                     //number of won games. It's shown in the high score activity
    private boolean won, wonAndReloaded;                                                            //shows if the player has won, needed to know if the timer can stop, or to deal new cards on game start
    private GameManager gm;
    private boolean movedFirstCard = false;
    private EntityMapper entityMapper = SharedData.getEntityMapper();
    private boolean dataSent = false;


    public GameLogic(GameManager gm) {
        this.gm = gm;
    }

    /**
     * checks if the first card of a game has been moved, if so, increment the number of played games
     */
    public void checkFirstMovement() {
        if (!movedFirstCard) {
            incrementPlayedGames();
            movedFirstCard = true;
        }
    }

    /**
     * saves all relevant data of the current game in shared preferences, so it can be loaded
     * when resuming the game, called in onPause() of the GameManager
     */
    public void save() {
        scores.save();
        recordList.save();
        putBoolean(GAME_WON, won);
        putBoolean(GAME_WON_AND_RELOADED, wonAndReloaded);
        putBoolean(GAME_MOVED_FIRST_CARD, movedFirstCard);
        putInt(GAME_NUMBER_OF_WON_GAMES, numberWonGames);
        putInt("SEED", currentGame.getGameseed());
        putLong("SCORE", currentGame.getScore()); //TODO BELANGRIJK HIER MOVES STOREN
        // Timer will be saved in onPause()
        for (Stack stack : stacks)
            stack.save();

        Card.save();
        saveRandomCards();
        currentGame.save();
        currentGame.saveRecycleCount();
    }


    public void setWonAndReloaded(){
        if (won){
            wonAndReloaded = true;
        }
    }

    /**
     * load everything saved on start of a game. If the last game has been won put every card
     * outside the screen.
     * The main loading part is put in a try catch block, so when there goes something wrong
     * on saving/loading, it won't crash the game. (in that case, it loads a new game)
     */
    public void load() {

        boolean firstRun = getBoolean(GAME_FIRST_RUN, DEFAULT_FIRST_RUN);
        numberWonGames = getInt(GAME_NUMBER_OF_WON_GAMES, 0);
        won = getBoolean(GAME_WON, DEFAULT_WON);
        wonAndReloaded = getBoolean(GAME_WON_AND_RELOADED, DEFAULT_WON_AND_RELOADED);
        movedFirstCard = getBoolean(GAME_MOVED_FIRST_CARD, DEFAULT_MOVED_FIRST_CARD);

        // @GN loading created counters when game is booted up
        currentGame.setScore(getLong(SharedData.SCORE, -1));
        currentGame.setGameseed(getInt("SEED", -1)); //TODO BELANGRIJK HIER MOVES STOREN

        //update and reset
        Card.updateCardDrawableChoice();
        Card.updateCardBackgroundChoice();
        animate.reset();
        autoComplete.reset();
        currentGame.load();
        currentGame.loadRecycleCount(gm);
        sounds.playSound(Sounds.names.DEAL_CARDS);

        try {
            if (firstRun) {
                newGame();
                putBoolean(GAME_FIRST_RUN, false);
            }  else if (wonAndReloaded && getSharedBoolean(PREF_KEY_AUTO_START_NEW_GAME,DEFAULT_AUTO_START_NEW_GAME)){        //in case the game was selected from the main menu and it was already won, start a new game
                newGame();
            } else if (won) {                   //in case the screen orientation changes, do not immediately start a new game
                loadRandomCards();

                for (Card card : cards) {
                    card.setLocationWithoutMovement(gm.layoutGame.getWidth(), 0);
                }
            } else {
                for (Card card : cards) {
                    card.setLocationWithoutMovement(currentGame.getDealStack().getX(), currentGame.getDealStack().getY());
                    card.flipDown();
                }

                scores.load();
                recordList.load();
                timer.setCurrentTime(getLong(TIMER_END_TIME, 0));

                //timer will be loaded in onResume() of the game manager

                Card.load();

                for (Stack stack : stacks) {
                    stack.load();
                }

                loadRandomCards();

                if (!autoComplete.buttonIsShown() && currentGame.autoCompleteStartTest()) {
                    autoComplete.showButtonWithoutSound();
                }
            }
        } catch (Exception e) {
            Log.e(gm.getString(R.string.loading_data_failed), e.toString());
            gm.showToast(gm.getString(R.string.game_load_error));
            newGame();
        }
    }

    /**
     * starts a new game. The only difference to a re-deal is the shuffling of the cards
     */
    public void newGame() {
        // @GN
        if (currentGame.getMoves().size()!=0) createGamePlayed(); // Must touch one card to start a game
        dataSent=true;

        System.arraycopy(cards, 0, randomCards, 0, cards.length);
        randomize(randomCards);

        redeal();
    }

//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) gm.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    /**
     * starts a new game, but with the same deal.
     */
    public void redeal() {
        //reset EVERYTHING

        if (!dataSent && currentGame.getMoves().size()!=0) {
            createGamePlayed();
            dataSent=true;
        }

        if (!won) {                                                                                 //if the game has been won, the score was already saved
            scores.addNewHighScore();
        }

        movedFirstCard = false;
        won = false;
        wonAndReloaded = false;
        dataSent=false;
        currentGame.reset(gm);
        sounds.playSound(Sounds.names.DEAL_CARDS);

        animate.reset();
        scores.reset();
        movingCards.reset();
        recordList.reset();
        timer.reset();
        autoComplete.hideButton();
        dataSent = false;
        Klondike.resetAllChecks();

        for (Stack stack : stacks)
            stack.reset();

        //Put cards to the specified "deal from" stack. (=main stack if the game has one, else specify it in the game
        for (Card card : randomCards) {
            card.setLocationWithoutMovement(currentGame.getDealStack().getX(), currentGame.getDealStack().getY());
            currentGame.getDealStack().addCard(card);
            card.flipDown();
        }

        resetAllPlayerActions();
        gm.resetTappedCard();
        //and finally deal the cards from the game!
        currentGame.dealCards();
    }


    private void createGamePlayed() {
        GamePlayed game = new GamePlayed(SharedData.user.getId(),
                (int) timer.getCurrentTime(),
                won,
                SharedData.getInt(SharedData.GAME_SEED,-1),
                SharedData.scores.getScore());

        entityMapper.getgMapper().createGame(game,getAllMoves());
        new SaveGameInDB().execute();
    }

    //KG Moves: hier gemaakt
    public List<Move> getAllMoves() {
        //TODO IMP HIER ALLES HALEN VANUIT CURRENTGAME, MAAR DAN ALS OBJECTEN
        List<Move> moves = currentGame.getMoves();

        return currentGame.getMoves();
    }

    // @GN
    // reset all the counters and arrayLists
    private void resetAllPlayerActions() {
        currentGame.resetAllMoveCounters();
    }

    private class SaveGameInDB extends AsyncTask<Void, Void, GamePlayed> {
        protected GamePlayed doInBackground(Void... voids) {
            GamePlayed game = new GamePlayed();
            while (!entityMapper.isDataReady() && !entityMapper.isErrorHappened()) {
                if (isCancelled()) break;
            }
            SharedData.getEntityMapper().errorHandled();
            if (entityMapper.isDataReady()) {
                game = getEntityMapper().game;
                getEntityMapper().dataGrabbed();
            }
            return game;
        }
    }

    /**
     * in case the current game is won: save the score and start the win animation. The record list
     * is reseted, so the player can't revert card movements after the animation
     */
    public void testIfWon() {
        if (!won && !autoComplete.isRunning() && currentGame.winTest()) {
            numberWonGames++;
            scores.updateBonus();
            scores.addNewHighScore();
            recordList.reset();
            timer.setWinningTime();
            autoComplete.hideButton();
            animate.winAnimation();
            won = true;
            putInt("NUMBERWONGAMES", numberWonGames);
        }
    }

    /**
     * Randomizes a given card array using the Fisher–Yates shuffle
     *
     * @param array The array to randomize
     */
    private void randomize(Card[] array) {
        gameLogic.updateMenuBar();
        //@KG Games are fixed here
        int index;
        Card dummy;
        Random rand;

        if(SharedData.getInt("gamecounter",0)>=winnableGameList.size()){SharedData.putInt("gamecounter", 0);} //@kg remove this to also play random games
        if(SharedData.getInt("gamecounter",0)<winnableGameList.size())
        {
            int gameseedposition = SharedData.getInt("gamecounter",0);
            rand = new Random(SharedData.winnableGameList.get(gameseedposition));
            SharedData.putInt(SharedData.GAME_SEED,SharedData.winnableGameList.get(gameseedposition));
            Log.i("SEED", "Now using "+ SharedData.winnableGameList.get(gameseedposition) + ". Playing game "+ (gameseedposition+1));
            SharedData.putInt("gamecounter", (SharedData.getInt("gamecounter",0)+1)); //increase gamecounter after every shuffle
            gm.mainTextViewGameNumber.post(() -> gm.mainTextViewGameNumber.setText("Game "+ (SharedData.getInt("gamecounter",0))));
        }
        else
        {
            rand = new Random();
            Log.i("SEED", "Now using a random seed");
            gm.showToast("Now using a random seed");

        }

        for (int i = array.length - 1; i > 0; i--) {
            if ((index = rand.nextInt(i + 1)) != i) {
                dummy = array[i];
                array[i] = array[index];
                array[index] = dummy;
            }
        }
    }

    /**
     * for left handed mode: mirrors the stacks to the other side and then updates the card
     * positions.
     */
    public void mirrorStacks() {


        if (stacks != null) {
            for (Stack stack : stacks) {
                stack.mirrorStack(gm.layoutGame);
            }
        }

        //move the re-deal counter too
        if (currentGame.hasLimitedRecycles()) {
            gm.mainTextViewRecycles.setX(currentGame.getMainStack().getX());
            gm.mainTextViewRecycles.setY(currentGame.getMainStack().getY());
        }

        //change the arrow direction
        if (currentGame.hasArrow()) {
            for (Stack stack : stacks) {
                stack.applyArrow();
            }
        }
    }

    /**
     * toggle the redeal counter: From enabled to disabled and vice versa. When enabled, the location
     * is also updated.
     */
    public void toggleRecycles() {
        currentGame.toggleRecycles();

        if (currentGame.hasLimitedRecycles()) {
            gm.mainTextViewRecycles.setVisibility(View.VISIBLE);
            gm.mainTextViewRecycles.setX(currentGame.getMainStack().getX());
            gm.mainTextViewRecycles.setY(currentGame.getMainStack().getY());
        } else {
            gm.mainTextViewRecycles.setVisibility(View.GONE);
        }
    }

    public void setNumberOfRecycles(String key, String defaultValue){
        currentGame.setNumberOfRecycles(key,defaultValue);

        gm.updateNumberOfRecycles();
    }

    public boolean hasWon() {
        return won;
    }

    public int getNumberWonGames() {
        return numberWonGames;
    }

    public void deleteStatistics() {
        numberWonGames = 0;
        putInt(GAME_NUMBER_OF_PLAYED_GAMES, 0);
    }

    private void saveRandomCards() {
        ArrayList<Integer> list = new ArrayList<>();

        for (Card card : randomCards)
            list.add(card.getId());

        putIntList(GAME_RANDOM_CARDS, list);
    }

    private void loadRandomCards() {
        ArrayList<Integer> list = getIntList(GAME_RANDOM_CARDS);

        for (int i = 0; i < randomCards.length; i++)
            randomCards[i] = cards[list.get(i)];
    }

    private void incrementPlayedGames() {
        int playedGames = getInt(GAME_NUMBER_OF_PLAYED_GAMES, numberWonGames);
        putInt(GAME_NUMBER_OF_PLAYED_GAMES, ++playedGames);
    }

    public int getNumberOfPlayedGames() {
        return getInt(GAME_NUMBER_OF_PLAYED_GAMES, numberWonGames);
    }

    public void updateMenuBar() {
        gm.updateMenuBar();
    }

    /*
    Function is never used
    public void incrementNumberWonGames(){
        numberWonGames++;
    }

    */
}