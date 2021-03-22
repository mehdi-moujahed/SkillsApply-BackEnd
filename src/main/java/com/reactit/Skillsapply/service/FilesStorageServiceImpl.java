package com.reactit.Skillsapply.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService{

    public final Path root = Paths.get("E:\\boudj\\Documents\\Test");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

//    @Override
//    public void save(MultipartFile file) {
//        try {
//            String uploadDir = "E:\\boudj\\Documents\\uploads\\";
//            String filename = "Avatar";
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            Path saveTO = Paths.get(uploadDir + filename + "." + extension);
////            Files.createDirectory(root);
//            byte[] bytes = file.getBytes();
//            Files.write(saveTO, bytes);
////            Files.copy(file.getInputStream(), saveTO);
//        } catch (Exception e) {
//            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//        }
//    }

    @Override
    public void save(MultipartFile file, String uploadDir, String filename) {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            Path saveTO = Paths.get(uploadDir + filename + "." + extension);
            byte[] bytes = file.getBytes();
            Files.write(saveTO, bytes);
    //            Files.copy(file.getInputStream(), saveTO);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
