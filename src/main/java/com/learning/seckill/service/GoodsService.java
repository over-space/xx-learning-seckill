package com.learning.seckill.service;

/**
 * @author lifang
 * @since 2022/3/21
 */
public interface GoodsService {

    boolean decrGoodsStore(String goodsNum);

    void init();
}
