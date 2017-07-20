package com.example.mahe.myapplication3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    String NID;
    String tag = new String("Lifecycle");
    Button button1, button2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            NID = data.getStringExtra("NID");
            Toast.makeText(this, "Got string " + NID, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Didnt get " + NID, Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d(tag, "OnCreate");
        Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();

//        button1.setOnClickListener(onCl);

    }

    //    private View.OnClickListener onClickListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId())
//            {
//                case R.id.Set
//            }
//        }
//
//
//    };
    public void sendMessage(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        Toast.makeText(this, "Start new " + NID, Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, 2);


    }

    public void startAlarmDetect(View view) {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        intent.putExtra("NID", NID);
//        Toast.makeText(this,"Sent string "+NID,Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void enableNFC(View v) {
        android.nfc.NfcAdapter mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(v.getContext());

        if (!mNfcAdapter.isEnabled()) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getContext());
            alertbox.setTitle("Info");
            alertbox.setMessage("Please Turn on nfc");
            alertbox.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }
            });
            alertbox.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertbox.show();
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getContext());
            alertbox.setTitle("Info");
            alertbox.setMessage("NFC is already enabled");
        }
    }

    public void onStart() {
        super.onStart();
        Log.d(tag, "OnStart");

    }

    public void onRestart() {
        super.onRestart();
        Log.d(tag, "OnRestart");


    }

    public void onPause() {
        super.onPause();
        Log.d(tag, "OnPause");


    }

    public void onStop() {
        super.onStop();
        Log.d(tag, "OnStop");

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "OnDestroy");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
