package com.learning.seckill.disruptor;


import com.lmax.disruptor.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 消费者
 */
public class GoodsConsumer implements EventHandler<GoodsEvent> {

    private static final Logger logger = LogManager.getLogger(GoodsConsumer.class);

    @Override
    public void onEvent(GoodsEvent event, long sequence, boolean endOfBatch) throws Exception {

        logger.info("sequence:{}, goodsNum:{}", sequence, event.getGoodsNum());

    }
}
