package com.singlife.filegateway.controller;

import com.singlife.filegateway.gateways.FileUploadGateway;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("/api/encrypt")
public class EncryptController {

    @Autowired
    private FileUploadGateway gateway;


    @GetMapping
    public String doEncypt() throws IOException, PGPException, NoSuchProviderException {

        String resultcontent = String.format("Hello... We have uploaded... %s \nwith some extra stuff: %s", "personal",
                " ssn");
        File file = new File("mytmpfile.txt");
        if (file.exists())
            file.delete();

        FileUtils.writeStringToFile(file, resultcontent, "UTF-8");
        gateway.uploadFile(file);

        return "200";
    }
}
