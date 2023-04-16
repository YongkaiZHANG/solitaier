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

package be.kuleuven.drsolitaire.ui;

import static android.view.View.GONE;
import static be.kuleuven.drsolitaire.SharedData.currentGame;
import static be.kuleuven.drsolitaire.SharedData.gameLogic;
import static be.kuleuven.drsolitaire.SharedData.scores;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.CustomAppCompatActivity;
import be.kuleuven.drsolitaire.database.AppDatabase;
import be.kuleuven.drsolitaire.database.MyThread;
import be.kuleuven.drsolitaire.dialogs.DialogHighScoreDelete;
import be.kuleuven.drsolitaire.helper.Scores;

public class Statistics extends CustomAppCompatActivity {

    private TableLayout tableLayout;
    private TextView textWonGames, textWinPercentage, textAdditonalStatistics;
    private Toast toast;
    private String dollar;


    /**
     * Loads the high score list
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_statistics);

        //



        int padding = (int) getResources().getDimension(R.dimen.statistics_table_padding);
        int textSize = getResources().getInteger(R.integer.statistics_text_size);
        boolean addedEntries = false;
        TableRow row;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tableLayout = (TableLayout) findViewById(R.id.statisticsTableHighScores);
        textWonGames = (TextView) findViewById(R.id.statisticsTextViewGamesWon);
        textWinPercentage = (TextView) findViewById(R.id.statisticsTextViewWinPercentage);
        textAdditonalStatistics = (TextView) findViewById(R.id.statisticsAdditionalText);

        //if the app got killed while the statistics are open and then the user restarts the app,
        //my helper classes aren't initialized so they can't be used. In this case, simply
        //close the statistics
        try {
            loadData();
            // to set the values of text like the information or the graph
            set_ability_score();
            setInfo();
            getData();

        } catch (NullPointerException e) {
            finish();
            return;
        }

        for (int i = 0; i < Scores.MAX_SAVED_SCORES; i++) {                                         //for each entry in highScores, add a new view with it
            if (scores.get(i, 0) == 0)                                                              //if the score is zero, don't show it
                continue;

            if (!addedEntries)
                addedEntries = true;

            row = new TableRow(this);

            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            TextView textView3 = new TextView(this);
            TextView textView4 = new TextView(this);

            textView1.setText(String.format(Locale.getDefault(),
                    "%s %s", scores.get(i, 0),dollar));
            textView2.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d",               //add it to the view
                    scores.get(i, 1) / 3600,
                    (scores.get(i, 1) % 3600) / 60,
                    (scores.get(i, 1) % 60)));

            textView3.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(scores.get(i, 2)));
            //textView4.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(scores.get(i,2)));
            textView4.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(scores.get(i, 2)));

            textView1.setPadding(padding, 0, padding, 0);
            textView2.setPadding(padding, 0, padding, 0);
            textView3.setPadding(padding, 0, padding, 0);
            textView4.setPadding(padding, 0, padding, 0);

            textView1.setTextSize(textSize);
            textView2.setTextSize(textSize);
            textView3.setTextSize(textSize);
            textView4.setTextSize(textSize);

            textView1.setGravity(Gravity.CENTER);
            textView2.setGravity(Gravity.CENTER);
            textView3.setGravity(Gravity.CENTER);
            textView4.setGravity(Gravity.CENTER);

            row.addView(textView1);
            row.addView(textView2);
            row.addView(textView3);
            row.addView(textView4);
            row.setGravity(Gravity.CENTER);
            tableLayout.addView(row);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * loads the other shown data
     */
    private void loadData() {
        int wonGames = gameLogic.getNumberWonGames();
        int totalGames = gameLogic.getNumberOfPlayedGames();

        textWonGames.setText(String.format(Locale.getDefault(), getString(R.string.statistics_text_won_games), wonGames, totalGames));
        textWinPercentage.setText(String.format(Locale.getDefault(), getString(R.string.statistics_win_percentage), totalGames > 0 ? ((float) wonGames * 100 / totalGames) : 0.0));
        dollar = currentGame.isPointsInDollar() ? "$" : "";

        String additionalText = currentGame.getAdditionalStatisticsData(getResources());

        if (additionalText != null) {
            textAdditonalStatistics.setText(additionalText);
            textAdditonalStatistics.setVisibility(View.VISIBLE);
        }
    }

