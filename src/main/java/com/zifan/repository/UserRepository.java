package com.zifan.repository;

import com.zifan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查找用户
    List<User> findByUsername(String username);

    // 根据邮箱查找用户
    Optional<User> findByEmail(String email);

    // 根据状态查找用户
    List<User> findByStatus(Integer status);

    // 根据角色查找用户
    List<User> findByRole(String role);

    // 根据用户名或邮箱查找用户
    Optional<User> findByUsernameOrEmail(String username, String email);

    // 根据创建时间范围查找用户
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 根据最后登录时间范围查找用户
    List<User> findByLastLoginAtBetween(LocalDateTime start, LocalDateTime end);

    // 根据用户名模糊查找用户
    List<User> findByUsernameContaining(String keyword);

    // 根据邮箱模糊查找用户
    List<User> findByEmailContaining(String keyword);

    // 根据用户名或邮箱模糊查找用户
    List<User> findByUsernameContainingOrEmailContaining(String usernameKeyword, String emailKeyword);

    // 根据姓氏查找用户
    List<User> findByLastName(String lastName);

    // 根据姓氏模糊查找用户
    List<User> findByLastNameContaining(String keyword);

    // 根据手机号码查找用户
    Optional<User> findByPhone(String phone);

    // 根据手机号码模糊查找用户
    List<User> findByPhoneContaining(String keyword);

    // 自定义查询：根据用户名和状态查找用户
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.status = :status")
    Optional<User> findByUsernameAndStatus(@Param("username") String username, @Param("status") Integer status);

    // 自定义查询：统计某个角色的用户数量
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") String role);

    // 自定义查询：查找最近登录的用户
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NOT NULL ORDER BY u.lastLoginAt DESC")
    List<User> findRecentlyLoggedInUsers();

    // 自定义查询：查找创建时间早于指定时间的用户
    @Query("SELECT u FROM User u WHERE u.createdAt < :date")
    List<User> findUsersCreatedBefore(@Param("date") LocalDateTime date);

    // 自定义查询：查找创建时间晚于指定时间的用户
    @Query("SELECT u FROM User u WHERE u.createdAt > :date")
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);

    // 自定义查询：查找头像 URL 不为空的用户
    @Query("SELECT u FROM User u WHERE u.avatarUrl IS NOT NULL")
    List<User> findUsersWithAvatar();

    // 自定义查询：查找简介不为空的用户
    @Query("SELECT u FROM User u WHERE u.bio IS NOT NULL")
    List<User> findUsersWithBio();
}
