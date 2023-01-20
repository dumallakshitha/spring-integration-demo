package com.singlife.filegateway;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchTestConfiguration {

//    @Autowired
//    @Qualifier("fileValidationJob")
//    private Job job;


//    @Autowired
//    public void setJob(Job job) {
//        this.job = job;
//    }

//    @Bean
//    public Job job(){
//        //BatchConfig batchConfig = new BatchConfig();
//        return jobBuilderFactory.get("job").start()
//    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(new HikariDataSource());
        return jobRepositoryFactoryBean.getObject();
    }
    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() throws Exception {

        JobLauncherTestUtils launcherTestUtils = new JobLauncherTestUtils();
        launcherTestUtils.setJobRepository(jobRepository());
        //launcherTestUtils.setJob(job);
        //launcherTestUftils.setJobLauncher(jobLauncher);
        return launcherTestUtils;
    }
}
