package com.example.simpltestapp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText mPaymentEditText;
    private View mPayBillBtn;
    private TextView mSimpleRadioSubTextView;
    private String mSimpleRadioSubText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.simpl_toolbar);
        setSupportActionBar(myToolbar);
        mPaymentEditText = findViewById(R.id.payment_edittext);
        mPaymentEditText.requestFocus();
        mPaymentEditText.addTextChangedListener(new CurrencyTextWatcher());
        mPayBillBtn = findViewById(R.id.pay_bill_button);
        mSimpleRadioSubTextView = findViewById(R.id.simpl_radio_subtext);
        mSimpleRadioSubText = getString(R.string.charged_to_your_account);
    }

    private class CurrencyTextWatcher implements TextWatcher {
        private String previousCleanString;
        private static final int MAX_LENGTH = 20;
        private static final int MAX_DECIMAL = 3;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();

            String cleanString = str.replaceAll("[,]", "");

            if (cleanString.equals(previousCleanString)) {
                return;
            }
            if (cleanString.isEmpty()) {
                updateButtonAndText(cleanString, "");
                return;
            }
            previousCleanString = cleanString;

            String formattedString;
            if (cleanString.contains(".")) {
                formattedString = formatDecimal(cleanString);
            } else {
                formattedString = formatInteger(cleanString);
            }
            mPaymentEditText.removeTextChangedListener(this);
            mPaymentEditText.setText(formattedString);
            handleSelection();
            mPaymentEditText.addTextChangedListener(this);
            updateButtonAndText(cleanString, formattedString);
        }

        private String formatInteger(String str) {
            BigDecimal parsed = new BigDecimal(str);
            DecimalFormat formatter =
                    new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.US));
            return formatter.format(parsed);
        }

        private String formatDecimal(String str) {
            BigDecimal parsed = new BigDecimal(str);
            DecimalFormat formatter = new DecimalFormat( "#,###." + getDecimalPattern(str),
                    new DecimalFormatSymbols(Locale.US));
            formatter.setRoundingMode(RoundingMode.DOWN);
            return formatter.format(parsed);
        }

        private String getDecimalPattern(String str) {
            int decimalCount = str.length() - str.indexOf(".") - 1;
            StringBuilder decimalPattern = new StringBuilder();
            for (int i = 0; i < decimalCount && i < MAX_DECIMAL; i++) {
                decimalPattern.append("0");
            }
            return decimalPattern.toString();
        }

        private void handleSelection() {
            if (mPaymentEditText.getText().length() <= MAX_LENGTH) {
                mPaymentEditText.setSelection(mPaymentEditText.getText().length());
            } else {
                mPaymentEditText.setSelection(MAX_LENGTH);
            }
        }
    }


    private void updateButtonAndText(String cleanString, String formattedString) {
        try {
            double amount = Double.parseDouble(cleanString);
            if (amount > 0) {
                mPayBillBtn.setEnabled(true);
                mSimpleRadioSubTextView.setText(String.format(mSimpleRadioSubText, formattedString));
                mSimpleRadioSubTextView.setVisibility(View.VISIBLE);
            } else {
                mPayBillBtn.setEnabled(false);
                mSimpleRadioSubTextView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            mPayBillBtn.setEnabled(false);
            mSimpleRadioSubTextView.setVisibility(View.GONE);
        }
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }
}
