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

    /**
     * 秒杀-mysql版本。
     * 流量直接打到数据库，压力都在数据库上。
     *
     * 优化点：引入redis，让redis先过滤掉一些无效流量。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByMySQL(String goodsNum) {
        goodsRepository.decrStoreByMySQL(goodsNum);
        return true;
    }

    /**
     * 秒杀-redis+mysql版本。
     * 先让redis扣减商品数量，返回值>=0表示有效流量，但此时redis也会遭受太多无效流量（redis中的库存已经变为负数）。
     *
     * 优化点：扣减为负数之后，将redis中key的结果改完字符串，通过异常捕获无效库存，减少redis扣减库存带来的损耗。
     * @param goodsNum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByRedisV1(String goodsNum) {
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
    public boolean seckillByRedisV2(String goodsNum) {
        // 先将流量打到redis上。
        try {
            ValueOperations<String, String> strOps = stringRedisTemplate.opsForValue();
            Long decrement = strOps.decrement(goodsNum);
            if (decrement >= 0) {
                // redis扣减成功了，再扣减数据库。
                goodsRepository.decrStoreByRedis(goodsNum);
                return true;
            } else {
                strOps.set(goodsNum, "none");
            }
        }catch (Exception e){
            // ignore
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        // 先清理一下
        goodsRepository.deleteAll();;

        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();

        List<GoodsEntity> goodsList = new ArrayList<>(1000);
        for (int i = 1000; i <= 1200; i++){

            // redis初始化
            stringValueOperations.set(String.valueOf(i), store.toString());

            // 数据库初始化
            GoodsEntity goods = new GoodsEntity();
            goods.setGoodsNum(String.valueOf(i));
            goods.setName(goodsName);
            goods.setStore(store);
            goodsList.add(goods);
        }
        goodsRepository.saveAll(goodsList);
    }
}
