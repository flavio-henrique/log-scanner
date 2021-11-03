package com.dn.logscan;



import com.dn.logscan.service.FileImporter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class ImportFileTest {

  @Test
  public void givenFilePath_whenUsingFilesLines_thenFileData() throws IOException, URISyntaxException {
    // Given
    FileImporter fileImporter = new FileImporter();

    // When
    Stream<String> result = fileImporter.getFileFromResource("server.log");

    // Then
    Assert.assertNotNull(result);
  }

}
