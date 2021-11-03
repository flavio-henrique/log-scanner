package com.dn.logscan.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class FileImporter {

  public Stream<String> getFileFromResource(String fileName) throws IOException, URISyntaxException {
    URL url = getClass().getClassLoader().getResource(fileName);
    URI uri = Objects.requireNonNull(url).toURI();
    Path path = Paths.get(uri);
    return Files.lines(path, StandardCharsets.UTF_8);
  }

}
