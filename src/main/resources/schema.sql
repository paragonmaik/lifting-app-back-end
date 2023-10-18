DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS profile CASCADE;
DROP TABLE IF EXISTS exercise CASCADE;
DROP SEQUENCE IF EXISTS exercise_sequence;
DROP TABLE IF EXISTS workout CASCADE;
DROP SEQUENCE IF EXISTS workout_sequence;
DROP TABLE IF EXISTS workout_exercise;
DROP TABLE IF EXISTS program CASCADE;
DROP SEQUENCE IF EXISTS program_sequence;
DROP TABLE IF EXISTS program_workout;


CREATE TABLE users (
  id TEXT PRIMARY KEY UNIQUE NOT NULL,
  login TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  role TEXT NOT NULL
);

INSERT INTO users(id, login, password, role)
VALUES ('a3a74802-75ad-4063-a8fe-27e5d34d773d', 'demo@email.com', '$2y$10$Y.qxjolFMcIDwlv4wATwz.0D1kr1wwM6Nbu0e3KoZ89LM.TLzwvnu', 1);

CREATE TABLE profile (
  user_id TEXT PRIMARY KEY REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
  weight INTEGER,
  height INTEGER,
  date_created TIMESTAMP
);

INSERT INTO profile(user_id, weight, height, date_created)
VALUES ('a3a74802-75ad-4063-a8fe-27e5d34d773d', 89, 172, NOW());

CREATE TABLE exercise (
  id SERIAL PRIMARY KEY,
  user_id TEXT REFERENCES profile (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
  name VARCHAR(30) NOT NULL,
  load INTEGER,
  goal VARCHAR(30) NOT NULL,
  rest_seconds INTEGER,
  instructions TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP,
  sets INTEGER,
  reps INTEGER,
  exec_order INTEGER
);

CREATE SEQUENCE exercise_sequence START WITH 1 INCREMENT BY 1 MINVALUE 1;

CREATE TABLE workout (
  id SERIAL PRIMARY KEY,
  user_id TEXT REFERENCES profile (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
  name VARCHAR(30) NOT NULL,
  duration_mins INTEGER,
  description TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

CREATE SEQUENCE workout_sequence START WITH 1 INCREMENT BY 1 MINVALUE 1;

CREATE TABLE workout_exercise (
  workout_id INT REFERENCES workout (id) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
  exercise_id INT REFERENCES exercise (id) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
  CONSTRAINT workout_exercise_pk PRIMARY KEY (workout_id, exercise_id)
);

CREATE TABLE program (
  id SERIAL PRIMARY KEY,
  user_id TEXT REFERENCES profile (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
  name VARCHAR(30) NOT NULL,
  duration_weeks INTEGER,
  description TEXT,
  date_created TIMESTAMP,
  date_updated TIMESTAMP
);

CREATE SEQUENCE program_sequence START WITH 1 INCREMENT BY 1 MINVALUE 1;

CREATE TABLE program_workout (
  program_id INT REFERENCES program (id) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
  workout_id INT REFERENCES workout (id) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
  CONSTRAINT program_workout_pk PRIMARY KEY (program_id, workout_id)
);
