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
import android.view.View;

import be.kuleuven.drsolitaire.ui.GameManager;

import static be.kuleuven.drsolitaire.SharedData.*;

/**
 * load the game data in a handler which waits a bit, so the initial card deal looks smoother
 */

public class HandlerLoadGame extends Handler {

    private GameManager gm;

    public HandlerLoadGame(GameManager gm) {
        this.gm = gm;
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        gameLogic.load();

        if (currentGame.hasLimitedRecycles()) {
            gm.mainTextViewRecycles.setVisibility(View.VISIBLE);
            gm.mainTextViewRecycles.setX(currentGame.getMainStack().getX());
            gm.mainTextViewRecycles.setY(currentGame.getMainStack().getY());
        }

        gm.hasLoaded = true;
    }
}
