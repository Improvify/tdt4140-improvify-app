-- Users
insert into UserAccount (firstname, lastname, height, birthdate, username, `password`, email) values
  ('Test', 'Testesen', 169, date('1994-4-6'), 'test', '$pbkdf2-sha512$10000$ifVmM4ne5CtGGYoCTvdXcSvZAGHZIB2/Meax67ecN6Y=$e3V+9IXhTTRVNN0rEq4E+fkTUSi5/58Cie7X9mZ+VdX7kweX7pIeQIjcjrM15e9zSjBK7ff4qL3u9tr0aryR7QCTgP8ePUPAUA2WMolAhz7emQkPxCyjtNVMVEJYv3pOG+8QHtXmlTjCe3VmKTl0V5Iam9RqIEGKPaGxlM3Lq6Fo5jiOU05MT4x3H2BaPDTt7Z4F9mYzPVvazc19Xxuwe4L+ev1DfwdR8BWGeu0SgitMWc1OTkMoW1bbS3FqR9fKh1q73vfdonbPQxARSimG7CdQjOzZlugVTvHiRr5hXA/KjFC+JqZTeXquU7Z3pCBwubOfg0ztzee5KU3SoZp8Sg==', 'test@improvify.no'),
  ('Clarabelle', 'Sissel', 191.7, date('1970-3-22'), 'csissel0', 'yolopass', 'csissel0@jugem.jp'),
  ('Coletta', 'Gerner', 154.19, date('1979-10-3'), 'cgerner1', 'cryptated69', 'cgerner1@tinyurl.com'),
  ('Keith', 'Bottrell', 170.23, date('1989-5-27'), 'kbottrell2', 'let$Hack1337', 'kbottrell2@ftc.gov'),
  ('Garth', 'Fotheringham', 178.48, date('1997-6-8'), 'gfotheringham3', 'who_d1d_dis', 'gfotheringham3@weebly.com'),
  ('Greg', 'Leversuch', 180.29, date('1975-12-7'), 'gleversuch4', 'supersecret123','gleversuch4@samsung.com');

-- Weight
insert into Weight (currentweight, date, measuredBy) values
  (90, date('2018-1-1'), 1),
  (87, date('2018-1-15'), 1),
  (85, date('2018-1-30'), 1),
  (83, date('2018-2-8'), 1),
  (85, date('2018-2-22'), 1),
  (84, date('2018-3-10'), 1),
  (80, date('2018-3-25'), 1),
  (81, date('2018-4-2'), 1),
  (79, date('2018-4-20'), 1),
  (75, date('2018-5-17'), 1);

-- WorkoutPlan
insert into RestingHeartRate (heartrate, date, measuredBy) values
  (76, date('2018-1-3'), 1),
  (75, date('2018-1-6'), 1),
  (73, date('2018-1-9'), 1),
  (74, date('2018-1-14'), 1),
  (69, date('2018-1-17'), 1),
  (68, date('2018-1-21'), 1),
  (70, date('2018-1-25'), 1),
  (66, date('2018-2-1'), 1),
  (65, date('2018-2-2'), 1),
  (65, date('2018-2-6'), 1),
  (70, date('2018-2-10'), 1),
  (72, date('2018-3-15'), 1),
  (72, date('2018-3-22'), 1),
  (70, date('2018-3-30'), 1),
  (68, date('2018-4-17'), 1),
  (69, date('2018-4-21'), 1),
  (67, date('2018-4-25'), 1),
  (66, date('2018-5-1'), 1),
  (65, date('2018-5-2'), 1),
  (66, date('2018-5-17'), 1);

-- WorkoutSession
insert into WorkoutSession (intensity, startTime, KCal, AvgHeartRate, MaxHeartRate, DistanceRun, durationSeconds, loggedBy) values
  (5, timestamp("2018-1-2", "13:43:53"), 368.5, 155, 167, 5, 3600, 1),
  (7, timestamp("2018-1-6", "14:20:34"), 414.7, 167, 183, 12, 2700, 1),
  (8, timestamp("2018-2-10", "18:32:24"), 400.5, 170, 185, 10, 1800, 1),
  (2, timestamp("2018-2-14", "15:36:59"), 213.8, 140, 166, 4, 5400, 1);

-- Serviceprovider
INSERT INTO serviceprovider (Name) VALUES ("improvify");

-- Serviceproviderpermission
INSERT INTO serviceproviderpermissions (ServiceProvider_id, UserAccount_id, Weight, Height, Email, Name, Username, RestingHeartRate, WorkoutSession, Birthdate) VALUES
  (1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
  (1, 2, 1, 1, 1, 1, 1, 1, 1, 1),
  (1, 3, 1, 1, 1, 1, 1, 1, 1, 1),
  (1, 4, 1, 1, 1, 1, 1, 1, 1, 1),
  (1, 5, 1, 1, 1, 1, 1, 1, 1, 1),
  (1, 6, 1, 1, 1, 1, 1, 1, 1, 1);