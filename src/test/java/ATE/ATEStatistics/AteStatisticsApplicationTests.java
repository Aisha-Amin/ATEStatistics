package ATE.ATEStatistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class AteStatisticsApplicationTests {

	@Test
	public void testFileNotEmpty() throws IOException {
		// Replace "path_to_your_file" with the actual path to the file you want to test
		Resource resource = new ClassPathResource("log.txt");
		File file = resource.getFile();

		// Check if the file is not empty
		assertFalse(Files.size(file.toPath()) == 0, "The file is empty");
	}

	@Test
	public void testLogParsing() {
		// Sample log content for testing
		String logContent = "#PE[14:34:31.300] : BEGIN INSERTION \"device #1\"\n"
				+ "#PE[14:34:31.400] : END INSERTION\n"
				+ "#PE[14:34:31.500] : BEGIN DEVICE_TEST\n"
				+ "#PE[14:34:32.300] : BEGIN DEVICE_TEST.SUBTEST \"subtest #1\"\n"
				+ "#PE[14:34:32.600] : END DEVICE_TEST.SUBTEST\n"
				+ "#PE[14:34:33.000] : END DEVICE_TEST\n"
				+ "#PE[14:34:35.200] : BEGIN REMOVAL\n"
				+ "#PE[14:34:35.500] : END REMOVAL\n";

		// Create a LogAnalyzer instance
		LogAnalyzer logAnalyzer = new LogAnalyzer();
		// Parse the log content
		List<Device> devices = logAnalyzer.parseLogContent(logContent);

		// Verify parsing results
		assertEquals(1, devices.size());

		Device device = devices.get(0);
		assertEquals("device #1", device.getName());
	}

	@Test
	@DisplayName("Test Calculate Duration")
	public void testStatisticsCalculation() {
		Device device = new Device("TestDevice");
		device.setInsertionStartTime(0);
		device.setInsertionEndTime(1000);
		device.setDeviceStartTestTime(2000);
		device.setDeviceEndTestTime(3000);
		device.setRemovalStartTime(4000);
		device.setRemovalEndTime(5000);

		TestAnalyzer testAnalyzer = new TestAnalyzer();
		assertEquals(5000, testAnalyzer.calculateDuration(device.getInsertionStartTime(), device.getRemovalEndTime()));
		assertEquals(1000, testAnalyzer.calculateDuration(device.getInsertionStartTime(), device.getInsertionEndTime()));
		assertEquals(1000, testAnalyzer.calculateDuration(device.getDeviceStartTestTime(), device.getDeviceEndTestTime()));
		assertEquals(1000, testAnalyzer.calculateDuration(device.getRemovalStartTime(), device.getRemovalEndTime()));
	}

}
