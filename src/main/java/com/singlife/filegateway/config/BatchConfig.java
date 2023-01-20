package com.singlife.filegateway.config;

import com.singlife.filegateway.jobs.steps.FileProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Configuration
@EnableBatchProcessing
public class BatchConfig {


    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job fileValidationJob(final Step readFileStep) {
        return jobBuilderFactory.get("fileValidationJob") //
                .incrementer(new RunIdIncrementer()) //
                .start(readFileStep) //
                .build();
    }

    @Lazy
    @Bean
    public Step readFileStep(final ItemReader<String> itemReader, final ItemWriter writer) {
        return stepBuilderFactory.get("readFileStep")
                .<String,String>chunk(1)
                .reader(itemReader)
                .processor(new FileProcessor())
                .writer(writer) //
                .build();
    }


    @Lazy
    @Bean
    @StepScope
    public FlatFileItemReader<String> itemReader(@Value("#{jobParameters[file_path]}") String filePath) {
        FlatFileItemReader<String> reader = new FlatFileItemReader();
        final FileSystemResource fileResource = new FileSystemResource(filePath);
        reader.setResource(fileResource);
        reader.setLineMapper(new PassThroughLineMapper());
        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter writer(@Value("#{jobParameters[file_name]}") String fileName){

        return  new FlatFileItemWriterBuilder<String>()
                .name("itemWriter")
                .resource(new FileSystemResource("outputfolder/"+fileName))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

}
