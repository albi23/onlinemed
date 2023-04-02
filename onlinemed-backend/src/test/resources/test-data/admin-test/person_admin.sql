-- security
INSERT INTO public.security (id, timestamp, version, hash, securitytoken, token)
VALUES ('29e89905-1768-49a4-90da-035433d20e1f', '2023-04-01 18:58:49.205472', 1, '$argon2id$v=19$m=4096,t=3,p=4$jf5ytsNoJ5OmxU5X3++UWw$R2ak2qEZQaJ+sfByIuITyHMs6IhbcHk3zlBle07tH14', null, null);

-- doctor info
INSERT INTO public.doctor_info (id, timestamp, version, doctortype, phonenumber, specialization) VALUES ('5b64cd2d-60c8-429b-b749-12f8669af825', '2023-04-01 18:58:49.206389', 1, 'Radiologist', '458689325', 'Specialization 15');

-- person
INSERT INTO public.person (id, timestamp, version, defaultlanguage, email, name, phonenumber, surname, username, security_id)
VALUES ('91839139-1b50-4b59-a31a-f636dc7df38e', '2023-04-01 18:58:49.208681', 1, 'en_GB', 'AdminSurname@niepodam.pl', 'Admin', '+48696878959', 'Surname', 'admin', '29e89905-1768-49a4-90da-035433d20e1f');

-- facility location
INSERT INTO public.facility_location (id, timestamp, version, facilityaddress, facilityname, doctorinfo_id) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '2023-04-01 18:58:49.208278', 1, 'Some Street 5, Some City51', 'Doctors Clinic nr. 13', '5b64cd2d-60c8-429b-b749-12f8669af825');
INSERT INTO public.facility_location (id, timestamp, version, facilityaddress, facilityname, doctorinfo_id) VALUES ('df1223d3-c1c3-4d34-b71c-f15fe81a78c9', '2023-04-01 18:58:49.208466', 1, 'Some Street 24, Some City96', 'Doctors Clinic nr. 18', '5b64cd2d-60c8-429b-b749-12f8669af825');

-- community
INSERT INTO public.community (id, register_date, version, comments, description, lastlogin)
VALUES ('91839139-1b50-4b59-a31a-f636dc7df38e', '2021-07-18 00:57:22.099000', 0, 9, ' Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nibh nulla, pretium at posuere quis, sagittis ac purus. Morbi congue dui eget neque iaculis consectetur. Quisque non risus massa. Phasellus quis augue eget tellus sagittis dictum. Curabitur pellentesque magna enim, vel finibus urna consequat at. In ac neque pretium, varius ex ut, maximus sapien. Donec ac dignissim erat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus sagittis libero non metus scelerisque rutrum. Pellentesque et neque molestie, commodo nisl nec, aliquet turpis. Nullam efficitur hendrerit mauris, vel placerat ipsum', '2021-07-23 00:57:22.099000');

-- functionalities
INSERT INTO public.functionality (id, timestamp, version, name) VALUES ('762a8721-fef8-4e45-b8bb-6e46189d9570', '2023-04-01 18:58:49.183571', 0, 'user-management');
INSERT INTO public.functionality (id, timestamp, version, name) VALUES ('dd649b8f-79d8-47e3-835c-7a46824e8ffb', '2023-04-01 18:58:49.770788', 6, 'drug-equivalents');
INSERT INTO public.functionality (id, timestamp, version, name) VALUES ('c0fad902-904f-463c-ba42-49da64b93c9f', '2023-04-01 18:58:50.069666', 17, 'profile');
INSERT INTO public.functionality (id, timestamp, version, name) VALUES ('0c0be044-0a46-4d4c-a289-3a6f06fcb1bb', '2023-04-01 18:58:50.069739', 17, 'doctors-profile');
INSERT INTO public.functionality (id, timestamp, version, name) VALUES ('34fc2337-8e71-4809-bb19-f318882dd19d', '2023-04-01 18:58:50.069763', 17, 'notifications');
INSERT INTO public.functionality (id, timestamp, version, name) VALUES ('61d3c9cc-887c-4b0f-888d-b4e8b31a576e', '2023-04-01 18:58:50.069786', 17, 'forum');

-- roles
INSERT INTO public.role (id, timestamp, version, roletype) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', '2023-04-01 18:58:49.211695', 1, 3);
INSERT INTO public.role (id, timestamp, version, roletype) VALUES ('ed984f7b-dfb4-45cc-8af6-ac747f20f4ac', '2023-04-01 18:58:49.387282', 1, 1);
INSERT INTO public.role (id, timestamp, version, roletype) VALUES ('425f06bc-d1f8-43ca-970c-b9c73b008858', '2023-04-01 18:58:49.770845', 1, 2);
INSERT INTO public.role (id, timestamp, version, roletype) VALUES ('0905500f-172d-494e-a003-09b25ef6e565', '2023-04-01 18:58:50.069701', 1, 0);

