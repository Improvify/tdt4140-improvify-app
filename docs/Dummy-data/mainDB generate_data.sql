-- Users
insert into UserAccount (firstname, lastname, height, birthdate, username, `password`, email) values
	('Clarabelle', 'Sissel', 191.7, date('1970-3-22'), 'csissel0', 'yolopass', 'csissel0@jugem.jp'),
	('Coletta', 'Gerner', 154.19, date('1979-10-3'), 'cgerner1', 'cryptated69', 'cgerner1@tinyurl.com'),
	('Keith', 'Bottrell', 170.23, date('1989-5-27'), 'kbottrell2', 'let$Hack1337', 'kbottrell2@ftc.gov'),
	('Garth', 'Fotheringham', 178.48, date('1997-6-8'), 'gfotheringham3', 'who_d1d_dis', 'gfotheringham3@weebly.com'),
	('Greg', 'Leversuch', 180.29, date('1975-12-7'), 'gleversuch4', 'supersecret123','gleversuch4@samsung.com');

-- Weight
insert into Weight values
	(1, 105.0, date('2018-1-3'), 1),
	(2, 103.7, date('2018-1-4'), 1),
	(3, 103.4, date('2018-1-11'), 1),
	(4, 102.8, date('2018-1-12'), 1),
	(5, 97.3, date('2018-1-18'), 1),
	(6, 95.4, date('2018-1-21'), 1),
	(7, 99.1, date('2018-1-25'), 1),
	(8, 94.6, date('2018-2-1'), 1),
	(9, 90.2, date('2018-2-2'), 1),
	(10, 94.0, date('2018-2-6'), 1);

-- WorkoutPlan
insert into RestingHeartRate values
	(0, 76, date('2018-1-3'), 1),
	(1, 75, date('2018-1-6'), 1),
	(2, 73, date('2018-1-9'), 1),
	(3, 74, date('2018-1-14'), 1),
	(4, 69, date('2018-1-17'), 1),
	(5, 68, date('2018-1-21'), 1),
	(6, 70, date('2018-1-25'), 1),
	(7, 66, date('2018-2-1'), 1),
	(8, 65, date('2018-2-2'), 1),
	(9, 65, date('2018-2-6'), 1);
    
-- WorkoutSession
insert into WorkoutSession values
	(1, 5, timestamp("2018-1-2", "13:43:53"), 368.5, 155, 167, 5, 1),
	(2, 7, timestamp("2018-1-6", "14:20:34"), 414.7, 167, 183, 12, 2),
	(3, 8, timestamp("2018-2-10", "18:32:24"), 400.5, 170, 185, 10, 1),
	(4, 2, timestamp("2018-2-14", "15:36:59"), 213.8, 140, 166, 4, 2);

INSERT INTO serviceprovider VALUES (1, "Gavin Belson"),
  (2, "Amanda Crew"),
  (3, "Richie Kendrichs"),
  (4, "Gabe Jenssen"),
  (5, "Kristian Fossnes"),
  (6, "Pål Rekstad"),
  (7, "Kåre smaråd"),
  (8, "Amanda volda"),
  (9, "Rick Roll");

INSERT INTO Goal VALUES (1, "kort beskrivelse", 0, 1, 1),
  (2, "kort beskrivelse crew", 0, 1, 1),
  (3, "kort beskrivelse kendrich", 0, 1, 1),
  (4, "kortere mål", 0, 1, 1),
  (5, "rew er et mål", 0, 1, 1),
  (6, "endrich", 0, 1, 1),
  (7, "kjapt mål", 0, 1, 1),
  (8, "langsiktig", 0, 1, 1),
  (9, "svada", 0, 1, 1);

INSERT INTO improvifytrainer VALUES (1, "lifecoach", 1, 1, 1),
  (2, "coacher", 2, 2, 2),
  (3, "sexcoach", 3, 3, 3),
  (4, "manspalincoach", 4, 4, 4),
  (5, "poachercoacher", 5, 5, 5),
  (6, "sexual intercourse coach", 6, 6, 6),
  (7, "humanlifecoach", 7, 7, 7),
  (8, "kamasutracoacher", 8, 8, 8),
  (9, "sprintcoach", 9, 9, 9);

INSERT INTO workoutplan VALUES (1, 3, 1, 1, 1, 1),
  (2, 3, 2, 2, 2, 2),
  (3, 6, 3, 3, 3, 3),
  (4, 3, 4, 4, 4, 4),
  (5, 3, 5, 5, 5, 5),
  (6, 6, 6, 6, 6, 6),
  (7, 3, 7, 7, 7, 7),
  (8, 3, 8, 8, 8, 8),
  (9, 6, 9, 9, 9, 9);

INSERT INTO periodplan VALUES (1, '2018-06-06, 10:34:00', '2018-06-06, 10:34:00', '2018-06-06, 10:54:00', 1, 1, 1),
  (2, '2018-06-06, 10:34:00', '2018-06-06, 10:34:00', '2018-06-06, 10:54:00', 2, 2, 2),
  (3, '2018-06-06, 11:34:00', '2018-06-06, 11:34:00', '2018-06-06, 11:54:00', 3, 3, 3),
  (4, '2018-06-06, 23:34:00', '2018-06-06, 23:34:00', '2018-06-06, 23:54:00', 4, 4, 4),
  (5, '2018-06-06, 20:34:00', '2018-06-06, 20:34:00', '2018-06-06, 20:54:00', 5, 5, 5),
  (6, '2018-06-06, 19:34:00', '2018-06-06, 19:34:00', '2018-06-06, 19:54:00', 6, 6, 6),
  (7, '2018-06-06, 17:34:00', '2018-06-06, 17:34:00', '2018-06-06, 17:54:00', 7, 7, 7),
  (8, '2018-06-06, 15:34:00', '2018-06-06, 15:34:00', '2018-06-06, 15:54:00', 8, 8, 8),
  (9, '2018-06-06, 12:34:00', '2018-06-06, 12:34:00', '2018-06-06, 12:54:00', 9, 9, 9);
    
    