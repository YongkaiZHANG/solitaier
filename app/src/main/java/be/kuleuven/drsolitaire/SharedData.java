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

package be.kuleuven.drsolitaire;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import be.kuleuven.drsolitaire.classes.Card;
import be.kuleuven.drsolitaire.classes.Move;
import be.kuleuven.drsolitaire.classes.Person;
import be.kuleuven.drsolitaire.classes.Stack;
import be.kuleuven.drsolitaire.database.AppDatabase;
import be.kuleuven.drsolitaire.games.Game;
import be.kuleuven.drsolitaire.handler.HandlerTestAfterMove;
import be.kuleuven.drsolitaire.handler.HandlerTestIfWon;
import be.kuleuven.drsolitaire.helper.Animate;
import be.kuleuven.drsolitaire.helper.AutoComplete;
import be.kuleuven.drsolitaire.helper.BackgroundMusic;
import be.kuleuven.drsolitaire.helper.Bitmaps;
import be.kuleuven.drsolitaire.helper.CardHighlight;
import be.kuleuven.drsolitaire.database.EntityMapper;
import be.kuleuven.drsolitaire.helper.GameLogic;
import be.kuleuven.drsolitaire.helper.Hint;
import be.kuleuven.drsolitaire.helper.MovingCards;
import be.kuleuven.drsolitaire.helper.RecordList;
import be.kuleuven.drsolitaire.helper.Scores;
import be.kuleuven.drsolitaire.helper.Sounds;
import be.kuleuven.drsolitaire.helper.Timer;

/**
 * static data which is shared across the whole project, i like this more than passing stuff around
 */

public class SharedData {

    public final static int OPTION_UNDO = 1, OPTION_NO_RECORD = 2, OPTION_REVERSED_RECORD = 3;
    //Strings
    public static String GAME_SEED;

