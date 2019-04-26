DROP TABLE IF EXISTS Professor_Office;
DROP TABLE IF EXISTS Building_Course;
DROP TABLE IF EXISTS Courses;
DROP TABLE IF EXISTS Building;
DROP TABLE IF EXISTS Professor;

CREATE TABLE Building
(
	room_id int AUTO_INCREMENT PRIMARY KEY,
	room_number int
);

CREATE TABLE Professor
(
	professor_id int AUTO_INCREMENT PRIMARY KEY,
	professor_first VARCHAR(15),
	professor_last VARCHAR(15),
	professor_department VARCHAR(35),
	professor_startedWork VARCHAR(15)
);

CREATE TABLE Courses
(
	course_id int AUTO_INCREMENT PRIMARY KEY,
	course_credit DECIMAL(3,2),
	course_section VARCHAR(15),
	course_name VARCHAR(35),
	course_season VARCHAR(15),
	professor_id int,
	FOREIGN KEY(professor_id) REFERENCES Professor(professor_id)
);

CREATE TABLE Building_Course
(
	room_id int,
	course_id int,
	FOREIGN KEY(room_id) REFERENCES Building(room_id),
	FOREIGN KEY(course_id) REFERENCES Courses(course_id)
);

CREATE TABLE Professor_Office
(
	room_id int,
	professor_id int,
	FOREIGN KEY(room_id) REFERENCES Building(room_id),
	FOREIGN KEY(professor_id) REFERENCES Professor(professor_id)
);


INSERT INTO Building (room_number) VALUES (121);
INSERT INTO Building (room_number) VALUES (122);
INSERT INTO Building (room_number) VALUES (123);
INSERT INTO Building (room_number) VALUES (124);
INSERT INTO Building (room_number) VALUES (125);
INSERT INTO Building (room_number) VALUES (130);
INSERT INTO Building (room_number) VALUES (132);
INSERT INTO Building (room_number) VALUES (134);
INSERT INTO Building (room_number) VALUES (136);
INSERT INTO Building (room_number) VALUES (138);

INSERT INTO Professor (professor_first, professor_last, professor_department, professor_startedWork) VALUES 
('John', 'Diggle', 'Computer_Science', '2015'), 
('Jennie', 'Crigler', 'Mathematical', '2014'),
('Wynona', 'James ', 'Communication', '2010'),
('Elizabeth', 'Vaughn', 'Science', '2017'),
('Ruth', 'Emmon', 'Economy', '2012'),
('Lucille', 'Lucille', 'Science', '2013'),
('Olivia', 'Mitchell', 'Computer_Science', '2012'),
('Mary', 'Hill', 'Art', '2013'),
('Frances', 'Nugent', 'Computer_Science', '2018');

INSERT INTO Courses (course_credit, course_section, course_name, course_season, professor_id) VALUES
(3.00, 'CS_347_1', 'Mobile_App_Development', 'Spring_2019', 1),
(3.00, 'CS_315_1', 'Modern_Database_Management', 'Spring_2019',7),
(3.00, 'CS_324_3', 'Intro_to_Design_Of_Algorithm', 'Spring_2019', 7),
(3.00, 'CS_317_1', 'Event-Driven_Programming', 'Spring_2019', 9),
(3.00, 'CS_308_1', 'Operating_Systems', 'Spring_2019', 1),
(4.00, 'MATH_187_2', 'Calculus_1', 'Spring_2019', 2),
(3.00, 'CMTC_100_4', 'Introduction_To_Communication', 'Spring_2019',3),
(3.00, 'CS_201_2', 'Discrete_Structures', 'Spring_2019', 1),
(3.00, 'ART_170G_3', 'Studio_Experiences', 'Spring_2019', 8),
(3.00, 'ECON_215_2', 'Principles_of_Macroeconomics', 'Spring_2019',5),
(3.00, 'CHEM_103_2', 'Chemistry_And_Society', 'Spring_2019', 7),
(3.00, 'CS_331_L18', 'Computer_NetWorks', 'Spring_2019', 7);

INSERT INTO Building_Course (room_id,course_id) VALUES
(10,1),
(4,2),
(5,3),
(6,4),
(9,5),
(8,6),
(6,7),
(1,8),
(2,9),
(3,10),
(7,11),
(5,12);

INSERT INTO Professor_Office (room_id,professor_id) VALUES
(1,1),
(3,2),
(4,3),
(7,4),
(5,5),
(2,6),
(8,7),
(9,8),
(10,9);











