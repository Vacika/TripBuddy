INSERT INTO public.cities(name)
VALUES ('Skopje'),
       ('Struga'),
       ('Strumica'),
       ('Ohrid'),
       ('Debar'),
       ('Gostivar'),
       ('Resen'),
       ('Prilep'),
       ('Bitola'),
       ('Tetovo'),
       ('Pehcevo'),
       ('Kicevo'),
       ('Mavrovo'),
       ('Kumanovo'),
       ('Veles'),
       ('Gevgelija'),
       ('Negotino'),
       ('Kavadarci'),
       ('Stip'),
       ('Probistip'),
       ('Kocani'),
       ('Sveti Nikole'),
       ('Delcevo'),
       ('Makedonski brod'),
       ('Makedonska Kamenica'),
       ('Vinica'),
       ('Valandovo'),
       ('Krusevo'),
       ('Radovish'),
       ('Kriva Palanka'),
       ('Berovo'),
       ('Demir kapija'),
       ('Kratovo'),
       ('Demir hisar'),
       ('Dojran');

insert into public.authorities(authority)
VALUES ('ROLE_USER');
insert into public.authorities(authority)
VALUES ('ROLE_ADMIN');

insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email@email.com', 'John', 'M', 'Smith', 'password', '071711033', 1, NULL);

insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email2@email.com', 'Jane', 'F', 'Doe', 'password', '45282138', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email3@email.com', 'Thierry', 'M', 'Henry', 'password', '357429566', 1, NULL);

insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email4@email.com', 'Renath', 'M', 'Minerou', 'password', '071711033', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email5@email.com', 'Millow', 'M', 'Hobs', 'password', '071711033', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email6@email.com', 'Nathan', 'M', 'Bowie', 'password', '071711033', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email7@email.com', 'John', 'M', 'Lennon', 'password', '071711033', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email8@email.com', 'Jennifer', 'F', 'Lopez', 'password', '071711033', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email9@email.com', 'Danielle', 'F', 'Rossi', 'password', '071711033', 1, NULL);
insert into public.users(birth_date, username, first_name, gender, last_name, password, phone_number, authority_id,
                         profile_photo)
VALUES ('2020-02-11 22:00:06', 'email10@email.com', 'Adrianna', 'F', 'Lima', 'password', '071711033', 1, NULL);

INSERT INTO public.cars(brand, model, total_seats, year_manufacture, owner_id)
VALUES ('Toyota', 'RAV4', 5, 2017, 1),
       ('BMW', 'X6', 5, 2009, 2),
       ('Volvo', 'xc90', 5, 2015, 3),
       ('Ford', 'Fiesta', 4, 2017, 4),
       ('Ford', 'Mondeo', 5, 2004, 5),
       ('Volkswagen', 'Golf 4', 5, 2001, 6),
       ('Volkswagen', 'UP', 4, 2018, 7),
       ('Audi', 'Q7', 5, 2017, 8),
       ('Mercedes', 'C350', 5, 2013, 9),
       ('Honda', 'Civic', 5, 2006, 10);


INSERT INTO public.rides(description, created_on, departure_time, price_per_head, total_seats_offered, to_location,
                         driver_id, from_location, status, has_air_condition, is_pet_allowed, is_smoking_allowed,
                         max_two_backseat)
VALUES ('test-description', '2020-02-12 19:50:55', '2021-02-12 19:50:57', 100, 5, 1, 1, 2, 'ACTIVE', true, false, true,
        true),
       ('test-ride-2', '2020-02-12 21:04:51', '2021-02-12 21:04:55', 150, 3, 1, 2, 1, 'ACTIVE', true, true, false,
        false);

INSERT INTO public.ride_requests(created_on, status, requester_id, ride_id, requested_seats)
VALUES ('2020-02-12 19:50:27', 'PENDING', 1, 1, 1),
       ('2020-02-12 19:50:27', 'PENDING', 1, 1, 2),
       ('2020-02-12 19:50:27', 'PENDING', 1, 2, 3);

INSERT INTO public.ratings(date_submitted, note, rating, ride_request_id, rated_user)
VALUES ('2020-02-12 21:01:04', 'dobra voznja', 5, 1, 1),
       ('2020-02-12 21:01:15', 'mnogu loso', 1, 3, 1),
       ('2020-02-12 21:04:36', 'Meh,sredno', 3, 2, 2);








