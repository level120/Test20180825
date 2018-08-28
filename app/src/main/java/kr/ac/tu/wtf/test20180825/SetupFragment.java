package kr.ac.tu.wtf.test20180825;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;


public class SetupFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText pathEdit, nameEdit;
    private Button btnWifi;

    private String path ="", fname = "", output = "output";

    public String[] pathFileName() {
        if (fname.matches("^\\S+.(?i)(csv|xls|xlsx)$"))
            return new String[] { path + File.separator + output, fname };
        else
            return new String[] { path + File.separator + output, fname + ".csv" };
    }

    public boolean isReady() {
        if (fname.length() == 0)
            return false;
        else return true;
    }

    public SetupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setup, container, false);

        btnWifi = v.findViewById(R.id.btnWifi);
        nameEdit = v.findViewById(R.id.filenameEdit);
        pathEdit = v.findViewById(R.id.pathEdit);
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        //fname = 중복이름 찾아서 뒤에 숫자 +1.csv
        //nameEdit.setText(fname);
        pathEdit.setText(path);

        setEvents();

        return v;
    }

    private void setEvents() {
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fname = nameEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        pathEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                path = pathEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        btnWifi.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WifiListViewActivity.class));
            }
        });
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
