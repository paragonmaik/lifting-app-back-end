CREATE TABLE  Exercises (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  load INTEGER,
  goal VARCHAR(30) NOT NULL,
  rest_period_in_ms INTEGER,
  description TEXT,
  set_kind VARCHAR(30) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  date_updated TIMESTAMP
);

INSERT INTO Exercises (name, load, goal, rest_period_in_ms, description, set_kind, date_created)
VALUES ('Squat', 120, 'Strength', 150, 'High bar back squat.', 'Work set', CURRENT_TIMESTAMP)
