DROP TABLE IF EXISTS Exercises;
DROP TABLE IF EXISTS Workouts;

CREATE TABLE exercises (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  load INTEGER,
  goal VARCHAR(30) NOT NULL,
  rest_period_sec INTEGER,
  description TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

INSERT INTO Exercises (name, load, goal, rest_period_sec, description, date_created)
VALUES ('Squat', 120, 'Strength', 150, 'High bar back squat.', CURRENT_TIMESTAMP);

CREATE TABLE workouts (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  duration_sec INTEGER,
  description TEXT,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

INSERT INTO Workouts (name, duration_sec, description, date_created)
VALUES ('Workout A', 3600, 'A really cool workout.', CURRENT_TIMESTAMP);
