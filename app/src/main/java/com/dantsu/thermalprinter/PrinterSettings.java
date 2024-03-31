package com.dantsu.thermalprinter;



import android.app.Activity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class PrinterSettings {
    public enum LAST_COMMENT{

        USER_STOP("USER_STOP"),PRINT_AFTER_POWER_ON("PRINT AFTER POWER ON");
        private String code;

        LAST_COMMENT(String code) {
            this.code= code;
        }

        public String getCode(){
            return code;
        }
    }
    private Button printName;
    private TextView carNumber;
    private TextView minutes;
    private TextView companyName;

    private Calendar startTime;
    private TextView startTimeView;
    private Calendar endTime;
    private TextView endTimeView;

    private CheckBox checkA;
    private CheckBox checkB;

    private TextView temperatureA;
    private TextView temperatureACha;
    private TextView temperatureB;
    private TextView temperatureBCha;

    private RadioButton endCommentStop;
    private RadioButton endCommentOff;

    public Button getPrintNameBtn() {
        return printName;
    }

    public void setPrintName(Button printName) {
        this.printName = printName;
    }
    public void setPrintName(String printName) {
        this.printName.setText(printName);
    }

    public String getCarNumber() {
        return getTString(carNumber);
    }

    public void setCarNumber(TextView carNumber) {
        this.carNumber = carNumber;
    }
    public void setCarNumber(String carNumber) {
        this.carNumber.setText(carNumber);
    }



    public Integer getMinutes() {
        return getTInteger(minutes);
    }

    public void setMinutes(TextView minutes) {
        this.minutes = minutes;
    }
    public void setMinutes(String minutes) {
        this.minutes.setText(minutes);
    }

    public String getCompanyName() {
        return getTString(companyName);
    }
    public TextView getCompanyName(String text) {
         companyName.setText(text);
        return companyName;
    }

    public void setCompanyName(TextView companyName) {
        this.companyName = companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName.setText(companyName);
    }
    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public TextView getStartTimeView() {
        return startTimeView;
    }

    public void setStartTimeView(TextView startTimeView) {
        this.startTimeView = startTimeView;
    }

    public TextView getEndTimeView() {
        return endTimeView;
    }

    public void setEndTimeView(TextView endTimeView) {
        this.endTimeView = endTimeView;
    }

    public Boolean getCheckA() {
        return checkA.isChecked();
    }

    public void setCheckA(CheckBox checkA) {
        this.checkA = checkA;
    }
    public void setCheckA(String checkA) {
        this.checkA.setChecked(Boolean.valueOf(checkA));
    }

    public Boolean getCheckB() {
        return checkB.isChecked();
    }

    public void setCheckB(CheckBox checkB) {
        this.checkB = checkB;
    }

    public void setCheckB(String checkB) {
        this.checkB.setChecked(Boolean.valueOf(checkB));
    }
    public Float getTemperatureA() {
        return getTFloat(temperatureA);
    }

    public void setTemperatureA(TextView temperatureA) {
        this.temperatureA = temperatureA;
    }
    public void setTemperatureA(String temperatureA) {
        this.temperatureA.setText(temperatureA);
    }

    public Float getTemperatureB() {
        return getTFloat(temperatureB);
    }

    public void setTemperatureB(TextView temperatureB) {
        this.temperatureB = temperatureB;
    }

    public void setTemperatureB(String temperatureB) {
        this.temperatureB.setText(temperatureB);
    }
    public LAST_COMMENT getLastComment() {
        if(endCommentStop.isChecked()){
            return LAST_COMMENT.USER_STOP;
        }else{
            return LAST_COMMENT.PRINT_AFTER_POWER_ON;
        }
    }

    public void setLastComment(RadioButton endCommentStop, RadioButton endCommentOff) {
        this.endCommentStop = endCommentStop;
        this.endCommentOff = endCommentOff;
    }
    public void setLastComment(String endCommentStop, String endCommentOff) {
        this.endCommentStop.setChecked(Boolean.valueOf(endCommentStop));
        this.endCommentOff.setChecked(Boolean.valueOf(endCommentOff));
    }

    public Boolean getEndCommentStop() {
        return endCommentStop.isChecked();
    }

    public Boolean getEndCommentOff() {
        return endCommentOff.isChecked();
    }

    public String getTString(TextView textView){
        return textView.getText().toString();
    }
    public Integer getTInteger(TextView textView){
        return Integer.valueOf(textView.getText().toString());
    }
    public Float getTFloat(TextView textView){
        return Float.valueOf(textView.getText().toString());
    }

    public Integer getTemperatureACha() {
        return getTInteger(temperatureACha);
    }


    public void setTemperatureACha(TextView temperatureACha) {
        this.temperatureACha = temperatureACha;
    }
    public void setTemperatureACha(String temperatureACha) {
        if(temperatureACha.trim().length()<1){
            temperatureACha="3";
        }
        this.temperatureACha.setText(temperatureACha);
    }

    public Integer getTemperatureBCha() {
        return getTInteger(temperatureBCha);
    }

    public void setTemperatureBCha(TextView temperatureBCha) {
        this.temperatureBCha = temperatureBCha;
    }
    public void setTemperatureBCha(String temperatureBCha) {
        if(temperatureBCha.trim().length()<1){
            temperatureBCha="3";
        }
        this.temperatureBCha.setText(temperatureBCha);
    }


    public boolean validata(Activity activity){
        if(TextUtils.isEmpty(this.minutes.getText())){
            popup(activity,"기록간격 입력필요.");
            return false;
        }
        if(this.getCheckA()&&( TextUtils.isEmpty(this.temperatureA.getText()) || TextUtils.isEmpty(this.temperatureACha.getText()))){
            popup(activity,"A출력 입력필요.");
            return false;
        }
        if(this.getCheckB()&&( TextUtils.isEmpty(this.temperatureB.getText()) || TextUtils.isEmpty(this.temperatureBCha.getText()))){
            popup(activity,"B출력 입력필요.");
            return false;
        }
        if(!this.getCheckA()&&!this.getCheckB()){
            popup(activity,"출력 선택필요.");
            return false;
        }



        return true;
    }
    private void popup(Activity activity,String message){

        Toast myToast = Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT);
        myToast.show();
    }

}
