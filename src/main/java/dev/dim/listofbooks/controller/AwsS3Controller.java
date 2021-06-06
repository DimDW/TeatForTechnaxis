package dev.dim.listofbooks.controller;


import dev.dim.listofbooks.service.AwsS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/storage/")
class BucketController {

    private AwsS3Client awsS3Client;

    @Autowired
    BucketController(AwsS3Client awsS3Client) {
        this.awsS3Client = awsS3Client;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.awsS3Client.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.awsS3Client.deleteFileFromS3Bucket(fileUrl);
    }
}