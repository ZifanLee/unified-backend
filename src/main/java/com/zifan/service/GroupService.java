package com.zifan.service;

import com.zifan.model.Group;
import com.zifan.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    // 创建群组
    @Transactional
    public Group createGroup(String name, String creatorId, List<String> members, String type) {
        Group group = new Group();
        group.setName(name);
        group.setCreatorId(creatorId);
        group.setMembers(members);
        group.setType(type);
        group.setCreatedAt(new Date());
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    // 分页查询群组
    public Page<Group> getGroupsByType(String type, Pageable pageable) {
        return groupRepository.findByType(type, pageable);
    }

    // 根据名称模糊查询群组
    public List<Group> searchGroupsByName(String name) {
        return groupRepository.findByNameContaining(name);
    }

    // 添加成员
    @Transactional
    public void addMember(String groupId, String memberId) {
        groupRepository.findById(groupId).ifPresent(group -> {
            group.getMembers().add(memberId);
            group.setUpdatedAt(new Date());
            groupRepository.save(group);
        });
    }

    // 移除成员
    @Transactional
    public void removeMember(String groupId, String memberId) {
        groupRepository.findById(groupId).ifPresent(group -> {
            group.getMembers().remove(memberId);
            group.setUpdatedAt(new Date());
            groupRepository.save(group);
        });
    }
}