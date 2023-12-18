package com.example.bank;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bank.DatabaseHelper;

public class TransferActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private EditText editSourceAccount, editTargetAccount, editTransferAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        myDb = new DatabaseHelper(this);
        editSourceAccount = findViewById(R.id.editText_source_account);
        editTargetAccount = findViewById(R.id.editText_target_account);
        editTransferAmount = findViewById(R.id.editText_transfer_amount);

        findViewById(R.id.button_perform_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTransfer();
            }
        });
    }

    private void performTransfer() {
        String sourceAccount = editSourceAccount.getText().toString();
        String targetAccount = editTargetAccount.getText().toString();
        String amountString = editTransferAmount.getText().toString();
        int amount;

        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            Toast.makeText(TransferActivity.this, "Invalid amount", Toast.LENGTH_LONG).show();
            return;
        }

        if (myDb.transferFunds(sourceAccount, targetAccount, amount)) {
            Toast.makeText(TransferActivity.this, "Transfer successful", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(TransferActivity.this, "Transfer failed", Toast.LENGTH_LONG).show();
        }
    }
}
