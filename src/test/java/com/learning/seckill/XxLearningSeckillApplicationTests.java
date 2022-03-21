package com.learning.seckill;

import com.learning.seckill.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
}
