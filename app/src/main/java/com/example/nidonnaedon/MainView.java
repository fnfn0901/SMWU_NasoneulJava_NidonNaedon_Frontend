package com.example.nidonnaedon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nisonnaeson.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainView extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<ExpenseItem> myExpenseList;
    private List<ExpenseItem> sharedExpenseList;
    private boolean isMyExpense = true;
    private Button myExpenseButton;
    private Button sharedExpenseButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myExpenseList = new ArrayList<>();
        sharedExpenseList = new ArrayList<>();

        // 예제 데이터를 추가합니다.
        myExpenseList.add(new ExpenseItem("독일 여행", "2024.3.31~2024.4.3"));
        myExpenseList.add(new ExpenseItem("데이트 통장", "2024.5.6~2024.5.29"));

        sharedExpenseList.add(new ExpenseItem("지원이 서프라이즈", "2023.2.17~2023.2.20"));
        sharedExpenseList.add(new ExpenseItem("저녁 산책 모임", "2024.3.31~2024.6.12"));
        sharedExpenseList.add(new ExpenseItem("어버이날 선물", "2024.5.3~2024.5.8"));

        expenseAdapter = new ExpenseAdapter(myExpenseList, new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                intent.putExtra("itemName", myExpenseList.get(position).getName());
                startActivity(intent);
            }
        }, this);

        recyclerView.setAdapter(expenseAdapter);

        ImageView profileIcon = findViewById(R.id.profile_image);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MyPageView.class);
                startActivity(intent);
            }
        });

        myExpenseButton = findViewById(R.id.my_expense_button);
        sharedExpenseButton = findViewById(R.id.shared_expense_button);

        myExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyExpense = true;
                updateExpenseList();
                updateButtonStyles();
            }
        });

        sharedExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyExpense = false;
                updateExpenseList();
                updateButtonStyles();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MainActivity_page8.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE_ACCOUNT);
            }
        });

        // 기본 상태에서 "나의 가계부" 버튼이 눌려있도록 설정
        updateButtonStyles();
    }

    private void updateExpenseList() {
        if (isMyExpense) {
            expenseAdapter = new ExpenseAdapter(myExpenseList, new ExpenseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                    intent.putExtra("itemName", myExpenseList.get(position).getName());
                    startActivity(intent);
                }
            }, this);
        } else {
            expenseAdapter = new ExpenseAdapter(sharedExpenseList, new ExpenseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                    intent.putExtra("itemName", sharedExpenseList.get(position).getName());
                    startActivity(intent);
                }
            }, this);
        }

        recyclerView.setAdapter(expenseAdapter);
    }

    private void updateButtonStyles() {
        myExpenseButton.setBackgroundColor(isMyExpense ? Color.parseColor("#DCE7D5") : Color.parseColor("#BDD3D3"));
        sharedExpenseButton.setBackgroundColor(isMyExpense ? Color.parseColor("#BDD3D3") : Color.parseColor("#DCE7D5"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_ACCOUNT && resultCode == RESULT_OK && data != null) {
            String accountName = data.getStringExtra("ACCOUNT_NAME");
            String accountDate = data.getStringExtra("ACCOUNT_DATE");
            if (accountName != null && accountDate != null) {
                ExpenseItem newAccount = new ExpenseItem(accountName, accountDate);
                addAccountToList(newAccount, isMyExpense);
            }
        }
    }

    private void addAccountToList(ExpenseItem account, boolean isMyExpense) {
        if (isMyExpense) {
            myExpenseList.add(account);
        } else {
            sharedExpenseList.add(account);
        }
        updateExpenseList();
    }
}