package com.reactit.Skillsapply.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {

    public void init();

    public void save(MultipartFile file, String uploadDir, String filename);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

}
