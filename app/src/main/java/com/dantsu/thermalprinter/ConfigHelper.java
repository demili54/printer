/*
package com.dantsu.thermalprinter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ConfigHelper {
    private final String FILE_PATH = System.getProperty("user.home") + "/coordinate.json";
    private List<Object> list;
    private static ConfigHelper _instance;

    public static ConfigHelper getInstance() {
        if (_instance == null) {
            _instance = new ConfigHelper();
        }
        return _instance;
    }

    private ConfigHelper() {
        list = new ArrayList<>();
    }

    */
/**
     * 저장소에 있는 파일을 읽어서 스트링으로 반환한다
     *
     * @return 파일 내용
     *//*

    private String readFile() {
        if (checkFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                StringBuilder stringBuilder = new StringBuilder();
                char[] buffer = new char[4096];
                int read;
                while ((read = inputStreamReader.read(buffer)) != -1) {
                    stringBuilder.append(buffer, 0, read);
                }
                inputStreamReader.close();
                fileInputStream.close();
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    */
/**
     * 객체를 제이슨 스트링으로 변환하여, 파일에 저장한다
     *//*

    public void saveFile() {
        String json = toJson();
        writeFile(json);
    }

    */
/**
     * 파일에 있는 제이슨을 읽어서, 객체로 파싱하여 반환한다
     *
     * @return 객체 리스트
     *//*

    public List<Object> getListToFile() {
        String msg = readFile();
        return JsonConvert.DeserializeObject<List<Object>>(msg);
    }

    */
/**
     * 현재 리스트를 반환한다
     *
     * @return 오브젝트 리스트
     *//*

    public List<Object> getList() {
        return list;
    }

    */
/**
     * 제이슨 스트링을 내부저장소에 저장한다
     *
     * @param json 제이슨 스트링
     *//*

    private void writeFile(String json) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            outputStreamWriter.write(json);
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 샘플
     *
     * @param name 오브젝트 이름
     *//*

    public void makeObject(String name) {
        Object iom = new Object();
        iom.setName(name);
        iom.setPosition(new InsVector((float) (Math.random() * 180), (float) (Math.random() * 180), (float) (Math.random() * 180)));
        iom.setRotate(new InsVector(30.0f, 45.0f, 90.0f));
        add(iom);
    }

    */
/**
     * 추가
     *
     * @param item 추가할 아이템
     *//*

    public void add(Object item) {
        list.add(item);
    }

    */
/**
     * 파일이 존재하는지 체크한다
     *
     * @return 결과
     *//*

    public boolean checkFile() {
        File file = new File(FILE_PATH);
        return file.exists();
    }

    */
/**
     * 리스트를 제이슨 스트링으로 변환한다
     *
     * @return 제이슨 스트링
     *//*

    private String toJson() {
        return JsonConvert.SerializeObject(list);
    }
}


*/
