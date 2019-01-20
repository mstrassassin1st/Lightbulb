package app.itdivision.lightbulb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class Register extends AppCompatActivity {

    EditText first_name;
    EditText last_name;
    EditText email;
    EditText password;
    EditText confirmpw;
    Button btnRegister;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.emailReg);
        password = (EditText) findViewById(R.id.passwordReg);
        confirmpw = (EditText) findViewById(R.id.confpasswordReg);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = first_name.getText().toString();
                String last = last_name.getText().toString();
                String em = email.getText().toString();
                String pw = password.getText().toString();
                String confpw = confirmpw.getText().toString();

                Date x = Calendar.getInstance().getTime();
                SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM yyyy");
                String finalDate = postFormater.format(x);
                Toast.makeText(Register.this, finalDate, Toast.LENGTH_LONG).show();

                //    Toast.makeText(Register.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();

                if(first.equals("") || em.equals("") || pw.equals("") || confpw.equals("")){
                    Toast.makeText(Register.this, "All forms must be filled!", Toast.LENGTH_SHORT).show();
                }else if(!isValidEmail(em)){
                    Toast.makeText(Register.this, "Email invalid!", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    password.setText("");
                    confirmpw.setText("");
                }else if(pw.length() < 8 ){
                    Toast.makeText(Register.this, "Password must contain at least 8 characters!", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmpw.setText("");
                }else if(confpw.equals(pw)) {
                    databaseAccess.open();
                    String name = first + " " + last;
                    int chck = databaseAccess.checkEmail(em);
                    if(chck == 0){
                        databaseAccess.getRegistered(name, em, pw, finalDate);
                        int id = databaseAccess.getLogin(em, pw);
                        if(id > 0){
                            showToast();
                            Intent intent = new Intent(Register.this, Homepage.class);
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
                        }else {
                            Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this, "You've already registered! Try Logging in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(Register.this, "Password and confirm password do not match!", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmpw.setText("");
                }

            }
        });

    }

    public void showToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastImage.setImageResource(R.drawable.ic_check_black_24dp);
        toastText.setText("Successfully Registered!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
