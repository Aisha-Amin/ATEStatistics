package ATE.ATEStatistics;

import java.util.HashMap;
import java.util.Map;

/* Represent Device Data */

public class Device {
    private String name;
    private long insertionStartTime;
    private long insertionEndTime;
    private long deviceStartTestTime;
    private long deviceEndTestTime;
    private long removalStartTime;
    private long removalEndTime;
    private Map<String, SubTest> subTests = new HashMap<>();

    public Device(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInsertionStartTime() {
        return insertionStartTime;
    }

    public void setInsertionStartTime(long insertionStartTime) {
        this.insertionStartTime = insertionStartTime;
    }

    public long getInsertionEndTime() {
        return insertionEndTime;
    }

    public void setInsertionEndTime(long insertionEndTime) {
        this.insertionEndTime = insertionEndTime;
    }

    public long getDeviceStartTestTime() {
        return deviceStartTestTime;
    }

    public void setDeviceStartTestTime(long deviceStartTestTime) {
        this.deviceStartTestTime = deviceStartTestTime;
    }

    public long getDeviceEndTestTime() {
        return deviceEndTestTime;
    }

    public void setDeviceEndTestTime(long deviceEndTestTime) {
        this.deviceEndTestTime = deviceEndTestTime;
    }

    public long getRemovalStartTime() {
        return removalStartTime;
    }

    public void setRemovalStartTime(long removalStartTime) {
        this.removalStartTime = removalStartTime;
    }

    public long getRemovalEndTime() {
        return removalEndTime;
    }

    public void setRemovalEndTime(long removalEndTime) {
        this.removalEndTime = removalEndTime;
    }

    public void addSubTest(SubTest subTest) {
        subTests.put(subTest.getName(), subTest);
    }

    public Map<String, SubTest> getSubTests() {
        return subTests;
    }
}
