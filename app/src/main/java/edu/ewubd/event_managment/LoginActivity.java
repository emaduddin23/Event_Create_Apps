package edu.ewubd.event_managment;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import edu.ewubd.event_managment.R;


public class LoginActivity extends Activity{


    private EditText useridTF, passwordTF;
    private CheckBox rememberUserIdCB, rememberLoginCB;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String rememberVal = sharedPreferences.getString("remember", "");
        String userId = "";

        if(rememberVal.equals("login")){
            Intent i = new Intent(LoginActivity.this, UpcomingEventsActivity.class);
            startActivity(i);
            finish();
        }

        else if(rememberVal.equals("user_id")){
            userId = sharedPreferences.getString("user_id", "");
        }

        setContentView(R.layout.activity_login);

        prefsEditor = sharedPreferences.edit();

        useridTF = findViewById(R.id.etUserId);
        setEditTextMaxLength(useridTF,8);
        passwordTF = findViewById(R.id.etPassword);
        setEditTextMaxLength(passwordTF,4);

        rememberUserIdCB = findViewById(R.id.cbRememberUserId);
        rememberLoginCB = findViewById(R.id.cbRememberLogin);


        useridTF.setText(userId);


        findViewById(R.id.linkSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(i);

            }
        });


        rememberUserIdCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                rememberUserIdCB.setChecked(true);
                rememberLoginCB.setChecked(false);
                prefsEditor.putString("remember", "user_id");
                prefsEditor.commit();


            }
        });

        rememberLoginCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                rememberLoginCB.setChecked(true);
                rememberUserIdCB.setChecked(false);
                prefsEditor.putString("remember", "login");
                prefsEditor.commit();

            }
        });




        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                System.out.println("Exit button was pressed");
                LoginActivity.this.finish();


            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                System.out.println("Login button was pressed");
                String userid = useridTF.getText().toString();
                String password = passwordTF.getText().toString();

                String errorMsg = "";
                if(userid == null || userid.length() < 4){
                    errorMsg +="User id must have at least 4 characters.";
                }
                if(password == null || password.length() != 4){
                    errorMsg +="Password must have 4 characters.";
                }
                if(errorMsg.isEmpty()){
                    //SharedPreferences prefs = LoginActivity.this.getSharedPreferences("myPrefs, MODE_PRIVATE");
                    String user = sharedPreferences.getString("user_id",null);
                    String pass = sharedPreferences.getString("password",null);

//                    if(user == null || pass == null){
////                        prefsEditor.putString("user_id", userid);
////                        prefsEditor.putString("password", password);
//
//                        if(rememberUserIdCB.isChecked()){
//                            prefsEditor.putString("remember", "user_id");
//                            prefsEditor.commit();
//                            showDialog("Sign up Successfully", "Sign UP", "OK", false);
//                        }
//
//                        else if(rememberLoginCB.isChecked()){
//                            prefsEditor.putString("remember", "login");
//                            prefsEditor.commit();
//                            showDialog("Sign up Successfully", "Sign UP", "OK", false);
//                        }
//
////                        else{
////                            prefsEditor.commit();
////                            showDialog("Sign up Successfully", "Sign UP", "OK", false);
////                        }
//
//
//                    } else{
                        if(user.equals(userid) && pass.equals(password)){
                            Intent i = new Intent(LoginActivity.this, UpcomingEventsActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            showDialog("incorrect User id or password", "Login Error", "Back", true);
                        }
                   // }
                } else{
                    showDialog(errorMsg, "Login Error", "Back", true);
                }

            }
        });

    }



    public void setEditTextMaxLength(EditText et, int length){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }


    private void showDialog(String message, String title, String buttonLabel, boolean closeDialog){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message);
        builder.setTitle(title);

        //Setting message manually and performing action on button click
        //builder.setMessage("Do you want to close this application ?")
        builder.setCancelable(false)
                .setNegativeButton(buttonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(closeDialog){
                            dialog.cancel();
                        }
                        else{
                            //finish();
                            Intent i = new Intent(LoginActivity.this, UpcomingEventsActivity.class);
                            startActivity(i);
                        }

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();

    }

}









