package com.project.digitalwellbeing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import static com.project.digitalwellbeing.utils.CommonDataArea.sharedPreferences;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.digitalwellbeing.utils.CommonDataArea;
import com.project.digitalwellbeing.utils.CommonFunctionArea;

public class LoginActivity extends AppCompatActivity {
    Button loginButton,signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        loginButton=(Button) findViewById(R.id.login_btn);
        signupButton=(Button) findViewById(R.id.signup_btn);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
       // new CommonFunctionArea().sendMessage(LoginActivity.this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(
                        CommonDataArea.prefName, Context.MODE_PRIVATE);
                CommonDataArea.editor = sharedPreferences.edit();
                int role = sharedPreferences.getInt(CommonDataArea.ROLESTR, 0);

                if (CommonDataArea.ROLE == R.id.role_parent) {
                    Intent intent = new Intent(LoginActivity.this, ChildActivity.class);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}
