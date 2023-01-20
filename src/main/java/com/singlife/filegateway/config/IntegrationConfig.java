package com.singlife.filegateway.config;

import com.singlife.filegateway.jobs.FileMessageToJobRequest;
import com.singlife.filegateway.gateways.FileUploadGateway;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;

@Configuration
public class IntegrationConfig {

    @Autowired
    private Job fileValidationJob;

    @Autowired
    private JobLauncher jobLauncher;


    @Autowired
    private FileUploadGateway gateway;

    protected DirectChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow readFlow(){

        return IntegrationFlows
                .from(fileReadingMessageSource("dropfolder"), c -> c.poller(Pollers.fixedDelay(2000)))
                .filter(onlyTxt())
                .channel(inputChannel())
                .transform(fileMessageToJobRequest())
                .handle(jobLaunchingMessageHandler())
                .handle(jobExecution -> {
                    System.out.println("+++++++++++ Logging " + jobExecution.getPayload());
                })//
                //.channel("uploadFile")
                .get();
    }

    @Bean
    public IntegrationFlow encryptionFlow(){

        return IntegrationFlows.from(plainFileReadingMessageSource("outputfolder"),c -> c.poller(Pollers.fixedDelay(2000)))
                .filter(onlyTxt())
                .channel(inputChannel())
                .handle(m -> encryptFile((Message<File>) m))
                .get();
    }

    @Bean
    public IntegrationFlow uploadFlow(){

        return IntegrationFlows.from(encryptFileReadingMessageSource("processed-folder"),c -> c.poller(Pollers.fixedDelay(2000)))
                .channel(inputChannel())
                //.handle(m -> encryptFile((Message<File>) m))
                .channel("uploadFile")
                .get();
    }

    @Bean
    public GenericSelector<File> onlyJpgs(){

        return new GenericSelector<File>() {
            @Override
            public boolean accept(File source) {
                return source.getName().endsWith(".jpg");
            }
        };
    }

    @Bean
    public GenericSelector<File> onlyTxt(){

        return new GenericSelector<File>() {
            @Override
            public boolean accept(File source) {
                return source.getName().endsWith(".txt");
            }
        };
    }

    @Bean
    public MessageHandler targetDirectory() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("outputfolder"));
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        return handler;
    }

    //@Bean
    public MessageSource<File> fileReadingMessageSource(String readFolder){
        return abstractfileReadingMessageSource(readFolder);
    }

    //@Bean
    public MessageSource<File> plainFileReadingMessageSource(String readFolder){
        return abstractfileReadingMessageSource(readFolder);
    }

    //@Bean
    public MessageSource<File> encryptFileReadingMessageSource(String readFolder){
        return abstractfileReadingMessageSource(readFolder);
    }

    private MessageSource<File> abstractfileReadingMessageSource(String readFolder){
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(readFolder));
        //messageSource.setFilter(new SimplePatternFileListFilter(".csv"));
        messageSource.setUseWatchService(true);
        messageSource.setWatchEvents(FileReadingMessageSource.WatchEventType.CREATE);
        return messageSource;
    }

    @Bean
    FileMessageToJobRequest fileMessageToJobRequest() {
        FileMessageToJobRequest transformer = new FileMessageToJobRequest();
        transformer.setJob(fileValidationJob);
        transformer.setFileParameterName("file_path");
        return transformer;
    }

    @Bean
    JobLaunchingMessageHandler jobLaunchingMessageHandler() {
        JobLaunchingMessageHandler handler = new JobLaunchingMessageHandler(jobLauncher);
        return handler;
    }

    public void encryptFile(Message<File> message){

        String originalFileName = message.getPayload().getAbsolutePath();

    }

}
