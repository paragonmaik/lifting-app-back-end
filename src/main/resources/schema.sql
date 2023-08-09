DROP TABLE IF EXISTS exercise CASCADE;
DROP TABLE IF EXISTS workout CASCADE;
DROP TABLE IF EXISTS workout_exercise;
DROP TABLE IF EXISTS program CASCADE;
DROP TABLE IF EXISTS program_workout;
DROP TABLE IF EXISTS profile CASCADE;

CREATE TABLE exercise (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  load INTEGER,
  goal VARCHAR(30) NOT NULL,
  rest_period_secs INTEGER,
  description TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

INSERT INTO exercise (name, load, goal, rest_period_secs, description, date_created)
VALUES ('Squat', 120, 'Strength', 150, 'High bar back squat.', CURRENT_TIMESTAMP);

CREATE TABLE workout (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  duration_secs INTEGER,
  description TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

INSERT INTO workout (name, duration_secs, description, date_created)
VALUES ('Workout A', 3600, 'A really cool workout.', CURRENT_TIMESTAMP);

CREATE TABLE workout_exercise (
  workout_id INT REFERENCES workout (id) ON UPDATE CASCADE,
  exercise_id INT REFERENCES exercise (id) ON UPDATE CASCADE,
  CONSTRAINT workout_exercise_pk PRIMARY KEY (workout_id, exercise_id)
);

INSERT INTO workout_exercise (workout_id, exercise_id)
VALUES (
  (SELECT id FROM workout WHERE name = 'Workout A'),
  (SELECT id FROM exercise WHERE name = 'Squat')
);

CREATE TABLE program (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  duration_weeks INTEGER,
  description TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

INSERT INTO program (name, duration_weeks, description, date_created)
VALUES ('Bulgarian Program', 52, 'A tough program.', CURRENT_TIMESTAMP);

CREATE TABLE program_workout (
  program_id INT REFERENCES program (id) ON UPDATE CASCADE,
  workout_id INT REFERENCES workout (id) ON UPDATE CASCADE,
  CONSTRAINT program_workout_pk PRIMARY KEY (program_id, workout_id)
);

INSERT INTO program_workout (program_id, workout_id)
VALUES (
  (SELECT id FROM program WHERE name = 'Bulgarian Program'),
  (SELECT id FROM workout WHERE name = 'Workout A')
);

CREATE TABLE profile (
  id SERIAL PRIMARY KEY,
  weight INTEGER,
  height INTEGER,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);
