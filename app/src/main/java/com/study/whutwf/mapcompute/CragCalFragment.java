package com.study.whutwf.mapcompute;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by whutw on 2017/2/19 0019.
 */

public class CragCalFragment extends Fragment {

    private Button mResultButton;
    private EditText mSpan;
    private EditText mNominalHeight;
    private EditText mInstrumentHeight;
    private EditText mStringLength;
    private EditText mValueA;
    private EditText mValueB;
    private EditText mHgDegree;
    private EditText mHgMinute;
    private EditText mHgSecond;
    private EditText mObDegree;
    private EditText mObMinute;
    private EditText mObSecond;
    private EditText mSag;
    private TextView mObAngle;

    private double valueOfA;
    private double valueOfB;
    private double sag;
    private double obAngle;

    ResultHandler mResultHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResultHandler = new ResultHandler();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmetn_crag_cal, container, false);
        initView(v);

        mResultButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(testInputArgs()) {
                    ResultThread resultThread = new ResultThread();
                    new Thread(resultThread).start();
                } else {
                    Toast.makeText(getContext(), "输入正确数据", Toast.LENGTH_LONG);
                }

            }
        });

        return v;
    }

    private void initView(View v) {
        mSpan = (EditText) v.findViewById(R.id.crag_cal_span);
        mNominalHeight = (EditText) v.findViewById(R.id.crag_cal_nominal_height);
        mInstrumentHeight = (EditText) v.findViewById(R.id.crag_cal_instrument_height);
        mStringLength = (EditText) v.findViewById(R.id.crag_cal_string_length);
        mHgDegree = (EditText) v.findViewById(R.id.tv_crag_cal_hg_degree);
        mHgMinute = (EditText) v.findViewById(R.id.tv_crag_cal_hg_minute);
        mHgSecond = (EditText) v.findViewById(R.id.tv_crag_cal_hg_second);
        mObDegree = (EditText) v.findViewById(R.id.tv_crag_cal_ob_degree);
        mObMinute = (EditText) v.findViewById(R.id.tv_crag_cal_ob_minute);
        mObSecond = (EditText) v.findViewById(R.id.tv_crag_cal_ob_second);

        mSag = (EditText) v.findViewById(R.id.crag_cal_value_of_sag);
        mValueA = (EditText) v.findViewById(R.id.crag_cal_value_of_a);
        mValueB = (EditText) v.findViewById(R.id.crag_cal_value_of_b);

        mObAngle = (TextView) v.findViewById(R.id.crag_cal_get_angle);

        mResultButton = (Button) v.findViewById(R.id.bt_crag_cal_get_result);
    }

    class ResultHandler extends Handler {
        public ResultHandler() {
        }

        public ResultHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle result = msg.getData();
            mValueA.setText(result.getDouble("value_of_a") + "");
            mValueB.setText(result.getDouble("value_of_b") + "");
            mSag.setText(result.getDouble("value_of_sag") + "");
            int[] angle;
            angle = result.getIntArray("value_of_obangle");
            mObAngle.setText("角度：" + angle[0] + "度" + angle[1] + "分" + angle[2] + "秒");
        }
    }

    class ResultThread implements Runnable {
        @Override
        public void run() {

            double[] obAngles = {
                    Double.parseDouble(mObDegree.getText().toString()),
                    Double.parseDouble(mObMinute.getText().toString()),
                    Double.parseDouble(mObSecond.getText().toString())
            };

            double[] hgAngles = {
                    Double.parseDouble(mHgDegree.getText().toString()),
                    Double.parseDouble(mHgMinute.getText().toString()),
                    Double.parseDouble(mHgSecond.getText().toString())
            };

            HashMap<String, Double> inputArgsMap = new HashMap<>();
            inputArgsMap.put("nominal_height", Double.valueOf(mNominalHeight.getText().toString()));
            inputArgsMap.put("instrument_height", Double.valueOf(mInstrumentHeight.getText().toString()));
            inputArgsMap.put("string_length", Double.valueOf(mStringLength.getText().toString()));
            inputArgsMap.put("span", Double.valueOf(mSpan.getText().toString()));


            double tanObAngle = UnitCalulate.calTanAngle(obAngles[0], obAngles[1], obAngles[2]);
            double tanHgAngle = UnitCalulate.calTanAngle(hgAngles[0], hgAngles[1], hgAngles[2]);

            valueOfA = UnitCalulate.calValueOfA(inputArgsMap.get("nominal_height"),
                    inputArgsMap.get("instrument_height"),
                    inputArgsMap.get("string_length"));
            valueOfB = UnitCalulate.calValueOfB(inputArgsMap.get("span"), tanHgAngle, tanObAngle);
            sag = UnitCalulate.calValueOfSag(valueOfA, valueOfB);

            obAngle = UnitCalulate.calDegreeObservation(tanHgAngle, sag, valueOfA, inputArgsMap.get("span"));

            Message msg = new Message();
            Bundle result = new Bundle();
            result.putDouble("value_of_a", valueOfA);
            result.putDouble("value_of_b", valueOfB);
            result.putDouble("value_of_sag", sag);
            result.putIntArray("value_of_obangle", UnitCalulate.getDegreeBit(obAngle));
            msg.setData(result);
            CragCalFragment.this.mResultHandler.sendMessage(msg);
        }
    }

    //检验文本是否输入
    private boolean testInputArgs() {

        if (mSpan.length() > 0 && mNominalHeight.length() > 0 &&
                mInstrumentHeight.length() > 0 && mStringLength.length() > 0 &&
                mHgDegree.length() > 0 && mHgMinute.length() > 0 && mHgSecond.length() > 0 &&
                mObDegree.length() > 0 && mHgMinute.length() > 0 && mObSecond.length() > 0) {
            return true;
        } else {
            return false;
        }

    }
}
