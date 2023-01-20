package com.singlife.filegateway.jobs.steps;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import java.io.File;


public class FileProcessor implements ItemProcessor<String,String> {

    @Override
    public String process(String input) throws Exception {

        if(StringUtils.hasText(input) && input.contains("Hello")){
            return input.replace("Hello","Hi");
        }else{
            return "Invalid input";
        }

    }
}
