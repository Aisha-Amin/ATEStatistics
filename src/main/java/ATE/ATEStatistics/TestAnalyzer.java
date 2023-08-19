package ATE.ATEStatistics;

import java.util.Map;

/**
 * The TestAnalyzer class handles the calculation and print statistics for devices and their subtests.
 */
public class TestAnalyzer {

    private static final long SLOW_SUBTEST_THRESHOLD = 600;
    /**
     * Prints overall statistics for a given device, including insertion, testing, and removal durations.
     * Also calls the deviceDetailedStatistics method.
     *
     * @param device The Device object for which statistics are calculated and printed.
     */
    public void deviceOverallStatistics(Device device) {
        System.out.println("Device : " + device.getName());
        System.out.println("  Total duration(Begin Insertion -> End Removal) : " + calculateDuration(device.getInsertionStartTime(),device.getRemovalEndTime()) + " ms");
        System.out.println("  Insertion Duration: " + calculateDuration(device.getInsertionStartTime(),device.getInsertionEndTime()) + " ms");
        System.out.println("  Device Test duration: " + calculateDuration(device.getDeviceStartTestTime(),device.getDeviceEndTestTime()) + " ms");
        System.out.println("  Removal duration: " + calculateDuration(device.getRemovalStartTime(),device.getRemovalEndTime()) + " ms");
        System.out.println("Device Detailed Statistics:");
        deviceDetailedStatistics(device);
        }


    /**
     * Prints detailed statistics for the subtests of a given device.
     *
     * @param device The Device object for which detailed subtest statistics are calculated and printed.
     */
    public void deviceDetailedStatistics(Device device) {
        System.out.println("  Total Device SubTests : " + device.getSubTests().size());
        if(!device.getSubTests().isEmpty()){

            // Parallelize the calculation of subtest durations using Java Stream API
            device.getSubTests().values().parallelStream().forEach(subTest -> {
                System.out.println("    Device SubTest Name: " + subTest.getName());
                long duration = calculateDuration(subTest.getStartTime(), subTest.getEndTime());
                System.out.println("    Device SubTest duration: " + duration + " ms");
                System.out.println("    ---------------");
            });

        }
    }

    /**
     * Calculates the duration between two timestamps.
     *
     * @param startTime The starting timestamp.
     * @param endTime   The ending timestamp.
     * @return The duration in milliseconds.
     */
    public long calculateDuration(long startTime, long endTime) {
        return endTime - startTime;
    }

    // Method to detect potential root causes and log diagnostic information
    public void detectAndLogRootCauses(Device device) {
        Map<String, SubTest> subTests = device.getSubTests();

        // Parallelize the detection and logging using parallelStream
        subTests.values().parallelStream().forEach(subTest -> {
            long duration = calculateDuration(subTest.getStartTime(), subTest.getEndTime());

            // Detect slow subtests
            if (duration >= SLOW_SUBTEST_THRESHOLD) {
                System.out.println("Potential Slow Subtest Detected:");
                System.out.println("  Device: " + device.getName());
                System.out.println("  SubTest: " + subTest.getName());
                System.out.println("  Duration: " + duration + " ms");
                System.out.println("-------------------");
            }
        });
    }

}
