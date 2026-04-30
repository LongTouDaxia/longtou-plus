**项目简介**
本项目是我在学习完Redis、RabbitMQ等技术后的一个综合实践项目。基于GitHub用户ZeroWdd的仿小米商城项目，我对其进行了全面的重构与功能扩展。
原项目地址：https://github.com/ZeroWdd/Xiaomi

🚧 **当前状态**
项目目前仍在积极开发中，更多功能持续迭代，敬请期待！

📁 **技术栈**
前端：Vue + Vue-router + Vuex + Element-ui + Axios  (打算过段时间用大模型按照接口文档进行生成)
后端：Spring Boot + Redis + Redission + MyBatis-Plus + MySQL + RabbitMQ

**核心特性：**
基于JWT的用户登录校验
基于redis的缓存和分布式锁
基于消息队列的商品秒杀功能
RESTful风格接口设计

🔄 改进与新增
在原项目基础上，我对整体架构进行了重构，将数据库架构替换为mybatis-plus框架，统一返回数据处理和异常处理，并实现了以下功能模块：
用户注册与登录
商城首页展示
商品分类浏览
商品详情页
购物车功能
订单结算
我的收藏 
商品秒杀模块
