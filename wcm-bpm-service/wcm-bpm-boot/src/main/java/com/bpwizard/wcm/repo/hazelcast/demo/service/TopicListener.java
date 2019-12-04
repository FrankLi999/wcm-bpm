package com.bpwizard.wcm.repo.hazelcast.demo.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.hazelcast.demo.controller.HazelcastJetDemoController;
import com.bpwizard.wcm.repo.hazelcast.demo.source.CustomSourceP;
import com.hazelcast.core.HazelcastJsonValue;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;

@Component
public class TopicListener implements MessageListener<HazelcastJsonValue> {
    @Autowired
    JetInstance instance;
    
	private Pipeline topicPipeline;
	@PostConstruct
    public void setupPipeline() {
    	topicPipeline = Pipeline.create();
    	topicPipeline.drawFrom(CustomSourceP.customSource())
                .drainTo(Sinks.logger());
    }
	
	@Override
	public void onMessage(Message<HazelcastJsonValue> message) {
		
        HazelcastJsonValue payload = message.getMessageObject();
        System.out.println(">>>>>>>>>>>>>>>>>>>>> got topic :" + payload.toString());
		JobConfig jobConfig = new JobConfig()
				.setProcessingGuarantee(ProcessingGuarantee.EXACTLY_ONCE)
                .addClass(HazelcastJetDemoController.class)
                .addClass(CustomSourceP.class);
        instance.newJob(this.topicPipeline, jobConfig).join();
	}

}