    public static String SCORE;
    public static String SAVED_SCORES;
    public static String OLD;
    public static String GAME;
    public static String GAME_REDEAL_COUNT;
    public static String GAME_WON;
    public static String GAME_WON_AND_RELOADED;
    public static String GAME_NUMBER_OF_WON_GAMES;
    public static String GAME_NUMBER_OF_PLAYED_GAMES;
    public static String GAME_RANDOM_CARDS;
    public static String GAME_FIRST_RUN;
    public static String GAME_MOVED_FIRST_CARD;
    public static String RECORD_LIST_ENTRY;
    public static String RECORD_LIST_ENTRIES_SIZE;
    public static String FLIP_CARD;
    public static String ORIGIN;
    public static String CARD;
    public static String CARDS;
    public static String ERROR;
    public static String STACK;
    public static String TIMER_END_TIME;
    public static String TIMER_START_TIME;
    public static String TIMER_WINNING_TIME;
    public static String CARD_DRAWABLES;
    public static String CARD_BACKGROUND;
    public static String CARD_BACKGROUND_COLOR;
    public static String MENU_COLUMNS_PORTRAIT;
    public static String MENU_COLUMNS_LANDSCAPE;
    public static String CANFIELD_START_CARD_VALUE;
    public static String RESTART_DIALOG;
    public static String WON_DIALOG;
    public static final String PREFS_NAME = "LoginRecordsFile";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_PASSWORD = "password";
    public static String PREF_KEY_YUKON_RULES;
    public static String PREF_KEY_YUKON_RULES_OLD;
    public static String PREF_KEY_KLONDIKE_DRAW;
    public static String PREF_KEY_KLONDIKE_DRAW_OLD;
    public static String PREF_KEY_VEGAS_DRAW;
    public static String PREF_KEY_VEGAS_DRAW_OLD;
    public static String PREF_KEY_GOLF_CYCLIC;
    public static String PREF_KEY_CANFIELD_DRAW;
    public static String PREF_KEY_CANFIELD_DRAW_OLD;
    public static String PREF_KEY_PYRAMID_DIFFICULTY;
    public static String PREF_KEY_SPIDER_DIFFICULTY;
    public static String PREF_KEY_SPIDER_DIFFICULTY_OLD;
    public static String PREF_KEY_LANGUAGE;
    public static String PREF_KEY_CURRENT_GAME;
    public static String PREF_KEY_ORIENTATION;
    public static String PREF_KEY_MENU_GAMES;
    public static String PREF_KEY_4_COLOR_MODE;
    public static String PREF_KEY_LEFT_HANDED_MODE;
    public static String PREF_KEY_MENU_BAR_POS_PORTRAIT;
    public static String PREF_KEY_MENU_BAR_POS_LANDSCAPE;
    public static String PREF_KEY_DOUBLE_TAP_ENABLED;
    public static String PREF_KEY_DOUBLE_TAP_ALL_CARDS;
    public static String PREF_KEY_DOUBLE_TAP_FOUNDATION_FIRST;
    public static String PREF_KEY_TAP_TO_SELECT_ENABLED;
    public static String PREF_KEY_SINGLE_TAP_ENABLE;
    public static String PREF_KEY_BACKGROUND_COLOR_TYPE;
    public static String PREF_KEY_BACKGROUND_COLOR;
    public static String PREF_KEY_BACKGROUND_COLOR_CUSTOM;
    public static String PREF_KEY_MOVEMENT_SPEED;
    public static String PREF_KEY_SOUND_ENABLED;
    public static String PREF_KEY_WIN_SOUND;
    public static String PREF_KEY_BACKGROUND_MUSIC;
    public static String PREF_KEY_BACKGROUND_VOLUME;
    public static String PREF_KEY_PYRAMID_LIMITED_RECYCLES;
    public static String PREF_KEY_FORTYEIGHT_LIMITED_RECYCLES;
    public static String PREF_KEY_PYRAMID_NUMBER_OF_RECYCLES;
    public static String PREF_KEY_FORTYEIGHT_NUMBER_OF_RECYCLES;
    public static String PREF_KEY_VEGAS_NUMBER_OF_RECYCLES;
    public static String PREF_KEY_VEGAS_BET_AMOUNT;
    public static String PREF_KEY_VEGAS_BET_AMOUNT_OLD;
    public static String PREF_KEY_MENU_ORDER;
    public static String PREF_KEY_AUTO_START_NEW_GAME;
    public static String DEFAULT_CANFIELD_DRAW;
    public static String DEFAULT_KLONDIKE_DRAW;
    public static String DEFAULT_VEGAS_DRAW;
    public static String DEFAULT_YUKON_RULES;
    public static String DEFAULT_MENU_BAR_POSITION_LANDSCAPE;
    public static String DEFAULT_MENU_BAR_POSITION_PORTRAIT;
    public static String DEFAULT_PYRAMID_DIFFICULTY;
    public static String DEFAULT_SPIDER_DIFFICULTY;
    public static String DEFAULT_LANGUAGE;
    public static String DEFAULT_MENU_COLUMNS_LANDSCAPE;
    public static String DEFAULT_MENU_COLUMNS_PORTRAIT;
    public static String DEFAULT_ORIENTATION;
    public static String DEFAULT_BACKGROUND_COLOR;
    public static String DEFAULT_BACKGROUND_MUSIC;
    public static String DEFAULT_PYRAMID_NUMBER_OF_RECYCLES;
    public static String DEFAULT_FORTYEIGHT_NUMBER_OF_RECYCLES;
    public static String DEFAULT_VEGAS_NUMBER_OF_RECYCLES;
    public static String DEFAULT_WIN_SOUND;
    public static String DEFAULT_MOVEMENT_SPEED;
    public static int DEFAULT_CURRENT_GAME;
    public static int DEFAULT_CARD_BACKGROUND;
    public static int DEFAULT_CARD_BACKGROUND_COLOR;
    public static int DEFAULT_WINNING_TIME;
    public static int DEFAULT_BACKGROUND_COLOR_TYPE;
    public static int DEFAULT_BACKGROUND_VOLUME;
    public static int DEFAULT_BACKGROUND_COLOR_CUSTOM;
    public static int DEFAULT_VEGAS_BET_AMOUNT;
    public static boolean DEFAULT_GOLF_CYCLIC;
    public static boolean DEFAULT_LEFT_HANDED_MODE;
    public static boolean DEFAULT_DOUBLE_TAP_ENABLE;
    public static boolean DEFAULT_DOUBLE_TAP_ALL_CARDS;
    public static boolean DEFAULT_DOUBLE_TAP_FOUNDATION_FIRST;
    public static boolean DEFAULT_WON;
    public static boolean DEFAULT_WON_AND_RELOADED;
    public static boolean DEFAULT_FIRST_RUN;
    public static boolean DEFAULT_MOVED_FIRST_CARD;
    public static boolean DEFAULT_4_COLOR_MODE;
    public static boolean DEFAULT_TAP_TO_SELECT_ENABLED;
    public static boolean DEFAULT_SINGLE_TAP_ENABLED;
    public static boolean DEFAULT_SOUND_ENABLED;
    public static boolean DEFAULT_AUTO_START_NEW_GAME;

