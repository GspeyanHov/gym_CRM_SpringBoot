TRUNCATE TABLE users RESTART IDENTITY CASCADE;
TRUNCATE TABLE training_type RESTART IDENTITY CASCADE;
TRUNCATE TABLE trainee RESTART IDENTITY CASCADE;
TRUNCATE TABLE trainer RESTART IDENTITY CASCADE;
TRUNCATE TABLE training RESTART IDENTITY CASCADE;
TRUNCATE TABLE address RESTART IDENTITY CASCADE;

INSERT INTO training_type (id, training_type_name) VALUES
                                                       (1, 'CARDIO'),
                                                       (2, 'STRENGTH'),
                                                       (3, 'FLEXIBILITY');

INSERT INTO users (firstname, lastname, username, password, isactive) VALUES
                                                                          ('John', 'Doe', 'john.doe', 'password1', TRUE),
                                                                          ('Jane', 'Smith', 'jane.smith', 'password2', TRUE),
                                                                          ('Eva', 'Adams', 'eva.adams', 'password5', TRUE),
                                                                          ('Alice', 'Johnson', 'alice.johnson', 'password3', FALSE);

INSERT INTO trainee (dateofbirth, id)
VALUES
    ('1990-01-01', 1),
    ('1985-05-15', 2);

INSERT INTO trainer (id, training_type_id)
VALUES
    (3, 1),
    (4, 2);

INSERT INTO training (trainingduration, id, trainee_id, trainer_id, training_type_id, trainingname, trainingdays)
VALUES
    (1, 1, 1, 3, 1, 'Cardio Training', '{MONDAY, WEDNESDAY, FRIDAY}'),
    (2, 2, 2, 4, 2, 'Strength Training', '{TUESDAY, THURSDAY}');


INSERT INTO address (apartmentnumber, buildingnumber, id, trainee_id, city, street)
VALUES
    (101, 10, 1, 1, 'New York', 'Broadway'),
    (202, 20, 2, 2, 'Los Angeles', 'Sunset Boulevard');