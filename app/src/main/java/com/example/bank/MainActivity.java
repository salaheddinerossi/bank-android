package com.example.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bank.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editAmount, editAccount;
    RadioButton debitButton, creditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);
        editAmount = findViewById(R.id.editText_amount);
        editAccount = findViewById(R.id.editText_account);
        debitButton = findViewById(R.id.radioButton_debit);
        creditButton = findViewById(R.id.radioButton_credit);

        findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        findViewById(R.id.button_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        Button transferButton = findViewById(R.id.button_transfer);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferIntent = new Intent(MainActivity.this, TransferActivity.class);
                startActivity(transferIntent);
            }
        });

}

    private void submitData() {
        String type = debitButton.isChecked() ? "Debit" : "Credit";
        String amount = editAmount.getText().toString();
        String account = editAccount.getText().toString();
        String date = "2023-01-01";

        if (account.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter an account name", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isInserted = myDb.insertData(type, amount, account, date);
        if (isInserted) {
            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
            updateBalanceDisplay(account);
        } else {
            Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
        }
    }

    private void updateBalanceDisplay(String account) {
        int balance = myDb.getAccountBalance(account);
        TextView textViewCurrentBalance = findViewById(R.id.textView_currentBalance);
        textViewCurrentBalance.setText(String.format("Current Balance: $%d", balance));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String account = editAccount.getText().toString();
        if (!account.isEmpty()) {
            updateBalanceDisplay(account);
        }
    }
}
