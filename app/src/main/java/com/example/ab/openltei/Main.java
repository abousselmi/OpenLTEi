package com.example.ab.openltei;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ab.openltei.netinfo.NetInfo;
import com.example.ab.openltei.netinfo.NetInfoAdapter;

import java.util.ArrayList;
import java.util.List;

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
            @TargetApi(24)
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
                TelephonyManager telephonyManager = (TelephonyManager)
                        getSystemService(TELEPHONY_SERVICE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Print IMEI number
                    String imei = "Not available";
                    if (telephonyManager.getDeviceId() != null)
                        imei = String.valueOf(telephonyManager.getDeviceId());
                    netInfoAdapter.add(new NetInfo("IMEI", imei));

                    //Print IMSI number
                    String imsi = "Not available";
                    if (telephonyManager.getSubscriberId() != null)
                        imsi = String.valueOf(telephonyManager.getSubscriberId());
                    netInfoAdapter.add(new NetInfo("IMSI", imsi));

                    //Print network type
                    String[] networkTypeList = {"UNKNOWN", "GPRS", "EDGE", "UMTS",
                            "CDMA (IS95A or IS95B)", "EVDO revision 0", "EVDO revision A",
                            "1xRTT", "HSDPA", "HSUPA", "HSPA/HSPA+", "IDEN", "EVDO revision B",
                            "LTE", "EHRPD", "HSPAP/HSPAP+", "GSM", "TD_SCDMA", "IWLAN"};
                    netInfoAdapter.add(new NetInfo("Network Type",
                            networkTypeList[telephonyManager.getNetworkType()]));

                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Print All Cell Info
                        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
                        for (int i=0; i<cellInfoList.size(); i++) {
                            //Only LTE info are processed
                            CellInfo cellInfo = cellInfoList.get(i);
                            System.out.println("i: " + i);
                            if (cellInfo.getClass() ==  CellInfoLte.class) {
                                CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.
                                        getCellSignalStrength();

                                //LTE signal level
                                netInfoAdapter.add(new NetInfo("LTE signal level [0..97]",
                                        String.valueOf(cellSignalStrengthLte.getAsuLevel())));

                                //Channel quality indicator (CQI)
                                /*netInfoAdapter.add(new NetInfo("Channel quality indicator " +
                                        "(CQI)", String.valueOf(cellSignalStrengthLte.getCqi())));*/

                                //Signal strength as dBm
                                netInfoAdapter.add(new NetInfo("Signal strength as dBm",
                                        String.valueOf(cellSignalStrengthLte.getDbm())));

                                //Signal strength as int
                                netInfoAdapter.add(new NetInfo("Signal strength as int " +
                                        "[0..4]",String.valueOf(cellSignalStrengthLte.getLevel())));

                                //Reference signal received power
                                /*netInfoAdapter.add(new NetInfo("Reference signal received " +
                                    "power", String.valueOf(cellSignalStrengthLte.getRsrp())));*/

                                //Reference signal received quality
                                /*netInfoAdapter.add(new NetInfo("Reference signal received " +
                                    "quality",String.valueOf(cellSignalStrengthLte.getRsrq())));*/

                                //Reference signal signal-to-noise ratio
                                /*netInfoAdapter.add(new NetInfo("Reference signal " +
                                    "signal-to-noise ratio", String.valueOf(cellSignalStrengthLte.
                                    getRssnr())));
*/
                                //Timing advance value
                                netInfoAdapter.add(new NetInfo("Timing advance [0..1282]",
                                        String.valueOf(cellSignalStrengthLte.getTimingAdvance())));

                                //Operator info
                                netInfoAdapter.add(new NetInfo("Registered to a " +
                                        "mobile network ", String.valueOf(cellInfoLte.
                                        isRegistered())));
                                if (cellInfoLte.isRegistered()) {
                                    netInfoAdapter.add(new NetInfo("Network Operator",
                                            telephonyManager.getNetworkOperatorName()));
                                }
                            }

                        }
                    }

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
}
