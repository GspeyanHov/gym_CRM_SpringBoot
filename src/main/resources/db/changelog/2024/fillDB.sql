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

INSERT INTO users (firstname, lastname, username, password, enabled) VALUES
--password1
('John', 'Doe', 'john.doe', '$2a$10$3JFTvofZ1cbSMcEe4PDED.091x12WgQlKDb0HgesR6/IYWoG026Bq', TRUE),
--password2
('Jane', 'Smith', 'jane.smith', '$2a$10$JXp0Y81qNXwgfw9.akeOcOyX5p4mgseEJO2Ih8uA73wcLFHd8KYNO', TRUE),
--password3
('Eva', 'Adams', 'eva.adams', '$2a$10$I.cdmi6/QCpM7Tc/aPmCKer8wpfyeQHfkMXJcvrkY6AQPc.JaLNK2', TRUE),
--password4
('Alice', 'Johnson', 'alice.johnson', '$2a$10$XJXWaCLIrKVtho./RWogpueDN59aGe57Ns0gFfeNPIGJlGXns4ne2', FALSE);

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

INSERT INTO authorities (username, authority)
VALUES
    ('john.doe', 'ROLE_ADMIN'),
    ('jane.smith', 'ROLE_USER'),
    ('eva.adams', 'ROLE_USER'),
    ('alice.johnson', 'ROLE_USER');