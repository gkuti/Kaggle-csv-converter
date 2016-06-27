package com.andela.gkuti;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Reader {
    private int recordLimit;
    private HashMap<String, ArrayList<String>> store;
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private int startIndex;
    private BufferedReader bufferedReader;
    private int totalRead;

    public Reader(int startIndex) {
        recordLimit = 500;
        this.startIndex = startIndex;
    }

    public void getReport() throws IOException {
        FileReader fileReader = new FileReader("SurveyData.csv");
        bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        ArrayList<String> keys = new ArrayList<>(Arrays.asList(line.split(",")));
        store = new HashMap<>();
        skipRecord(startIndex);
        line = bufferedReader.readLine();
        for (int j = 0; j < recordLimit && line != null; j++) {
            ArrayList<String> values = new ArrayList<>(Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)));
            if (j == 0) {
                for (int i = 0; i < keys.size(); i++) {
                    ArrayList<String> value = new ArrayList<>();
                    value.add(values.get(i));
                    store.put(keys.get(i), value);
                }
            } else {
                for (int i = 0; i < keys.size(); i++) {
                    ArrayList<String> value = store.get(keys.get(i));
                    value.add(values.get(i));
                    store.put(keys.get(i), value);
                }
            }
            line = bufferedReader.readLine();
            totalRead++;
        }
        getSchoolEducation();
        getSchoolMajor();
        getDebtPercentage();
        getStudentDebtAverage();
        getBootcampPercentage();
    }

    public void getSchoolMajor() {
        String major = "";
        int rate = 0;
        ArrayList<String> schoolMajorList = store.get("\"SchoolMajor\"");
        HashMap<String, Integer> schoolMajor = new HashMap<>();
        ArrayList<String> schoolMajorKeys = new ArrayList<>();
        for (String item : schoolMajorList) {
            if (schoolMajor.containsKey(item)) {
                int num = schoolMajor.get(item) + 1;
                schoolMajor.put(item, num);
            } else {
                schoolMajor.put(item, 1);
                if (!item.equals("NA"))
                    schoolMajorKeys.add(item);
            }
        }
        for (String key : schoolMajorKeys) {
            if (schoolMajor.get(key) > rate) {
                rate = schoolMajor.get(key);
                major = key;
            }
        }
        System.out.println("Most Popular school major amongst new coders is " + major + " with " + rate);
    }

    public void getSchoolEducation() {
        String level = "";
        int rate = 0;
        ArrayList<String> schoolMajorList = store.get("\"SchoolDegree\"");
        ArrayList<String> educationKeys = new ArrayList<>();
        HashMap<String, Integer> schoolMajor = new HashMap<>();
        for (String item : schoolMajorList) {
            if (schoolMajor.containsKey(item)) {
                int num = schoolMajor.get(item) + 1;
                schoolMajor.put(item, num);
            } else {
                schoolMajor.put(item, 1);
                if (!item.equals("NA"))
                    educationKeys.add(item);
            }
        }
        for (String key : educationKeys) {
            if (schoolMajor.get(key) > rate) {
                rate = schoolMajor.get(key);
                level = key;
            }
        }
        System.out.println("Highest level of education of new coders is " + level + " with " + rate);
    }

    public void getDebtPercentage() {
        ArrayList<String> studentDebtList = store.get("\"HasStudentDebt\"");
        HashMap<String, Integer> studentDebt = new HashMap<>();
        for (String item : studentDebtList) {
            if (studentDebt.containsKey(item)) {
                int num = studentDebt.get(item) + 1;
                studentDebt.put(item, num);
            } else {
                studentDebt.put(item, 1);
            }
        }
        double value = (studentDebt.get("1") / (totalRead * 1.0)) * 100;
        System.out.println("Percentage of new coders who owe student debt " + decimalFormat.format(value));
    }

    public void getStudentDebtAverage() {
        ArrayList<String> studentDebtList = store.get("\"StudentDebtOwe\"");
        double totalSum = 0;
        int totalStudent = 0;
        for (String item : studentDebtList) {
            if (!item.equals("NA")) {
                totalSum += Double.valueOf(item.replace("\"", ""));
                totalStudent++;
            }
        }
        System.out.println("Average amount of student debt owed " + decimalFormat.format(totalSum / (totalStudent * 1.0)));
    }

    public void getBootcampPercentage() {
        ArrayList<String> attendedBootcampList = store.get("\"AttendedBootcamp\"");
        HashMap<String, Integer> attentedBootcamp = new HashMap<>();
        for (String item : attendedBootcampList) {
            if (attentedBootcamp.containsKey(item)) {
                int num = attentedBootcamp.get(item) + 1;
                attentedBootcamp.put(item, num);
            } else {
                attentedBootcamp.put(item, 1);
            }
        }
        double attend = (attentedBootcamp.get("1") / (totalRead * 1.0)) * 100;
        System.out.println("Percentage of new coders that attended coding bootcamps " + decimalFormat.format(attend));
        double notAttend = (attentedBootcamp.get("0") / (totalRead * 1.0)) * 100;
        System.out.println("Percentage of new coders that didn't attend coding bootcamps " + decimalFormat.format(notAttend));
    }

    private void skipRecord(int startIndex) throws IOException {
        for (int i = 0; i < startIndex; i++) {
            bufferedReader.readLine();
        }
    }
}
