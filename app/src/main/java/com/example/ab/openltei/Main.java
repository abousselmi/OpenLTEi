package com.example.ab.openltei;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ab.openltei.R;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    public int btnClicksCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Network info button
        Button getInfoBtn = (Button) findViewById(R.id.get_info_btn);
        getInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(28)
            public void onClick(View v) {
                //Network info snapshot list view
                ArrayList<NetInfo> netInfoArray = new ArrayList<NetInfo>();
                NetInfoAdapter netInfoAdapter = new NetInfoAdapter(getApplicationContext(),
                        netInfoArray);
                ListView netInfoListView = (ListView) findViewById(R.id.net_info_lv);
                netInfoListView.setAdapter(netInfoAdapter);

                //Increment snapshot id
                btnClicksCounter++;

                //Print the snapshot ID
                netInfoAdapter.add(new NetInfo("Snapshot ID",
                        String.valueOf(btnClicksCounter)));

                //Print the timestamp
                netInfoAdapter.add(new NetInfo("Timestamp",
                        String.valueOf(java.util.Calendar.getInstance().getTime())));

                //Print the phone and network info
                TelephonyManager telManager = (TelephonyManager)
                        getSystemService(TELEPHONY_SERVICE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Print IMEI number
                    String imei = "Not available";
                    if (telManager.getDeviceId() != null)
                        imei = String.valueOf(telManager.getDeviceId());
                    netInfoAdapter.add(new NetInfo("IMEI", imei));

                    //Print IMSI number
                    String imsi = "Not available";
                    if (telManager.getSubscriberId() != null)
                        imsi = String.valueOf(telManager.getSubscriberId());
                    netInfoAdapter.add(new NetInfo("IMSI", imsi));

                    //Print network type
                    String[] networkTypeList = {"UNKNOWN", "GPRS", "EDGE", "UMTS",
                            "CDMA (IS95A or IS95B)", "EVDO revision 0", "EVDO revision A",
                            "1xRTT", "HSDPA", "HSUPA", "HSPA/HSPA+", "IDEN", "EVDO revision B",
                            "LTE", "EHRPD", "HSPAP/HSPAP+", "GSM", "TD_SCDMA", "IWLAN"};
                    netInfoAdapter.add(new NetInfo("Network Type",
                            networkTypeList[telManager.getNetworkType()]));

                    //Print Signal Strength
                    telManager = (TelephonyManager)
                            getSystemService(TELECOM_SERVICE);

                    SignalStrength signalStrength = telManager.getSignalStrength();

                    if (signalStrength.isGsm()) {
                        System.out.println("##########  IS GSM  ##########");
                    } else {
                        System.out.println("##########  IS NOT GSM  ##########");
                    }
                    /*netInfoAdapter.add(new NetInfo("GSM signal strength",
                            String.valueOf(.
                                    getGsmSignalStrength())));*/
                    /*try {
                        if (telManager.getSignalStrength() != null) {
                            netInfoAdapter.add(new NetInfo("GSM bit error rate",
                                    String.valueOf(telManager.getSignalStrength().
                                            getGsmBitErrorRate())));
                            netInfoAdapter.add(new NetInfo("GSM signal strength",
                                    String.valueOf(telManager.getSignalStrength().
                                            getGsmSignalStrength())));
                        }
                    } catch (NullPointerException exp) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Signal info not available",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }*/
                }

                //Print the dummy end of list
                netInfoAdapter.add(new NetInfo("Dummy",
                        "End of network info list"));
            }
        });

        // Quick info snack bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "OpenLTEi is experimental..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
            Toast toast = Toast.makeText(getApplicationContext(), R.string.not_implemented,
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        if (id == R.id.action_close_app) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class NetInfo {
        private String infoKey;
        private String infoValue;

        private NetInfo(String infoKey, String infoValue) {
            this.infoKey = infoKey;
            this.infoValue = infoValue;
        }
    }

    private class NetInfoAdapter extends ArrayAdapter<NetInfo> {
        private NetInfoAdapter(Context context, ArrayList<NetInfo> info) {
            super(context, 0, info);
        }

        @Override
        public @NonNull  View getView(int position, View convertView, @NonNull  ViewGroup parent) {
            NetInfo netInfo = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.net_info,
                        parent, false);
            }
            TextView netInfoTvKey = (TextView) convertView.findViewById(R.id.net_info_tv_key);
            TextView netInfoTvValues = (TextView) convertView.findViewById(R.id.net_info_tv_value);
            if (netInfo != null) {
                netInfoTvKey.setText(netInfo.infoKey);
                netInfoTvValues.setText(netInfo.infoValue);
            } else {
                netInfoTvKey.setText(R.string.not_available);
                netInfoTvValues.setText(R.string.not_available);
            }
            return convertView;
        }
    }
}
