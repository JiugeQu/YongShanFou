package com.mizore.mob.util;

public class Constant {

    public static final String RUNTIME_EXCEPTION_MSG = "服务器被玩坏了，绝对不是服务器的错！！";

    public static final String LOGIN_CODE_PREFIX = "login:code:";
    public static final int LOGIN_CODE_TTL = 5;
    public static final int MAX_COUNT_DISH_PER_PAGE = 10;

    /**
     * 1：顾客，2：配送员。3：餐厅员工
     */
    public static final Byte ROLE_CUSTOMER = 1;
    public static final Byte ROLE_DELIVERY = 2;
    public static final Byte ROLE_STAFF = 3;

    /**
     *  菜品 1：上架  2：下架   默认未上架状态
     */
    public static final Byte DISH_ON_SALE = 1;
    public static final Byte DISH_OUT_SALE = 2;

    /**
     * 订单状态
     * 1：待接单-用户完成支付但待商家确认，
     * 2：备餐中-商家确认后处于备餐中，
     * 3：待配送-备餐完毕等待骑手配送，
     * 4：配送中-骑手抢单后到送达前，
     * 5：已送达-骑手送达
     */
    public static final Byte ORDER_WAIT_CONFIRM = 1;
    public static final Byte ORDER_PREPARING = 2;
    public static final Byte ORDER_WAIT_DELIVERED = 3;
    public static final Byte ORDER_DELIVERING = 4;
    public static final Byte ORDER_DELIVERED = 5;
    public static final String STAFF_PREFIX = "staff:";
    public static final String DELIVERY_PREFIX = "delivery:";
    public static final String CUSTOMER_PREFIX = "customer:";
    public static final String SSE_REFRESH_MESSAGE = "refresh";

    // 订单被确认时发给顾客的sse消息
    public static final String SSE_CONFIRMED_MESSAGE = "confirmed";

    // 订单备餐完成时发给顾客的sse消息
    public static final String SSE_COMPLETED_MESSAGE = "preparation completed";
    public static final String SSE_DELIVERING_MESSAGE = "delivering";

    // 幂等性redis setnx 的键前缀
    public static final String IDEMPOTENT_KEY_PREFIX = "idempotent:";
    public static final String IDEMPOTENT_VALUE = "1";

    public static final String LOGIN_TOKEN_PREFIX = "login:token:";


}
