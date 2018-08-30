package kr.ac.tu.wtf.test20180825;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MeasureFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILEPATH = "file_path";
    private static final String FILENAME = "file_name";

    private ToggleButton btnRun;
    private TextView resNoti;
    private String mParamPath, mParamName;
    private ProgressBar pgbar;
    private EditText distanceEdit;

    private RssiScan rs;

    private final int MAX_DATA_COUNT = 10000;

    private static Thread writeJob, progressJob;
    private int workingCountState = 0;

    private OnFragmentInteractionListener mListener;

    public MeasureFragment() {
        // Required empty public constructor
    }

    public static MeasureFragment newInstance(String param1, String param2) {
        MeasureFragment fragment = new MeasureFragment();
        Bundle args = new Bundle();
        args.putString(FILEPATH, param1);
        args.putString(FILENAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamPath = getArguments().getString(FILEPATH);
            mParamName = getArguments().getString(FILENAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_measure, container, false);

        distanceEdit = v.findViewById(R.id.distanceEdit);
        pgbar = v.findViewById(R.id.progressBar);
        btnRun = v.findViewById(R.id.btnRun);
        resNoti = v.findViewById(R.id.resEdit);
        resNoti.setText(mParamPath + File.separator + mParamName);

        rs = new RssiScan(getActivity());

        checkPermisson();
        initEvent();

        return v;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            btnRun.setChecked(false);
        }
    };

    private void initEvent() {
        writeJob = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i=0; i<MAX_DATA_COUNT; ++i) {
                        workingCountState = i;
                        runWriteCsv();
                        Thread.sleep(200);
                    }
                    progressJob.interrupt();
                    handler.sendMessage(handler.obtainMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        progressJob = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        pgbar.setProgress(workingCountState);
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (distanceEdit.getText().toString().matches("\\d+.\\d+")) {
                        Toast.makeText(getActivity().getApplicationContext(), "작업 시작!", Toast.LENGTH_SHORT).show();
                        writeJob.start();
                        progressJob.start();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "거리값이 올바르지 않음", Toast.LENGTH_SHORT).show();
                        writeJob.interrupt();
                        progressJob.interrupt();
                        btnRun.setChecked(false);
                    }
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "작업 종료!", Toast.LENGTH_SHORT).show();
                    writeJob.interrupt();
                    progressJob.interrupt();
                }
            }
        });
    }

    /* Activity를 받아서 적용필요 */
    private void checkPermisson() {
        final int MY_PERMISSIONS_REQUEST_READ_APP = 1;

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                Toast.makeText(getActivity(), "앱 실행을 위해서는 외부저장소 쓰기 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_APP);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void runWriteCsv() throws IOException {
        CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                new File(mParamPath, mParamName),
                                true
                        )
                )
        );

        rs.searchWifi();

        ArrayList<ScanResult> res = rs.results;
        String[] data = new String[res.size() * 5];
        int dataItr = 0;

        for (int n = 0, size = res.size(); n < size; ++n) {
            boolean isSelected = false;

            for (int m = 0, _size = WifiListViewActivity.wifiList.size();
                    m < _size; ++m) {
                if (res.get(n).BSSID.equals(
                        WifiListViewActivity.wifiList.get(m).BSSID)
                        && res.get(n).SSID.equals(
                                WifiListViewActivity.wifiList.get(m).SSID)
                ) {
                   isSelected = true;
                   break;
                }
            }

            if (isSelected) {
                data[dataItr++] = res.get(n).SSID;
                data[dataItr++] = res.get(n).BSSID;
                data[dataItr++] = res.get(n).level + "";
                data[dataItr++] = String.format("%.4f", calcDistance(res.get(n).frequency, res.get(n).level));
                data[dataItr++] = distanceEdit.getText().toString();
            }
            writer.writeNext(data);
        }
        writer.close();
    }

    private double calcDistance(int frequency, int level) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
