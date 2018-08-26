package kr.ac.tu.wtf.test20180825;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class RssiScan {
    private WifiManager wifiManager;
    private Activity activity;

    /* AP 정보 결과 리스트 */
    public ArrayList<ScanResult> results = new ArrayList<>();

    private BroadcastReceiver WifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                searchWifi();
            }
        }
    };

    public RssiScan(Activity _activity) {
        activity = _activity;
        checkPermission();
        initRssi();
    }

    private void initRssi() {
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.startScan();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        activity.registerReceiver(WifiReceiver, filter);
    }

    /* 작업 성공 시 true, 실패 시 false 반환 */
    public boolean searchWifi() {
        wifiManager.startScan();
        List<ScanResult> apList = wifiManager.getScanResults();

        for (int i=0, size=results.size(); i<size; ++i) {
            results.remove(0);
        }

        if (apList != null) {
            for (int i=0, size=apList.size(); i<size; ++i) {
                results.add(apList.get(i));
            }
        }
        try {
            activity.unregisterReceiver(WifiReceiver);
        } catch (IllegalArgumentException e) {
            return false;
        }

        Log.e("check", "apList -> " + apList.size() + ", res -> " + results.size());
        return true;
    }

    /* API 23 이후부터 적용 */
    private void checkPermission() {
        Log.e("check","permission");
        final int MY_PERMISSIONS_REQUEST_READ_WIFI = 3;

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CHANGE_WIFI_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_WIFI_STATE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CHANGE_WIFI_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                Toast.makeText(activity, "앱 실행을 위해서는 네트워크 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CHANGE_WIFI_STATE},
                        MY_PERMISSIONS_REQUEST_READ_WIFI);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}