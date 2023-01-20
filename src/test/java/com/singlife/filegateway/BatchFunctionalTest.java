package com.singlife.filegateway;

//import com.singlife.filegateway.config.BatchConfig;
import com.singlife.filegateway.config.BatchConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
//@ContextConfiguration(classes = {BatchConfig.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BatchFunctionalTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BatchConfig batchConfig;


    @Before
    public void start(){
        //jobBuilderFactory = new JobBuilderFactory(new SimpleJobRepository());
        //stepBuilderFactory
        //jobLauncherTestUtils.
    }

    @Test
    public void testReadFileFlow() throws Exception {

        //batchConfig = new BatchConfig(jobBuilderFactory,stepBuilderFactory);

        ItemReader<String> itemReader = batchConfig.itemReader("");
        ItemWriter writer = batchConfig.writer("output.txt");
        Step step = batchConfig.
                readFileStep(itemReader,writer);

        JobParametersBuilder jobParametersBuilder =
                new JobParametersBuilder();

        jobParametersBuilder.addString("file_path",
                "input.txt");
        jobParametersBuilder.addString("file_name","input");

        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();

        JobExecution results = jobLauncherTestUtils.getJobLauncher()
                .run(batchConfig.fileValidationJob(step),jobParameters);

        Assert.assertNotNull(results);
        Assert.assertEquals(BatchStatus.COMPLETED, results.getStatus());
    }

    @Test
    public void runTest() throws Exception {
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();
        JobExecution je1 = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(BatchStatus.FAILED, je1.getStatus());
        //AssertFile.assertLineCount(10, outputResource);
        JobExecution je2 = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(BatchStatus.COMPLETED, je2.getStatus());
        //AssertFile.assertLineCount(20, outputResource);
    }
}
