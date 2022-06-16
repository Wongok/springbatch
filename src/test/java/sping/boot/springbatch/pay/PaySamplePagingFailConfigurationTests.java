package sping.boot.springbatch.pay;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sping.boot.springbatch.pay.domain.PaySample;
import sping.boot.springbatch.pay.domain.PaySampleRepository;
import sping.boot.springbatch.pay.job.PaySampleFailJobConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaySampleFailJobConfiguration.class, TestJobConfiguration.class})
@TestPropertySource(properties = {"job.name=" + PaySampleFailJobConfiguration.JOB_NAME})
public class PaySamplePagingFailConfigurationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PaySampleRepository paySampleRepository;

    @Test
    public void deadlockTest() throws Exception {
        // given
        for (long i = 0; i < 50; i++) {
            paySampleRepository.save(new PaySample(i, false));
        }

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        Assert.assertEquals(jobExecution.getStatus(), CoreMatchers.is(BatchStatus.COMPLETED));
        Assert.assertEquals(paySampleRepository.findAll().size(), CoreMatchers.is(50));
    }

}