    public static boolean DEFAULT_PYRAMID_LIMITED_RECYCLES;
    public static boolean DEFAULT_FORTYEIGHT_LIMITED_RECYCLES;


    public static Card[] cards;
    public static Stack[] stacks;

    public static Scores scores;

    public static GameLogic gameLogic;
    public static Animate animate;

    public static AutoComplete autoComplete;
    public static Timer timer;
    public static Sounds sounds;
    public static Hint hint = new Hint();
    public static MovingCards movingCards = new MovingCards();
    public static RecordList recordList = new RecordList();
    public static LoadGame lg = new LoadGame();
    public static Bitmaps bitmaps = new Bitmaps();
    public static CardHighlight cardHighlight = new CardHighlight();

    public static SharedPreferences savedSharedData;
    public static SharedPreferences savedGameData;
    public static Game currentGame;

    public static HandlerTestAfterMove handlerTestAfterMove = new HandlerTestAfterMove();
    public static HandlerTestIfWon handlerTestIfWon = new HandlerTestIfWon();
    public static BackgroundMusic backgroundSound = new BackgroundMusic();
    public static int activityCounter = 0;

    public static int NUMBER_OF_CARD_BACKGROUNDS;
    public static int NUMBER_OF_CARD_THEMES;

    private static EntityMapper entityMapper = EntityMapper.UNIQUEMAPPER;
    private static AppDatabase appDatabase;
    public static Person user = null;