-- role x functionalities
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', '762a8721-fef8-4e45-b8bb-6e46189d9570');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', 'dd649b8f-79d8-47e3-835c-7a46824e8ffb');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', 'c0fad902-904f-463c-ba42-49da64b93c9f');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', '0c0be044-0a46-4d4c-a289-3a6f06fcb1bb');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', '34fc2337-8e71-4809-bb19-f318882dd19d');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34', '61d3c9cc-887c-4b0f-888d-b4e8b31a576e');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('ed984f7b-dfb4-45cc-8af6-ac747f20f4ac', 'c0fad902-904f-463c-ba42-49da64b93c9f');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('ed984f7b-dfb4-45cc-8af6-ac747f20f4ac', '0c0be044-0a46-4d4c-a289-3a6f06fcb1bb');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('ed984f7b-dfb4-45cc-8af6-ac747f20f4ac', '34fc2337-8e71-4809-bb19-f318882dd19d');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('ed984f7b-dfb4-45cc-8af6-ac747f20f4ac', '61d3c9cc-887c-4b0f-888d-b4e8b31a576e');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('425f06bc-d1f8-43ca-970c-b9c73b008858', 'dd649b8f-79d8-47e3-835c-7a46824e8ffb');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('425f06bc-d1f8-43ca-970c-b9c73b008858', 'c0fad902-904f-463c-ba42-49da64b93c9f');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('425f06bc-d1f8-43ca-970c-b9c73b008858', '0c0be044-0a46-4d4c-a289-3a6f06fcb1bb');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('425f06bc-d1f8-43ca-970c-b9c73b008858', '61d3c9cc-887c-4b0f-888d-b4e8b31a576e');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('425f06bc-d1f8-43ca-970c-b9c73b008858', '34fc2337-8e71-4809-bb19-f318882dd19d');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('0905500f-172d-494e-a003-09b25ef6e565', 'c0fad902-904f-463c-ba42-49da64b93c9f');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('0905500f-172d-494e-a003-09b25ef6e565', '0c0be044-0a46-4d4c-a289-3a6f06fcb1bb');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('0905500f-172d-494e-a003-09b25ef6e565', '34fc2337-8e71-4809-bb19-f318882dd19d');
INSERT INTO public.role_x_functionality (role_id, functionality_id) VALUES ('0905500f-172d-494e-a003-09b25ef6e565', '61d3c9cc-887c-4b0f-888d-b4e8b31a576e');

-- person x roles
INSERT INTO public.person_x_role (person_id, role_id) VALUES ('91839139-1b50-4b59-a31a-f636dc7df38e', 'e4f437a5-bc1d-4cf3-b759-fc1ea23ecb34');
INSERT INTO public.person_x_role (person_id, role_id) VALUES ('91839139-1b50-4b59-a31a-f636dc7df38e', 'ed984f7b-dfb4-45cc-8af6-ac747f20f4ac');
INSERT INTO public.person_x_role (person_id, role_id) VALUES ('91839139-1b50-4b59-a31a-f636dc7df38e', '425f06bc-d1f8-43ca-970c-b9c73b008858');
INSERT INTO public.person_x_role (person_id, role_id) VALUES ('91839139-1b50-4b59-a31a-f636dc7df38e', '0905500f-172d-494e-a003-09b25ef6e565');

-- calendar events
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('a635fa75-8f3d-4e9c-9315-68b084ea4d66', '2023-04-03 09:49:34.046000', 0, '2023-04-03 10:19:34.046000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit1');
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('17246bfd-2018-452d-a4b3-556b53ac6c31', '2023-04-04 14:58:33.963000', 0, '2023-04-04 15:28:33.963000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit2');
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('ecef1223-20c9-41d2-8143-1ccae8029b31', '2023-04-04 11:00:53.518000', 0, '2023-04-04 11:30:53.518000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit3');
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('b79c3d03-008c-4976-a256-51ee15ec26a6', '2023-04-01 17:12:57.355000', 0, '2023-04-01 17:42:57.355000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit4');
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('c4f4f4f0-be39-4f65-a871-49141332cc89', '2023-03-31 14:27:43.569000', 0, '2023-03-31 14:57:43.569000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit5');
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('63035a5b-6afa-4b4b-8881-fecb7b158cad', '2023-04-06 12:50:47.896000', 0, '2023-04-06 13:20:47.896000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit6');
INSERT INTO public.calendar_event (id, start_date, version, enddate, person_id, title) VALUES ('fb88a6a8-4023-445d-abcd-bc2d2b3addd9', '2023-04-02 11:24:02.401000', 0, '2023-04-02 11:54:02.401000', '91839139-1b50-4b59-a31a-f636dc7df38e', 'Visit7');

