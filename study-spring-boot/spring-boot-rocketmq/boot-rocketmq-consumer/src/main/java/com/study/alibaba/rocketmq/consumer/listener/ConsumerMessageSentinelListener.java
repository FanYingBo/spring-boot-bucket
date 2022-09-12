package com.study.alibaba.rocketmq.consumer.listener;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RocketMQMessageListener(consumerGroup = "group_consumer", topic = "localTest",selectorExpression="*")
public class ConsumerMessageSentinelListener implements RocketMQListener<String> {

    private static Logger logger = LoggerFactory.getLogger(ConsumerMessageSentinelListener.class);

    private AtomicInteger count = new AtomicInteger(0);
    private static final String RESOURCE_TOPIC = "localTest";
    @PostConstruct
    private void initConfig(){
        FlowRule rule = new FlowRule();
        rule.setResource(RESOURCE_TOPIC); // 对应的 key 为 `groupName:topicName`
        rule.setCount(5);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        // 匀速器模式下，设置了 QPS 为 5，则请求每 200 ms 允许通过 1 个
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        // 如果更多的请求到达，这些请求会被置于虚拟的等待队列中。等待队列有一个 max timeout，如果请求预计的等待时间超过这个时间会直接被 block
        // 在这里，timeout 为 5s
        rule.setMaxQueueingTimeMs(5 * 1000);
        FlowRuleManager.loadRules(Collections.singletonList(rule));
    }
    @Override
    public void onMessage(String message) {

        ContextUtil.enter(RESOURCE_TOPIC);
        Entry entry = null;
        try {
            entry = SphU.entry(RESOURCE_TOPIC, EntryType.OUT);
            int co = count.addAndGet(1);
            logger.info("receive sentinel {} message: {} ", co, message);
        } catch (BlockException e) {
            e.printStackTrace();
            // Blocked.
            // NOTE: 在处理请求被拒绝的时候，需要根据需求决定是否需要重新消费消息
            logger.warn("receive sentinel message: {} was gone",  message);
        }finally {
            if(entry != null){
                entry.exit();
            }

        }
    }
}
