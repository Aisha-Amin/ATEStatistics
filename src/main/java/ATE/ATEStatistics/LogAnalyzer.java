package ATE.ATEStatistics;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * The LogAnalyzer class handles the parsing of log content
 */
public class LogAnalyzer {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /**
     * Parses the log content and extracts information about devices, insertion, testing, and removal.
     *
     * @param content The log content to be parsed.
     * @return A list of Device objects containing parsed information.
     */
    public List<Device> parseLogContent(String content) {
        List<Device> devices = new ArrayList<>();
        String[] lines = content.split("\n");

        Device currentDevice = null;
        SubTest currentSubTest = null;

        for (String line : lines) {
            if (line.contains("BEGIN INSERTION")) {
                String deviceName = getName(line);
                currentDevice = new Device(deviceName);
                currentDevice.setInsertionStartTime(getTime(line));

            } else if(line.contains("END INSERTION")){
                currentDevice.setInsertionEndTime(getTime(line));

            } else if (line.contains("BEGIN DEVICE_TEST")) {
                if (line.contains("BEGIN DEVICE_TEST.SUBTEST")){
                    String subTestName = getName(line);
                    currentSubTest = new SubTest(subTestName);
                    currentSubTest.setStartTime(getTime(line));

                }else{
                    currentDevice.setDeviceStartTestTime(getTime(line));

                }

            }else if (line.contains("END DEVICE_TEST")) {
                if(line.contains("END DEVICE_TEST.SUBTEST")){
                    currentSubTest.setEndTime(getTime(line));
                    currentDevice.addSubTest(currentSubTest);

                }else{
                    currentDevice.setDeviceEndTestTime(getTime(line));

                }

            } else if (line.contains("BEGIN REMOVAL")) {
                currentDevice.setRemovalStartTime(getTime(line));

            }else if (line.contains("END REMOVAL")) {
                currentDevice.setRemovalEndTime(getTime(line));
                devices.add(currentDevice);

            }
        }
        return devices;

    }

    // Method to convert timestamp in the log to milliseconds
    public static long getTime(String line) {
        int startIndex = line.indexOf("[") + 1;
        int endIndex = line.indexOf("]");
        String timestamp = line.substring(startIndex, endIndex);

        // Parse the timestamp using java.time.LocalTime
        LocalTime localTime = LocalTime.parse(timestamp, TIMESTAMP_FORMATTER);

        // Convert to milliseconds
        return localTime.toNanoOfDay() / 1_000_000;
    }

    // Method to extract the name from a log line
    public static String getName(String line) {
        int startIndex = line.indexOf("\"") + 1;
        int endIndex = line.lastIndexOf("\"");
        return line.substring(startIndex, endIndex);
    }


}
