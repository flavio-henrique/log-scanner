package com.dn.logscan.service;

import lombok.extern.log4j.Log4j2;

import java.io.File;
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

  public Stream<String> getFile(String fileName) throws IOException {
    File file = new File(fileName);
    return Files.lines(file.toPath(), StandardCharsets.UTF_8);
  }

}
