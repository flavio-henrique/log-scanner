package com.dn.logscan.service;



import com.dn.logscan.service.FileImporter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ImportFileTest {

  @Test
  public void givenFileNameFromResource_whenUsingGetFile_thenStreamString() throws IOException, URISyntaxException {
    // Given
    FileImporter fileImporter = new FileImporter();

    // When
    Stream<String> result = fileImporter.getFileFromResource("server.log");

    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void givenFileName_whenUsingFilesLines_thenIOException() {
    // Given
    FileImporter fileImporter = new FileImporter();

    // When
    IOException exception = assertThrows(IOException.class,
                                         () -> fileImporter.getFile("fileNotFound"));

    // Then
    assertNotNull(exception);
  }

  @Test
  public void givenFileName_whenUsingFilesLines_thenFileData() throws URISyntaxException, IOException {
    // Given
    FileImporter fileImporter = new FileImporter();
    URL url = getClass().getClassLoader().getResource("server.log");

    // When
    Stream<String> lines = fileImporter.getFile(Objects.requireNonNull(url).getPath());

    // Then
    assertTrue(lines.count() > 0);
  }

}
