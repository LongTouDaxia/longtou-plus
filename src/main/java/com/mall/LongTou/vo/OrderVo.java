package com.mall.LongTou.vo;

import com.mall.LongTou.entity.Orders;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: wdd
 * @Date: 2020-03-27 16:29
 * @Description:
 */
@Data
public class OrderVo extends Orders {

        private String productName;

        private String productPicture;

        private LocalDateTime orderTime;

}
