///* (C) 2022 */
//package com.spacesloth.meditrack;
//
//import static com.spacesloth.meditrack.Medication.NULL_DB_ENTRY_STRING;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.platform.app.InstrumentationRegistry;
//import java.sql.SQLDataException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class DatabaseTest {
//
//    private static final Uri DEFAULT_ALARM_URI =
//            Uri.parse("android.resource://com.spacesloth.meditrack/" + R.raw.eas_alarm);
//
//    public String getTestTime(int offset) {
//       DateTimeManager dateTimeManager = new DateTimeManager();
//       long oneMin = 60000L;
//       return dateTimeManager.formatLongAsTimeString(System.currentTimeMillis() + (oneMin * offset));
//    }
//
//    public Medication[] getTestPills() {
//        Medication[] medications = new Medication[5];
//
//        String[] pillNames = {"Melatonin", "Prozac", "Adderall", "Vitamins", "Supplements"};
//        String[][] pillTimes = {
//                new String[]{getTestTime(0)},
//                new String[]{getTestTime(1), getTestTime(2)},
//                new String[]{getTestTime(3), getTestTime(4)},
//                new String[]{getTestTime(5), getTestTime(6), getTestTime(7)},
//                new String[]{getTestTime(8), getTestTime(9), getTestTime(10), getTestTime(11)}
//        };
//        String[] pillStockups = {
//                "2023/01/19", "2023/01/19", "2023/01/19", "2023/01/19", "2023/01/19"
//        };
//        Uri[] customAlarmUris = {
//                DEFAULT_ALARM_URI,
//                DEFAULT_ALARM_URI,
//                DEFAULT_ALARM_URI,
//                DEFAULT_ALARM_URI,
//                DEFAULT_ALARM_URI
//        };
//        String[] pillStartDates = {
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING
//        };
//        int[] pillSupplies = {30, 30, 30, 30, 30};
//        int[] pillFrequency = {1, 0, 0, 0, 0};
//        int[] isTaken = {0, 0, 0, 0, 0};
//        int[] alarmTypes = {
//                DatabaseHelper.ALARM,
//                DatabaseHelper.ALARM,
//                DatabaseHelper.ALARM,
//                DatabaseHelper.ALARM,
//                DatabaseHelper.ALARM
//        };
//        String[] pillTakenTimes = {
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING,
//                NULL_DB_ENTRY_STRING
//        };
//        int[] alarmsSetArr = {0, 0, 0, 0, 0};
//        int[] bottleColorArr = {2, 2, 2, 2, 2};
//
//        for (int index = 0; index < medications.length; index++) {
//            medications[index] =
//                    new Medication(
//                            pillNames[index],
//                            pillTimes[index],
//                            pillStartDates[index],
//                            pillStockups[index],
//                            customAlarmUris[index],
//                            pillFrequency[index],
//                            isTaken[index],
//                            pillTakenTimes[index],
//                            pillSupplies[index],
//                            alarmTypes[index],
//                            alarmsSetArr[index],
//                            bottleColorArr[index]);
//        }
//        return medications;
//    }
//
//    @Test
//    public void addPills() {
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//
//        databaseHelper.deleteDatabase();
//
//        Medication[] medications = getTestPills();
//        for (Medication medication : medications) {
//            medication.addToDatabase(context);
//        }
//    }
//
//    @Test
//    public void retrieveAndVerifyPills() throws SQLDataException {
//        addPills();
//
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//
//        Medication[] medications = getTestPills();
//        Medication[] retrievedMedications = databaseHelper.getAllPills();
//
//        for (int index = 0; index < medications.length; index++) {
//            Medication medication = medications[index];
//            Medication retrievedMedication = retrievedMedications[index];
//
//            boolean name = retrievedMedication.getName().equals(medication.getName());
//            boolean times = true;
//            String[] pillTimesArray = medication.getTimesArray();
//            String[] retrievedPillTimesArray = retrievedMedication.getTimesArray();
//            for (int timeIndex = 0; timeIndex < retrievedPillTimesArray.length; timeIndex++) {
//                if (!retrievedPillTimesArray[timeIndex].equals(pillTimesArray[timeIndex])) {
//                    times = false;
//                    break;
//                }
//            }
//            boolean timeTaken = retrievedMedication.getTimeTaken().equals(medication.getTimeTaken());
//            boolean startDate = retrievedMedication.getStartDate().equals(medication.getStartDate());
//            boolean stockupDate = retrievedMedication.getStockupDate().equals(medication.getStockupDate());
//            boolean customAlarmUri =
//                    medication.getCustomAlarmUri()
//                            .toString()
//                            .equals(retrievedMedication.getCustomAlarmUri().toString());
//            boolean alarmType = retrievedMedication.getAlarmType() == medication.getAlarmType();
//            boolean alarmsSet = retrievedMedication.getAlarmsSet() == medication.getAlarmsSet();
//            boolean bottleColor = retrievedMedication.getBottleColor() == medication.getBottleColor();
//
//            String eq = "equal";
//            String notEq = "not equal";
//
//            System.out.println("Name " + (name ? eq : notEq));
//            System.out.println("Times " + (times ? eq : notEq));
//            System.out.println("TimeTaken " + (timeTaken ? eq : notEq));
//            System.out.println("StartDate " + (startDate ? eq : notEq));
//            System.out.println("StockupDate " + (stockupDate ? eq : notEq));
//            System.out.println("Uri " + (customAlarmUri ? eq : notEq));
//            System.out.println("AlarmType " + (alarmType ? eq : notEq));
//            System.out.println("AlarmsSet " + (alarmsSet ? eq : notEq));
//            System.out.println("BottleColor " + (bottleColor ? eq : notEq));
//
//
//            if (!name
//                    || !times
//                    || !timeTaken
//                    || !startDate
//                    || !stockupDate
//                    || !customAlarmUri
//                    || !alarmType
//                    || !alarmsSet
//                    || !bottleColor) throw new SQLDataException();
//
//            System.out.println();
//        }
//    }
//
//    @Test
//    public void readPillsCursor() {
//        addPills();
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//
//        Cursor cursor = databaseHelper.readSqlDatabase();
//        cursor.moveToFirst();
//    }
//
//    @Test
//    public void retrievePillsByPrimaryKey() {
//        addPills();
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//
//        Medication[] retrievedMedications = databaseHelper.getAllPills();
//
//        for (Medication retrievedMedication : retrievedMedications) {
//            databaseHelper.getPill(retrievedMedication.getPrimaryKey());
//        }
//    }
//}
