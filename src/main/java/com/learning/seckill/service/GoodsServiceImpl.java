package com.learning.seckill.service;

import com.learning.seckill.domain.GoodsEntity;
import com.learning.seckill.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lifang
 * @since 2022/3/21
 */
@Service
@Configurable
public class GoodsServiceImpl implements GoodsService{

    @Value("${xx.learning.seckill.goods.name}")
    private String goodsName;

    @Value("${xx.learning.seckill.goods.store}")
    private Integer store;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByMySQL(String goodsNum) {
        goodsRepository.decrStoreByMySQL(goodsNum);
        return true;
    }

    @Override
    public boolean seckillByRedis(String goodsNum) {
        // 先将流量打到redis上。
        ValueOperations<String, String> strOps = stringRedisTemplate.opsForValue();
        Long decrement = strOps.decrement(goodsNum);
        if(decrement >= 0) {
            // redis扣减成功了，再扣减数据库。
            goodsRepository.decrStoreByRedis(goodsNum);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        // 先清理一下
        goodsRepository.deleteAll();;

        List<GoodsEntity> goodsList = new ArrayList<>(1000);
        for (int i = 1000; i <= 1200; i++){
            GoodsEntity goods = new GoodsEntity();
            goods.setGoodsNum(String.valueOf(i));
            goods.setName(goodsName);
            goods.setStore(store);
            goodsList.add(goods);
        }
        goodsRepository.saveAll(goodsList);
    }
}
