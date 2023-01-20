package com.singlife.filegateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class PgpTransformer {


//    @Bean
//    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "30000"))
//    public MessageSource<File> fileReadingMessageSource() {
//        FileReadingMessageSource source = new FileReadingMessageSource();
//        source.setDirectory(new File(ftpUploadDir));
//        source.setFilter(new SimplePatternFileListFilter("*.pgp"));
//        source.setScanEachPoll(true);
//        source.setUseWatchService(true);
//        return source;
//    }
//
//    @Value("${pgp.archive.dir}")
//    private String archiveDir;
//
//    @Autowired
//    private PGPFileProcessor pgpFileProcessor;
//
//    @Transformer(inputChannel = "fileInputChannel", outputChannel = "fileToJobProcessor")
//    public File transform(File aFile) throws Exception {
//
//        if (!pgpFileProcessor.decrypt(aFile)) {
//            throw new Exception("Failed to decrypted input file.");
//        }
//
//        //Move old file to archive directory.
//        if (aFile.renameTo(new File(archiveDir + "/" + aFile.getName()))) {
//            log.info(String.format("%s file archived to %s", aFile.getName(), aFile.getAbsolutePath()));
//        }
//        return pgpFileProcessor.getDecryptFile();
//    }
}
