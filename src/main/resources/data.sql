insert into ROLE (id , name) values (1 , 'ROLE_ADMIN');
insert into ROLE (id , name) values (2 , 'ROLE_USER') ;

insert into APP_USER (id , username, password, enabled) values (1l , 'admin', '123',true);
insert into USER_ROLES (USER_ID , ROLE_ID) values (1 , 1);
--
insert into APP_USER (id , username, password, enabled) values (2 , 'user', '123',true);
insert into USER_ROLES (USER_ID , ROLE_ID) values (2 , 2);