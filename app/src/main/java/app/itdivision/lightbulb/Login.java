package app.itdivision.lightbulb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button btnLogin;
    Button btn_forgot_password;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btn_forgot_password = (Button) findViewById(R.id.btn_forgot_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etemail = email.getText().toString();
                String pw = password.getText().toString();
                if(etemail.equals("") || pw.equals("")){
                    Toast.makeText(Login.this,"Username or password is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                    databaseAccess.open();
                    int id = databaseAccess.getLogin(etemail, pw);
                    if(id > 0){
                        showToast();
                        Intent intent = new Intent(Login.this, Homepage.class);
                        startActivity(intent);
                        databaseAccess.setHasSignedIn(id);
                        ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
                        activeIdPassing.setActiveId(id);

                        int ctr = 0;

                        ctr += databaseAccess.getCompletedCourse(id, 1,1);
                        ctr += databaseAccess.getCompletedCourse(id, 2,1);
                        ctr += databaseAccess.getCompletedCourse(id, 3,1);
                        ctr += databaseAccess.getCompletedCourse(id, 4,1);
                        ctr += databaseAccess.getCompletedCourse(id, 5,1);
                        ctr += databaseAccess.getCompletedCourse(id, 6,1);

                        String award = " ";
                        if(ctr <= 10){
                            award = "Bronze Medal";
                            activeIdPassing.setReward(award);
                        }else if( ctr <= 20){
                            award = "Silver Medal";
                            activeIdPassing.setReward(award);
                        }else{
                            award = "Gold Medal";
                            activeIdPassing.setReward(award);
                        }

                        databaseAccess.close();
                        finish();
                    }else{
                        Toast.makeText(Login.this, "Login failed! Recheck Username and Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public void showToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastImage.setImageResource(R.drawable.ic_check_black_24dp);
        toastText.setText("Successfully Logged In!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public boolean shouldAllowBack(){
        return false;
    }
    public void doNothing(){
        finish();
        moveTaskToBack(true);
    }

    @Override
    public void onBackPressed() {
        if (!shouldAllowBack()) {
            doNothing();
        } else {
            super.onBackPressed();
        }
    }
}
