package sping.boot.springbatch.pay.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sping.boot.springbatch.pay.domain.PaySample;

import javax.persistence.EntityManagerFactory;

import static sping.boot.springbatch.pay.job.PaySampleFailJobConfiguration.JOB_NAME;

@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class PaySampleFailJobConfiguration {

    public static final String JOB_NAME = "paySampleFailJob";

    private final EntityManagerFactory entityManagerFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    private final int chuckSize = 10;


    @Bean
    public Job paySamplePagingJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
                .start(paySamplePagingStep())
                .build();
    }

    @Bean
    public Step paySamplePagingStep() throws Exception {
        return stepBuilderFactory.get("paySamplePagingStep")
                .<PaySample, PaySample>chunk(chuckSize)
                .reader(paySamplePagingReader())
                .processor(paySamplePagingProcessor())
                .writer(paySamplePagingWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<PaySample> paySamplePagingReader() throws Exception {
        return new JpaPagingItemReaderBuilder<PaySample>()
                .name("paySamplePagingReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chuckSize)
                .queryString("SELECT p FROM PaySample p WHERE p.successStatus = false")
                .build();
    }

    public ItemProcessor<PaySample, PaySample> paySamplePagingProcessor() {
        return item -> {
            item.success();
            return item;
        };
    }

    private JpaItemWriter<PaySample> paySamplePagingWriter() {
        JpaItemWriter<PaySample> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
