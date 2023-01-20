package com.singlife.filegateway.gateways;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.File;

@MessagingGateway
public interface FileUploadGateway {

    @Gateway(requestChannel = "uploadFile")
    public void uploadFile(File file);

}
