package edu.ewubd.event_managment;

//import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.database.Cursor;
import java.util.ArrayList;

import edu.ewubd.event_managment.R;


public class UpcomingEventsActivity extends Activity {

    private ListView lvEvents;
    private ArrayList<Event> events;
    private CustomEventAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        lvEvents = findViewById(R.id.lvEvents);

        //initializeEventList();

        //initializeCustomEventList();

        findViewById(R.id.btnCreateNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("Cancel button was pressed");

                Intent i = new Intent(UpcomingEventsActivity.this, EventFormActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("Share button was pressed");
            }
        });


        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpcomingEventsActivity.this.finish();
                //System.exit(0);

            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        initializeCustomEventList();
    }



    private void initializeEventList(){
        String eventsList[] = {"Event 1", "Event 2", "Event 3", "Event 4"};


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventsList);
        lvEvents.setAdapter(arrayAdapter);

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                System.out.println(item);
            }
        });
    }

    private void initializeCustomEventList(){
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");

        if(rows.getCount() == 0){
            return;
        }
        //events = new Event[rows.getCount()];
        events = new ArrayList<>();

        int i = 0;
        while(rows.moveToNext()){
            String key = rows.getString(0);
            String eventData = rows.getString(1);

            String[] fieldValues = eventData.split(":-;-:");
            String name = fieldValues[0];
            String place = fieldValues[1];
            String description = fieldValues[2];
            String capacity = fieldValues[3];
            String budget = fieldValues[4];
            String email = fieldValues[5];
            String phone = fieldValues[6];
            String date = fieldValues[7];
            String radioButtonValue = fieldValues[8];

            //events[i] = new Event(key, name, place, description, capacity, budget, email, phone, date, radioButtonValue);

            Event e = new Event(key, name, place, description, capacity, budget, email, phone, date, radioButtonValue);
            events.add(e);
            i++;

        }
        db.close();

        adapter = new CustomEventAdapter(this, events);
        lvEvents.setAdapter(adapter);


        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // String item = (String) parent.getItemAtPosition(position);
                System.out.println(position);

                Intent i = new Intent(UpcomingEventsActivity.this, EventFormActivity.class);
                //i.putExtra("EventKey", events[position].key);
                i.putExtra("EventKey", events.get(position).key);
                startActivity(i);
            }
        });

        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "Do you want to delete event - "+events.get(position).name +" ?";
                showDialog(message, "Delete Event", position);
                return true;
            }
        });



    }

    private void showDialog(String message, String title, int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message);
        builder.setTitle(title);

        //Setting message manually and performing action on button click
        //builder.setMessage("Do you want to close this application ?")
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Util.getInstance().deleteByKey(UpcomingEventsActivity.this, events.get(position).key);
                        dialog.cancel();
                        initializeCustomEventList();
                        adapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Delete Event");
        alert.show();

    }
}



