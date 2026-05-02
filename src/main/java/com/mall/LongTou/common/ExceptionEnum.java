package com.mall.LongTou.common;

public enum ExceptionEnum {

    // 通用
    SYSTEM_BUSY(500, "系统繁忙，请稍后重试"),
    REQUEST_TOO_FREQUENT(429, "请求过于频繁"),
    PARAM_ERROR(430,"参数不合法"),
    TOKEN_CREATE_ERROR(431,"令牌生成失败"),
    GET_RESULT_ERROR(432,"结果已过期或不存在"),
    RESULT_PROCESSING(433,"处理中，请稍后重试"),
    DONT_KNOW_ERROR(434,"未知状态"),

    // 轮播图
    GET_CAROUSEL_NOT_FOUND(10001, "获取轮播图失败"),

    // 用户
    USER_NOT_FOUND(20001, "用户不存在"),
    USER_CANTOT_NULL(20002, "用户不能为空"),
    USER_PASSWORD_ERROR(20003, "密码错误"),
    USERNAME_ALREADY_EXIST(20004, "用户名已存在"),

    // 商品
    PRODUCT_NOT_FOUND(30001, "商品不存在"),
    PRODUCTID_NOT_NULL(30002, "商品Id不能为空"),
    CATEGORY_PRODUCT_NULL(30003, "还没有该分类的商品哦"),
    PRODUCT_DELETE_EXCEPTION(30004, "商品删除异常"),

    // 分类
    CATEGORY_NOT_FIND(40001, "分类不存在"),

    // 用户收藏
    USER_OR_PRODUCTID_IS_NULL(50001, "用户名或商品为空"),
    PRODUCT_COLLECTED(50002, "商品已收藏"),
    COLLECT_IS_NULL(50003, "用户收藏为空"),
    PRODUCT_NOT_COLLECT(50004, "商品未收藏，不能删除"),

    // 秒杀
    SECKILL_PRODUCT_NULL(60001, "秒杀商品不存在"),
    SECKILL_NOT_START(60002, "秒杀尚未开始"),
    SECKILL_ALREADY_ENDED(60003, "秒杀已结束"),
    SECKILL_NOT_IN_TIME(60004, "不在秒杀活动时间内"),
    SECKILL_STOCK_INSUFFICIENT(60005, "秒杀库存不足"),
    USER_HAS_SECOND_KILL_LIMIT(60006, "您已达到该商品秒杀限购数量"),
    USER_REPEAT_SECOND_KILL(60007, "您已参与过该场秒杀，不可重复下单"),
    SECKILL_OPTIMISTIC_LOCK_FAIL(60008, "秒杀太火爆，请重试"),
    SECKILL_ACTIVITY_NOT_EXIST(60009, "秒杀活动不存在"),
    SECKILL_ACTIVITY_NOT_START(60010, "秒杀活动尚未开始"),
    SECKILL_ACTIVITY_ENDED(60011, "秒杀活动已结束"),
    SECKILL_GOODS_NOT_EXIST(60012, "秒杀商品配置不存在"),
    SECKILL_ACTIVITY_STATUS_DISABLED(60013, "秒杀活动已被禁用"),

    // 订单
    ADD_ORDER_ERROR(70001, "订单添加异常"),
    USER_ORDER_NULL(70002, "用户订单为空"),
    ORDER_CREATE_EXCEPTION(70003, "订单创建异常"),
    USER_ORDER_AGAIN(70004, "不能重复购买"),
    ORDER_NOT_FOUND(70005, "订单不存在"),
    ORDER_CANNOT_CANCEL(70006, "订单当前状态不可取消"),
    ORDER_PAY_EXPIRE(70007, "订单支付已超时"),
    ORDER_PAY_FAILED(70008, "订单支付失败"),
    ORDER_ROOLBACK_ERROR(70009,"订单回滚异常"),

    // 购物车
    SHOPPINGCART_IS_UNLL(80001,"购物车为空"),
    PRODUCT_NOT_IN_CART(80002, "商品未在购物车，不能删除"),

    // 支付
    PAY_PARAM_ERROR(90001, "支付参数错误"),
    PAY_SERVICE_ERROR(90002, "支付服务异常");

    private final Integer code;
    private final String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}