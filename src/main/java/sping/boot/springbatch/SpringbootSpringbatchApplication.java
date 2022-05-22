package sping.boot.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing  // 배치 기능 활성화
@SpringBootApplication
public class SpringbootSpringbatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSpringbatchApplication.class, args);
    }

}
