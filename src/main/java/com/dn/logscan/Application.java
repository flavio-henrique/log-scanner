package com.dn.logscan;

import com.dn.logscan.service.ReportProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Application {

	public static void main(String[] args) throws IOException {
		if (0 < args.length) {
			String filename = args[0];
			File file = new File(filename);
			Stream<String> logLines = Files.lines(file.toPath(), StandardCharsets.UTF_8);

			ReportProcessor reportProcessor = new ReportProcessor();
			ObjectMapper mapper = new ObjectMapper();
			try {
				String json = mapper.writeValueAsString(reportProcessor.processReport(logLines));
				System.out.println(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
}
