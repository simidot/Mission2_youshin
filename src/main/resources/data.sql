# 초기 admin 계정 생성
Insert IGNORE Into user_entity (account_id, password, authority)
values ('admin', '$2a$10$hithVJ9E/VPOzKwGfn9kQu9c0QcBfAWnaFNFWh0gohj03Ij05pm9.', 'ROLE_ADMIN');