    //@KG manipulations to make the game deal in the same manner every time
    public static final List<Integer> winnableGameList = Arrays.asList(
            3333333,
            498512, // ja
            221, // ja
            7894515,
            231472, // ja
            1111111 // ja

            // 4444444,
            // 6666666,
            // 888888888, // ja
            // 10 // ja
            // 11

    );
    /**
     * Reload the needed data. Because if the android device runs out of memory, the app gets
     * killed. If the user restarts the app and it loads  for example the settings activity, all
     * the strings and the shared preferences need to be reinitialized.
     *
     * @param context Used to get the resources
     */
    public static void reinitializeData(Context context) {
        //Strings
        if (GAME == null) {
            loadStrings(context.getResources());
        }

        //Bitmaps
        if (!bitmaps.checkResources()) {
            bitmaps.setResources(context.getResources());
        }

        //SharedPrefs
        if (savedSharedData == null) {
            savedSharedData = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if (savedGameData == null) {
            savedGameData = context.getSharedPreferences(lg.getSharedPrefName(), Context.MODE_PRIVATE);
        }

        if (lg.getGameCount()==0){
            lg.loadAllGames();
        }
    }

    /**
     * Load the static strings, so i can use them in every file instead of writing the string itself,
     * which would be susceptible for errors.
     *
     * @param res Used to load the strings
     */
    public static void loadStrings(Resources res) {
        GAME = res.getString(R.string.game);
        OLD = "_old";

        PREF_KEY_YUKON_RULES = res.getString(R.string.pref_key_yukon_rules);
        PREF_KEY_KLONDIKE_DRAW = res.getString(R.string.pref_key_klondike_draw);
        PREF_KEY_VEGAS_DRAW = res.getString(R.string.pref_key_vegas_draw);
        PREF_KEY_CANFIELD_DRAW = res.getString(R.string.pref_key_canfield_draw);
        PREF_KEY_YUKON_RULES_OLD = PREF_KEY_YUKON_RULES + OLD;
        PREF_KEY_KLONDIKE_DRAW_OLD = PREF_KEY_KLONDIKE_DRAW + OLD;
        PREF_KEY_VEGAS_DRAW_OLD = PREF_KEY_VEGAS_DRAW + OLD;
        PREF_KEY_CANFIELD_DRAW_OLD = PREF_KEY_CANFIELD_DRAW + OLD;
        PREF_KEY_GOLF_CYCLIC = res.getString(R.string.pref_key_golf_cyclic);
        PREF_KEY_PYRAMID_DIFFICULTY = res.getString(R.string.pref_key_pyramid_difficulty);
        PREF_KEY_SPIDER_DIFFICULTY = res.getString(R.string.pref_key_spider_difficulty);
        PREF_KEY_SPIDER_DIFFICULTY_OLD = PREF_KEY_SPIDER_DIFFICULTY + OLD;
        PREF_KEY_LANGUAGE = res.getString(R.string.pref_key_language);
        PREF_KEY_CURRENT_GAME = res.getString(R.string.pref_key_current_game);
        PREF_KEY_MENU_GAMES = res.getString(R.string.pref_key_menu_games);
        PREF_KEY_ORIENTATION = res.getString(R.string.pref_key_orientation);
        PREF_KEY_4_COLOR_MODE = res.getString(R.string.pref_key_4_color_mode);
        PREF_KEY_LEFT_HANDED_MODE = res.getString(R.string.pref_key_left_handed_mode);
        PREF_KEY_MENU_BAR_POS_PORTRAIT = res.getString(R.string.pref_key_menu_bar_position_portrait);
        PREF_KEY_MENU_BAR_POS_LANDSCAPE = res.getString(R.string.pref_key_menu_bar_position_landscape);
        PREF_KEY_DOUBLE_TAP_ENABLED = res.getString(R.string.pref_key_double_tap_enable);
        PREF_KEY_DOUBLE_TAP_ALL_CARDS = res.getString(R.string.pref_key_double_tap_all_cards);
        PREF_KEY_DOUBLE_TAP_FOUNDATION_FIRST = res.getString(R.string.pref_key_double_tap_foundation_first);
        PREF_KEY_TAP_TO_SELECT_ENABLED = res.getString(R.string.pref_key_tap_to_select_enable);
        PREF_KEY_SINGLE_TAP_ENABLE = res.getString(R.string.pref_key_single_tap_enable);
        PREF_KEY_BACKGROUND_COLOR_TYPE = res.getString(R.string.pref_key_background_color_type);
        PREF_KEY_BACKGROUND_COLOR = res.getString(R.string.pref_key_background_color);
        PREF_KEY_BACKGROUND_COLOR_CUSTOM = res.getString(R.string.pref_key_background_color_custom);
        PREF_KEY_MOVEMENT_SPEED = res.getString(R.string.pref_key_movement_speed);
        PREF_KEY_SOUND_ENABLED = res.getString(R.string.pref_key_sound_enabled);
        PREF_KEY_WIN_SOUND = res.getString(R.string.pref_key_win_sound);
        PREF_KEY_BACKGROUND_MUSIC = res.getString(R.string.pref_key_background_music);
        PREF_KEY_BACKGROUND_VOLUME = res.getString(R.string.pref_key_background_volume);
        PREF_KEY_PYRAMID_LIMITED_RECYCLES = res.getString(R.string.pref_key_pyramid_limit_recycles);
        PREF_KEY_FORTYEIGHT_LIMITED_RECYCLES = res.getString(R.string.pref_key_fortyeight_limit_recycles);
        PREF_KEY_PYRAMID_NUMBER_OF_RECYCLES = res.getString(R.string.pref_key_pyramid_number_of_recycles);
        PREF_KEY_FORTYEIGHT_NUMBER_OF_RECYCLES = res.getString(R.string.pref_key_fortyeight_number_of_recycles);
        PREF_KEY_VEGAS_NUMBER_OF_RECYCLES = res.getString(R.string.pref_key_vegas_number_of_recycles);
        PREF_KEY_VEGAS_BET_AMOUNT = res.getString(R.string.pref_key_vegas_bet_amount);
        PREF_KEY_MENU_ORDER = res.getString(R.string.pref_key_menu_order);
        PREF_KEY_VEGAS_BET_AMOUNT_OLD = PREF_KEY_VEGAS_BET_AMOUNT + OLD;
        PREF_KEY_AUTO_START_NEW_GAME = res.getString(R.string.pref_key_auto_start_new_game);

        DEFAULT_PYRAMID_DIFFICULTY = res.getStringArray(R.array.pref_pyramid_difficulty_values)[0];
        DEFAULT_LANGUAGE = res.getStringArray(R.array.pref_language_values)[0];
        DEFAULT_SPIDER_DIFFICULTY = res.getStringArray(R.array.pref_spider_difficulty_values)[0];
        DEFAULT_ORIENTATION = res.getStringArray(R.array.pref_orientation_values)[0];
        DEFAULT_DOUBLE_TAP_ALL_CARDS = res.getBoolean(R.bool.default_double_tap_all_cards);
        DEFAULT_DOUBLE_TAP_ENABLE = res.getBoolean(R.bool.default_double_tap_enable);
        DEFAULT_DOUBLE_TAP_FOUNDATION_FIRST = res.getBoolean(R.bool.default_double_tap_foundation_first);
        DEFAULT_LEFT_HANDED_MODE = res.getBoolean(R.bool.default_left_handed_mode);
        DEFAULT_GOLF_CYCLIC = res.getBoolean(R.bool.default_golf_cyclic);
        DEFAULT_TAP_TO_SELECT_ENABLED = res.getBoolean(R.bool.default_tap_to_select_enable);
        DEFAULT_SINGLE_TAP_ENABLED = res.getBoolean(R.bool.default_single_tap_enable);
        DEFAULT_AUTO_START_NEW_GAME = res.getBoolean(R.bool.default_auto_start_new_game);
        DEFAULT_CURRENT_GAME = res.getInteger(R.integer.default_current_game);
        DEFAULT_MENU_COLUMNS_LANDSCAPE = res.getString(R.string.default_menu_columns_landscape);
        DEFAULT_MENU_COLUMNS_PORTRAIT = res.getString(R.string.default_menu_columns_portrait);
        DEFAULT_MENU_BAR_POSITION_LANDSCAPE = res.getString(R.string.default_menu_bar_position_landscape);
        DEFAULT_MENU_BAR_POSITION_PORTRAIT = res.getString(R.string.default_menu_bar_position_portrait);
        DEFAULT_FIRST_RUN = res.getBoolean(R.bool.default_first_run);
        DEFAULT_WON = res.getBoolean(R.bool.default_won);
        DEFAULT_WON_AND_RELOADED = res.getBoolean(R.bool.default_won_and_reloaded);
        DEFAULT_MOVED_FIRST_CARD = res.getBoolean(R.bool.default_moved_first_card);
        DEFAULT_4_COLOR_MODE = res.getBoolean(R.bool.default_4_color_mode);
        DEFAULT_CARD_BACKGROUND = res.getInteger(R.integer.default_card_background);
        DEFAULT_CARD_BACKGROUND_COLOR = res.getInteger(R.integer.default_card_background_color);
        DEFAULT_WINNING_TIME = res.getInteger(R.integer.default_winning_time);
        DEFAULT_BACKGROUND_COLOR_TYPE = res.getInteger(R.integer.default_background_color_type);
        DEFAULT_BACKGROUND_COLOR = res.getString(R.string.default_background_color);
        DEFAULT_BACKGROUND_COLOR_CUSTOM = res.getInteger(R.integer.default_background_color_custom);
        DEFAULT_MOVEMENT_SPEED = res.getString(R.string.default_movement_speed);
        DEFAULT_SOUND_ENABLED = res.getBoolean(R.bool.default_sound_enabled);
        DEFAULT_WIN_SOUND = res.getString(R.string.default_win_sound);
        DEFAULT_BACKGROUND_MUSIC = res.getString(R.string.default_background_music);
        DEFAULT_BACKGROUND_VOLUME = res.getInteger(R.integer.default_background_volume);
        DEFAULT_VEGAS_BET_AMOUNT = res.getInteger(R.integer.default_vegas_bet_amount);
        DEFAULT_PYRAMID_NUMBER_OF_RECYCLES = res.getString(R.string.default_pyramid_number_of_recycles);
        DEFAULT_FORTYEIGHT_NUMBER_OF_RECYCLES = res.getString(R.string.default_fortyeight_number_of_recycles);
        DEFAULT_VEGAS_NUMBER_OF_RECYCLES = res.getString(R.string.default_vegas_number_of_recycles);
        DEFAULT_PYRAMID_LIMITED_RECYCLES = res.getBoolean(R.bool.default_pyramid_limited_recycles);
        DEFAULT_FORTYEIGHT_LIMITED_RECYCLES = res.getBoolean(R.bool.default_fortyeight_limited_recycles);
        DEFAULT_YUKON_RULES = res.getStringArray(R.array.pref_yukon_rules_values)[0];
        DEFAULT_KLONDIKE_DRAW = res.getStringArray(R.array.pref_draw_values)[0];
        DEFAULT_VEGAS_DRAW = res.getStringArray(R.array.pref_draw_values)[0];
        DEFAULT_CANFIELD_DRAW = res.getStringArray(R.array.pref_draw_values)[0];

        GAME_REDEAL_COUNT = res.getString(R.string.game_recycle_count);
        GAME_WON = res.getString(R.string.game_won);
        GAME_WON = res.getString(R.string.game_won_and_reloaded);
        GAME_NUMBER_OF_WON_GAMES = res.getString(R.string.game_number_of_won_games);
        GAME_NUMBER_OF_PLAYED_GAMES = res.getString(R.string.game_number_of_played_games);
        GAME_RANDOM_CARDS = res.getString(R.string.game_random_cards);
        GAME_FIRST_RUN = res.getString(R.string.game_first_run);
        GAME_MOVED_FIRST_CARD = res.getString(R.string.game_moved_first_card);
        GAME_SEED = res.getString(R.string.game_seed);

        RESTART_DIALOG = res.getString(R.string.restart_dialog);
        WON_DIALOG = res.getString(R.string.won_dialog);
        CANFIELD_START_CARD_VALUE = res.getString(R.string.canfield_start_value);
        SCORE = res.getString(R.string.score);
        SAVED_SCORES = res.getString(R.string.saved_scores);

        RECORD_LIST_ENTRY = res.getString(R.string.record_list_entry);
        RECORD_LIST_ENTRIES_SIZE = res.getString(R.string.record_list_entries_size);
        FLIP_CARD = res.getString(R.string.flip_card);
        ORIGIN = res.getString(R.string.origin);
        CARD = res.getString(R.string.card);
        CARDS = res.getString(R.string.cards);
        STACK = res.getString(R.string.stack);

        TIMER_END_TIME = res.getString(R.string.saved_current_time);
        TIMER_START_TIME = res.getString(R.string.saved_start_time);
        TIMER_WINNING_TIME = res.getString(R.string.saved_shown_time);

        CARD_DRAWABLES = res.getString(R.string.pref_key_card_drawables);
        CARD_BACKGROUND = res.getString(R.string.pref_key_cards_background);
        CARD_BACKGROUND_COLOR = res.getString(R.string.pref_key_cards_background_color);
        MENU_COLUMNS_PORTRAIT = res.getString(R.string.pref_key_menu_columns_portrait);
        MENU_COLUMNS_LANDSCAPE = res.getString(R.string.pref_key_menu_columns_landscape);

        NUMBER_OF_CARD_BACKGROUNDS = res.getInteger(R.integer.number_of_card_backgrounds);
        NUMBER_OF_CARD_THEMES = res.getInteger(R.integer.number_of_card_themes);
    }


    /**
     * Moves a card to a stack.
     *
     * @param card        The card to move
     * @param destination The destination of the movement
     */
    public static void moveToStack(Card card, Stack destination) {
        moveToStack(card, destination, 0);
    }

    /**
     * Moves a card to a stack. but with an additional option
     *
     * @param card        The card to move
     * @param destination The destination of the movement
     * @param option      The option to apply
     */
    public static void moveToStack(Card card, Stack destination, int option) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);

