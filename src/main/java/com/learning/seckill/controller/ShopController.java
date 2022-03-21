package com.learning.seckill.controller;

import com.learning.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lifang
 * @since 2022/3/21
 */
@RestController
public class ShopController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping(value = "/mysql/shop")
    public ResponseEntity shop(@RequestParam String goodsNum){
        goodsService.decrGoodsStore(goodsNum);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/init/goods")
    public ResponseEntity init(){
        goodsService.init();
        return new ResponseEntity(HttpStatus.OK);
    }

}
