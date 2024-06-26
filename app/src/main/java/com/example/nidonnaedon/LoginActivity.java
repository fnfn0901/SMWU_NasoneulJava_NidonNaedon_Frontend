package com.example.nidonnaedon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private TextView textView;
    private Button kakaoLoginButton, kakaoSignupButton;
    private NidonNaedonAPI nidonNaedonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new ChangeBounds());
        getWindow().setExitTransition(new ChangeBounds());

        flexboxLayout = new FlexboxLayout(this);
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);
        flexboxLayout.setAlignItems(AlignItems.CENTER);
        flexboxLayout.setBackgroundColor(Color.parseColor("#6db33f"));

        // Initialize the text view
        textView = new TextView(this);
        textView.setText("니돈내돈");
        textView.setTextSize(100);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.nanumpenscript_regular)); // Set custom font
        textView.setTransitionName("title_transition");

        FlexboxLayout.LayoutParams textLayoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(convertToPx(76), convertToPx(237), convertToPx(75), convertToPx(80));
        textView.setLayoutParams(textLayoutParams);
        flexboxLayout.addView(textView);

        // Initialize the Kakao login button
        kakaoLoginButton = new Button(this);
        kakaoLoginButton.setText("카카오톡 로그인");
        kakaoLoginButton.setTextColor(Color.BLACK);
        kakaoLoginButton.setTypeface(null, Typeface.BOLD);
        // Set background with rounded corners and yellow color
        GradientDrawable loginButtonShape = new GradientDrawable();
        loginButtonShape.setShape(GradientDrawable.RECTANGLE);
        loginButtonShape.setColor(Color.parseColor("#f9e000"));
        loginButtonShape.setCornerRadius(convertToPx(5)); // 5px border radius
        kakaoLoginButton.setBackground(loginButtonShape);
        FlexboxLayout.LayoutParams loginButtonParams = new FlexboxLayout.LayoutParams(
                convertToPx(297), convertToPx(48)); // Set width and height
        loginButtonParams.setMargins(0, convertToPx(20), 0, 0); // top margin
        kakaoLoginButton.setLayoutParams(loginButtonParams);
        flexboxLayout.addView(kakaoLoginButton);

        // Initialize the Kakao signup button
        kakaoSignupButton = new Button(this);
        kakaoSignupButton.setText("카카오톡 회원가입");
        kakaoSignupButton.setTextColor(Color.BLACK);
        kakaoSignupButton.setTypeface(null, Typeface.BOLD);
        // Set background with rounded corners and yellow color
        GradientDrawable signupButtonShape = new GradientDrawable();
        signupButtonShape.setShape(GradientDrawable.RECTANGLE);
        signupButtonShape.setColor(Color.parseColor("#f9e000"));
        signupButtonShape.setCornerRadius(convertToPx(5)); // 5px border radius
        kakaoSignupButton.setBackground(signupButtonShape);
        FlexboxLayout.LayoutParams signupButtonParams = new FlexboxLayout.LayoutParams(
                convertToPx(297), convertToPx(48)); // Set width and height
        signupButtonParams.setMargins(0, convertToPx(20), 0, 0); // top margin
        kakaoSignupButton.setLayoutParams(signupButtonParams);
        flexboxLayout.addView(kakaoSignupButton);

        // HTTP 로깅 인터셉터 추가
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic("user", "password")); // replace with your credentials
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // Localhost 주소 수정
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        setContentView(flexboxLayout);
    }

    // 로그인 성공 시 kakaoId 저장
    private void onLoginSuccess(String kakaoId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kakao_id", kakaoId);
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainView.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith("yourapp://callback")) {
            // 로그인 성공 후 서버에서 받은 데이터를 처리
            String code = uri.getQueryParameter("code");
            // 코드를 이용해 서버와 통신하여 사용자 정보를 받아오거나 토큰을 처리
            // 예: 서버에 이 코드를 보내서 사용자 인증 정보를 받아옵니다.
        }
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
