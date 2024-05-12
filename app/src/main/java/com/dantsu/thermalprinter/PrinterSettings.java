package com.dantsu.thermalprinter;



import android.app.Activity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class PrinterSettings {
    public enum LAST_COMMENT{

        USER_STOP("USER STOP"),PRINT_AFTER_POWER_ON("PRINT AFTER POWER ON");
        private String code;

        LAST_COMMENT(String code) {
            this.code= code;
        }

        public String getCode(){
            return code;
        }
    }
    private Button printName;
    private EditText carNumber;
    private EditText minutes;
    private EditText companyName;

    private Calendar startTime;
    private EditText startTimeView;
    private Calendar endTime;
    private EditText endTimeView;

    private CheckBox checkA;
    private CheckBox checkB;

    private EditText temperatureA;
    private EditText temperatureACha;
    private EditText temperatureAChaJum;
    private EditText temperatureB;
    private EditText temperatureBCha;
    private EditText temperatureBChaJum;

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

    public void setCarNumber(EditText carNumber) {
        this.carNumber = carNumber;
    }
    public void setCarNumber(String carNumber) {
        this.carNumber.setText(carNumber);
    }



    public Integer getMinutes() {
        return getTInteger(minutes);
    }

    public void setMinutes(EditText minutes) {
        this.minutes = minutes;
    }
    public void setMinutes(String minutes) {
        this.minutes.setText(minutes);
    }

    public String getCompanyName() {
        return getTString(companyName);
    }
    public EditText getCompanyName(String text) {
         companyName.setText(text);
        return companyName;
    }

    public void setCompanyName(EditText companyName) {
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

    public EditText getStartTimeView() {
        return startTimeView;
    }

    public void setStartTimeView(EditText startTimeView) {
        this.startTimeView = startTimeView;
    }

    public EditText getEndTimeView() {
        return endTimeView;
    }

    public void setEndTimeView(EditText endTimeView) {
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

    public void setTemperatureA(EditText temperatureA) {
        this.temperatureA = temperatureA;
    }
    public void setTemperatureA(String temperatureA) {
        this.temperatureA.setText(temperatureA);
    }

    public Float getTemperatureB() {
        return getTFloat(temperatureB);
    }

    public void setTemperatureB(EditText temperatureB) {
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

    public String getTString(EditText EditText){
        return EditText.getText().toString();
    }
    public Integer getTInteger(EditText EditText){
        return Integer.valueOf(EditText.getText().toString());
    }
    public Float getTFloat(EditText EditText){
        return Float.valueOf(EditText.getText().toString());
    }

    public Integer getTemperatureACha() {
        return getTInteger(temperatureACha);
    }


    public void setTemperatureACha(EditText temperatureACha) {
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

    public void setTemperatureBCha(EditText temperatureBCha) {
        this.temperatureBCha = temperatureBCha;
    }
    public void setTemperatureBCha(String temperatureBCha) {
        if(temperatureBCha.trim().length()<1){
            temperatureBCha="3";
        }
        this.temperatureBCha.setText(temperatureBCha);
    }

    public Integer getTemperatureAChaJum() {
        return getTInteger(temperatureAChaJum);
    }

    public void setTemperatureAChaJum(EditText temperatureAChaJum) {
        this.temperatureAChaJum = temperatureAChaJum;
    }
    public void setTemperatureAChaJum(String temperatureAChaJum) {
        if(temperatureAChaJum.trim().length()<1){
            temperatureAChaJum="0";
        }
        this.temperatureAChaJum.setText(temperatureAChaJum);
    }
    public Integer getTemperatureBChaJum() {
        return getTInteger(temperatureBChaJum);
    }

    public void setTemperatureBChaJum(EditText temperatureBChaJum) {
        this.temperatureBChaJum = temperatureBChaJum;
    }
    public void setTemperatureBChaJum(String temperatureBChaJum) {
        if(temperatureBChaJum.trim().length()<1){
            temperatureBChaJum="0";
        }
        this.temperatureBChaJum.setText(temperatureBChaJum);
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
