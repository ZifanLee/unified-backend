# Unified-Backend

unified backend service for lingxi im system


## 安全
使用JWT作为鉴权组件，token作为核心鉴权字段

## 存储方案
### redis
用于支持高性能并发读写场景，例如用户状态存储
### mysql
用于支持关系型数据场景，例如业务数据存储
### mongoDB
用于支持nosql场景，以及大量数据场景，例如消息存储


