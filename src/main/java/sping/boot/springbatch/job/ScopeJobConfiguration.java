package sping.boot.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScopeJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scopeJob() {
        return jobBuilderFactory.get("scopeJob")
                .start(scopeStep1(null))
                .next(scopeStep2())
                .build();
    }

    @Bean
    // @JopScope는 Step의 선언문에서 사용 가능
    // Step의 실행시점에 해당 컴포넌트를 Spring Bean으로 생성
    // 장점 1) JobParameter의 Late Binding이 가능
    // 장점 2) 동일한 컴포넌트를 병렬 혹은 동시에 사용할때 유용
    @JobScope
    public Step scopeStep1(@Value("#{jobParameters[requestData]}") String requestData) {
        return stepBuilderFactory.get("scopeStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is scopeStep1");
                    log.info(">>>>> requestData = {}", requestData);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step scopeStep2() {
        return stepBuilderFactory.get("scopeStep2")
                .tasklet(scopeStep2Tasklet(null))
                .build();
    }

    @Bean
    @StepScope  // @StepScope는 Tasklet, ItemReader, ItemWriter, ItemProcessor에서 사용 가능
    public Tasklet scopeStep2Tasklet(@Value("#{jobParameters[requestData]}") String requestData) {
        return (contribution, chunkContext) -> {
            log.info(">>>>> This is scopeStep2");
            log.info(">>>>> requestData = {}", requestData);
            return RepeatStatus.FINISHED;
        };
    }
}
