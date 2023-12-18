package com.example.bank;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bank.DatabaseHelper;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewTransactions;
    DatabaseHelper myDb;
    ArrayList<String> listItem;
    ArrayAdapter adapter;

    private void viewData() {
        Cursor cursor = myDb.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int balance = myDb.getAccountBalance(cursor.getString(3));
                listItem.add("Account: " + cursor.getString(3) + ", Amount: " + cursor.getString(2) + ", Balance: " + balance);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            listViewTransactions.setAdapter(adapter);
        }
        cursor.close();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewTransactions = findViewById(R.id.listView_transactions);
        myDb = new DatabaseHelper(this);
        listItem = new ArrayList<>();
        viewData();
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
