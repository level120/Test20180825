package kr.ac.tu.wtf.test20180825;

import android.net.wifi.ScanResult;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class WifiListViewActivity extends AppCompatActivity {

    private ActionBar ab;
    private ListView list;
    private WifiChoiceListViewAdapter a;
    private RssiScan rs;

    public static ArrayList<WifiListViewItem> wifiList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list_view);

        a = new WifiChoiceListViewAdapter();
        list = findViewById(R.id.wifiListView);
        list.setAdapter(a);

        rs = new RssiScan(this);

        init();
    }

    private void init() {
        a.removeAll();
        if (rs.searchWifi()) {
            duplicationJob();
        }
    }

    private void duplicationJob() {
        ArrayList<ScanResult> res = rs.results;

        for (int i = 0, size = res.size(); i < size; ++i) {
            boolean isChecked = false;

            for (int j=0, _size = wifiList.size(); j < _size; ++j) {
                if (res.get(i).BSSID.equals(wifiList.get(j).BSSID)
                        && res.get(i).SSID.equals(wifiList.get(j).SSID)) {
                    isChecked = true;
                    break;
                }
            }
            a.addItem(
                    ContextCompat.getDrawable(this, R.drawable.ic_wifi_black_36dp),
                    res.get(i).SSID,
                    res.get(i).BSSID,
                    String.format("%s\n(%s)", res.get(i).SSID, res.get(i).BSSID),
                    isChecked
            );
        }
//        findViewById(R.id.wifiListView).invalidate(); // refersh
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }
}
