package com.study.whutwf.mapcompute;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by whutw on 2017/2/19 0019.
 */

public class MenuFragment extends Fragment {

    private Button mCragCalution;
    private Button mCragCalOut;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        mCragCalution = (Button) v.findViewById(R.id.sag_cal);

        mCragCalution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CragCalActivity.class);
                startActivity(intent);
            }
        });

        mCragCalOut = (Button) v.findViewById(R.id.sag_cal_out);
        mCragCalOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CragCalOutActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
