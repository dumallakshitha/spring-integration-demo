package com.singlife.filegateway;

import com.singlife.filegateway.jobs.steps.FileProcessor;
import org.junit.Assert;
import org.junit.Test;

public class FileProcessorTest {

    @Test
    public void testFileLineItemProcessor() throws Exception {

        FileProcessor fileProcessor = new FileProcessor();
        Assert.assertEquals("Invalid input",fileProcessor.process("Welcome Dumal"));
        Assert.assertEquals("Hi Dumal",fileProcessor.process("Hello Dumal"));
    }
}
