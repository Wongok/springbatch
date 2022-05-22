package sping.boot.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        // simpleStep1이라는 이름의 Batch Step 생성
        // step의 이름은 빌더를 통해 지정
        // tasklet - Step안에서 수행될 기능들을 명시, Tasklet은 Step안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용
        // 1개의 Step = Tasklet 1개 + Reader & Processor & Writer
        return stepBuilderFactory.get("simpleStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;})
                ).build();
    }
}
