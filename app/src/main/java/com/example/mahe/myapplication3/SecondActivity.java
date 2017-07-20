package com.example.mahe.myapplication3;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class SecondActivity extends ActionBarActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mtag;
    Context ctx;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static  String TAG = "Hellozz";
    public static  String NID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);






        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        Button button=(Button)findViewById(R.id.submit);
        final TextView pass=(TextView)findViewById(R.id.ID);
        ctx=this;
        final TextView mTextView = (TextView) findViewById(R.id.textView_explanation);

        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter!=null && nfcAdapter.isEnabled())
        {
            mTextView.setText("NFC is enabled");

        }
        else if(!nfcAdapter.isEnabled())
        {
            mTextView.setText("NFC is disabled");

        }
        else
        {
            Toast.makeText(this,"This device doesn't support NFC.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {




                if(!pass.toString().equals("")){
                    mTextView.setText("Your NID is "+pass.getText().toString());
                    TAG=pass.getText().toString();
                    NID=pass.getText().toString();
                }
                else{
                    mTextView.setText("Please input only alphabets upper or lower or mix");
                    Toast.makeText(SecondActivity.this,"Nothing written(cleared tag)",Toast.LENGTH_SHORT).show();
                    pass.setText("");
                }


            }
        });


        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };



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

    public void formatTag(Tag tag,NdefMessage ndefMessage)
    {
        NdefFormatable ndefFormatable=NdefFormatable.get(tag);
        try {
            if (ndefFormatable == null) {

            }
            try {
                ndefFormatable.format(ndefMessage);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
        }catch (Exception e){
            Toast.makeText(ctx, "Couldnt connect, trying again.", Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    public void onNewIntent(Intent intent) {
        // When an NFC tag is being written, call the write tag function when an intent is
        // received that says the tag is within range of the device and ready to be written to

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String nfcMessage = intent.getStringExtra("On intent");
        Toast.makeText(ctx, "NFC intent recieved", Toast.LENGTH_SHORT ).show();

        try {
            formatTag(tag,createRecord(TAG));
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(ctx,"UnsupportedEncodingException",Toast.LENGTH_SHORT).show();
        }

        try {
            write(TAG,tag);
            Toast.makeText(ctx,"Wrote",Toast.LENGTH_SHORT).show();
            Intent intnt=new Intent();
            intnt.putExtra("NID",NID);
            setResult(RESULT_OK,intnt);
            Toast.makeText(ctx,"Sent result" + NID,Toast.LENGTH_SHORT).show();
            finish();

        } catch (IOException e) {
            Toast.makeText(ctx,"Couldnt write",Toast.LENGTH_SHORT).show();
        } catch (FormatException e) {
            Toast.makeText(ctx,"Couldnt write",Toast.LENGTH_SHORT).show();
        }
        super.onNewIntent(intent);
    }
//    private void writeToFile(String data) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(/res/logpass.txt, Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e(TAG, "File write failed: " + e.toString());
//        }
//
//    }

    private NdefMessage createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        NdefMessage message2=new NdefMessage(recordNFC);
        return message2;
    }

    public void write(String text, Tag tag) throws IOException, FormatException {


        NdefMessage message = createRecord(text);

        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();


    }


    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */

        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }




    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     *
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }






}
