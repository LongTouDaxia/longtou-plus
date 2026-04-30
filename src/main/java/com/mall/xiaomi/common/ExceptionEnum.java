package com.mall.xiaomi.common;

//错误异常枚举类
public enum ExceptionEnum {


    //轮播图
    GET_CAROUSEL_NOT_FOUND( "获取轮播图失败"),

    //用户
    USER_NOT_FOUND( "用户不存在"),
    USER_CANTOT_NULL("用户不能为空"),

    //商品
    PRODUCT_NOT_FOUND( "商品不存在"),
    PRODUCTID_NOT_NULL("商品Id不能为空"),
    CATEGORY_PRODUCT_NULL("还没有该分类的商品哦"),
    PRODUCT_DELETE_EXCEPTION("商品删除异常"),

    //分类
    CATEGORY_NOT_FIND("分类不存在"),

    //用户收藏
    USER_OR_PRODUCTID_IS_NULL("用户名或商品为空"),
    PRODUCT_COLLECTED("商品已收藏"),
    COLLECT_IS_NULL("用户收藏为空"),
    PRODUCT_NOT_COLLECT("商品未收藏，不能删除"),
    SECKILL_PRODUCT_NULL("秒杀商品不存在"),
    SECKILL_NOT_START("秒杀尚未开始"),

    //订单
    ADD_ORDER_ERROR("订单添加异常"),
    USER_ORDER_NULL("用户订单为空"),
    ORDER_CREATE_EXCEPTION("订单创建异常"),
    USER_ORDER_AGAIN("不能重复购买"),


    //购物车
    PRODUCT_NOT_IN_CART("商品未在购物车，不能删除"),





    ;



    private final String message;

    ExceptionEnum( String message) {

        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}