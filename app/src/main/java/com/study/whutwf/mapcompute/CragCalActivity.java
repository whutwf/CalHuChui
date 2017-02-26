package com.study.whutwf.mapcompute;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CragCalActivity extends SingleFramentActivity {

    @Override
    protected Fragment createFragment() {
        return new CragCalFragment();
    }

}
