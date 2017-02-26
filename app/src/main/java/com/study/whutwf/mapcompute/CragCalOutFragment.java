package com.study.whutwf.mapcompute;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by whutw on 2017/2/25 0025.
 */

public class CragCalOutFragment extends Fragment {

    private Button mSubmitButton;
    private EditText mOutL;
    private EditText mOutLOther;
    private EditText mOutF;
    private EditText mA1Degree;
    private EditText mA1Minute;
    private EditText mA1Second;
    private EditText mA2Degree;
    private EditText mA2Minute;
    private EditText mA2Second;

    private EditText mTestOutF;
    private EditText mResultDegree;

    ResultHandler mResultHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResultHandler = new ResultHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crag_cal_out, container, false);
        initView(v);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testInputArgs()) {
                    ResultThread resultThread = new ResultThread();
                    new Thread(resultThread).start();
                } else {
                    Toast.makeText(getContext(), "输入正确数据", Toast.LENGTH_LONG).show();
                }

            }
        });

        return v;
    }

    private void initView(View v) {
        mSubmitButton = (Button) v.findViewById(R.id.bt_crag_cal_out_get_result);
        mOutL = (EditText) v.findViewById(R.id.crag_cal_out_L);
        mOutLOther = (EditText) v.findViewById(R.id.crag_cal_out_L_other);
        mOutF = (EditText) v.findViewById(R.id.crag_cal_out_f);

        mA1Degree = (EditText) v.findViewById(R.id.tv_crag_cal_a1_degree);
        mA1Minute = (EditText) v.findViewById(R.id.tv_crag_cal_a1_minute);
        mA1Second = (EditText) v.findViewById(R.id.tv_crag_cal_a1_second);
        mA2Degree = (EditText) v.findViewById(R.id.tv_crag_cal_a2_degree);
        mA2Minute = (EditText) v.findViewById(R.id.tv_crag_cal_a2_minute);
        mA2Second = (EditText) v.findViewById(R.id.tv_crag_cal_a2_second);

        mTestOutF = (EditText) v.findViewById(R.id.crag_cal_out_F);
        mResultDegree = (EditText) v.findViewById(R.id.crag_cal_out_a3);

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

            int[] angle;
            angle = result.getIntArray("value_of_a3angle");
            mResultDegree.setText("角度：" + angle[0] + "度" + angle[1] + "分" + angle[2] + "秒");
            mTestOutF.setText(result.getDouble("value_of_test_f") + "");
        }
    }

    class ResultThread implements Runnable {
        @Override
        public void run() {
            double[] a1Angle = {
                    Double.parseDouble(mA1Degree.getText().toString()),
                    Double.parseDouble(mA1Minute.getText().toString()),
                    Double.parseDouble(mA1Second.getText().toString()),
            };

            double[] a2Angle = {
                    Double.parseDouble(mA2Degree.getText().toString()),
                    Double.parseDouble(mA2Minute.getText().toString()),
                    Double.parseDouble(mA2Second.getText().toString()),
            };

            //除了Angle之外的参数
            HashMap<String, Double> inputTextMap = new HashMap<>();
            inputTextMap.put("outL", Double.parseDouble(mOutL.getText().toString()));
            inputTextMap.put("outLOther", Double.parseDouble(mOutLOther.getText().toString()));
            inputTextMap.put("outF", Double.parseDouble(mOutF.getText().toString()));

            double B6 = UnitCalulate.calTanAngle(a1Angle[0], a1Angle[1], a1Angle[2])
                    * inputTextMap.get("outLOther");
            double B7 = UnitCalulate.calTanAngle(a2Angle[0], a2Angle[1], a2Angle[2])
                    * (inputTextMap.get("outLOther") + inputTextMap.get("outL"));
            double B8 = B7 - B6;
            double D6 = inputTextMap.get("outL") * (B8 - 4 * inputTextMap.get("outF"))
                    - 8 * inputTextMap.get("outLOther") * inputTextMap.get("outF");
            double D7 = inputTextMap.get("outF") * (8 * B8 + 16 * B6)
                    - 16 * Math.pow(inputTextMap.get("outF"), 2) - Math.pow(B8, 2);
            double D8 = (D6 + Math.sqrt(Math.pow(D6, 2) +
                    D7 * Math.pow(inputTextMap.get("outL"), 2))) /
                    Math.pow(inputTextMap.get("outL"), 2);
            double F5 = Math.atan(D8) * 180 / Math.PI;

            double B10 = UnitCalulate.calTanAngle(F5);
            double C11 = B6 - inputTextMap.get("outLOther") * B10;
            double E11 = Math.sqrt(C11);
            double C12 = B6 - (inputTextMap.get("outL") + inputTextMap.get("outLOther")) * B10 + B8;
            double E12 = Math.sqrt(C12);
            double F = Math.pow(E11 + E12, 2) / 4;

            Message msg = new Message();
            Bundle result = new Bundle();
            result.putIntArray("value_of_a3angle", UnitCalulate.getDegreeBit(F5));
            result.putDouble("value_of_test_f", F);
            msg.setData(result);
            CragCalOutFragment.this.mResultHandler.sendMessage(msg);
        }
    }

    //检验文本是否输入
    private boolean testInputArgs() {

        if (mOutL.length() > 0 && mOutLOther.length() > 0 && mOutF.length() > 0 &&
                mA1Degree.length() > 0 && mA1Minute.length() > 0 && mA1Second.length() > 0 &&
                mA2Degree.length() > 0 && mA2Minute.length() > 0 && mA2Second.length() > 0) {
            return true;
        } else {
            return false;
        }

    }

}
