package com.dn.logscan;

import com.dn.logscan.model.report.Report;
import com.dn.logscan.service.FileImporter;
import com.dn.logscan.service.ReportProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.stream.Stream;

public class Application {

	public static void main(String[] args) throws IOException {
		if (0 < args.length) {
			FileImporter fileImporter = new FileImporter();
			Stream<String> logLines = fileImporter.getFile(args[0]);

			ReportProcessor reportProcessor = new ReportProcessor();
			Report report = reportProcessor.processReport(logLines);

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(report);
			System.out.println(json);
		}
	}
}
