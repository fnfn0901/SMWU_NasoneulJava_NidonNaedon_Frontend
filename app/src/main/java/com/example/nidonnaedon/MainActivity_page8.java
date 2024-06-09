package com.example.nidonnaedon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.nisonnaeson.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
import com.google.android.material.datepicker.DateValidatorPointForward;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity_page8 extends AppCompatActivity {

    private EditText accountName;
    private EditText date;
    private Spinner currencySpinner;
    private Button applyExchangeRateButton;
    private EditText participantName;
    private Button addParticipantButton;
    private Button createAccountButton;
    private LinearLayout participantList;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page8);

        accountName = findViewById(R.id.account_name);
        date = findViewById(R.id.date);
        currencySpinner = findViewById(R.id.currency_spinner);
        applyExchangeRateButton = findViewById(R.id.apply_exchange_rate_button);
        participantName = findViewById(R.id.participant_name);
        addParticipantButton = findViewById(R.id.add_participant_button);
        createAccountButton = findViewById(R.id.create_account_button);
        participantList = findViewById(R.id.participant_list);

        // 뒤로 가기 버튼 설정
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 통화 선택 스피너 설정
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.currency_array)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(formatCurrencyString(textView.getText().toString()));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(formatCurrencyString(textView.getText().toString()));
                return view;
            }

            private String formatCurrencyString(String original) {
                if (original.contains("-")) {
                    String[] parts = original.split("-");
                    return parts[0].trim() + "    " + parts[1].trim();
                }
                return original;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapter);

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCurrency = parentView.getItemAtPosition(position).toString();
                // 선택된 통화에 따라 환율 적용 등의 로직을 추가합니다.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리를 여기에 추가합니다.
            }
        });

        // 참여자 추가 버튼 클릭 시 처리
        addParticipantButton.setOnClickListener(v -> {
            String participant = participantName.getText().toString();
            if (!participant.isEmpty()) {
                TextView textView = new TextView(MainActivity_page8.this);
                textView.setText(participant);
                participantList.addView(textView);
                participantName.setText(""); // 입력 필드를 초기화합니다.
            } else {
                participantName.setError("참가자 이름을 입력하세요.");
            }
        });

        // 계좌 생성 버튼 클릭 시 처리
        createAccountButton.setOnClickListener(v -> {
            String newAccountName = accountName.getText().toString();
            String newAccountDate = date.getText().toString();

            if (validateFields(newAccountName, newAccountDate)) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACCOUNT_NAME", newAccountName);
                resultIntent.putExtra("ACCOUNT_DATE", newAccountDate);
                setResult(RESULT_OK, resultIntent);
                finish(); // 이전 화면으로 돌아가기
            }
        });

        // 캘린더 아이콘 클릭 이벤트 설정
        findViewById(R.id.calendar_icon).setOnClickListener(this::showDatePicker);
    }

    // 필드 검증 함수
    private boolean validateFields(String accountName, String accountDate) {
        boolean isValid = true;

        if (TextUtils.isEmpty(accountName)) {
            this.accountName.setError("가계부 이름을 입력하세요.");
            isValid = false;
        }
        if (TextUtils.isEmpty(accountDate)) {
            this.date.setError("날짜를 선택하세요.");
            isValid = false;
        }
        return isValid;
    }

    // 캘린더 아이콘 클릭 이벤트 핸들러
    public void showDatePicker(View view) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now());

        Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("날짜 선택")
                .setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<Pair<Long, Long>> dateRangePicker = builder.build();

        dateRangePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        startDate.setTimeInMillis(selection.first);
                        endDate.setTimeInMillis(selection.second);
                        updateDateField();
                    }
                });

        dateRangePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void updateDateField() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String dateRange = sdf.format(startDate.getTime()) + " - " + sdf.format(endDate.getTime());
        date.setText(dateRange);
    }
}