        ArrayList<Stack> destinations = new ArrayList<>();
        destinations.add(destination);

        moveToStack(cards, destinations, option);
    }

    /**
     * Moves multiple cards to a destination
     *
     * @param cards       The cards to move
     * @param destination The destination of the movement
     */
    public static void moveToStack(ArrayList<Card> cards, Stack destination) {
        moveToStack(cards, destination, 0);
    }

    /**
     * Moves multiple cards to a destination, with an additional option
     *
     * @param cards       The cards to move
     * @param destination The destination of the movement
     * @param option      The option to apply
     */
    public static void moveToStack(ArrayList<Card> cards, Stack destination, int option) {
        ArrayList<Stack> destinations = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++)
            destinations.add(destination);

        moveToStack(cards, destinations, option);
    }

    public static void moveToStack(ArrayList<Card> cards, ArrayList<Stack> destinations) {
        moveToStack(cards, destinations, 0);
    }

    /**
     * Moves multiple cards to multiple destinations, with an additional option
     * <p>
     * moves a card to a stack by doing this:
     * - change the score according to the cards
     * - add the cards to the record list
     * - move every card one by one
     * - bring the moving cards to front
     * - and start handlers to call some methods
     *
     * @param cards        The cards to move
     * @param destinations The destinations of the movements
     * @param option       The option to apply
     */
    public static void moveToStack(ArrayList<Card> cards, ArrayList<Stack> destinations, int option) {

        if (option == OPTION_UNDO) {
            scores.undo(cards, destinations);
        } else if (option == 0) {
            scores.move(cards, destinations);
            recordList.add(cards);
        } else if (option == OPTION_REVERSED_RECORD) {
            //reverse the cards and add the reversed list to the record
            ArrayList<Card> cardsReversed = new ArrayList<>();

            for (int i = 0; i < cards.size(); i++) {
                cardsReversed.add(cards.get(cards.size() - 1 - i));
            }

            recordList.add(cardsReversed);
            scores.move(cards, destinations);
        }

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getStack() == destinations.get(i)) {                                     //this means to flip a carf
                cards.get(i).flip();
            }
        }

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getStack() != destinations.get(i)) {
                cards.get(i).getStack().removeCard(cards.get(i));
                destinations.get(i).addCard(cards.get(i));
            }
        }

        for (Card card : cards) {
            card.view.bringToFront();
        }

        //following stuff in handlers, because they should wait until possible card movements are over.
        if (option == 0) {
            handlerTestAfterMove.sendEmptyMessageDelayed(0, 100);
            handlerTestIfWon.sendEmptyMessageDelayed(0, 200);
        }
    }

    /**
     * Saves data for games individually
     * Thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     * @param list The integer list to save
     */
    public static void putIntList(String name, List<Integer> list) {

        String s = "";
        for (int i : list) {
            s += i + ",";
        }
        savedGameData.edit().putString(name, s).apply();
    }

    // @GN
    public static void putLongArrayList(String name, ArrayList<Long> list) {
        String s = "";
        for (long i : list) {
            s += i + ",";
        }

        savedGameData.edit().putString(name, s).apply();
    }

    public static void saveErrorCount(String name, int errorCount) { // function to save error count when game is disrupted or app is closed.
        String s = " ";
        s += errorCount;

        System.out.println("Saving errorCount: " + s);

        //savedGameData.edit().putString(name, s).apply(); // saving errorCount as ERROR

    }

    /**
     * Gets data for games individually
     * Thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     */
    public static ArrayList<Integer> getIntList(String name) {
        String s = savedGameData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Integer> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(Integer.parseInt(st.nextToken()));
        }

        return result;
    }

    // @GN
    public static ArrayList<Long> getLongArrayList(String name) {
        String s = savedGameData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Long> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(Long.parseLong(st.nextToken()));
        }

        return result;
    }

    /**
     * Saves data for games individually
     * Thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     * @param list The long list to save
     */
    public static void putLongList(String name, List<Long> list) {
        String s = "";
        for (long i : list) {
            s += i + ",";
        }
        savedGameData.edit().putString(name, s).apply();
    }

    /**
     * Gets data for games individually
     * Thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     */
    public static ArrayList<Long> getLongList(String name) {
        String s = savedGameData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Long> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(Long.parseLong(st.nextToken()));
        }

        return result;
    }



    public static EntityMapper getEntityMapper() {
        return entityMapper;
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static void setAppDatabase(AppDatabase appDatabase) {
        SharedData.appDatabase = appDatabase;
    }

    /**
     * Gets data for games individually
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static Long getLong(String name, long defaultValue) {
        return savedGameData.getLong(name, defaultValue);
    }

    /**
     * Gets data for games individually
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static int getInt(String name, int defaultValue) {
        return savedGameData.getInt(name, defaultValue);
    }

    /**
     * Gets data for games individually
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static boolean getBoolean(String name, boolean defaultValue) {
        return savedGameData.getBoolean(name, defaultValue);
    }

    /**
     * Gets data for games individually
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static String getString(String name, String defaultValue) {
        return savedGameData.getString(name, defaultValue);
    }

    /**
     * Saves data for games individually
     *
     * @param name  The name in the shared pref
     * @param value The value to save
     */
    public static void putLong(String name, long value) {
        savedGameData.edit().putLong(name, value).apply();
    }

    /**
     * Saves data for games individually
     *
     * @param name  The name in the shared pref
     * @param value The value to save
     */
    public static void putInt(String name, int value) {
        savedGameData.edit().putInt(name, value).apply();
    }


    /**
     * Saves data for games individually
     *
     * @param name  The name in the shared pref
     * @param value The value to save
     */
    public static void putBoolean(String name, boolean value) {
        savedGameData.edit().putBoolean(name, value).apply();
    }

    public static void putStringList(String name, ArrayList<String> list) { // function to save the timestamps into savedGameData
        String s = " ";
        for (String i: list) {
            s += i + "-";
        }
        savedGameData.edit().putString(name, s).apply();
    }

    public static void putIntArrayList(String name, ArrayList<Integer> list) {
        String s = " ";
        for (int i: list) {
            s += i + "-";
        }
        savedGameData.edit().putString(name, s).apply();
    }

    public static void putIntArray(String name, int[] array) {
        String s = "";
        for(int i: array) {
            s += i + ",";
        }
        savedGameData.edit().putString(name, s).apply();
    }

    public static ArrayList<String> getStringList(String name) { // function to fetch timestamps saved in savedGameData
        String s = savedGameData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, "-");
        ArrayList<String> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }

        return result;
    }

    public static int[] getIntArray(String name) {
        String s = savedGameData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Integer> result = new ArrayList<>();

        int[] stackCounter = new int[15];
        int counter = 0;

        while (st.hasMoreTokens()) {
            //System.out.println("st: " + st.nextToken());
            result.add(Integer.parseInt(st.nextToken()));
        }

        for(Integer i : result) {
            stackCounter[counter++] = i.intValue();
        }

        return stackCounter;


    }

    /**
     * Gets data for shared data (same for every game)
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static int getSharedInt(String name, int defaultValue) {
        return savedSharedData.getInt(name, defaultValue);
    }

    /**
     * Gets data for shared data (same for every game)
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static String getSharedString(String name, String defaultValue) {
        return savedSharedData.getString(name, defaultValue);
    }

    /**
     * Gets data for shared data (same for every game)
     *
     * @param name         The name in the shared pref
     * @param defaultValue The default to apply, if not found
     */
    public static boolean getSharedBoolean(String name, boolean defaultValue) {
        return savedSharedData.getBoolean(name, defaultValue);
    }

    /**
     * Saves shared data (same for every game)
     *
     * @param name  The name in the shared pref
     * @param value The value to save
     */
    public static void putSharedInt(String name, int value) {
        savedSharedData.edit().putInt(name, value).apply();
    }

    /**
     * Saves shared data (same for every game)
     *
     * @param name  The name in the shared pref
     * @param value The value to save
     */
    public static void putSharedString(String name, String value) {
        savedSharedData.edit().putString(name, value).apply();
    }

    /**
     * Saves shared data (same for every game)
     * thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     * @param list The default tos ave
     */
    public static void putSharedIntList(String name, List<Integer> list) {
        //
        String s = "";
        for (int i : list) {
            s += i + ",";
        }
        savedSharedData.edit().putString(name, s).apply();
    }

    /**
     * Gets shared data (same for every game)
     * thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     */
    public static ArrayList<Integer> getSharedIntList(String name) {
        String s = savedSharedData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Integer> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(Integer.parseInt(st.nextToken()));
        }

        return result;
    }

    /**

     */
    public static void putMoveTime(Long move) {
        String s = savedSharedData.getString("MOVES", "");
        s += move + ",";
        savedSharedData.edit().putString("MOVES", s).apply();
        Log.d("MOVES", "Move has been logged at timestamp "+move);
    }

    /**
     * Gets shared data (same for every game)
     * thanks to this answer for this idea http://stackoverflow.com/a/11201225/7016229
     *
     * @param name The name in the shared pref
     */
    public static ArrayList<Long> getSharedLongList(String name) {
        String s = savedSharedData.getString(name, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Long> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(Long.parseLong(st.nextToken()));
        }

        return result;
    }

    /**
     * Little method I use to test if my code reaches some point
     *
     * @param text The text to show
     */
    public static void logText(String text) {
        Log.e("hey", text);
    }

    /**
     * Tests if the saved value equals the given default value
     *
     * @param name         The name of the key
     * @param defaultValue The default value of it
     * @return True if the current value saved is the default value
     */
    public static boolean sharedStringEqualsDefault(String name, String defaultValue) {
        return savedSharedData.getString(name, defaultValue).equals(defaultValue);
    }

    /**
     * Tests if the saved value equals the given value
     *
     * @param name         The name of the key
     * @param defaultValue The default value of it
     * @param testValue    The value to compare
     * @return True if the current value saved is the value to compare
     */
    public static boolean sharedStringEquals(String name, String defaultValue, String testValue) {
        return savedSharedData.getString(name, defaultValue).equals(testValue);
    }

    public static int min(int value1, int value2) {
        return value1 < value2 ? value1 : value2;
    }

    public static float min(float value1, float value2) {
        return value1 < value2 ? value1 : value2;
    }

    public static int max(int value1, int value2) {
        return value1 > value2 ? value1 : value2;
    }

    public static float max(float value1, float value2) {
        return value1 > value2 ? value1 : value2;
    }

    public static boolean leftHandedModeEnabled() {
        return getSharedBoolean(PREF_KEY_LEFT_HANDED_MODE, false);
    }


}