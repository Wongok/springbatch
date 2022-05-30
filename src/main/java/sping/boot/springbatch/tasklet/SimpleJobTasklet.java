package sping.boot.springbatch.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component  // @Component와 @StepScope로 Bean생성
@StepScope  // JobParameters를 사용하기 위해선 꼭 @StepScope, @JobScope로 Bean을 생성
public class SimpleJobTasklet implements Tasklet {

    @Value("#{jobParameters[requestData]}")
    private String requestData;

    public SimpleJobTasklet() {
        log.info(">>>>> tasklet 생성");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info(">>>>> This is Step3");
        log.info(">>>>> requestData = {}", requestData);

        return RepeatStatus.FINISHED;
    }
}
