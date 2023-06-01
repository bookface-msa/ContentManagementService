//package com.bookface.postsservice.firebase;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Bucket;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.cloud.StorageClient;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.event.EventListener;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//@Slf4j
//
//public class FirebaseService implements FirebaseInterface {
//
//    @Lazy
//    @Autowired
//    Properties properties;
//
//    @EventListener
//    public void init(ApplicationReadyEvent event) {
//
//        // initialize Firebase
//
//        try {
//
//            if (FirebaseApp.getApps().size() == 0 ) {
//                ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
//
//                FirebaseOptions options = new FirebaseOptions.Builder()
//                        .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
//                        .setStorageBucket(properties.getBucketName())
//                        .build();
//
//                FirebaseApp.initializeApp(options);
//            }
//
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//
//        }
//    }
//
//    @Override
//    public String getImageUrl(String name) {
//        return String.format(properties.getImageUrl(), name);
//    }
//
//    @Override
//    public String save(MultipartFile file) throws IOException {
//
//        try {
//            Bucket bucket = StorageClient.getInstance().bucket();
//
//            String name = generateFileName(file.getOriginalFilename());
//
//            bucket.create(name, file.getBytes(), file.getContentType());
//            return name;
//        } catch (IOException e) {
//            log.info("Error uploading image", file);
//        }
//
//        return "";
//    }
//
//
//    @Override
//    public void delete(String name) throws IOException {
//
//        Bucket bucket = StorageClient.getInstance().bucket();
//
//        if (StringUtils.isEmpty(name)) {
//            throw new IOException("invalid file name");
//        }
//
//        Blob blob = bucket.get(name);
//
//        if (blob == null) {
//            throw new IOException("file not found");
//        }
//
//        blob.delete();
//    }
//
//    public void download(String fileName) throws IOException {
//        String destFileName = UUID.randomUUID().toString().concat("." + this.getExtension(fileName));
//        String destFilePath = "D:\\FirebaseDownloadedImages\\" + destFileName;
//
//        Bucket bucket = StorageClient.getInstance().bucket();
//        Blob blob = bucket.get(fileName);
//        blob.downloadTo(Paths.get(destFilePath));
//    }
//
//    @Data
//    @Configuration
//    @ConfigurationProperties(prefix = "firebase")
//    public class Properties {
//
//        private String bucketName;
//
//        private String imageUrl;
//
//        public String getBucketName() {
//            return bucketName;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//
//    }
//
//}