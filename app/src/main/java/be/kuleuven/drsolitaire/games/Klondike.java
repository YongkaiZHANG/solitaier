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

package be.kuleuven.drsolitaire.games;

import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.Card;
import be.kuleuven.drsolitaire.classes.CardAndStack;
import be.kuleuven.drsolitaire.classes.Move;
import be.kuleuven.drsolitaire.classes.MoveType;
import be.kuleuven.drsolitaire.classes.Stack;
import be.kuleuven.drsolitaire.ui.GameManager;

import static be.kuleuven.drsolitaire.SharedData.DEFAULT_KLONDIKE_DRAW;
import static be.kuleuven.drsolitaire.SharedData.OPTION_NO_RECORD;
import static be.kuleuven.drsolitaire.SharedData.OPTION_REVERSED_RECORD;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_KLONDIKE_DRAW;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_KLONDIKE_DRAW_OLD;
import static be.kuleuven.drsolitaire.SharedData.autoComplete;
import static be.kuleuven.drsolitaire.SharedData.gameLogic;
import static be.kuleuven.drsolitaire.SharedData.getSharedString;
import static be.kuleuven.drsolitaire.SharedData.hint;
import static be.kuleuven.drsolitaire.SharedData.min;
import static be.kuleuven.drsolitaire.SharedData.moveToStack;
import static be.kuleuven.drsolitaire.SharedData.movingCards;
import static be.kuleuven.drsolitaire.SharedData.putSharedString;
import static be.kuleuven.drsolitaire.SharedData.recordList;
import static be.kuleuven.drsolitaire.SharedData.sharedStringEquals;
import static be.kuleuven.drsolitaire.SharedData.sharedStringEqualsDefault;
import static be.kuleuven.drsolitaire.SharedData.stacks;
import static be.kuleuven.drsolitaire.SharedData.timer;
import static be.kuleuven.drsolitaire.games.Game.testMode.ALTERNATING_COLOR;
import static be.kuleuven.drsolitaire.games.Game.testMode.SAME_FAMILY;
import static be.kuleuven.drsolitaire.games.Game.testMode2.SAME_VALUE_AND_COLOR;
import static be.kuleuven.drsolitaire.games.Game.testMode3.ASCENDING;
import static be.kuleuven.drsolitaire.games.Game.testMode3.DESCENDING;

/**
 * Klondike game! This game has 7 tableau stacks, 4 foundation fields,
 * 1 main stack and 3 discard stacks. The 3 discard stacks are for the "deal3" option. if it's
 * set to "deal1", the last discard stack will be used
 */

public class Klondike extends Game {
    //@KG
    private int gameseed = -1;
    private long score;

    protected String PREF_KEY_DRAW_OLD, PREF_KEY_DRAW, DEFAULT_DRAW;



    public static boolean getError() {
        return wrongColorBoolean||wrongNumberBoolean;
    }


    public static boolean wrongColorBoolean;
    public static boolean wrongNumberBoolean;
    public static boolean kingBetaError;
    public static boolean aceBetaError;
    public static boolean betaError;
    public static boolean noAceOnSuitError;
    public static boolean noKingOnBuildStackError;


    public static boolean tempwrongColorBoolean;
    public static boolean tempwrongNumberBoolean;
    public static boolean tempkingBetaError;
    public static boolean tempaceBetaError;
    public static boolean tempbetaError;
    public static boolean tempnoAceOnSuitError;
    public static boolean tempnoKingOnBuildStackError;



    // @GN
    private static boolean moveAvailable = false;

    private static boolean mainstack = false;
    private Date currentTime = Calendar.getInstance().getTime();

    // @GN
    private int[] stackCounter = new int[15]; // 15 because when you draw 3 cards, you have 2 more "discard" stacks

    private static LinkedList<Move> moves;

    public static boolean isBetaError() {
        return betaError;
    }

    public static void setBetaError(boolean betaError) {
        Klondike.betaError = betaError;
    }

    public static boolean isAceBetaError() {
        return aceBetaError;
    }

    public static void setAceBetaError(boolean aceBetaError) {
        Klondike.aceBetaError = aceBetaError;
    }

    public static boolean isKingBetaError() {
        return kingBetaError;
    }

    public static void setKingBetaError(boolean kingBetaError) {
        Klondike.kingBetaError = kingBetaError;
    }

