# 🔥 Seckill System – 高性能电商秒杀中间件

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-brightgreen)
![Redis](https://img.shields.io/badge/Redis-red)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-orange)
![MySQL](https://img.shields.io/badge/MySQL-blue)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-blueviolet)
![吞吐量](https://img.shields.io/badge/吞吐量-700~800%20TPS-success)

一个基于 **Spring Boot + Redis + RabbitMQ + MySQL** 的电商秒杀系统，支持 **JWT 登录、购物车、商品收藏、商品分类**，并通过 **Lua脚本 + 异步消息队列 + 延迟队列** 实现了高并发秒杀与订单自动超时取消。

> 经 JMeter 压测，**秒杀接口吞吐量稳定在 700 TPS 左右**，具备良好的抗流量冲击能力。

---

## 📚 目录

- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [系统架构图](#系统架构图)
- [秒杀核心流程](#秒杀核心流程)
    - [流程图](#秒杀时序图)
    - [关键步骤说明](#关键步骤说明)
- [订单超时取消机制](#订单超时取消机制)
- [项目结构](#项目结构)
- [数据库设计简要](#数据库设计简要)
- [环境配置与启动](#环境配置与启动)
- [JMeter 压测说明](#jmeter-压测说明)
- [注意事项](#注意事项)

---

## 🧰 技术栈

| 技术               | 用途                                         |
| ------------------ | -------------------------------------------- |
| Spring Boot        | 基础框架，快速集成                           |
| Redis              | 库存预扣、用户资格标记、生成临时订单ID       |
| RabbitMQ           | 异步解耦秒杀订单落库、延迟队列处理订单取消   |
| MySQL              | 持久化订单、商品、购物车、收藏等数据         |
| MyBatis-Plus       | ORM 简化数据库操作                           |
| JWT                | 用户身份认证（登录、购物车等接口需要）       |
| JMeter             | 压力测试，验证吞吐量                         |

---

## ✨ 核心功能

- ✅ **用户模块**：JWT 登录、注册（未强制令牌时可模拟随机 userId）
- ✅ **秒杀模块**：
    - Redis 库存预热 + Lua 脚本原子检查资格
    - 生成临时订单 ID 返回前端，异步生成真实订单
    - 前端轮询获取最终订单结果
- ✅ **订单超时取消**：RabbitMQ 延迟队列 + 幂等回滚库存
- ✅ **购物车**：商品加入/移除/查询
- ✅ **商品收藏**：用户收藏商品管理
- ✅ **商品分类**：多级分类树展示
- ✅ **JMeter 友好**：秒杀接口支持通过请求体传入随机 userId（不强制依赖 JWT 令牌）

---

## 🧭 系统架构图

```mermaid
flowchart TB
    subgraph Client
        A[前端/APP<br/>轮询订单状态]
    end

    subgraph Gateway
        B[Nginx / Spring Boot 网关层]
    end

    subgraph Core
        C[秒杀接口<br/>Lua脚本 + Redis]
        D[Redis<br/>库存/订单临时状态]
        E[RabbitMQ<br/>订单队列 & 延迟队列]
        F[消费者<br/>订单落库 & 取消回滚]
        G[MySQL<br/>订单/商品/购物车]
    end

    A -->|1. 秒杀请求| C
    C -->|2. 原子检查用户资格| D
    D -->|3. 返回临时订单ID| C
    C -->|4. 返回临时ID| A
    A -->|5. 轮询订单状态| B
    B -->|6. 查询Redis| D
    C -.->|7. 异步发送订单消息| E
    E -->|8. 消费消息| F
    F -->|9. 幂等写入MySQL| G
    F -->|10. 最终订单ID写入Redis| D
    D -->|11. 前端轮询拿到订单ID| A

图中核心路径为 秒杀预检 → 临时ID → 异步落库 → 轮询获取最终订单。

⚡ 秒杀核心流程
秒杀时序图
关键步骤说明
步骤	描述
① 库存预热	秒杀开始前，将商品库存加载到 Redis。
② Lua 原子检查	使用 Lua 脚本检查库存是否充足、用户是否已购买过（防重复下单）。若通过，则扣减库存并记录用户资格。
③ 生成临时订单 ID	利用 Redis INCR 生成一个全局唯一的 orderTempId，作为本次秒杀会话标识立即返回给前端。
④ 前端轮询	前端拿到 orderTempId 后，每 200~500ms 请求状态接口，查询该临时 ID 对应的最终 orderId。
⑤ 异步落库	后端将 (orderTempId, goodsId, userId) 推入 RabbitMQ 队列，由消费者处理。
⑥ 消费者幂等建单	消费者根据 orderTempId 做数据库唯一性约束，防止重复消费；生成真实订单并写入 MySQL。
⑦ 结果回写 Redis	消费者将最终的 orderId 写入 Redis，key 为 order:temp:{orderTempId}，value 为 orderId。
⑧ 前端获取结果	轮询接口从 Redis 中读取到 orderId 后返回给前端，完成秒杀。
⏰ 订单超时取消机制
为了防止用户秒杀后不付款导致库存被长期占用，系统采用 RabbitMQ 延迟队列 实现订单自动取消。

流程图
实现要点
延迟队列配置：使用 RabbitMQ 的 x-delayed-message 插件或 TTL + 死信队列。

幂等检查：取消消费者先查询订单状态，只有“待支付”状态才执行取消操作，避免重复回滚。

数据回滚：

MySQL：订单状态改为 CANCELED。

Redis：商品库存 +1，用户购买资格标记删除。

精准取消：每个订单独立延迟消息，互不影响。

📁 项目结构（关键模块）
text
src/main/java/com/seckill/
├── controller
│   ├── SeckillController.java      // 秒杀入口、轮询接口
│   ├── OrderController.java        // 订单查询
│   ├── CartController.java         // 购物车
│   └── CollectController.java      // 收藏
├── service
│   ├── SeckillService.java         // 秒杀逻辑+Lua调用
│   ├── OrderService.java           // 订单落库、幂等
│   └── RabbitMQSender.java         // 消息发送
├── config
│   ├── RedisConfig.java
│   ├── RabbitMQConfig.java         // 队列、延迟队列配置
│   └── WebMvcConfig.java           // JWT拦截器
├── utils
│   ├── JwtUtil.java
│   └── RedisLuaScript.java          // 加载Lua脚本
└── consumer
    ├── SeckillOrderConsumer.java    // 订单落库消费者
    └── OrderTimeoutConsumer.java    // 超时取消消费者
🗄️ 数据库设计简要
表名	字段说明
user	id, username, password (加密)
goods	id, name, stock, seckill_price, ...
seckill_order	id, order_temp_id(唯一), user_id, goods_id, status, create_time
cart	id, user_id, goods_id, quantity
collection	id, user_id, goods_id
category	id, name, parent_id
秒杀订单表通过 order_temp_id 唯一索引保证幂等。

⚙️ 环境配置与启动
1. 必备环境
JDK 1.8+

Maven 3.6+

MySQL 8.0+

Redis 7.0+

RabbitMQ 3.12+ (需启用 rabbitmq_delayed_message_exchange 插件)

2. 修改配置文件（❗重要）
拉取项目后，请务必修改以下连接信息为你的实际环境：

打开 application.yml，找到以下配置并替换：

yaml
spring:
  datasource:
    url: jdbc:mysql://你的MySQL地址:3306/seckill_db?useSSL=false
    username: 你的MySQL用户名
    password: 你的MySQL密码

  redis:
    host: 你的Redis地址
    port: 6379
    password: 你的Redis密码（若无则留空）

  rabbitmq:
    host: 你的RabbitMQ地址
    port: 5672
    username: guest
    password: guest
3. 初始化数据库
执行项目中的 schema.sql 创建表结构，并导入商品测试数据。

4. 启动顺序
启动 MySQL、Redis、RabbitMQ

启动 Spring Boot 应用（默认端口 8080）

使用 JMeter 或 Postman 测试秒杀接口

5. 秒杀接口示例
http
POST /seckill/1001
Content-Type: application/json

{
    "userId": 12345
}
响应（有资格时）：

json
{
    "code": 200,
    "data": "temp_ord_987654321",
    "msg": "进入秒杀队列，请轮询订单状态"
}
轮询接口：

http
GET /order/status/temp_ord_987654321
最终成功响应：

json
{
    "code": 200,
    "data": 1000001,
    "msg": "订单创建成功"
}
🧪 JMeter 压测说明
由于秒杀接口不强制要求 JWT 令牌，压测变得非常简单：

创建线程组（例如 200 线程，循环 5 次）

添加 HTTP 请求取样器：

路径：/seckill/1001

Method：POST

Body 数据：{"userId": ${__Random(1,10000,userId)}}

添加聚合报告

运行后 吞吐量可达 700~800 TPS（取决于服务器配置）

若需测试完整 JWT 流程，可先调用登录接口获取令牌，并添加 HTTP Header Authorization: Bearer <token>。

⚠️ 注意事项
本项目为个人学习/演示作品，其他接口（购物车、收藏等）可能存在小 Bug，但不影响秒杀主流程运行。

秒杀未做严格的前端限流（如令牌桶），生产环境建议在网关层增加限流。

轮询间隔建议 200ms，过低会增加服务器压力，过高则会延迟感知结果。

订单超时取消的延迟时间可在配置文件中调整。

请务必修改配置文件中的 MySQL、Redis、RabbitMQ 连接信息为实际环境。

📈 性能表现
指标	数值
秒杀接口 TPS	≈ 700 (单机 4C8G)
平均响应时间	95ms (Lua + 返回临时ID)
轮询接口 QPS	> 2000 (纯 Redis 读)
消息队列堆积能力	无限制（可削峰填谷）
👨‍💻 作者
一个热爱高并发与中间件技术的后端开发者。
如果这个项目对你有帮助，欢迎 ⭐Star 支持一下！

