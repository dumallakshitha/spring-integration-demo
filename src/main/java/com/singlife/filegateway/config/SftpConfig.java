package com.singlife.filegateway.config;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
public class SftpConfig {

    @Value("${remote.sftp.host}")
    private String host;

    @Value("${remote.sftp.port}")
    private int port;

    @Value("${remote.sftp.username}")
    private String userName;

    @Value("${remote.sftp.password}")
    private String password;
    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(userName);
        factory.setPassword(password);
        factory.setAllowUnknownKeys(true);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "uploadFile")
    public MessageHandler handler() {
        SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
        handler.setRemoteDirectoryExpression(new LiteralExpression("/upload/"));
        handler.setFileNameGenerator(message ->((File)message.getPayload()).getName());
        return handler;
    }


    @Bean
    @ServiceActivator(inputChannel = "encryptChannel")
    public MessageHandler handler2() {
        //System.out.println(message.getPayload());
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/outputfolder/myfile.txt"));
        handler.setFileNameGenerator(m ->((File)m.getPayload()).getName());
        handler.setExpectReply(false);

        return handler;
    }

}
