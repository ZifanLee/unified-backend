// 切换到 lingxi 数据库
db = db.getSiblingDB('lingxi');

// 创建初始用户
db.createUser({
  user: 'lingxi_user',
  pwd: 'lingxi123',
  roles: [
    { role: 'readWrite', db: 'lingxi' }  // 授予 lingxi 数据库的读写权限
  ]
});

// 创建 groups 集合
db.createCollection('groups');

// 为 groups 集合创建索引
db.groups.createIndex({ creatorId: 1 });  // 按 creatorId 查询
db.groups.createIndex({ members: 1 });    // 按 members 查询

// 插入初始 groups 数据（可选）
db.groups.insertMany([
  {
    name: 'Public Group 1',
    creatorId: 'user1',
    members: ['user1', 'user2', 'user3'],
    type: 'public',
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Private Group 1',
    creatorId: 'user2',
    members: ['user2', 'user3'],
    type: 'private',
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

// 创建 messages 集合
db.createCollection('messages');

// 为 messages 集合创建索引
db.messages.createIndex({ senderId: 1 });    // 按 senderId 查询
db.messages.createIndex({ receiverId: 1 });  // 按 receiverId 查询
db.messages.createIndex({ groupId: 1 });     // 按 groupId 查询
db.messages.createIndex({ sentAt: 1 });      // 按 sentAt 查询

// 插入初始 messages 数据（可选）
db.messages.insertMany([
  {
    senderId: 'user1',
    receiverId: 'user2',
    content: 'Hello, this is a private message!',
    type: 'text',
    status: 'sent',
    sentAt: new Date(),
    updatedAt: new Date()
  },
  {
    senderId: 'user2',
    groupId: 'group1',
    content: 'Hello, this is a group message!',
    type: 'text',
    status: 'sent',
    sentAt: new Date(),
    updatedAt: new Date()
  }
]);

// 打印初始化完成信息
print('MongoDB initialization completed successfully!');