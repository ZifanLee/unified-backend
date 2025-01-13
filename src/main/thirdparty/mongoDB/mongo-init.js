// mongo-init.js
db = db.getSiblingDB('lingxi');

// 创建初始用户
db.createUser({
  user: 'lingxi_user',
  pwd: 'lingxi123',
  roles: [
    { role: 'readWrite', db: 'lingxi' }
  ]
});

// 创建初始集合
db.createCollection('groups');
db.createCollection('messages');

// 创建索引
db.groups.createIndex({ creator_id: 1 });
db.groups.createIndex({ members: 1 });

db.messages.createIndex({ sender_id: 1 });
db.messages.createIndex({ receiver_id: 1 });
db.messages.createIndex({ group_id: 1 });
db.messages.createIndex({ sent_at: 1 });