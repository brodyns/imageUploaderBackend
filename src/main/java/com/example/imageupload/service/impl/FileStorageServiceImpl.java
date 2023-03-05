package com.example.imageupload.service.impl;

import com.example.imageupload.mapper.ImageMapper;
import com.example.imageupload.model.ImageInfo;
import com.example.imageupload.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * FileName: FileStorageServiceImpl
 * author: gxs
 * Date: 2023/2/17  14:30
 */
@Service
@AllArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private ImageMapper imageMapper;

    private final Path root = Paths.get("uploads");
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    private File convertMultiPartToFile(MultipartFile file ) throws IOException {
        File convFile = new File(root+ "\\" + file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
    @Override
    public String save(MultipartFile file, String label) {
        File newfile = null;
        String imageURL;
        try {
            newfile = convertMultiPartToFile(file);
            imageURL = "localhost:8080\\" + newfile;
            imageMapper.addImage(imageURL, "this is a label");
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return imageURL;
    }

    @Override
    public Resource load(String filename) {
        try{
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filename) {
        return false;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public List<ImageInfo> getAllImages() {
        List imageInfo = imageMapper.getAllImages();
        for(int i=0;i<imageInfo.size();i++){
            String newUrl = ((ImageInfo)imageInfo.get(i)).getUrl().replace("localhost",getCurrentIp().toString());
            System.out.println(newUrl);
            ((ImageInfo)imageInfo.get(i)).setUrl(newUrl);
        }
        return imageMapper.getAllImages();
    }

    public static InetAddress getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                        return ia;
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
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
