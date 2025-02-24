-- 관리자 계정 생성
INSERT INTO users (
    email,
    password,
    nickname,
    role,
    created_at,
    updated_at
  )
VALUES (
    'admin@freeadddictionary.com',
    '$2a$10$6RyRzKqYwXvpPzPbhNHdA.YFCw0bRHxiBlbNaZAQcAeGQtEv4gYme',
    -- password: admin123
    '관리자',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );
-- 테스트용 사용자 계정 생성
INSERT INTO users (
    email,
    password,
    nickname,
    role,
    created_at,
    updated_at
  )
VALUES (
    'user@freeadddictionary.com',
    '$2a$10$6RyRzKqYwXvpPzPbhNHdA.YFCw0bRHxiBlbNaZAQcAeGQtEv4gYme',
    -- password: user123
    '일반사용자',
    'USER',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );
-- 샘플 단어 데이터 추가
INSERT INTO dictionary (
    word,
    language,
    part_of_speech,
    pronunciation,
    meaning,
    example_sentence,
    view_count,
    user_id,
    created_at,
    updated_at
  )
VALUES (
    'Hello',
    'English',
    'exclamation',
    'həˈləʊ',
    '안녕하세요',
    'Hello, how are you today?',
    0,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    'World',
    'English',
    'noun',
    'wɜːld',
    '세계',
    'Hello, World!',
    0,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );
-- 샘플 문의 데이터 추가
INSERT INTO inquiry (
    title,
    content,
    author_email,
    resolved,
    user_id,
    created_at,
    updated_at
  )
VALUES (
    '사이트 이용 문의',
    '단어 추가는 어떻게 하나요?',
    'user@freeadddictionary.com',
    false,
    2,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );