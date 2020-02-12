
--
-- Data for Name: members; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.members VALUES (1, '2020-02-11 22:00:06', 'email@email.com', 'John', 'M', 'Smith', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (2, '2020-02-11 22:00:06', 'email2@email.com', 'Jane', 'F', 'Doe', 'password', '45282138', NULL);
INSERT INTO public.members VALUES (3, '2020-02-11 22:00:06', 'email3@email.com', 'Thierry', 'M', 'Henry', 'password', '357429566', NULL);
INSERT INTO public.members VALUES (4, '2020-02-11 22:00:06', 'email4@email.com', 'Renath', 'M', 'Minerou', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (5, '2020-02-11 22:00:06', 'email5@email.com', 'Millow', 'M', 'Hobs', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (6, '2020-02-11 22:00:06', 'email6@email.com', 'Nathan', 'M', 'Bowie', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (7, '2020-02-11 22:00:06', 'email7@email.com', 'John', 'M', 'Lennon', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (8, '2020-02-11 22:00:06', 'email8@email.com', 'Jennifer', 'f', 'Lopez', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (9, '2020-02-11 22:00:06', 'email9@email.com', 'Danielle', 'F', 'Rossi', 'password', '071711033', NULL);
INSERT INTO public.members VALUES (10, '2020-02-11 22:00:06', 'email10@email.com', 'Adrianna', 'F', 'Lima', 'password', '071711033', NULL);


--
-- Data for Name: cars; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.cars VALUES (1, 'Toyota', 'RAV4', 5, 2017, 1);
INSERT INTO public.cars VALUES (2, 'BMW', 'X6', 5, 2009, 2);
INSERT INTO public.cars VALUES (3, 'Volvo', 'xc90', 5, 2015, 3);
INSERT INTO public.cars VALUES (4, 'Ford', 'Fiesta', 4, 2017, 4);
INSERT INTO public.cars VALUES (5, 'Ford', 'Mondeo', 5, 2004, 5);
INSERT INTO public.cars VALUES (6, 'Volkswagen', 'Golf 4', 5, 2001, 6);
INSERT INTO public.cars VALUES (7, 'Volkswagen', 'UP', 4, 2018, 7);
INSERT INTO public.cars VALUES (8, 'Audi', 'Q7', 5, 2017, 8);
INSERT INTO public.cars VALUES (9, 'Mercedes', 'C350', 5, 2013, 9);
INSERT INTO public.cars VALUES (10, 'Honda', 'Civic', 5, 2006, 10);


--
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.cities VALUES (1, 'Skopje');
INSERT INTO public.cities VALUES (2, 'Struga');
INSERT INTO public.cities VALUES (3, 'Strumica');
INSERT INTO public.cities VALUES (4, 'Ohrid');
INSERT INTO public.cities VALUES (5, 'Debar');
INSERT INTO public.cities VALUES (6, 'Gostivar');
INSERT INTO public.cities VALUES (7, 'Resen');
INSERT INTO public.cities VALUES (8, 'Prilep');
INSERT INTO public.cities VALUES (9, 'Bitola');
INSERT INTO public.cities VALUES (10, 'Tetovo');
INSERT INTO public.cities VALUES (11, 'Pehcevo');
INSERT INTO public.cities VALUES (12, 'Kicevo');
INSERT INTO public.cities VALUES (13, 'Mavrovo');
INSERT INTO public.cities VALUES (14, 'Kumanovo');
INSERT INTO public.cities VALUES (15, 'Veles');
INSERT INTO public.cities VALUES (16, 'Gevgelija');
INSERT INTO public.cities VALUES (17, 'Negotino');
INSERT INTO public.cities VALUES (18, 'Kavadarci');
INSERT INTO public.cities VALUES (19, 'Stip');
INSERT INTO public.cities VALUES (20, 'Probistip');
INSERT INTO public.cities VALUES (21, 'Kocani');
INSERT INTO public.cities VALUES (22, 'Sveti Nikole');
INSERT INTO public.cities VALUES (23, 'Delcevo');
INSERT INTO public.cities VALUES (24, 'Makedonski brod');
INSERT INTO public.cities VALUES (25, 'Makedonska Kamenica');
INSERT INTO public.cities VALUES (26, 'Vinica');
INSERT INTO public.cities VALUES (27, 'Valandovo');
INSERT INTO public.cities VALUES (28, 'Krusevo');
INSERT INTO public.cities VALUES (29, 'Radovish');
INSERT INTO public.cities VALUES (30, 'Kriva Palanka');
INSERT INTO public.cities VALUES (31, 'Berovo');
INSERT INTO public.cities VALUES (32, 'Demir kapija');
INSERT INTO public.cities VALUES (33, 'Kratovo');
INSERT INTO public.cities VALUES (34, 'Demir hisar');
INSERT INTO public.cities VALUES (35, 'Dojran');


--
-- Data for Name: member_preferences; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.member_preferences VALUES (1, false, false, 1);
INSERT INTO public.member_preferences VALUES (2, false, true, 2);
INSERT INTO public.member_preferences VALUES (3, true, false, 3);
INSERT INTO public.member_preferences VALUES (4, true, true, 4);
INSERT INTO public.member_preferences VALUES (5, false, true, 5);
INSERT INTO public.member_preferences VALUES (6, true, true, 6);
INSERT INTO public.member_preferences VALUES (7, false, false, 7);
INSERT INTO public.member_preferences VALUES (8, true, false, 8);
INSERT INTO public.member_preferences VALUES (9, false, true, 9);
INSERT INTO public.member_preferences VALUES (10, true, true, 10);


--
-- Data for Name: rides; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.rides VALUES (1, 'test-description', '2020-02-12 19:50:55', '2020-02-12 19:50:57', true, 100, 5, 'Struga', 1, 'Skopje');
INSERT INTO public.rides VALUES (2, 'test-ride-2', '2020-02-12 21:04:51', '2020-02-12 21:04:55', true, 150, 3, 'Ohrid', 2, 'Strumica');


--
-- Data for Name: ratings; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.ratings VALUES (1, '2020-02-12 21:01:04', 'dobra voznja', 1, 1, 5);
INSERT INTO public.ratings VALUES (2, '2020-02-12 21:01:15', 'mnogu loso', 2, 1, 1);
INSERT INTO public.ratings VALUES (3, '2020-02-12 21:04:36', 'Meh,sredno', 3, 2, 4);


--
-- Data for Name: ride_requests; Type: TABLE DATA; Schema: public; Owner: najdiprevoz
--

INSERT INTO public.ride_requests VALUES (5, '2020-02-12 19:50:27', '1', 1, 2, 1, 1);