    public static boolean isNoAceOnSuitError() {
        return noAceOnSuitError;
    }

    public static void setNoAceOnSuitError(boolean noAceOnSuitError) {
        Klondike.noAceOnSuitError = noAceOnSuitError;
    }

    public  LinkedList<Move> getMoves() {
        return moves;
    }


    public static MoveType getLastMove() {
        return lastMove;
    }

    public static void setLastMove(MoveType lastMove) {
        Klondike.lastMove = lastMove;
    }

    private static MoveType lastMove = MoveType.MOVESTART;

     public static void AddMoveTypeError(MoveType type,  int originstack, int destinationstack, String origincard, String destinationcard, double accuracy, int numberOfCardsMoved,float xCoordinate,float yCoordinate)
     {

     }

    public static void addMove(MoveType type,  int originstack, int destinationstack, String origincard, String destinationcard, double accuracy, int numberOfCardsMoved,float xCoordinate,float yCoordinate)
    {
        long timestamp = System.currentTimeMillis();
        long time = timer.getCurrentTime();
        long score = SharedData.scores.getScore();


        switch (type) {
            case MOVESTART:
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard,-1, numberOfCardsMoved, false, false, score,
                xCoordinate, yCoordinate,  false,  isAceBetaError(),  isKingBetaError(), false,false));
                aceBetaError=false;
                kingBetaError=false;
                break;
            case MOVESUCCESFUL:
                lastMove = MoveType.MOVESUCCESFUL;
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard, accuracy, numberOfCardsMoved, false, false,score,
                 xCoordinate, yCoordinate,  false,  false,  false, false,false));
                break;
            case MOVEERROR:
                if(lastMove != MoveType.MOVEERROR)
                {
                    lastMove = MoveType.MOVEERROR;
                    Move temp = null;
                    if(!noAceOnSuitError && !noKingOnBuildStackError) {
                         Log.d("MOVEMETRICS","Replacing last moveStart with an moveError.");
                         temp = moves.remove(moves.size() - 1);
                        moves.add(new Move(type,timestamp, temp.getTime(), originstack, temp.getOriginstack(), origincard, destinationcard, accuracy, numberOfCardsMoved,wrongColorBoolean, wrongNumberBoolean,score,
                                xCoordinate, yCoordinate,  false,  false,  false, isNoAceOnSuitError(),isNoKingOnBuildStackError()));
                    }
                    else
                    {
                        //noAceOnSuitError or noKingOnBuildStackError case case
                        Log.d("MOVEMETRICS","Card got deselected. NOt removing any error.");
                        temp = moves.get(moves.size()-1);
                        lastMove = MoveType.MOVESTART; //this is nessecary for the error after error algorithm. The card gets deselected after this error
                        moves.add(new Move(type,timestamp, temp.getTime(), originstack, destinationstack, origincard, destinationcard, accuracy, numberOfCardsMoved,wrongColorBoolean, wrongNumberBoolean,score,
                                xCoordinate, yCoordinate,  false,  false,  false, isNoAceOnSuitError(), isNoKingOnBuildStackError()));
                    }

                    wrongColorBoolean = false;
                    wrongNumberBoolean = false;
                    noAceOnSuitError = false;
                    noKingOnBuildStackError=false;
                }
                else{
                    Log.d("MOVEMETRICS", "Prevented an error after error.");
                    lastMove = MoveType.MOVESTART;
                    wrongColorBoolean = false;
                    wrongNumberBoolean = false;
                    noAceOnSuitError = false;
                    noKingOnBuildStackError=false;
                    return;
                }
                break;
            case MOVEPILE:
                lastMove = MoveType.MOVEPILE;
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard, -1, numberOfCardsMoved, false, false, score,
                 xCoordinate, yCoordinate,  isBetaError(),  isAceBetaError(),  isKingBetaError(), false, false));
                betaError=false;
                aceBetaError=false;
                kingBetaError=false;
                break;
            case MOVEPILEFINISHED:
                lastMove = MoveType.MOVEPILEFINISHED;
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard, -1, numberOfCardsMoved, false, false, score,
                 xCoordinate, yCoordinate,  isBetaError(),  isAceBetaError(),  isKingBetaError(), false, false));
                betaError=false;
                aceBetaError=false;
                kingBetaError=false;
                break;
            case MOVEHINT:
                lastMove = MoveType.MOVEHINT;
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard, -1, numberOfCardsMoved, false, false, score,
                 xCoordinate, yCoordinate,  false,  false,  false, false, false));
                break;
            case MOVEUNDO:
                lastMove = MoveType.MOVEUNDO;
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard, -1, numberOfCardsMoved, false, false, score,
                 xCoordinate, yCoordinate,  false,  false,  false, false, false));
                break;
            case MOVETAP:
                moves.add(new Move(type,timestamp, time, originstack, destinationstack, origincard, destinationcard, -1, numberOfCardsMoved, false, false, score,
                        xCoordinate, yCoordinate,  false,  false,  false, false, false));
                break;
            default:
                Log.e("ERR", "You should not be here. Add the missing movetype to the switch case.");
                break;
        }

        Move temp = moves.getLast();
        Log.d("MOVEMETRICS", temp.getType()+" logged at "+ String.format("%d min. %d sec",
                TimeUnit.MILLISECONDS.toMinutes(temp.getTime()),
                TimeUnit.MILLISECONDS.toSeconds(temp.getTime()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(temp.getTime()))
        )+" Originstack:"+temp.getOriginstack()+" Origincard:"+temp.getOrigincard()+ " Destinationstack:"+temp.getDestinationstack()+" Destinationcard: "+temp.getDestinationcard()+" Accuracy: "+temp.getAccuracy()+"%"
        +" Number of cards moved:"+temp.getNumberOfCardsMoved()+ (temp.isSuitError()?" with suit error ": " with NO color error")+ (temp.isRankError()?" with rank error ": " with NO number error"+"  with score "+score
        +(temp.isBetaError()?" with beta error ": " with NO beta error")+(temp.isAceBetaError()?" with ace beta error ": " with NO ace beta error")
        +(temp.isKingBetaError()?" with king beta error ": " with NO king beta error"+(temp.isNoKingOnBuildStackError()?" with noKingOnBuildStackError ": " with NO noKingOnBuildStackError"+(temp.isNoAceOnSuitError()?" with  ace on suit error ": " with NO ace on suit error")+" Xcoordinate: "+temp.getXCoordinate()+" Ycoordinate: "+temp.getYCoordinate()))));
    }



    public void resetAllMoveCounters()
    {
        Log.d("RESET", "Resetted all moves");
        moves.clear();
        lastMove = MoveType.MOVESTART;
    }

    public static void resetAllChecks()
    {
        wrongColorBoolean=false;
        wrongNumberBoolean=false;
        kingBetaError=false;
        aceBetaError=false;
        noAceOnSuitError = false;
        betaError=false;
        moveAvailable = false;
        noKingOnBuildStackError=false;
        mainstack = false;
    }

    private ArrayList<Integer> accuracy;


    private boolean hintUsed = false;

    public Klondike() {
        setNumberOfDecks(1);
        setNumberOfStacks(15);
        setFirstMainStackID(14);
        setFirstDiscardStackID(11);
        setLastTableauID(6);
        setHasFoundationStacks(true);

        PREF_KEY_DRAW_OLD = PREF_KEY_KLONDIKE_DRAW_OLD;
        PREF_KEY_DRAW = PREF_KEY_KLONDIKE_DRAW;
        DEFAULT_DRAW = DEFAULT_KLONDIKE_DRAW;

        // @GN
        accuracy = new ArrayList<>();
        moves = new LinkedList<Move>();

        Arrays.fill(stackCounter, 0);
    }




    public boolean getHintUsed() {
        return hintUsed;
    }

    public void setHintUsed(boolean hint) {
        this.hintUsed = hint;
    }

    public boolean getMoveAvailable() {
        return moveAvailable;
    }

    public void setMoveAvailable(boolean moveAvailable) {
        this.moveAvailable = moveAvailable;
    }

    public void setStackCounter(int[] stackCounter) {
        this.stackCounter = stackCounter;
    }

    public int[] getStackCounter() {
        return stackCounter;
    }

    public int getGameseed() {
        return gameseed;
    }

    public void setGameseed(int gameseed) {
        this.gameseed = gameseed;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setMainstackBoolean(boolean isTouched) {
        mainstack = isTouched;
    }

    public boolean getMainstackBoolean() {
        return mainstack;
    }


    public void setAccuracy(ArrayList<Integer> accuracy) {
        this.accuracy = accuracy;
    }

    public ArrayList<Integer> getAccuracy() {
        return accuracy;
    }


    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }


    public void setStacks(RelativeLayout layoutGame, boolean isLandscape) {

        // initialize the dimensions
        setUpCardWidth(layoutGame, isLandscape, 8, 10);

        //calculate spacing and startposition of cards
        int spacing = setUpHorizontalSpacing(layoutGame, 7, 8);
        int startPos = layoutGame.getWidth() / 2 - Card.width / 2 - 3 * Card.width - 3 * spacing;

        //first order the foundation stacks
        for (int i = 0; i < 4; i++) {
            stacks[7 + i].setX(startPos + spacing * i + Card.width * i);
            stacks[7 + i].view.setY((isLandscape ? Card.width / 4 : Card.width / 2) + 1);
        }

        //then the trash and main stacks
        startPos = layoutGame.getWidth() - 2 * spacing - 3 * Card.width;
        for (int i = 0; i < 3; i++) {
            stacks[11 + i].setX(startPos + Card.width / 2 * i);
            stacks[11 + i].view.setY((isLandscape ? Card.width / 4 : Card.width / 2) + 1);
        }
        stacks[14].setX(stacks[13].getX() + Card.width + spacing);
        stacks[14].setY(stacks[13].getY());

        //now the tableau stacks
        startPos = layoutGame.getWidth() / 2 - Card.width / 2 - 3 * Card.width - 3 * spacing;
        for (int i = 0; i < 7; i++) {
            stacks[i].setX(startPos + spacing * i + Card.width * i);
            stacks[i].setY(stacks[7].getY() + Card.height +
                    (isLandscape ? Card.width / 4 : Card.width / 2));
        }

        //also set backgrounds of the stacks
        for (Stack stack : stacks) {
            if (stack.getId() > 6 && stack.getId() <= 10)  {
                stack.view.setImageBitmap(Stack.background1);
            } else if (stack.getId() > 10 && stack.getId() <= 13) {
                stack.view.setImageBitmap(Stack.backgroundTransparent);
            }
            else if (stack.getId() == 14) {
                stack.view.setImageBitmap(Stack.backgroundTalon);
            }
        }
    }

    public boolean winTest() {
        // @GN
        // to check if a game is won or not, when the autocomplete button is shown, the game is won
        if (autoComplete.buttonIsShown() == true) {
            return true;
        }

        return false;
    }

    public void dealCards() {
        //save the new settings, so it only takes effect on new deals
        putSharedString(PREF_KEY_DRAW_OLD, getSharedString(PREF_KEY_DRAW, DEFAULT_DRAW));

        //and move cards to the tableau
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                moveToStack(getMainStack().getTopCard(), stacks[i], OPTION_NO_RECORD);
                if (j == i)
                    stacks[i].getTopCard().flipUp();
            }
        }

        //deal cards to trash according to the draw option
        if (sharedStringEqualsDefault(PREF_KEY_DRAW_OLD, "1")) {
            moveToStack(getMainStack().getTopCard(), stacks[13], OPTION_NO_RECORD);
            stacks[13].getTopCard().flipUp();
        } else {
            for (int i = 0; i < 3; i++) {
                moveToStack(getMainStack().getTopCard(), stacks[11 + i], OPTION_NO_RECORD);
                stacks[11 + i].getTopCard().flipUp();
            }
        }
    }

    public int onMainStackTouch(float X, float Y) {

        boolean deal3 = sharedStringEquals(PREF_KEY_DRAW_OLD, DEFAULT_DRAW,"3");
        Klondike.kingBetaTest();
        Klondike.aceBetaTest();


        //if there are cards on the main stack
        if (getMainStack().getSize() > 0) {
            if (deal3) {
                int size = min(3, getMainStack().getSize());
                ArrayList<Card> cardsReversed = new ArrayList<>();
                ArrayList<Stack> originReversed = new ArrayList<>();
                ArrayList<Card> cards = new ArrayList<>();
                ArrayList<Stack> origin = new ArrayList<>();

                //add cards from 2. and 3. discard stack to the first one
                while (!stacks[12].isEmpty()) {
                    cards.add(stacks[12].getTopCard());
                    origin.add(stacks[12]);
                    moveToStack(stacks[12].getTopCard(), stacks[11], OPTION_NO_RECORD);
                }
                while (!stacks[13].isEmpty()) {
                    cards.add(stacks[13].getTopCard());
                    origin.add(stacks[13]);
                    moveToStack(stacks[13].getTopCard(), stacks[11], OPTION_NO_RECORD);
                }

                //reverse the array orders, soon they will be reversed again so they are in the right
                // order again at this part, because now there are only the cards from the discard
                // stacks on. So i don't need to save how many cards are actually moved
                // (for example, when the third stack is empty
                for (int i = 0; i < cards.size(); i++) {
                    cardsReversed.add(cards.get(cards.size() - 1 - i));
                    originReversed.add(origin.get(cards.size() - 1 - i));
                }
                for (int i = 0; i < cards.size(); i++) {
                    cards.set(i, cardsReversed.get(i));
                    origin.set(i, originReversed.get(i));
                }

                //add up to 3 cards from main to the first discard stack
                for (int i = 0; i < size; i++) {
                    cards.add(getMainStack().getTopCard());
                    origin.add(getMainStack());
                    moveToStack(getMainStack().getTopCard(), stacks[11], OPTION_NO_RECORD);
                    stacks[11].getTopCard().flipUp();
                }

                //then move up to 2 cards to the 2. and 3. discard stack
                size = min(3, stacks[11].getSize());
                if (size > 1) {
                    moveToStack(stacks[11].getCardFromTop(1), stacks[12], OPTION_NO_RECORD);
                    if (!cards.contains(stacks[12].getTopCard())) {
                        cards.add(stacks[12].getTopCard());
                        origin.add(stacks[11]);
                    }
                }
                if (size > 0) {
                    moveToStack(stacks[11].getTopCard(), stacks[13], OPTION_NO_RECORD);
                    if (!cards.contains(stacks[13].getTopCard())) {
                        cards.add(stacks[13].getTopCard());
                        origin.add(stacks[11]);
                    }
                }

                //now bring the cards to front
                if (!stacks[12].isEmpty()) {
                    stacks[12].getTopCard().view.bringToFront();
                }
                if (!stacks[13].isEmpty()) {
                    stacks[13].getTopCard().view.bringToFront();
                }

                //reverse everything so the cards on the stack will be in the right order when using an undo
                //the cards from 2. and 3 trash stack are in the right order again
                cardsReversed.clear();
                originReversed.clear();
                for (int i = 0; i < cards.size(); i++) {
                    cardsReversed.add(cards.get(cards.size() - 1 - i));
                    originReversed.add(origin.get(cards.size() - 1 - i));
                }

                //finally add the record
                recordList.add(cardsReversed, originReversed);
            } else {
                //no deal3 option, just deal one card without that fucking huge amount of calculation for the recordLit
                moveToStack(getMainStack().getTopCard(), stacks[13]);
                stacks[13].getTopCard().flipUp();
            }
            addMove(MoveType.MOVEPILE, 14, 14, "x0", "x0", -1, 0, X,Y);

            return 1;
        }
        //if there are NO cards on the main stack, but cards on the discard stacks, move them all to main
        else if ((stacks[11].getSize() != 0 || stacks[12].getSize() != 0 || stacks[13].getSize() != 0)) {
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < stacks[11].getSize(); i++) {
                cards.add(stacks[11].getCard(i));
            }

            for (int i = 0; i < stacks[12].getSize(); i++) {
                cards.add(stacks[12].getCard(i));
            }

            for (int i = 0; i < stacks[13].getSize(); i++) {
                cards.add(stacks[13].getCard(i));
            }

            ArrayList<Card> cardsReversed = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                cardsReversed.add(cards.get(cards.size() - 1 - i));
            }

            moveToStack(cardsReversed, getMainStack(), OPTION_REVERSED_RECORD);
            addMove(MoveType.MOVEPILEFINISHED, 14, 14, "x0", "x0", -1, 0, X,Y);
            return 2;
        }

        return 0;
    }

    public boolean autoCompleteStartTest() {

        //if every card is faced up, show the auto complete button
        for (int i = 0; i < 7; i++) {
            if (stacks[i].getSize() > 0 && !stacks[i].getCard(0).isUp()) {
                return false;
            }
        }

        //for deal3 mode, discard and main stack have to be empty too
        if (!sharedStringEqualsDefault(PREF_KEY_DRAW_OLD, DEFAULT_DRAW) || hasLimitedRecycles()) {
            if (getMainStack().getSize()>0 || getDiscardStack().getSize()>0){
                return false;
            }
        }

        return true;
    }

    public boolean cardTest(Stack destinationStack, Card originCard) {
        //move cards according to the klondike rules
        //Log.d("STACK", "Stack id is "+stack.getId()+" de card value is " + (stack.isEmpty()?"empty":Integer.toString(stack.getTopCard().getValue())));

        if (destinationStack.getId() < 7) {
            if (destinationStack.isEmpty()) {
                this.noKingOnBuildStackError = !(originCard.getValue() == 13);
                return !noKingOnBuildStackError;
            } else {
                if(hintUsed == false && mainstack == false) { //hintused zorgt er voor dat er bij hints niets accidental getriggered wordt
                    faultCounterStack(destinationStack, originCard);
                }
                boolean canBePlaced = canCardBePlaced(destinationStack, originCard, ALTERNATING_COLOR, DESCENDING);
                return canBePlaced;
            }
        } else if (destinationStack.getId() < 11 && movingCards.hasSingleCard()) {
            if (destinationStack.isEmpty()) {
                this.noAceOnSuitError = !(originCard.getValue() == 1);
                return !noAceOnSuitError;
            } else {
                if(hintUsed == false && mainstack == false && ((destinationStack.getId() == 7) || (destinationStack.getId() == 8) || (destinationStack.getId() == 9) || (destinationStack.getId() == 10))) {
                    faultCounterTalon(destinationStack, originCard);
                }
                boolean canBePlaced = canCardBePlaced(destinationStack, originCard, SAME_FAMILY, ASCENDING);
                return canBePlaced;
            }
        } else
            return false;
    }

    private void faultCounterStack(Stack stack, Card card) {
        if (stack.getTopCard().getColor() % 2 == card.getColor() % 2) {
            wrongColorBoolean = true;
        }
        if (!(stack.getTopCard().getValue() == card.getValue() + 1 || (false && stack.getTopCard().getValue() == 1 && card.getValue() == 13)))
        {
            wrongNumberBoolean = true;
        }
    }

    private void faultCounterTalon(Stack stack, Card card) {
        if (stack.getTopCard().getColor() != card.getColor()) {
            wrongColorBoolean = true;
        }
        if (!(stack.getTopCard().getValue() == card.getValue() - 1 || (false && stack.getTopCard().getValue() == 13 && card.getValue() == 1)))
        {
            wrongNumberBoolean = true;
        }
    }






    public boolean addCardToMovementTest(Card card) {
        //don't move cards from the discard stacks if there is a card on top of them
        //for example: if touched a card on stack 11 (first discard stack) but there is a card
        //on stack 12 (second discard stack) don't move if.
        return !(((card.getStackId() == 11 || card.getStackId() == 12) && !stacks[13].isEmpty())
                || (card.getStackId() == 11 && !stacks[12].isEmpty()));
    }

    public CardAndStack hintTest() {
        tempwrongColorBoolean = wrongColorBoolean;
        tempwrongNumberBoolean=wrongNumberBoolean;
        tempkingBetaError=kingBetaError;
        tempaceBetaError=aceBetaError;
        tempbetaError=betaError;
        tempnoAceOnSuitError=noAceOnSuitError;
        tempnoKingOnBuildStackError=noKingOnBuildStackError;


        Card card;
        if (hint.getHintVisible() == true)
            hintUsed = true;

        for (int i = 0; i <= 6; i++) {

            Stack origin = stacks[i];

            if (origin.isEmpty()) {
                continue;
            }

            /* complete visible part of a stack to move on the tableau */
            card = origin.getFirstUpCard();

            if (!hint.hasVisited(card) && !(card.isFirstCard() && card.getValue() == 13)
                    && card.getValue() != 1) {
                for (int j = 0; j <= 6; j++) {
                    if (j == i) {
                        continue;
                    }

                    if (card.test(stacks[j])) {
                        moveAvailable = true;
                        return new CardAndStack(card, stacks[j]);
                    }
                }
            }

            /* last card of a stack to move to the foundation */
            card = origin.getTopCard();

            if (!hint.hasVisited(card)) {
                for (int j = 7; j <= 10; j++) {
                    if (card.test(stacks[j])) {
                        moveAvailable = true;
                        return new CardAndStack(card, stacks[j]);
                    }
                }
            }

        }

        /* card from trash of stock to every other stack*/
        for (int i = 0; i < 3; i++) {
            if ((i < 2 && !stacks[13].isEmpty()) || (i == 0 && !stacks[12].isEmpty())) {
                continue;
            }

            if (stacks[11 + i].getSize() > 0 && !hint.hasVisited(stacks[11 + i].getTopCard())) {
                for (int j = 10; j >= 0; j--) {
                    if (stacks[11 + i].getTopCard().test(stacks[j])) {
                        moveAvailable = true;
                        return new CardAndStack(stacks[11 + i].getTopCard(), stacks[j]);
                    }
                }
            }
        }

        return null;
    }

    private static boolean testStackKingBetaError(int stacknumber)
    {
        Card card;
        Stack buildstack = stacks[stacknumber]; //get a build stack

        if (buildstack.isEmpty()) { //if empty continue
            return false;
        }

        card = buildstack.getFirstUpCard(); //get bovenste zichtbare kaart
        if(card.getValue()==13) //if it's a king
        {
            if(stacknumber==13 || !card.isFirstCard())//and not the first card (i.e. on its place) except on talon cards (111/12/13)
            {
                for (int j = 0; j <= 6; j++) { //go over all build stacks again
                    if (j == stacknumber) {
                        continue;  //skip for the build stack that's currently being inspected
                    }
                    if(stacks[j].isEmpty()) {
                        Log.v("KINGBETA", "a king beta error has been made");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void kingBetaTest() {

        for (int i = 0; i <= 6; i++) { //voor alle build stacks
            if(testStackKingBetaError(i))
            {
                setKingBetaError(true);
                return;
            }
        }


        //(int j = 13; j>=11; j--)
        //{
            //if(!stacks[j].isEmpty())
            //{
                if(testStackKingBetaError(13))
                {
                    setKingBetaError(true);
                    return;
                }
                //break; //als 13 niet empty is, testen en breaken. ANders de andere 3 proberen.
            //}
        //}

        setKingBetaError(false);
    }

    private static boolean testStackAceBetaError(int stacknumber)
    {
        Card card;
        Stack buildstack = stacks[stacknumber]; //get a build stack

        if (buildstack.isEmpty()) { //if empty continue
            return false;
        }

        card = buildstack.getFirstUpCard(); //get bovenste zichtbare kaart
        if(card.getValue()==1) //if it's an Ace
        {
            //if(stacknumber==13 || stacknumber == 12 || stacknumber==11 || !card.isFirstCard())//and not the first card (i.e. on its place) except on talon (13)
            //{
                Log.v("ACEBETA", "a ace beta error has been made");
                return true;
            //}
        }

        return false;
    }

    public static void aceBetaTest() {
        for (int i = 0; i <= 6; i++) { //voor alle build stacks
            if(testStackAceBetaError(i))
            {
                setAceBetaError(true);
                return;
            }
        }


        //for(int j = 13; j>=11; j--)
       // {
            //if(!stacks[j].isEmpty())
            //{
        if(testStackAceBetaError(13))
        {
            setAceBetaError(true);
            return;
        }
                //break; //als 13 niet empty is, testen en breaken. ANders de andere 3 proberen.
            //}
        //}

        setAceBetaError(false);
    }


    public Stack doubleTapTest(Card card) {
        //foundation stacks
        if (card.isTopCard()) {
            for (int j = 7; j < 11; j++) {
                if (card.test(stacks[j])) {
                    return stacks[j];
                }
            }
        }

        //tableau stacks
        for (int j = 0; j < 7; j++) {

            if (card.getStackId() < 7 && sameCardOnOtherStack(card, stacks[j], SAME_VALUE_AND_COLOR))
                continue;

            if (card.getValue() == 13 && card.isFirstCard() && card.getStackId() <= 6)
                continue;

            if (card.test(stacks[j])) {
                return stacks[j];
            }
        }

        //empty tableau stacks
        for (int j = 0; j < 7; j++) {
            if (stacks[j].isEmpty() && card.test(stacks[j]))
                return stacks[j];
        }
        return null;
    }

    public CardAndStack autoCompletePhaseOne() {
        return null;
    }

    public CardAndStack autoCompletePhaseTwo() {
        //just go through every stack
        for (int i = 7; i <= 10; i++) {
            Stack destination = stacks[i];

            for (int j = 0; j <= 6; j++) {
                Stack origin = stacks[j];

                if (origin.getSize() > 0 && origin.getTopCard().test(destination)) {
                    return new CardAndStack(origin.getTopCard(), destination);
                }
            }

            for (int j = 11; j < 15; j++) {
                Stack origin = stacks[j];

                for (int k = 0; k < origin.getSize(); k++) {
                    if (origin.getCard(k).test(destination)) {
                        origin.getCard(k).flipUp();
                        return new CardAndStack(origin.getCard(k), destination);
                    }
                }
            }
        }

        return null;
    }

    public int addPointsToScore(ArrayList<Card> cards, int[] originIDs, int[] destinationIDs, boolean isUndoMovement) {
        int originID = originIDs[0];
        int destinationID = destinationIDs[0];

        //relevant for deal3 options, because cards on the waste move first and checking only
        // the first id wouldn't be enough
        for (int i=0;i<originIDs.length;i++){
            if (originIDs[i] >=11 && originIDs[i]<=13 && destinationIDs[i] <=10){                   //stock to tableau/foundation
                return 45;
            }
        }

        if (originID < 7 && destinationID >= 7 && destinationID <= 10) {                            //transfer from tableau to foundations
            return 60;
        }
        if (destinationID < 7 && originID >= 7 && originID <= 10) {                                 //foundation to tableau
            return -75;
        }
        if (originID == destinationID) {                                                            //turn a card over
            return 25;
        }
        if (originID >= 11 && originID < 14 && destinationID == 14) {                               //returning cards to stock
            return -200;
        }

        return 0;
    }

    public void testAfterMove() {
        /*
         *  after a card is moved from the discard stacks, it needs to update the order of the cards
         *  on the discard stacks. (But only in deal3 mode).
         *  This movement will be added to the last record list entry, so it will be also undone if
         *  the card will be moved back to the discard stacks
         */
        if (sharedStringEquals(PREF_KEY_DRAW_OLD, DEFAULT_DRAW,"1") || gameLogic.hasWon()) {
            return;
        }

        if (stacks[12].getSize() == 0 || stacks[13].getSize() == 0) {
            ArrayList<Card> cards = new ArrayList<>();
            ArrayList<Stack> origin = new ArrayList<>();

            //add the cards to the first discard pile
            while (!stacks[12].isEmpty()) {
                cards.add(stacks[12].getTopCard());
                origin.add(stacks[12]);
                moveToStack(stacks[12].getTopCard(), stacks[11], OPTION_NO_RECORD);
            }

            //and then move cards from there to fill the discard stacks
            if (stacks[11].getSize() > 1) {
                moveToStack(stacks[11].getCardFromTop(1), stacks[12], OPTION_NO_RECORD);
                if (!cards.contains(stacks[12].getTopCard())) {
                    cards.add(stacks[12].getTopCard());
                    origin.add(stacks[11]);
                }
            }
            if (!stacks[11].isEmpty()) {
                moveToStack(stacks[11].getTopCard(), stacks[13], OPTION_NO_RECORD);
                if (!cards.contains(stacks[13].getTopCard())) {
                    cards.add(stacks[13].getTopCard());
                    origin.add(stacks[11]);
                }
            }

            //reverse order for the record
            ArrayList<Card> cardsReversed = new ArrayList<>();
            ArrayList<Stack> originReversed = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                cardsReversed.add(cards.get(cards.size() - 1 - i));
                originReversed.add(origin.get(cards.size() - 1 - i));
            }

            if (!stacks[12].isEmpty()) {
                stacks[12].getTopCard().view.bringToFront();
            }

            if (!stacks[13].isEmpty()) {
                stacks[13].getTopCard().view.bringToFront();
            }

            //and add it IN FRONT of the last entry
            recordList.addInFrontOfLastEntry(cardsReversed, originReversed);
        }
    }

    public static boolean isNoKingOnBuildStackError() {
        return noKingOnBuildStackError;
    }

    public static void setNoKingOnBuildStackError(boolean noKingOnBuildStackError) {
        Klondike.noKingOnBuildStackError = noKingOnBuildStackError;
    }

}
