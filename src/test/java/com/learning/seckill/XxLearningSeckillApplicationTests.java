package com.learning.seckill;

import com.learning.seckill.disruptor.GoodsConsumer;
import com.learning.seckill.disruptor.GoodsEvent;
import com.learning.seckill.disruptor.MessageProducer;
import com.learning.seckill.service.GoodsService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Random;

@SpringBootTest
class XxLearningSeckillApplicationTests {

    @Resource
    private GoodsService goodsService;

    @Test
    void contextLoads() {
    }

    @Test
    void init(){
        goodsService.init();
    }

    @Test
    void shop(){
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            goodsService.seckillByMySQL("" + random.nextInt(2000));
        }
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedis(){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set("k1", "1");
    }


    @Test
    void testDisruptor(){
        MessageProducer.publish(new GoodsEvent(), Lists.newArrayList(new GoodsConsumer(), new GoodsConsumer()), (ringBuffer -> {
            for (int i = 1; i < 1000; i++) {

                long seq = ringBuffer.next();

                GoodsEvent goodsEvent = (GoodsEvent) ringBuffer.get(seq);

                goodsEvent.setGoodsNum(i * 1000L);

                ringBuffer.publish(seq);
            }
        }));
    }
}
