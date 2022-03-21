package com.learning.seckill.service;

/**
 * @author lifang
 * @since 2022/3/21
 */
public interface GoodsService {

    boolean seckillByMySQL(String goodsNum);


    boolean seckillByRedis(String goodsNum);

    void init();
}