    /**
     * deletes the data, reloads it and hides the high score list (easier way to "delete" it)
     */
    public void deleteHighScores() {
        scores.deleteHighScores();
        gameLogic.deleteStatistics();
        currentGame.deleteAdditionalStatisticsData();
        loadData();
        tableLayout.setVisibility(GONE);
        showToast(getString(R.string.statistics_button_deleted_all_entries));
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistics, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                DialogFragment deleteDialog = new DialogHighScoreDelete();
                deleteDialog.show(getSupportFragmentManager(), "high_score_delete");
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    private void showToast(String text) {
        if (toast == null)
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        else
            toast.setText(text);

        toast.show();
    }
    /**
     *  deal with the ability score also set other text value
     * we assume we can get the data and also set the
     * time, accuracy, solved_rate, score, values
     */
    public void set_ability_score(){
        //there are four parts to consist the score, time takes 15p
        //scores obtained takes 15p, solved rate  takes 35p and the accuracy takes 35p
        double ability_score;
        //the time takes in seconds, full point is 240s, zero point is 1800s
        //so between them, we calculate by ratio,1800-240=1560,(1-(time-240)/1560)*15=score_time
        double time=500;
        TextView text_time=findViewById(R.id.time_spend);
        text_time.setText(String.valueOf((int)time)+" Sec");
        if(time>900){
            text_time.setTextColor(Color.parseColor("#FFB751"));
        }else{
            text_time.setTextColor(Color.parseColor("#0F8A4F"));
        }
        //the score get can be negative, so over -1000 is zero, and over 500 is full point
        // so we can count by (score+1000)/1500*15
        double score_get=456;
        TextView text_score=findViewById(R.id.score_average);
        text_score.setText(String.valueOf((int)score_get));
        if(time>-100){
            text_score.setTextColor(Color.parseColor("#0F8A4F"));
        }else{
            text_score.setTextColor(Color.parseColor("#FFB751"));
        }
        //this part can be solved_rate*35
        double solved_rate=0.65;
        TextView text_solved_rate=findViewById(R.id.select_accuracy);
        int rate_phrase=(int)(solved_rate*100);
        String percentage=String.format("%d%%",rate_phrase);
        if(rate_phrase%10!=0){
            percentage = String.format("%.1f%%", solved_rate * 100);
        }
        text_solved_rate.setText(percentage);
        if(solved_rate>0.7){
            text_solved_rate.setTextColor(Color.parseColor("#0F8A4F"));
        }else{
            text_solved_rate.setTextColor(Color.parseColor("#FFB751"));
        }
        //this part can be accuracy*35
        double accuracy=0.67;
        TextView text_accuracy=findViewById(R.id.move_accuracy);
         rate_phrase=(int)(accuracy*100);
        percentage=String.format("%d%%",rate_phrase);
        if(rate_phrase%10!=0){
            percentage = String.format("%.1f%%", accuracy * 100);
        }
        text_accuracy.setText(percentage);
        if(solved_rate>0.7){
            text_accuracy.setTextColor(Color.parseColor("#0F8A4F"));
        }else{
            text_accuracy.setTextColor(Color.parseColor("#FFB751"));
        }
        //calculate the time score
        if(time<=240){
            time=15;
        } else if (time>=1800) {
            time=0;
        }else {
            time=(1-(time-240)/1560)*15;
        }
        //calculate the score's score
        if(score_get<=-1000){
            score_get=0;
        } else if (score_get>=500) {
            score_get=15;
        }else {
            score_get=((score_get+1000)/1500)*15;
        }
        //calculate the solved rate score
        solved_rate=solved_rate*35;
        //calculate the accuracy score
        accuracy=accuracy*35;
        //here to create the radar chart
        create_radar_chart(time,accuracy,solved_rate,score_get);
        //create the bar chart
        create_game_results_bar_chart();
        //create the pie chart
        create_accuracy_pie_chart();
        // calculate the whole score
        ability_score=time+score_get+solved_rate+accuracy;
        //find the text view
        TextView Text_ability_score=findViewById(R.id.the_ability_score);
        Text_ability_score.setText(String.valueOf((int)ability_score));
        if(ability_score>60){
            Text_ability_score.setTextColor(Color.parseColor("#0F8A4F"));
        }else{
            Text_ability_score.setTextColor(Color.parseColor("#FFB751"));
        }

    }
    /**
     * this function is to create the radar chart
     */
    public void create_radar_chart(double time, double accuracy, double solved_rate, double score) {
        RadarChart chart_id = findViewById(R.id.ability_RadarChart);
        RadarDataSet dataset=new RadarDataSet(data_value_radar(time,accuracy,solved_rate,score),"Ability Chart");
        dataset.setColor(Color.parseColor("#5932EA"));
        RadarData data=new RadarData();
        data.addDataSet(dataset);
        String[] labels={"time","accuracy","solved rate","score"};
        XAxis axis=chart_id.getXAxis();
        axis.setValueFormatter(new IndexAxisValueFormatter(labels));
        chart_id.setData(data);
    }

    /**
     * This function create_entry is used to create the RadarEntry objects
     * which are the data points that will be used to create the radar chart.
     * @param
     * @return
     */
    private ArrayList<RadarEntry> data_value_radar(double time, double accuracy, double solved_rate, double score ) {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry((float) time));
        entries.add(new RadarEntry((float) accuracy));
        entries.add(new RadarEntry((float) solved_rate));
        entries.add(new RadarEntry((float) score));
        return entries;
    }

