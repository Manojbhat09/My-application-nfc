package com.example.mahe.myapplication3;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity {

    private SoundPool spool;
    private int soundID;
    private static final int SOURCE_QUALITY =0 ;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mtag;
    Context ctx;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static  String NID ;
    String nfcData;
    int flag2;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NID = extras.getString("NID");
        }

        Toast.makeText(this,"Got string "+NID,Toast.LENGTH_SHORT).show();
//        mySoundPool = new SoundPool(
//                1, AudioManager.STREAM_ALARM, SOURCE_QUALITY);
//        mySoundPool.setOnLoadCompleteListener((SoundPool.OnLoadCompleteListener) StartActivity.this);

        if(nfcAdapter==null)
        {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        spool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundID = spool.load(this,R.raw.hit , 1);
    }

    public void Sound(int soundID ){
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        android.util.Log.v("SOUND","["+volume+"]["+spool.play(soundID, volume, volume, 1, 0, 1f)+"]");
    };
//    private void loadSound()
//    {
//        hitSoundID = mySoundPool.load(this, R.raw.hit, 1);
//
//    }
//    public void onLoadComplete(SoundPool soundpool,int sampleId,int status)
//    {
//        if(status==0)
//        {
//            switch (sampleId){
//                case 1: {
//                    Toast.makeText(this, "Sound is loaded", Toast.LENGTH_SHORT).show();
//
//                    break;
//                }
//
//            }
//
//        }
//    }
    public  void stopAlarm(View v)
    {
//        mySoundPool.stop(hitSoundID);
//        hitSoundID=0;
        Toast.makeText(this,"Stopping the alarm",Toast.LENGTH_LONG).show();
    }
    public void onStart()
    {




        super.onStart();

    }
    @Override
    public void onNewIntent(Intent intent) {
        int flag=0;char data='i';
        if(intent.getType()!=null) {
            Parcelable[] rawMsg= intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefRecord recordGet=((NdefMessage)rawMsg[0]).getRecords()[0];
            String rawData =new String(recordGet.getPayload());
            nfcData=rawData.substring(3).trim();
            Toast.makeText(this,"Got data as "+nfcData,Toast.LENGTH_SHORT).show();

//            byte status = rawData.to);
//            int enc = status & 0x80; // Bit mask 7th bit 1
//            String encString = null;
//            if (enc == 0)
//                encString = "UTF-8";
//            else
//                encString = "UTF-16";




        }




//        for(int i=0;nfcData.charAt(i)!='\0';i++)
//        {
//            if(NID.charAt(i)!=nfcData.charAt(i))
//            {
//                flag=1;
//                data=nfcData.charAt(i);
//            }
//        }
        if(flag2==1) {
            if (!(nfcData.compareTo(NID) == 0)) {
                Toast.makeText(this, "Data is not same " + nfcData + " and " + NID + "wrong char ", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Data is same, ringing the alarm", Toast.LENGTH_LONG).show();


            Sound(soundID);
            flag2=0;
        }
        else {
            flag2++;
        }
    }
    public void onRestart()
    {
        super.onRestart();

    }

    public void onPause()
    {
        super.onPause();


    }
    public void onStop()
    {
        super.onStop();

    }
    public void onDestroy()
    {
        super.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

    }
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
