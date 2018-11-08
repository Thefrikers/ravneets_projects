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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindLabs extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Spinner block;
    ListView listView;
    ArrayList<roomData> list;
    CustomAdapter adapter1;
    DatabaseReference databaseReference,databaseReference1;
    Button find;
    String blocktype;
    String tname;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Transparent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);

        setContentView(R.layout.activity_find_labs);
        block =findViewById(R.id.block);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.roomType2,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        find=findViewById(R.id.find);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        block.setAdapter(adapter);
        block.setOnItemSelectedListener(this);
        listView=findViewById(R.id.lv);
        list = new ArrayList<>();
        adapter1 = new CustomAdapter(this, list);
        find.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==find){
            progressBar.setVisibility(View.VISIBLE);
            listView.setAdapter(null);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("LAB");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list.clear();
                        for(DataSnapshot ds:dataSnapshot.getChildren()) {

                            String name = ds.getKey().toString();
                            String availability = ds.getValue().toString();
                            roomData roomData;
                            if(availability.equals("true")){
                                roomData = new roomData(name, "Available","lab_available");
                            }
                            else {
                                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Teachers").child(availability).child("name");
                                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        tname=dataSnapshot.getValue().toString();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                roomData = new roomData(name, tname,"lab_not");
                            }

                            if(name.startsWith(blocktype)) {
                                list.add(roomData);
                                listView.setAdapter(adapter1);

                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        blocktype=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
