package com.learning.seckill.repository;

import com.learning.seckill.domain.GoodsEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lifang
 * @since 2022/3/21
 */
@Repository
public interface GoodsRepository extends CrudRepository<GoodsEntity, Long> {

    @Modifying
    @Query(value = "UPDATE t_goods SET store=store-1 WHERE goods_num=?1 AND store > 0", nativeQuery = true)
    void decrGoodsStore(String goodsNum);

}
