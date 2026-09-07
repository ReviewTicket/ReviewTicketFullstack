-- ReviewTicket 회원·인증 스키마
--
-- 아래 CREATE TABLE 은 이 시점의 users 표다. 지금의 users 는 여기에 더해
-- version(13_optimistic_lock.sql), tickets(14_user_tickets.sql) 를 가진다.
-- 이 파일을 고치지 않고 뒤 스크립트로 덧붙이는 이유는, 이미 04 를 실행해 둔
-- DB 가 여럿이라 여기서 컬럼을 늘리면 그쪽에 반영될 방법이 없기 때문이다.
-- 새 DB 는 04 부터 번호 순서대로 전부 실행하면 같은 결과가 된다.
--
-- 실행:
--   Get-Content "C:\dev\ReviewTicket\BackEnd\DB\04_auth.sql" |
--     & "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe" -u reviewticket -p

USE reviewticket;

CREATE TABLE IF NOT EXISTS users (
  id                 BIGINT       NOT NULL AUTO_INCREMENT,
  email              VARCHAR(190) NOT NULL,
  password_hash      VARCHAR(72)  NOT NULL,
  role               ENUM('CUSTOMER','OWNER') NOT NULL,
  display_name       VARCHAR(32)  NOT NULL,
  email_verified     BOOLEAN      NOT NULL DEFAULT FALSE,
  token_version      INT          NOT NULL DEFAULT 0,
  created_at         DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at         DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
                                      ON UPDATE CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),
  UNIQUE KEY uk_users_email (email),
  UNIQUE KEY uk_users_display_name (display_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
