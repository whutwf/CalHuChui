package com.study.whutwf.mapcompute;

import android.util.Log;

/**
 * Created by whutw on 2017/2/19 0019.
 */

public class UnitCalulate {

    //计算A值
    public static double calValueOfA(double nomHeight, double insHeight, double stringLen) {
        return nomHeight - insHeight - stringLen;
    }

    //计算B值
    public static double calValueOfB(double span, double tanHangleAngle, double tanObsAngle) {
        return (span * (tanHangleAngle - tanObsAngle));
    }

    //转化为度数
    private static double toAngles(int degree, int minute, int second) {
        return degree + minute / 60.0 + second / 3600.0;
    }

    //转化为度数
    private static double toAngles(double degree, double minute, double second) {
        return degree + minute / 60.0 + second / 3600.0;
    }

    //根据角度计算tan
    public static double calTanAngle(int degree, int minute, int second) {
        return Math.tan(Math.toRadians(toAngles(degree, minute, second)));
    }

    //根据角度计算tan
    public static double calTanAngle(double degree, double minute, double second) {
        return calTanAngle(toAngles(degree, minute, second));
    }

    //根据角度计算tan
    public static double calTanAngle(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    //计算弧垂值
    public static double calValueOfSag(double a, double b) {
        return Math.pow((Math.sqrt(a) + Math.sqrt(b)), 2) / 4;
    }

    //计算观测角度
    public static double calDegreeObservation(double tanHgAngle, double f, double a, double L) {
        double tmpl = Math.pow((2 * Math.pow(f, 0.5) - Math.pow(a, 0.5)), 2) / L;
        double result = tanHgAngle - tmpl;
        return Math.toDegrees(Math.atan(result));
    }

    public static int[] getDegreeBit(double result) {
        int[] temp = {0, 0, 0};
        temp[0] = (int) (result);
        double m_s = result - temp[0];
        temp[1] = (int) (m_s * 60);
        double s = m_s * 60 - temp[1];
        temp[2] = (int) Math.ceil (s * 60);
        return temp;
    }
}