    /**
     * this function is to create the results bar_chart
     */
    public void create_game_results_bar_chart() {
        //here we assume we have the average score of the time, hint, undo, move
        float score_aver = 100;
        float hint_aver = 30;
        float undo_aver = 45;
        float move_aver = 307;
        //here we get the data from user
        float score = 200;
        float hint = 10;
        float undo = 20;
        float move = 238;

        BarChart barChart = findViewById(R.id.Results_chart);

        // Set layout parameters for the chart
        ViewGroup.LayoutParams layoutParams = barChart.getLayoutParams();
        layoutParams.width = (int) getResources().getDimension(R.dimen.chart_width);
        layoutParams.height = (int) getResources().getDimension(R.dimen.chart_height);
        barChart.setLayoutParams(layoutParams);

        // Set margins for the chart to fit within the CardView
        int marginPx = (int) getResources().getDimension(R.dimen.chart_margin);
        barChart.setExtraTopOffset(marginPx);
        barChart.setExtraBottomOffset(marginPx);
        barChart.setExtraLeftOffset(marginPx);
        barChart.setExtraRightOffset(marginPx);

        BarDataSet barDataSet_average = new BarDataSet(average_value(score_aver, hint_aver, undo_aver, move_aver), "Average");
        BarDataSet barDataSet_user = new BarDataSet(user_value(score, hint, undo, move), "Yours");

        // Set color
        barDataSet_average.setColor(Color.parseColor("#F2EFFF"));
        barDataSet_user.setColor(Color.parseColor("#5932EA"));

        BarData data = new BarData(barDataSet_average, barDataSet_user);
        barChart.setData(data);

        // Set label
        String[] actions = new String[]{"Score", "Hint", "Undo", "Move"};

        // X axis
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(actions));
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setXOffset(0f);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setAxisMinimum(0);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(actions));
        xAxis.setCenterAxisLabels(true);
        xAxis.setXOffset(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMinimum(0);


        // Y axis
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setGranularity(1);
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setAxisMinimum(0);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        barChart.setVisibleXRangeMaximum(4);
        barChart.setDragEnabled(true);

        // to have the mark of the data
        barDataSet_average.setLabel("Average");
        barDataSet_user.setLabel("Yours");
        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);


        float groupSpace = 0.4f;
        float barSpace = 0;
        data.setBarWidth(0.3f);

        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 4);
        barChart.groupBars(0, groupSpace, barSpace);


        barChart.invalidate();
    }

    /**
     * this two function  is to create the data for bar chart
     */
    private ArrayList<BarEntry> average_value(float score, float hint, float undo, float move) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, score));
        barEntries.add(new BarEntry(2, hint));
        barEntries.add(new BarEntry(3, undo));
        barEntries.add(new BarEntry(4, move));
        return barEntries;
    }
    private ArrayList<BarEntry> user_value(float score, float hint, float undo, float move) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, score));
        barEntries.add(new BarEntry(2, hint));
        barEntries.add(new BarEntry(3, undo));
        barEntries.add(new BarEntry(4, move));
        return barEntries;
    }
    /**
     * this function is to create the accuracy pie chart
     */
    public void create_accuracy_pie_chart(){
        //we assume we know the correct value and the wrong value
        int correct=201;
        int wrong=91;
        PieChart pieChart=findViewById(R.id.accuracy_chart);
        int[] colorArray=new int[]{Color.parseColor("#0F8A4F"),Color.parseColor("#BE0707")};

        PieDataSet pieDataSet= new PieDataSet(accuracy_value(correct,wrong),"Accuracy");
        pieDataSet.setColors(colorArray);

        PieData pieData= new PieData(pieDataSet);
        pieChart.setData(pieData);
    }

    /**
     * this function will create the entity of the pie chart data
     */
    private ArrayList<PieEntry> accuracy_value(int correct, int wrong){
        ArrayList<PieEntry> data_value= new ArrayList<PieEntry>();
        data_value.add(new PieEntry(correct,"correct action"));
        data_value.add(new PieEntry(wrong," wrong action"));
        return data_value;
    }
    /**
     * this function is use to set the info_icon works
     */
    private void setInfo(){
        //set the info dialog context of each icon
        String abilityScore_info;
        abilityScore_info="Here is the test info. And you will see this is the ability score.";
        String moveTime_info="Here is the test info. And you will see this is the move time.";
        String averageScore_info="Here is the test info. And you will see this is the average score.";
        String selectAccuracy_info="Here is the test info. And you will see this is the accuracy information.";
        String gameResult_info="Here is the test info. And you will see this is the game results.";
        String moveAccuracy_Info="Here is the test info. And you will see this is the move accuracy.";

        //bund the icon
        ImageView icon_abilityScore=findViewById(R.id.info_ablityScore);
        ImageView icon_moveTime=findViewById(R.id.info_moveTime);
        ImageView icon_averageScore=findViewById(R.id.info_averageScore);
        ImageView icon_selectAccuracy=findViewById(R.id.info_selectAccuracy);
        ImageView icon_gameResult=findViewById(R.id.info_gameResult);
        ImageView icon_moveAccuracy=findViewById(R.id.info_moveAccuracy);

        //here to set the onlisten event
        icon_abilityScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(abilityScore_info);
            }
        });
        icon_moveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(moveTime_info);
            }
        });
        icon_averageScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(averageScore_info);
            }
        });
        icon_selectAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(selectAccuracy_info);
            }
        });
        icon_gameResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(gameResult_info);
            }
        });
        icon_moveAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(moveAccuracy_Info);
            }
        });
    }
    /**
     * this is the function to show the dialog
     */
    private void showMyDialog(String context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * this is a function to get the data from the database
     */

    public void getData(){
        String url = "jdbc:mysql://icts-db-mysqldb2.icts.kuleuven.be:3306/www_drsolitaire?user=www_drsolitaire&password=JMmS7YxI5kO2pxeU";
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, url).build();
        MyThread myThread=new MyThread(db,findViewById(R.id.testData));
        myThread.start();
    }

}
