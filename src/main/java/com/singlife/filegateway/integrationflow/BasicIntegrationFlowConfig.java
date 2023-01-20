package com.singlife.filegateway.integrationflow;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;

import java.io.File;

public abstract class BasicIntegrationFlowConfig {

    protected DirectChannel inputChannel() {
        return new DirectChannel();
    }

    public abstract void setupIntegrationWorkFlow();

    public MessageSource<File> abstractfileReadingMessageSource(String readFolder){
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(readFolder));
        //messageSource.setFilter(new SimplePatternFileListFilter(".csv"));
        messageSource.setUseWatchService(true);
        messageSource.setWatchEvents(FileReadingMessageSource.WatchEventType.CREATE);
        return messageSource;
    }
}
