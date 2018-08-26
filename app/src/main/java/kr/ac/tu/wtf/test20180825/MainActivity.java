package kr.ac.tu.wtf.test20180825;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements SetupFragment.OnFragmentInteractionListener, MeasureFragment.OnFragmentInteractionListener {

    private final int SETUP = 1, MEASURE = 2;
    private SetupFragment setupFrag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    callFragment(SETUP);
                    return true;
                case R.id.navigation_dashboard:
                    callFragment(MEASURE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupFrag = new SetupFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        callFragment(SETUP);
    }

    private void callFragment(int frament_no){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                transaction.replace(R.id.fragment_container, setupFrag);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                if (setupFrag.isReady()) {
                    transaction.replace(R.id.fragment_container, MeasureFragment.newInstance(setupFrag.pathFileName()));
                    transaction.commit();
                }
                else {
                    Toast.makeText(getApplicationContext(), "파일명 미작성", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
