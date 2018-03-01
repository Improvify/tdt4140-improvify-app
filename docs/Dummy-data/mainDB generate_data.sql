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
    
    