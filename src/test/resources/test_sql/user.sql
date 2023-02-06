SET FOREIGN_KEY_CHECKS = 0;
DELETE
FROM authorities;
DELETE
FROM user_authorities;
DELETE
FROM users;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO authorities(authority_name, created_at, updated_at)
VALUES ('ROLE_USER', now(), now());
INSERT INTO authorities(authority_name, created_at, updated_at)
VALUES ('ROLE_ADMIN', now(), now());
INSERT INTO authorities(authority_name, created_at, updated_at)
VALUES ('ROLE_STORE_MANAGER', now(), now());

delete
from stores
where stores_id = 20585779;
insert into stores (stores_id, created_at, updated_at, lot_number_address, store_name, position, road_name_address)
values (20585779, '2023-01-27T20:53:47.440803', '2023-01-27T20:53:47.440803', '서울특별시 종로구 동숭동 30', '스타벅스동숭로아트점',
        ST_PointFromText('POINT(37.58296442 127.003887)', 4326), '서울특별시 종로구 동숭길 110 (동숭동)');