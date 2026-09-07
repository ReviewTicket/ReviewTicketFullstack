-- users 에 리뷰 티켓 컬럼을 추가한다.
--
-- 이 컬럼은 그동안 스크립트 없이 각자 손으로 ALTER 해서 쓰고 있었다. 저장소에
-- 만드는 문장이 없다 보니 04_auth.sql 만 보면 존재 자체가 안 보이고, 04~13 을
-- 순서대로 실행해 새로 만든 DB 에는 컬럼이 없어 서버가 뜨지 않는다
-- (ddl-auto:validate 가 User.tickets 와 실제 표의 차이를 잡는다).
--
-- 기본값 3 은 User.CUSTOMER_INITIAL_TICKETS 와 같은 값이다. 둘 중 하나만
-- 고치면 가입 직후 값과 기존 행의 값이 어긋나므로 함께 고쳐야 한다.
--
-- 사장은 티켓 개념이 없어 -1 로 둔다(User.OWNER_TICKETS). 0 장 남은 고객과
-- 구분해야 프론트가 티켓 영역을 감출지 판단할 수 있다. 그래서 기존 행은
-- DEFAULT 로 끝내지 않고 역할에 맞춰 한 번 채워 준다.
--
-- 이미 손으로 컬럼을 넣어 둔 DB 에서도 그대로 실행할 수 있도록 컬럼이 있는지
-- 보고 없을 때만 붙인다 — MySQL 에는 ADD COLUMN IF NOT EXISTS 가 없다.
--
-- 실행:
--   Get-Content "C:\dev\ReviewTicketFullstack\backend\DB\14_user_tickets.sql" | & "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe" -u reviewticket -p -P 21096 --default-character-set=utf8mb4 reviewticket

USE reviewticket;

SET @has_tickets = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'tickets'
);

SET @ddl = IF(@has_tickets = 0,
  'ALTER TABLE users ADD COLUMN tickets INT NOT NULL DEFAULT 3 COMMENT ''남은 리뷰 티켓. 고객은 가입 시 3장, 사장은 -1(티켓 개념 없음)'' AFTER token_version',
  'DO 0');

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 컬럼을 방금 만든 경우에만 기존 행을 역할에 맞게 채운다. 이미 운영 중이던
-- DB 에서 이 UPDATE 가 돌면 고객이 쓰던 티켓 수가 3 으로 되돌아간다.
SET @backfill = IF(@has_tickets = 0,
  'UPDATE users SET tickets = CASE WHEN role = ''OWNER'' THEN -1 ELSE 3 END',
  'DO 0');

PREPARE stmt FROM @backfill;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
