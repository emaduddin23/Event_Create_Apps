package edu.ewubd.event_managment;

//import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.annotation.SuppressLint;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import java.util.ArrayList;
import java.util.List;

import edu.ewubd.event_managment.R;


public class EventFormActivity extends Activity{


    private EditText nameTF, placeTF, dateTimeTF, capacityTF, budgetTF, emailTF, phoneTF, descriptionTF;
    private RadioButton indoorRB, outdoorRB, onlineRB;
    private String existingKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nameTF = findViewById(R.id.etName);
        setEditTextMaxLength(nameTF,20);
        placeTF = findViewById(R.id.etPlace);
        descriptionTF = findViewById(R.id.etDescription);
        capacityTF = findViewById(R.id.etCapacity);
        budgetTF = findViewById(R.id.etBudget);
        emailTF = findViewById(R.id.etEmail);
        phoneTF = findViewById(R.id.etPhone);
        dateTimeTF = findViewById(R.id.etDateTime);


        indoorRB = findViewById(R.id.rbIndoor);
        outdoorRB = findViewById(R.id.rbOutdoor);
        onlineRB = findViewById(R.id.rbOnline);

        Intent i = getIntent();
        existingKey = i.getStringExtra("EventKey");


        initializeFormWithExistingData(existingKey);




        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                //System.out.println("Cancel button was pressed");

                EventFormActivity.this.finish();
            }
        });

        findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                System.out.println("Share button was pressed");
            }
        });



        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                saveEventData();
            }
        });

    }




    private void saveEventData(){


        String name = nameTF.getText().toString();
        String place = placeTF.getText().toString();
        String desc = descriptionTF.getText().toString();
        String capacity = capacityTF.getText().toString();
        String budget = budgetTF.getText().toString();
        String email = emailTF.getText().toString();
        String phone = phoneTF.getText().toString();
        String date = dateTimeTF.getText().toString();

        String radioButtonValue = "";
        if(indoorRB.isChecked()) {
            radioButtonValue = "Indoor";
        }
        else if(outdoorRB.isChecked()) {
            radioButtonValue = "Outdoor";
        }
        else if(onlineRB.isChecked()) {
            radioButtonValue = "Online";
        }

        System.out.println("Event Name: " + name);
        System.out.println("Event Place: " + place);
        System.out.println("Event Description: " + desc);
        System.out.println("Capacity: " + capacity);
        System.out.println("Budget: " + budget);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Event Date: " + date);
        System.out.println("Event Type: " + radioButtonValue);

        String errorMsg = "";


        if(name == null || name.length() < 5) {

            errorMsg = "Event Name Should Have At Least 5 Characters.";
        }

        if(place.isEmpty()) {
            errorMsg = "Event Place Can't Be Empty";
        }

        if(desc.isEmpty()){
            errorMsg = "Description Can't Be Empty";
        }

        if(capacity.isEmpty()){
            errorMsg = "Capacity Can't Be Empty";
        }

        if(budget.isEmpty()){
            errorMsg = "Budget Can't Be Empty";
        }

        if(email.isEmpty()){
            errorMsg = "E-mail Can't Be Empty";
        }

        if(phone.isEmpty()){
            errorMsg = "Phone Can't Be Empty";
        }

        if(radioButtonValue.isEmpty()) {
            errorMsg = "Please Select The Event Type";
        }

        if(date.isEmpty()){
            errorMsg = "date/Time Can't Be Empty";
        }

        if(errorMsg.isEmpty()) {
            // save data in database

            String value = name+":-;-:"+place+":-;-:"+desc+":-;-:"+capacity+":-;-:"+budget+":-;-:"+email+":-;-:"+phone+":-;-:"+date+":-;-:"+radioButtonValue;
            String key = "";
            if(existingKey != null){
                key = existingKey;

            }else{
                key = name+"_"+System.currentTimeMillis();
            }

            //String key = name+"_"+System.currentTimeMillis();


            System.out.println("Key: "+key);
            System.out.println("Value: "+value);

            // Save event locally in sqlite
            Util.getInstance().setKeyValue(EventFormActivity.this, key, value);

            // Send save request to remote server
            httpRequest(new String[] {"key", "event"}, new String[] {key, value});


            //showSuccessDialog("Event Information has been saved Successfully", "Info");
            //showDialog("Event information has been saved successfully", "Info","OK", false);
        } else {

            //((TextView)findViewById(R.id.btnErrorMsg)).setText(errorMsg);
            //showErrorDialog(errorMsg, "Error in Event Data");
            showDialog(errorMsg, "Error in event data", "Back", true);
        }

    }

    private void initializeFormWithExistingData(String eventKey){

        String value = Util.getInstance().getValueByKey(this, eventKey);
        System.out.println("Value "+value);

        if(value != null) {
            String[] fieldValues = value.split(":-;-:");

            String name = fieldValues[0];
            String place = fieldValues[1];
            String description = fieldValues[2];
            String capacity = fieldValues[3];
            String budget = fieldValues[4];
            String email = fieldValues[5];
            String phone = fieldValues[6];
            String date = fieldValues[7];
            String radioButtonValue = fieldValues[8];


            nameTF.setText(name);
            placeTF.setText(place);
            descriptionTF.setText(description);
            capacityTF.setText(capacity);
            budgetTF.setText(budget);
            emailTF.setText(email);
            phoneTF.setText(phone);
            dateTimeTF.setText(date);


            if(radioButtonValue.equals("Indoor")){
                indoorRB.setChecked(true);
            } else if(radioButtonValue.equals("Outdoor")){
                outdoorRB.setChecked(true);
            } else if(radioButtonValue.equals("Online")){
                onlineRB.setChecked(true);
            }

        }
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
                            finish();
                        }

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();

    }

    private void showErrorDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message);
        builder.setTitle(title);

        //Setting message manually and performing action on button click
        //builder.setMessage("Do you want to close this application ?")
        builder.setCancelable(false)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error Dialog");
        alert.show();
    }



    private void showSuccessDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message);
        builder.setTitle(title);

        //Setting message manually and performing action on button click
        //builder.setMessage("Do you want to close this application ?")
        builder.setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                        finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error Dialog");
        alert.show();
    }


    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }



            @Override
            protected JSONObject doInBackground(Void... param) {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for(int i=0; i<keys.length; i++) {
                        params.add(new BasicNameValuePair(keys[i], values[i]));
                    }
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/cse489/index.php" , "POST", params);
                    return jObj;
//                    if(jObj.getInt("success")==1){
//                        return jObj;
//                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(JSONObject jobj){
                String msg = "Failed to send request";
                if(jobj != null){

                    try{
                        msg = jobj.getString("msg");

                    }catch(Exception e){
                        //showDialog(msg,"Info", "ok", false);
                        e.printStackTrace();
                    }

                }
                showDialog(msg, "Info", "Ok", false);
            }
        }.execute();
    }

}