-- visits
INSERT INTO public.visit (id, timestamp, version, localisation, optionalmessage, visitdate, visitstate, visittype) VALUES ('fcd15c29-e736-457e-9752-37d06d714eb3', '2023-04-01 18:58:49.166502', 0, 'Doctors Clinic nr. 19', null, '2023-04-08 14:16:37.806000', 0, 'Visit Type B3');
INSERT INTO public.visit (id, timestamp, version, localisation, optionalmessage, visitdate, visitstate, visittype) VALUES ('a5005478-358d-43a4-bf44-e450a73992b3', '2023-04-01 18:58:49.170350', 0, 'Doctors Clinic nr. 4', null, '2023-04-07 11:51:59.901000', 0, 'Visit Type B1');
INSERT INTO public.visit (id, timestamp, version, localisation, optionalmessage, visitdate, visitstate, visittype) VALUES ('c967d754-32b0-4bea-83ea-7984aa677c68', '2023-04-01 18:58:49.173479', 0, 'Doctors Clinic nr. 17', null, '2023-04-08 17:02:14.720000', 0, 'Visit Type A6');
INSERT INTO public.visit (id, timestamp, version, localisation, optionalmessage, visitdate, visitstate, visittype) VALUES ('c4c689bc-db23-4d78-acb8-c0c078ef64e9', '2023-04-01 18:58:49.176466', 0, 'Doctors Clinic nr. 2', null, '2023-04-07 11:35:39.785000', 0, 'Visit Type B6');

-- notifications
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('d81caa47-0f2a-488e-b19d-c0f4b1833fa1', '2023-04-01 18:58:49.159255', 0, 'Pratt', 0, 'e718172a-ef10-451e-8637-b0137136fb43', 'Husk', null, '91839139-1b50-4b59-a31a-f636dc7df38e');
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('7d9988f0-4443-42c6-b71b-c5650b5df4ed', '2023-04-01 18:58:49.161505', 0, 'John', 0, '1b012129-33c5-42b1-9ca8-4f1c5398833b', 'Smith', null, '91839139-1b50-4b59-a31a-f636dc7df38e');
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('8718d375-7405-4334-a78b-78d86acf8c65', '2023-04-01 18:58:49.167228', 0, 'James', 2, '2a60e493-4e0a-4e88-84ee-d97ca7432920', 'Acker', 'fcd15c29-e736-457e-9752-37d06d714eb3', '91839139-1b50-4b59-a31a-f636dc7df38e');
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('c8a71341-3e73-4852-a8b3-67b91e1739f8', '2023-04-01 18:58:49.170620', 0, 'Duston', 3, '9565d8c5-46d2-4549-b549-0cb13616d7da', 'Gray', 'a5005478-358d-43a4-bf44-e450a73992b3', '91839139-1b50-4b59-a31a-f636dc7df38e');
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('90817260-ffea-4e8d-87f1-34dc2e37e04e', '2023-04-01 18:58:49.173606', 0, 'Rob', 4, '66d594a7-97b0-4a2a-aff7-bbf7c4146d0b', 'Ida', 'c967d754-32b0-4bea-83ea-7984aa677c68', '91839139-1b50-4b59-a31a-f636dc7df38e');
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('5f9bfcce-312a-4b30-9e88-82fcc8512abd', '2023-04-01 18:58:49.176647', 0, 'Rain', 0, '9dd9462e-8041-43e8-a30f-bbe00a236c25', 'Eagle', 'c4c689bc-db23-4d78-acb8-c0c078ef64e9', '91839139-1b50-4b59-a31a-f636dc7df38e');
INSERT INTO public.notification (id, timestamp, version, name, notificationtype, senderid, surname, visit_id, person_id)
VALUES ('86d8845b-d48e-4057-b6ad-b238bdaf21df', '2023-04-01 18:58:49.178247', 0, 'Quixley', 0, '15fa2a40-645b-42c6-aa78-7b7deed7c4a6', 'Eskins', null, '91839139-1b50-4b59-a31a-f636dc7df38e');

-- person x doctor info
INSERT INTO public.person_x_doctor_info (doctor_info_id, person_id) VALUES ('5b64cd2d-60c8-429b-b749-12f8669af825', '91839139-1b50-4b59-a31a-f636dc7df38e');

-- price values
INSERT INTO public.price_values (facility_location_id, price_value, field_key) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '200', 'Visit Type B4');
INSERT INTO public.price_values (facility_location_id, price_value, field_key) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '900', 'Visit Type B3');
INSERT INTO public.price_values (facility_location_id, price_value, field_key) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '400', 'Visit Type B2');
INSERT INTO public.price_values (facility_location_id, price_value, field_key) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '500', 'Visit Type B1');
INSERT INTO public.price_values (facility_location_id, price_value, field_key) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '200', 'Visit Type B6');
INSERT INTO public.price_values (facility_location_id, price_value, field_key) VALUES ('61fa1dea-f014-456f-8fee-c76cdb0c53bb', '500', 'Visit Type B5');