package edu.ewubd.event_managment;

//import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;

import edu.ewubd.event_managment.R;


public class ProfileActivity extends Activity{


    private EditText nameTF,emailTF, phoneTF, userIdTF, password1TF, password2TF;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // to check the email pattern validation
    // Reference Link: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        nameTF = findViewById(R.id.etName);
        setEditTextMaxLength(nameTF,10);
        emailTF = findViewById(R.id.etEmail);
        setEditTextMaxLength(emailTF,20);
        phoneTF = findViewById(R.id.etPhone);
        setEditTextMaxLength(phoneTF,11);
        userIdTF = findViewById(R.id.etUserId);
        setEditTextMaxLength(userIdTF,15);
        password1TF = findViewById(R.id.etPW1);
        password2TF = findViewById(R.id.etPW2);


        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                //System.out.println("Cancel button was pressed");

                ProfileActivity.this.finish();
            }
        });


        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                saveProfileData();
            }
        });

    }




    private void saveProfileData(){


        String name = nameTF.getText().toString();
        String email = emailTF.getText().toString();
        String phone = phoneTF.getText().toString();
        String userid = userIdTF.getText().toString();
        String pw1 = password1TF.getText().toString();
        String pw2 = password2TF.getText().toString();



        System.out.println("Name: " + name);
        System.out.println("E-mail: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("User Id: " + userid);
        System.out.println("Password1: " + pw1);
        System.out.println("Password2: " + pw2);


        String errorMsg = "";


        if(name == null) {

            errorMsg = "Name Can't Be Empty. ";
        }

        if(email.isEmpty()) {
            errorMsg += "E-mail Can't Be Empty. ";
        }

        if(!(email.trim().matches(emailPattern))) {
            errorMsg += "E-mail address is not valid. ";
        }

        if(phone.isEmpty() || phone.length() != 11){
            errorMsg += "phone Can't Be Empty and must be 11 digits. ";
        }

        if(userid.isEmpty() || userid.length() < 4){
            errorMsg += "User Id must have at least 4 characters. ";
        }

        if(pw1.isEmpty() || pw1.length() != 4){
            errorMsg += "Password Must have 4 characters. ";
        }

        if(pw2.isEmpty() || pw2.length() != 4){
            errorMsg += "Confirm password Must have 4 characters. ";
        }

        if((pw1.equals(pw2)) != true){
            errorMsg += "Password and confirm password does not match. ";
        }


        if(errorMsg.isEmpty()) {
            // save data in database

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString("user_id", userid);
            prefsEditor.putString("password", pw1);
            prefsEditor.putString("user_name", name);
            prefsEditor.putString("user_email", email);
            prefsEditor.putString("user_phone", phone);
            prefsEditor.commit();


            // Store Profile Information in Remote Server

            showDialog("Profile information has been saved successfully", "Info","Go", false);
        } else {

            showDialog(errorMsg, "Error in Profile information", "Back", true);
        }

    }

    public void setEditTextMaxLength(EditText et, int length){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }
//
//
    private void showDialog(String message, String title, String buttonLabel, boolean closeDialog){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);


        builder.setCancelable(false)
                .setNegativeButton(buttonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(closeDialog){
                            dialog.cancel();
                        }
                        else{
                            finish();
                        }

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();

    }


}
