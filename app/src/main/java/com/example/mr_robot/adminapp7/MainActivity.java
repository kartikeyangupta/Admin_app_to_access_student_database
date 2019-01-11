package com.example.mr_robot.adminapp7;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final Uri CONTENT_URI = Uri.parse("content://com.example.mr_robot.student_map7.ContactsProvider/students");
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    Button bt1,bt2,bt3;
    TextView dataText = null;
    EditText deleteIDET;
    ContentResolver resolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resolver = getContentResolver();
        dataText = findViewById(R.id.dataTV);
        deleteIDET = findViewById(R.id.deleteIDET);
        bt1 = findViewById(R.id.showdata);
        bt2 = findViewById(R.id.deleteIDButton);
        bt3 = findViewById(R.id.checkcgpa);
        bt3.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)) {
            bt3.setEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                Toast.makeText(MainActivity.this,"DATA UPDATED",Toast.LENGTH_SHORT).show();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idtoDelete = deleteIDET.getText().toString();
                long idDelete = resolver.delete(CONTENT_URI,"ID = ?",new String[]{idtoDelete});
                getData();
                Toast.makeText(MainActivity.this,"DATA UPDATED",Toast.LENGTH_SHORT).show();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Trying to send message",Toast.LENGTH_SHORT).show();
                calculator();
            }
        });


    }
    public void getData() {
        String[] projections =  new String[]{"ID","NAME","CGPA","NUMBER"};
        Cursor cursor = resolver.query(CONTENT_URI,projections,null,null,null);
        String studentList = "";
        if(cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("ID"));
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                String cgpa = cursor.getString(cursor.getColumnIndex("CGPA"));
                String number = cursor.getString(cursor.getColumnIndex("NUMBER"));
                studentList = studentList + id + "::" + name + "::" + cgpa + "::" + number + "\n";
            }while (cursor.moveToNext());
            }
        dataText.setText(studentList);
    }
    public void calculator() {
        Log.d("lol","gg");
        String[] projections =  new String[]{"ID","NAME","CGPA","NUMBER"};
        Cursor cursor = resolver.query(CONTENT_URI,projections,null,null,null);
        if(cursor.moveToFirst()) {
            do{
                Log.d("lol","gg");
                String id = cursor.getString(cursor.getColumnIndex("CGPA"));
                char[] array = id.toCharArray();
                int z = Integer.parseInt(String.valueOf(array[0]));
                Log.d("lol",String.valueOf(z));
                if(z>=8) {
                    Log.d("lol","gg");
                    String name =  cursor.getString(cursor.getColumnIndex("NAME"));
                    Log.d("lol",name);
                    String number =  cursor.getString(cursor.getColumnIndex("NUMBER"));
                    Log.d("lol",number);
                    onSend(number,name);
                }
            }while(cursor.moveToNext());
        }
    }
    public boolean checkPermission(String permission) {
            int check = ContextCompat.checkSelfPermission(this,permission);
            return (check == PackageManager.PERMISSION_GRANTED);
    }
    public void onSend(String phoneNumber,String name) {
        if(phoneNumber == null || phoneNumber.length() == 0 )
            return;
        if(checkPermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            String msg = "Congratulation "+name+" you are selected.";
            //String call = "+91"+phoneNumber;
            Log.d("lol",phoneNumber + " "+msg);
            smsManager.sendTextMessage(phoneNumber,null,msg,null,null);
            Toast.makeText(this,"Message sent!",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Message not Sent",Toast.LENGTH_SHORT).show();
        }
    }
}
