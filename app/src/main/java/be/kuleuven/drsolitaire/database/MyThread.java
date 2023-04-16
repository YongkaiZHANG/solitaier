package be.kuleuven.drsolitaire.database;

import android.widget.TextView;

import java.util.List;

import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.classes.Move;
//this class is the new thread to deal with the asynchronize data fetch from the database
public class MyThread extends Thread {
    private AppDatabase db;
    private TextView textView;

    public MyThread(AppDatabase db, TextView textView) {
        this.db = db;
        this.textView = textView;
    }

    @Override
    public void run() {
        List<Integer> movetest = db.moveDAO().getIdOfmove();
        //here if I didn't get data from database or get just for test 
        if(movetest.isEmpty()){
            textView.post(() -> textView.setText("Ep"));
        }else {
            textView.post(() -> textView.setText(String.valueOf(movetest.get(0))));
        }
    }
}

