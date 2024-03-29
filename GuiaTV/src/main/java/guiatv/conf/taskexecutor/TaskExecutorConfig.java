package guiatv.conf.taskexecutor;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskExecutorConfig {
	
	@Bean(name="rtmpSpyingTaskExecutor")
	public Executor getRtmpSpyingTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(15);
		taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;
	}
	
	@Bean(name="classificationTaskExecutor")
	public Executor getClassificationTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(15);
		taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;
	}
	
	@Bean(name="unclassifiedBlobTaskExecutor")
	public Executor getUnclassifiedBlobTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(15);
		taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;
	}
	
	@Bean(name="realtimeBrainTaskExecutor")
	public Executor getRealTimeBrainTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(15);
		taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;
	}

//	@Override
//	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//		return new SimpleAsyncUncaughtExceptionHandler();
//	}
	

}
