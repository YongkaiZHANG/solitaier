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

package be.kuleuven.drsolitaire.handler;

import android.os.Handler;
import android.os.Message;

import be.kuleuven.drsolitaire.classes.CardAndStack;
import be.kuleuven.drsolitaire.classes.MoveType;
import be.kuleuven.drsolitaire.games.Klondike;
import be.kuleuven.drsolitaire.helper.Hint;
import be.kuleuven.drsolitaire.helper.Sounds;

import static be.kuleuven.drsolitaire.SharedData.*;

/**
 * shows hints, waits until the movement is done and then starts the next hint
 */

public class HandlerHint extends Handler {


    boolean soundPlayed = false;

    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if (hint.getCounter() < Hint.MAX_NUMBER_OF_HINTS) {
            CardAndStack cardAndStack;

            if (!animate.cardIsAnimating()) {
                cardAndStack = currentGame.hintTest();
                Klondike.wrongColorBoolean = Klondike.tempwrongColorBoolean;
                Klondike.wrongNumberBoolean=Klondike.tempwrongNumberBoolean;
                Klondike.kingBetaError=Klondike.tempkingBetaError;
                Klondike.aceBetaError=Klondike.tempaceBetaError;
                Klondike.betaError=Klondike.tempbetaError;
                Klondike.noAceOnSuitError=Klondike.tempnoAceOnSuitError;
                Klondike.noKingOnBuildStackError=Klondike.tempnoKingOnBuildStackError;


                if (cardAndStack == null) {
                    hint.stop();
                } else {

                    if (!soundPlayed) {
                        sounds.playSound(Sounds.names.HINT);
                        soundPlayed = true;
                    }
                    // @NG
                    if(hint.getHintVisible() == true) {
                        hint.move(cardAndStack.getCard(), cardAndStack.getStack());
                        hint.setCounter(hint.getCounter() + 1);
                        hint.setHintVisible(false);
                    }

                }


            }

            hint.handlerHint.sendEmptyMessageDelayed(0, 100);
        } else {
            soundPlayed = false;
            Klondike.addMove(MoveType.MOVEHINT,0,0,"X0", "X0", -1.0, 0, -1.0f, -1.0f);
            currentGame.setHintUsed(false);
            hint.setCounter(0);
        }
    }
}