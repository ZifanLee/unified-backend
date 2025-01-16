package com.zifan.repository;

import com.zifan.model.UserSignal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface SignalRepository extends MongoRepository<UserSignal, String> {
    // 根据用户 email 查询信号文档
    Optional<UserSignal> findByUserEmail(String userEmail);

    // 根据用户 email 和模块名称查询信号
    @Query("{ 'userEmail': ?0, 'modules.?1': { $exists: true } }")
    Optional<UserSignal> findByUserEmailAndModule(String userEmail, String module);

    // 根据用户 email 和信号类型查询信号
    @Query("{ 'userEmail': ?0, 'modules': { $elemMatch: { 'type': ?1 } } }")
    Optional<UserSignal> findByUserEmailAndSignalType(String userEmail, String signalType);

    // 删除指定用户指定模块
    @Query(value = "{ 'userEmail': ?0 }", delete = false)
    @Update("{ $unset: { 'modules.?1': '' } }")
    void deleteModuleByUserEmail(String userEmail, String module);

    // 根据用户 email 删除某个信号类型的信号
    @Query(value = "{ 'userEmail': ?0 }", delete = false)
    @Update("{ $pull: { 'modules.$[].signals': { 'type': ?1 } } }")
    void deleteSignalTypeByUserEmail(String userEmail, String signalType);

    // 根据用户 email 删除所有信号
    void deleteByUserEmail(String userEmail);

    // 查询某个用户某个模块下的某个 ID 是否存在
    @Query("{ 'userEmail': ?0, 'modules.?1': { $elemMatch: { '_id': ?2 } } }")
    Optional<UserSignal> findByUserEmailAndModuleAndId(String userEmail, String module, String id);

    // 删除指定用户模块 ID 的记录
    @Query(value = "{ 'userEmail': ?0 }", delete = false)
    @Update("{ $pull: { 'modules.?1': { '_id': ?2 } } }")
    void deleteByUserEmailAndModuleAndId(String userEmail, String module, String id);
}