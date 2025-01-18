# Unified-Backend

unified backend service for lingxi im system

本系统旨在打造安全 高性能的后端服务，为lingxi-client提供稳定可靠的服务，预计未来会全面支持在线即时通讯和流媒体系统。

本系统目前还在原型开发阶段，如果你有兴趣，非常欢迎加入此项目！


## 安全
使用JWT作为鉴权组件，token作为核心鉴权字段

## 存储方案
### redis
用于支持高性能并发读写场景，例如用户状态存储
### mysql
用于支持关系型数据场景，例如业务数据存储
### mongoDB
用于支持nosql场景，以及大量数据场景，例如消息存储
### rabbitMQ
用于支持在线的消息传递，基于发布订阅实现单点消息和群发消息
### ElasticSearch
用于支持全文搜索，支持论坛社区功能


