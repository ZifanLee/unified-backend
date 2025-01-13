package com.zifan.repository;

import com.zifan.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {

    // 查询用户创建的群组
    List<Group> findByCreatorId(String creatorId);

    // 查询用户加入的群组
    List<Group> findByMembersContaining(String memberId);

    // 分页查询群组
    Page<Group> findByType(String type, Pageable pageable);

    // 根据名称模糊查询群组
    List<Group> findByNameContaining(String name);
}
