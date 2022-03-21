
## 秒杀系统

### 秒杀之MySQL实现

![png 秒杀之MySQL实现](./seckill-mysql.png)

优点：
1. 实现简单，依赖MySQL事务控制，也不会超卖。

缺点：
1. 流量(有效流量，无效流量)直接打到MySQL,MySQL压力大，扛不住就容易奔溃，并且容易雪崩。


### 秒杀之Redis缓存


### 秒杀之Thread版本