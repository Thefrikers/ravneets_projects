package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Remove extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Spinner block;
    ListView listView;
    ArrayList<roomData> list;
    CustomAdapter adapter1;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3;
    Button find;
    String blocktype;
    String tname;
    ProgressBar progressBar;
    Spinner roomType;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Transparent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        setContentView(R.layout.activity_remove);
        roomType=findViewById(R.id.roomType);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.roomType,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomType.setAdapter(adapter);
        roomType.setOnItemSelectedListener(this);
        find=findViewById(R.id.find);
        listView=findViewById(R.id.lv);
        list = new ArrayList<>();
        adapter1 = new CustomAdapter(this, list);
        find.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = (TextView) view.findViewById(R.id.className);
                String str = temp.getText().toString();
                TextView temp1= (TextView) view.findViewById(R.id.availability);
                String str1 = temp.getText().toString();
                databaseReference2 = FirebaseDatabase.getInstance().getReference().child(blocktype).child(str);
                databaseReference3 = FirebaseDatabase.getInstance().getReference().child(blocktype).child("JOINED").child(str1);
                databaseReference3.removeValue();
                databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Done!", Toast.LENGTH_SHORT).show();
                            loaddata();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Done!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v==find){

           loaddata();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        blocktype=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void loaddata(){
        listView.setAdapter(null);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(blocktype);
        temp=blocktype;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    list.clear();

                    for(DataSnapshot ds:dataSnapshot.getChildren()) {

                        String name = ds.getKey().toString();
                        String availability = ds.getValue().toString();
                        roomData roomData;
                        if (availability.equals("true")) {
                            if(blocktype.equals("CLASS")) {
                                roomData = new roomData(name, "Available", "class_available");
                            }
                            else {
                                roomData = new roomData(name, "Available", "lab_available");

                            }
                        } else {
                            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Teachers").child(availability).child("name");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    tname = dataSnapshot.getValue().toString();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            if (blocktype.equals("CLASS")){
                                roomData = new roomData(name, tname, "class_not");
                            }
                            else {
                                roomData = new roomData(name, tname, "lab_not");
                            }
                        }
                        list.add(roomData);
                        listView.setAdapter(adapter1);


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
