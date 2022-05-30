package sping.boot.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sping.boot.springbatch.tasklet.SimpleJobTasklet;

@Slf4j
@RequiredArgsConstructor
@Configuration  // Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용함
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob() {
        // simpleJob이라는 이름의 Batch Job 생성
        // job의 이름은 빌더를 통해 지정
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .next(simpleStep3())
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestData]}") String requestData) {
        // simpleStep1이라는 이름의 Batch Step 생성
        // step의 이름은 빌더를 통해 지정
        // tasklet - Step안에서 수행될 기능들을 명시, Tasklet은 Step안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용
        // 1개의 Step = Tasklet 1개 + Reader & Processor & Writer
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    // 1. 에러 발생 (예외 처리) -> 2. 정상 실행 : 같은 JOB_INSTANCE_ID로 JOB_EXECUTION_ID가 발번 됨, 성공한 JOB_INSTANCE_ID가 있으면 에러 발생
                    // throw new IllegalArgumentException("step1에서 실패합니다.");
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestData = {}", requestData);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestData]}") String requestData) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestData = {}", requestData);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private final SimpleJobTasklet tasklet3;

    // @Bean
    // @JobScope
    public Step simpleStep3() {
        log.info(">>>>> definition simpleStep3");
        return stepBuilderFactory.get("simpleStep3")
                .tasklet(tasklet3)
                .build();
    }
}
