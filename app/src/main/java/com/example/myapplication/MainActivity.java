package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    private TelephonyManager telephonyManager;
    private CallStateListener callStateListener;
    private EditText mEditTextNumber;
    private Handler handler;
    private Runnable rejectCallRunnable;
    private static final long REJECT_CALL_DELAY = 10000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextNumber = findViewById(R.id.edit_text_number);
        ImageView imageCall = findViewById(R.id.image_call);

        System.out.println("Check OnCreate");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
//            makePhoneCall();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CALL);
        } else {
//            makePhoneCall();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS}, REQUEST_CALL);
        } else {
//            makePhoneCall();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CALL);
        } else {
//            makePhoneCall();
        }

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the PhoneStateListener
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        callStateListener = new CallStateListener();
        if (telephonyManager != null) {
            telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the PhoneStateListener
        if (telephonyManager != null) {
            telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    private void makePhoneCall() {
        String phoneNumber = mEditTextNumber.getText().toString();
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (telecomManager != null) {
            Uri uri = Uri.fromParts("tel", phoneNumber, null);

            PhoneAccountHandle sim1Handle = getSim1PhoneAccountHandle(telecomManager);
            if (sim1Handle != null) {
                Bundle extras = new Bundle();
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, sim1Handle);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(uri);
                callIntent.putExtras(extras);
                startActivity(callIntent);

                // Schedule the call rejection after 5 seconds
                handler = new Handler();
                rejectCallRunnable = new Runnable() {
                    @Override
                    public void run() {
                        rejectCall();
                    }
                };
                handler.postDelayed(rejectCallRunnable, REJECT_CALL_DELAY);

            } else {
                // Handle the case where SIM card 1 is not available or not found
                Toast.makeText(this, "SIM card 1 not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where TelecomManager is not available
            Toast.makeText(this, "TelecomManager not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void rejectCall() {
        Toast.makeText(this, "Reject Call ", Toast.LENGTH_SHORT).show();
        if (telephonyManager != null) {
            Toast.makeText(this, "Process Reject Call ", Toast.LENGTH_SHORT).show();
            try {
                Log.i("Process Reject","Check Error : 1");
//                ITelephony telephonyService;

                Log.i("Process Reject","Check Error : 2");
//                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//                Class clazz = Class.forName(telephonyManager.getClass().getName());
//                Method method = clazz.getDeclaredMethod("getITelephony");
//                method.setAccessible(true);
//                ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
//                telephonyService.endCall();
//                Log.i("Process Reject","Check Error : 3");


                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                Class<?> telephonyClass = Class.forName(telephonyManager.getClass().getName());
                Method method = telephonyClass.getDeclaredMethod("getITelephony");
                method.setAccessible(true);
                Object telephonyObject = method.invoke(telephonyManager);

                Class<?> telephonyInterfaceClass = Class.forName(telephonyObject.getClass().getName());
                Method endCallMethod = telephonyInterfaceClass.getDeclaredMethod("endCall");
                endCallMethod.invoke(telephonyObject);




//                Cara Ke Dua
//                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                Class c = Class.forName(tm.getClass().getName());
//                Method m = c.getDeclaredMethod("getITelephony"); m.setAccessible(true);
//                Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
//                c = Class.forName(telephonyService.getClass().getName()); // Get its class
//                m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
//                m.setAccessible (true); // Make it accessible
//                m.invoke(telephonyService); // invoke endCall()


//                Cara Pertama
//                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                @SuppressLint("SoonBlockedPrivateApi") Method getITelephony = telephonyManager
//                        .getClass()
//                        .getDeclaredMethod("getITelephony");
//
//                Log.i("Process Reject","Check Error : 3");
//                getITelephony.setAccessible(true);
//
//                Log.i("Process Reject","Check Error : 4");
//                ITelephony telephonyService = (ITelephony) getITelephony.invoke(telephonyManager);
//
//                Log.i("Process Reject","Check Error : 5");
//                telephonyService.endCall();

            } catch (Exception ignored) {
                Log.i("Process Reject","Check Error : 4 : "+ignored);
            }
        }
    }

    private PhoneAccountHandle getSim1PhoneAccountHandle(TelecomManager telecomManager) {
        System.out.println("Check sim 1 phone account handle");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return;
        }
        List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

        for (PhoneAccountHandle handle : phoneAccountHandleList) {
            PhoneAccount account = telecomManager.getPhoneAccount(handle);
            if (account != null && account.hasCapabilities(PhoneAccount.CAPABILITY_SIM_SUBSCRIPTION)) {
                // Check if the PhoneAccount corresponds to SIM card 1
                System.out.println("Check Label : "+account.getLabel().toString());
                if (account.getLabel().toString().contains("SIM 1")||account.getLabel().toString().contains("SIM1")) {
                    return handle;
                }
            }
        }

        return null; // SIM card 1 not found
    }

    private class CallStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            System.out.println("Check Call State : "+state);
            Toast.makeText(MainActivity.this, "phone state : "+state, Toast.LENGTH_SHORT).show();
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // Handle the ringing state here
                    Toast.makeText(MainActivity.this, "Phone is ringing", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // Handle the call established state here
                    Toast.makeText(MainActivity.this, "Call is established", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // Handle the call ended state here
                    Toast.makeText(MainActivity.this, "Call ended", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}