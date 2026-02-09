INSERT INTO users (created_at, created_by, updated_at, updated_by, email, name, nickname, password_hash, role, status)
VALUES
(NOW(), 1, NOW(), 1, 'test@naver.com', '홍길동', '길동', '{noop}12345678', 'USER', 'ACTIVE'),
(NOW(), 1, NOW(), 1, 'user2@example.com', '김철수', '철수', '{noop}12345678', 'USER', 'ACTIVE');

INSERT INTO `user_profiles`
(`created_at`, `created_by`, `updated_at`, `updated_by`, `user_id`, `nickname`, `profile_image_url`, `bio`)
VALUES
(NOW(), 1, NOW(), 1, 1, '맛집러1', '/images/profiles/user1.jpg', '안녕하세요! 맛집 탐방을 좋아합니다.'),
(NOW(), 1, NOW(), 1, 2, '맛집러2', '/images/profiles/user2.jpg', '맛있는 음식과 커피를 좋아해요.');

INSERT INTO restaurants (created_at, created_by, updated_at, updated_by, address, description, latitude, longitude, name, phone)
VALUES
(NOW(), 1, NOW(), 1, '서울특별시 강남구 테헤란로 123', '맛있는 한식집입니다.', 37.5012, 127.0396, '강남맛집1', '02-123-4567'),
(NOW(), 1, NOW(), 1, '서울특별시 서초구 반포대로 45', '점심 메뉴가 다양합니다.', 37.4923, 127.0101, '서초점심맛집', '02-234-5678'),
(NOW(), 1, NOW(), 1, '서울특별시 마포구 월드컵북로 12', '분위기 좋은 카페 겸 식당', 37.5665, 126.9012, '마포카페맛집', '02-345-6789'),
(NOW(), 1, NOW(), 1, '서울특별시 송파구 올림픽로 88', '전통 한정식', 37.5145, 127.1052, '송파한정식', '02-456-7890'),
(NOW(), 1, NOW(), 1, '서울특별시 용산구 한강대로 200', '강남 스타일 퓨전 음식', 37.5290, 126.9649, '용산퓨전맛집', '02-567-8901'),
(NOW(), 1, NOW(), 1, '서울특별시 중구 세종대로 150', '커피와 디저트 전문점', 37.5633, 126.9820, '중구디저트카페', '02-678-9012'),
(NOW(), 1, NOW(), 1, '서울특별시 강서구 공항대로 300', '가성비 좋은 한식뷔페', 37.5610, 126.8050, '강서한식뷔페', '02-789-0123'),
(NOW(), 1, NOW(), 1, '서울특별시 동작구 사당로 55', '분위기 있는 일식집', 37.4845, 126.9812, '사당일식', '02-890-1234'),
(NOW(), 1, NOW(), 1, '서울특별시 은평구 연서로 78', '이탈리안 레스토랑', 37.6172, 126.9223, '은평이탈리안', '02-901-2345'),
(NOW(), 1, NOW(), 1, '서울특별시 노원구 상계로 101', '치킨과 맥주 전문점', 37.6543, 127.0567, '노원치킨', '02-012-3456');

INSERT INTO `categories` (`name`, `created_at`, `updated_at`) VALUES
('한식', NOW(), NOW()),
('중식', NOW(), NOW()),
('일식', NOW(), NOW()),
('양식', NOW(), NOW());

INSERT INTO `restaurant_categories` (`restaurant_id`, `category_id`) VALUES
(1, 1),  -- 맛집A - 한식
(1, 2),  -- 맛집A - 양식
(2, 3);  -- 맛집B - 일식

INSERT INTO restaurant_likes
(created_at, created_by, restaurant_id, updated_at, updated_by, user_id)
VALUES
(NOW(), 1, 1, NOW(), 1, 1),
(NOW(), 2, 2, NOW(), 1, 1),
(NOW(), 3, 3, NOW(), 1, 1),
(NOW(), 1, 4, NOW(), 1, 1),
(NOW(), 1, 5, NOW(), 1, 1),
(NOW(), 1, 6, NOW(), 1, 1),
(NOW(), 1, 7, NOW(), 1, 1),
(NOW(), 1, 8, NOW(), 1, 1),
(NOW(), 1, 9, NOW(), 1, 1),
(NOW(), 1, 10, NOW(), 1, 1);
;

INSERT INTO reviews (rating, created_at, created_by, updated_at, updated_by, restaurant_id, user_id, content)
VALUES
(5, NOW(), 1, NOW(), 1, 1, 1, '정말 맛있게 먹었습니다! 강력 추천합니다.'),
(4, NOW(), 2, NOW(), 2, 2, 2, '점심 메뉴가 다양하고 괜찮네요.'),
(3, NOW(), 1, NOW(), 1, 3, 1, '카페 분위기는 좋지만 음식은 보통입니다.'),
(5, NOW(), 2, NOW(), 2, 4, 2, '전통 한정식이라 만족스러웠습니다.'),
(4, NOW(), 1, NOW(), 1, 5, 1, '퓨전 음식 맛있고 분위기 좋습니다.'),
(2, NOW(), 2, NOW(), 2, 6, 2, '디저트가 조금 달아서 아쉬웠습니다.'),
(5, NOW(), 1, NOW(), 1, 7, 1, '한식 뷔페 가성비 최고!'),
(4, NOW(), 2, NOW(), 2, 8, 2, '일식집이라 깔끔하고 좋네요.'),
(3, NOW(), 1, NOW(), 1, 9, 1, '이탈리안 레스토랑인데 평범했습니다.'),
(5, NOW(), 2, NOW(), 2, 10, 2, '치킨과 맥주 최고! 친구들과 다시 오고 싶네요.');


--INSERT INTO `recommendations` (`score`, `created_at`, `created_by`, `updated_at`, `updated_by`, `board_id`, `user_id`, `reason`)
--VALUES
--(4.5, NOW(), 1, NOW(), 1, 1, 1, '친절한 서비스'),
--(5.0, NOW(), 2, NOW(), 2, 1, 1, '맛있음'),
--(3.5, NOW(), 1, NOW(), 1, 1, 1, '가격 대비 만족'),
--(4.0, NOW(), 2, NOW(), 2, 1, 2, '분위기 좋음'),
--(2.5, NOW(), 1, NOW(), 1, 1, 2, '조금 짰음'),
--(4.8, NOW(), 2, NOW(), 2, 1, 2, '다시 방문 의사 있음'),
--(3.0, NOW(), 1, NOW(), 1, 1, 1, '평범함'),
--(4.2, NOW(), 2, NOW(), 2, 2, 2, '추천함'),
--(5.0, NOW(), 1, NOW(), 1, 3, 1, '강추'),
--(4.7, NOW(), 2, NOW(), 2, 1, 1, '인테리어 예쁨');
