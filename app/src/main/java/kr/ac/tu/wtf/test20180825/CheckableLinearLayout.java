package kr.ac.tu.wtf.test20180825;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Checkable;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.itemCheck) ;
        return cb.isChecked() ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.itemCheck) ;
        setChecked(cb.isChecked() ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.itemCheck) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }
    }
}
