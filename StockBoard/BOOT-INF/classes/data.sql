INSERT IGNORE INTO user (user_id, password, user_name, user_type, created_at)
VALUES 
  ('admin', '$2a$10$4X9exmgx0Fb9xqEQWjlFneuS6XAls1DsQTQ.Vgv35NBwm7JoJ4vou', '관리자', 'admin', CURRENT_TIMESTAMP),
  ('guest', '$2a$10$4X9exmgx0Fb9xqEQWjlFneuS6XAls1DsQTQ.Vgv35NBwm7JoJ4vou', '사용자', 'user', CURRENT_TIMESTAMP);
