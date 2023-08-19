package ATE.ATEStatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class AteStatisticsApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AteStatisticsApplication.class, args);

		// Read profiling event data from file
		Resource resource = new ClassPathResource("log.txt");
		File file = resource.getFile();
		String content = new String(Files.readAllBytes(file.toPath()));

		LogAnalyzer logAnalyzer = new LogAnalyzer();
		// Process log data and create devices
		List<Device> devices = logAnalyzer.parseLogContent(content);

		TestAnalyzer testAnalyzer = new TestAnalyzer();

		System.out.println("Device Overall Statistics: Sorted Devices Based on Total Time Consumption in Descending Order");
		List<Device> sortedDevices = devices.stream()
				.sorted(Comparator.comparing(device -> testAnalyzer.calculateDuration(device.getInsertionStartTime(), device.getRemovalEndTime()), Comparator.reverseOrder()))
				.collect(Collectors.toList());

		for(Device device : sortedDevices){
			testAnalyzer.deviceOverallStatistics(device);
		}

		// Detect Slow Subtests
		for(Device device : sortedDevices){
			testAnalyzer.detectAndLogRootCauses(device);
		}

	}

}
