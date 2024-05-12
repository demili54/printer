package com.dantsu.thermalprinter;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dantsu.thermalprinter.async.AsyncBluetoothEscPosPrint;
import com.dantsu.thermalprinter.async.AsyncEscPosPrint;
import com.dantsu.thermalprinter.async.AsyncEscPosPrinter;
import com.dantsu.thermalprinter.async.AsyncUsbEscPosPrint;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MainActivity me;
    private PrinterSettings printerSettings;
    private DatePickerDialog dateStartPickerDialog;
    private TimePickerDialog timeStartPickerDialog;

    private DatePickerDialog dateEndPickerDialog;
    private TimePickerDialog timeEndPickerDialog;

    private TextView startTimeInput;
    private TextView endTimeInput;
    private TextToImageService textToImageService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me =this;
        this.printerSettings=new PrinterSettings();



        setContentView(R.layout.activity_main);
        Button btnReprint = (Button) this.findViewById(R.id.btnReprint);
        btnReprint.setOnClickListener(view -> lastPrint());
        Button button = (Button) this.findViewById(R.id.button_bluetooth_browse);
        button.setOnClickListener(view -> browseBluetoothDevice());
        button = (Button) findViewById(R.id.button_bluetooth);
        button.setOnClickListener(view -> printBluetooth());

        this.setPrinterSettings();
        this.initPicker();






    }
    public void setPrinterSettings(){
        bindAll();
        loadAll();
        //시간
        this.printerSettings.setStartTime(Calendar.getInstance());
        this.printerSettings.setEndTime(Calendar.getInstance());

    }
    private void initPicker(){
        Calendar startDateCal = Calendar.getInstance();
        dateStartPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                printerSettings.getStartTime().set(year,month,dayOfMonth);
                //startTimeInput.setText(year + "-" + month + "-" + dayOfMonth);
                dateStartPickerDialog.hide();
                showStartTimeDialLog();
            }
        }, startDateCal.get(Calendar.YEAR), startDateCal.get(Calendar.MONTH), startDateCal.get(Calendar.DATE));


        Calendar startTimeCal = Calendar.getInstance();
        timeStartPickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar start = printerSettings.getStartTime();
                start.set(Calendar.HOUR_OF_DAY,hourOfDay);
                start.set(Calendar.MINUTE,minute);
                printerSettings.setStartTime(start);

                startTimeInput.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(start.getTime()));
                timeStartPickerDialog.hide();
            }
        }, startTimeCal.get(Calendar.HOUR_OF_DAY), startTimeCal.get(Calendar.MINUTE), false);

        Calendar endTimeCal = Calendar.getInstance();
        timeEndPickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar end = printerSettings.getEndTime();
                end.set(Calendar.HOUR_OF_DAY,hourOfDay);
                end.set(Calendar.MINUTE,minute);
                printerSettings.setEndTime(end);
                endTimeInput.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(end.getTime()));
                timeEndPickerDialog.hide();

            }
        }, endTimeCal.get(Calendar.HOUR_OF_DAY), endTimeCal.get(Calendar.MINUTE), false);

        Calendar endDateCal = Calendar.getInstance();
        dateEndPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                printerSettings.getEndTime().set(year,month,dayOfMonth);
                //EndTimeInput.setText(year + "-" + month + "-" + dayOfMonth);
                dateEndPickerDialog.hide();
                showEndTimeDialLog();
            }
        }, endDateCal.get(Calendar.YEAR), endDateCal.get(Calendar.MONTH), endDateCal.get(Calendar.DATE));



        startTimeInput = (TextView) me.findViewById(R.id.startTime);
        startTimeInput.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        startTimeInput.setOnClickListener(view -> {
            showStartDateDialLog();
        });

        endTimeInput = (TextView) me.findViewById(R.id.endTime);
        endTimeInput.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        endTimeInput.setOnClickListener(view -> {
            showEndDateDialLog();
        });
    }
    public void bindAll(){
        textToImageService=new TextToImageService((TextView) me.findViewById(R.id.imageTempL),(TextView) me.findViewById(R.id.imageTempR),(TextView) me.findViewById(R.id.imageTempC),(TextView) me.findViewById(R.id.imageTempLineFeed),this.printerSettings);
        printerSettings.setPrintName((Button)me.findViewById(R.id.button_bluetooth_browse));
        printerSettings.setCarNumber((EditText) me.findViewById(R.id.carNumber));
        printerSettings.setMinutes((EditText) me.findViewById(R.id.timeTerm));
        printerSettings.setCompanyName((EditText) me.findViewById(R.id.companyName));
        printerSettings.setTemperatureA((EditText) me.findViewById(R.id.temperatureA));
        printerSettings.setTemperatureB((EditText) me.findViewById(R.id.temperatureB));
        printerSettings.setTemperatureACha((EditText) me.findViewById(R.id.temperatureACha));
        printerSettings.setTemperatureBCha((EditText) me.findViewById(R.id.temperatureBCha));
        printerSettings.setTemperatureAChaJum((EditText) me.findViewById(R.id.temperatureAChaJum));
        printerSettings.setTemperatureBChaJum((EditText) me.findViewById(R.id.temperatureBChaJum));

        printerSettings.setCheckA((CheckBox) me.findViewById(R.id.aSelect));
        printerSettings.setCheckB((CheckBox) me.findViewById(R.id.bSelect));
        printerSettings.setLastComment((RadioButton) me.findViewById(R.id.endCommentStop),(RadioButton) me.findViewById(R.id.endCommentOff));
    }
    public void addMinute(Date date,Integer minute){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        date = cal.getTime();
    }

    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/

    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }

    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;

    public OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case MainActivity.PERMISSION_BLUETOOTH:
                case MainActivity.PERMISSION_BLUETOOTH_ADMIN:
                case MainActivity.PERMISSION_BLUETOOTH_CONNECT:
                case MainActivity.PERMISSION_BLUETOOTH_SCAN:
                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
                    break;
            }
        }
    }

    public void checkBluetoothPermissions(OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            this.onBluetoothPermissionsGranted.onPermissionsGranted();
        }
    }

    private BluetoothConnection selectedDevice;

    public void browseBluetoothDevice() {
        this.checkBluetoothPermissions(() -> {
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

            if (bluetoothDevicesList != null) {
                final String[] items = new String[bluetoothDevicesList.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothDevicesList) {
                    items[++i] = device.getDevice().getName();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Bluetooth printer selection");
                alertDialog.setItems(
                    items,
                    (dialogInterface, i1) -> {
                        int index = i1 - 1;
                        if (index == -1) {
                            selectedDevice = null;
                        } else {
                            selectedDevice = bluetoothDevicesList[index];
                        }
                        Button button = printerSettings.getPrintNameBtn();
                        button.setText(items[i1]);
                        saveData("printerName",items[i1]);

                    }
                );

                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });

    }
    public void selectPrinter(){
        this.checkBluetoothPermissions(() -> {
            if(selectedDevice!=null){
                return;
            }
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();
            BluetoothConnection first=null;
            if (bluetoothDevicesList != null) {
                for (BluetoothConnection device : bluetoothDevicesList) {
                    if(first==null){
                        first=device;
                    }
                    String settingName = printerSettings.getPrintNameBtn().getText().toString();
                    String curDeviceName=device.getDevice().getName();
                    if(settingName.equals(curDeviceName)){
                        selectedDevice= device;
                        printerSettings.getPrintNameBtn().setText(curDeviceName);
                        saveData("printerName",curDeviceName);
                        return;
                    }
                }
                //return 안됬을경우
                selectedDevice = first;
                if(selectedDevice==null){
                    debug();
                    return;
                }
                String firstName = first.getDevice().getName();
                printerSettings.getPrintNameBtn().setText(firstName);
                saveData("printerName",firstName);
             }
        });
    }

    public void printBluetooth() {

        if(!printerSettings.validata(this)){
            return;
        }
        saveAll();
        selectPrinter();
        this.checkBluetoothPermissions(() -> {
            AsyncBluetoothEscPosPrint printer = new AsyncBluetoothEscPosPrint(
                this,
                new AsyncEscPosPrint.OnPrintFinished() {
                    @Override
                    public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                        Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                    }

                    @Override
                    public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                        Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                    }
                }
            );

            printer.execute(me.getAsyncEscPosPrinter(selectedDevice));


        });
    }

    /*==============================================================================================
    ===========================================USB PART=============================================
    ==============================================================================================*/

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MainActivity.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                context,
                                new AsyncEscPosPrint.OnPrintFinished() {
                                    @Override
                                    public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                        Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                    }

                                    @Override
                                    public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                        Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                                    }
                                }
                            )
                                .execute(getAsyncEscPosPrinter(new UsbConnection(usbManager, usbDevice)));
                        }
                    }
                }
            }
        }
    };


    /*==============================================================================================
    =========================================TCP PART===============================================
    ==============================================================================================*/



    /*==============================================================================================
    ===================================ESC/POS PRINTER PART=========================================
    ==============================================================================================*/
    private void showStartDateDialLog(){
            dateStartPickerDialog.show();
    }


    private void showStartTimeDialLog(){
            timeStartPickerDialog.show();
    }
    private void showEndDateDialLog(){
        dateEndPickerDialog.show();
    }


    private void showEndTimeDialLog(){
        timeEndPickerDialog.show();
    }



    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年 MM月 dd日");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        double gap = Math.random();


        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);


        //차량번호 + 기록간격 + 상호
        String printerText ="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageL("차량번호 : "+printerSettings.getCarNumber()))+"</img>\n";
        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageL("기록간격 : "+printerSettings.getMinutes()+ " 분"))+"</img>\n";
        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageL("상   호 : "+printerSettings.getCompanyName()))+"</img>\n";
        printerText+="[L]\n";
        printerText+="[L]\n";
        boolean run = true;
        Calendar start = printerSettings.getStartTime();
        Calendar end = printerSettings.getEndTime();

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(end.getTime());



        String currentDateString = formatDate.format(end.getTime());
        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageL(currentDateString))+"</img>\n";
        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageAuto(genTemperature(formatTime.format(end.getTime()),true,false)))+"</img>\n";
        while(run){
            currentTime.add(Calendar.MINUTE, -1*printerSettings.getMinutes());
            if(start.after(currentTime) || start.equals(currentTime)){
                run=false;
                break;
            }

            if(!currentDateString.equals(formatDate.format(currentTime.getTime()))){
                printerText+="[L]\n";
                currentDateString=formatDate.format(currentTime.getTime());
                printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageL(currentDateString))+"</img>\n";

            }
            printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageAuto(genTemperature(formatTime.format(currentTime.getTime()),false,false)))+"</img>\n";
            printerText+="[L]"+genTemperature(formatTime.format(currentTime.getTime()),false,false)+"\n";
        }

        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageAuto(genTemperature(formatTime.format(start.getTime()),false,true)))+"</img>\n";
        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageLineFeed())+"</img>\n";
        printerText+="[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,textToImageService.createImageL(printerSettings.getLastComment().getCode()))+"</img>\n";


        saveData("lastPrint",printerText);

        printer.addTextToPrint(
                printerText
        );





        return printer;
    }
    public void debug(){
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        double gap = Math.random();


        //차량번호 + 기록간격 + 상호
        boolean run = true;
        Calendar start = printerSettings.getStartTime();
        Calendar end = printerSettings.getEndTime();

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(end.getTime());

        String printerText="";

        String currentDateString = formatDate.format(end.getTime());
        while(run){
            currentTime.add(Calendar.MINUTE, -1*printerSettings.getMinutes());
            if(!currentDateString.equals(formatDate.format(currentTime.getTime()))){

            }
            if(start.after(currentTime)){
                run=false;
                break;
            }
            //printerText+="[R]"+genTemperature(formatTime.format(currentTime.getTime()),false,false) + "\n";
            printerText+=genTemperature(formatTime.format(currentTime.getTime()),false,false);
            printerText+="\n";
        }
        //printerText+="[R]"+genTemperature(formatTime.format(start.getTime()),false,true) + "\n";
        printerText+=genTemperature(formatTime.format(start.getTime()),false,true);
        //printerText+="[L]"+printerSettings.getLastComment().getCode() + "\n";
        printerText+=printerSettings.getLastComment().getCode();

        System.out.println(printerText);

        saveData("lastPrint",printerText);
    }

    public void lastPrint() {
        this.checkBluetoothPermissions(() -> {
            AsyncBluetoothEscPosPrint printer = new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                        }

                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                        }
                    }
            );

            printer.execute(me.getAsyncEscPosPrinterLast(selectedDevice));


        });
    }
    public AsyncEscPosPrinter getAsyncEscPosPrinterLast(BluetoothConnection printerConnection){
        String lastPrint = loadData("lastPrint","");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        printer.addTextToPrint(
                lastPrint
        );
        return printer;
    }
    private String genTemperature(String date,boolean start,boolean end){


        String returnString = date;
        String position = "     ";
        String AB = "B:";

        if(start){
            position="S   ";
        }else if(end){
            position="P   ";
        }



        returnString +=position;
        float chaA = ceil((printerSettings.getTemperatureACha()+(printerSettings.getTemperatureAChaJum()/10f))/2d,1);
        float chaB = ceil((printerSettings.getTemperatureBCha()+(printerSettings.getTemperatureBChaJum()/10f))/2d,1);
        if(printerSettings.getCheckA()){
            AB="A: ";
            returnString +=AB;
            Float resultA = printerSettings.getTemperatureA().floatValue()-(getPlusMinus()*random(chaA,1));
            if(resultA>0){
                returnString +='+';
            }else{
                resultA = Math.abs(resultA);
                returnString +='-';
            }
            returnString +=ceil(resultA.doubleValue(),1);
        }
        if(printerSettings.getCheckB()){
            if(printerSettings.getCheckA()){
                returnString +="  ";
            }
            AB="B: ";
            returnString +=AB;
            Float resultB = printerSettings.getTemperatureB().floatValue()-(getPlusMinus()*random(chaB,1));

            if(resultB>0){
                returnString +='+';
            }else{
                resultB = Math.abs(resultB);
                returnString +='-';
            }
            returnString +=ceil(resultB.doubleValue(),1);
        }


        returnString+=" ℃";





        return returnString;
    }

    private float getPlusMinus(){
        if(Math.round(Math.random()*10)%2==0){
            return -1f;
        }else{
            return 1f;
        }
    }
    private Float ceil(Double num,int size){
        Float value = new BigDecimal(num).setScale(size, BigDecimal.ROUND_DOWN).floatValue();
        return value;
    }
    private Float random(Float num,int size){
        return ceil(Math.random() * num,1).floatValue();
    }

    private void saveAll(){
        //차번호
        /*saveData("carNumber",printerSettings.getCarNumber());*/

        //프린터 이름
        //saveData("printerName",printerSettings.getPrintNameBtn().getText().toString());

        //기록간격
        saveData("minutes",printerSettings.getMinutes().toString());

        //상호
        saveData("companyName",printerSettings.getCompanyName());
        //시작

        //끝

        //온도기록
        saveData("checkA",printerSettings.getCheckA().toString());
        saveData("checkB",printerSettings.getCheckB().toString());



        //온도
        saveData("temperatureA",printerSettings.getTemperatureA().toString());
        saveData("temperatureB",printerSettings.getTemperatureB().toString());
        saveData("temperatureACha",printerSettings.getTemperatureACha().toString());
        saveData("temperatureBCha",printerSettings.getTemperatureBCha().toString());
        saveData("temperatureAChaJum",printerSettings.getTemperatureAChaJum().toString());
        saveData("temperatureBChaJum",printerSettings.getTemperatureBChaJum().toString());


        //종료문구
        saveData("endCommentStop",printerSettings.getEndCommentStop().toString());
        saveData("endCommentOff",printerSettings.getEndCommentOff().toString());
    }
    private void loadAll(){
        //차번호
        /*printerSettings.setCarNumber(loadData("carNumber","12가3456"));*/
        //프린터 이름
        printerSettings.setPrintName(loadData("printerName","프린터찾기"));

        //기록간격
        printerSettings.setMinutes(loadData("minutes","10"));
        //상호
        printerSettings.setCompanyName(loadData("companyName",""));
        //시작

        //끝

        //온도기록
        printerSettings.setCheckA(loadData("checkA",TRUE.toString()));
        printerSettings.setCheckB(loadData("checkB", FALSE.toString()));

        //온도
        printerSettings.setTemperatureA(loadData("temperatureA","-20"));
        printerSettings.setTemperatureB(loadData("temperatureB","-20"));
        printerSettings.setTemperatureACha(loadData("temperatureACha","3"));
        printerSettings.setTemperatureBCha(loadData("temperatureBCha","3"));
        printerSettings.setTemperatureAChaJum(loadData("temperatureAChaJum","0"));
        printerSettings.setTemperatureBChaJum(loadData("temperatureBChaJum","0"));

        //종료문구
        printerSettings.setLastComment(loadData("endCommentStop","true"),loadData("endCommentOff","false"));

    }



    private void saveData(String key,String value) {
        SharedPreferences pref = getSharedPreferences("print", 0);
        SharedPreferences.Editor edit = pref.edit(); // 수정 모드
        // 1번째 인자는 키, 2번째 인자는 실제 담아둘 값
        edit.putString(key, value);
        edit.apply(); // 저장완료
    }

    private String loadData(String key,String defaultVal) {
        SharedPreferences pref = getSharedPreferences("print", 0);
        // 1번째 인자는 키, 2번째 인자는 데이터가 존재하지 않을경우의 값
        return pref.getString(key,defaultVal);
    }
}
