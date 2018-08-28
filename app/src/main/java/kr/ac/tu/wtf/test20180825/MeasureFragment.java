package kr.ac.tu.wtf.test20180825;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class MeasureFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILEPATH = "file_path";
    private static final String FILENAME = "file_name";

    private ToggleButton btnRun;
    private TextView resNoti;
    private String mParamPath, mParamName;

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

        btnRun = v.findViewById(R.id.btnRun);
        resNoti = v.findViewById(R.id.resEdit);
        resNoti.setText(mParamPath + File.separator + mParamName);

        initEvent();

        return v;
    }

    private void initEvent() {
        btnRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getActivity().getApplicationContext(), "작업 시작!", Toast.LENGTH_SHORT).show();
                    try {
//                        checkPermisson();
                        runWriteCsv();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("err", e.toString());
                    }
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "진행중인 작업 중단!", Toast.LENGTH_SHORT).show();
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
//        new File(mParamPath + File.separator).mkdirs();
        CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(
//                        new FileOutputStream(mParamPath + File.separator + mParamName)
                        new FileOutputStream(
                                new File(mParamPath, mParamName)
                        )
                )
        );

        String[] data = { "test", "data", "...", "checked" };
        writer.writeNext(data);

        writer.close();
        Toast.makeText(getActivity().getApplicationContext(), "작업 끝!", Toast.LENGTH_SHORT).show();
        btnRun.setChecked(false);
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
