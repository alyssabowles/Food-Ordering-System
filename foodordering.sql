create database if not exists foodordering;
use foodordering;

create table if not exists car(
	car_num int not null,
	driver_id int not null,
	primary key (car_num));
	
insert into car(car_num, driver_id)
values(328, 387),
	  (123, 478),
	  (921, 112),
	  (888, 190),
	  (726, 220),
	  (113, 189),
	  (567, 778),
	  (278, 898),
	  (450, 156),
	  (900, 836);
	
create table if not exists driver(
	driver_id int not null,
	name varchar(255) not null,
	hours_worked int not null, 
	start_date date not null,
	primary key(driver_id));

insert into driver(driver_id, name, hours_worked, start_date)
values (387, 'Zack Smith', 30, '2020-10-10'),
	   (478, 'James White', 280, '2020-07-01'),
	   (112, 'Robert Johnson', 155, '2020-08-15'),
	   (190, 'Mary Jones', 53, '2020-09-20'),
	   (220, 'Jennifer Miller', 98, '2020-07-30'),
	   (189, 'Taylor Thompson', 105, '2020-06-17'),
	   (778, 'George Harris', 19, '2020-10-15'),
	   (898, 'Edward Walker', 470, '2020-05-09'),
	   (156, 'Ryan Allen', 80, '2020-08-25'),
	   (836, 'Rebecca Jackson', 186, '2020-06-19');

create table if not exists customer(
	customer_id int not null,
	name varchar(255) not null,
	address varchar(255) not null,
	phone_number varchar(255),
	primary key(customer_id));
	
insert into customer(customer_id, name, address, phone_number)
values (530, 'Martin Hall', '7680 Washington Drive', '7389190002'),
	   (120, 'Emily Carter', '7723 Jones Road', '2831119203'),
	   (790, 'Lawrence Wright', '286 Creek Street', '8267480982'),
	   (300, 'Nick Rogers', '918 Addison Street', '3528793389'),
	   (901, 'Michael Morris', '441 Sherman Road', '6061548907'),
	   (500, 'Justin Stuart', '8893 Rock Creek Avenue', '1027892608'),
	   (432, 'Morgan Gray', '9543 Maple Road', '8908234456'),
	   (181, 'Kelly Coleman', '775 Boston Street', '3142486673'),
	   (672, 'Dennis Bryant', '879 William Street', '6712398879'),
	   (770, 'Aaron Hughes', '8311 Henry Drive', '2730103038');

create table if not exists menu_item(
	food_id int not null,
	name varchar(255) not null,
	price decimal(10,2) not null,
	primary key(food_id));
	
insert into menu_item(food_id, name, price)
values (001, 'Hamburger', 3.00),
       (002, 'Cheeseburger', 4.00),
	   (003, 'Bacon cheeseburger', 5.00),
	   (004, 'Fries', 2.50),
	   (005, 'Soda bottle', 1.50);

create table if not exists food(
	order_id int not null,
	menu_id int not null,
	quantity int not null,
	primary key(order_id, menu_id));

insert into food(order_id, menu_id, quantity)
values (1112, 002, 2),
       (1112, 005, 2),
	   (1113, 001, 2),
	   (1114, 002, 1),
	   (1114, 003, 1),
	   (1114, 004, 2),
	   (1115, 002, 4),
	   (1115, 004, 2),
	   (1116, 001, 1),
	   (1116, 002, 1),
	   (1117, 005, 1),
	   (1118, 001, 3),
	   (1118, 002, 3),
	   (1118, 003, 3),
	   (1118, 004, 5),
	   (1119, 003, 1),
	   (1119, 005, 1),
	   (1120, 004, 2),
	   (1121, 001, 1),
	   (1121, 002, 2);
	
create table if not exists orders(
	order_id int not null,
	purchase_date date not null,
	customer varchar(255) not null,
	car_num int not null,
	credit_card varchar(255) not null,
	price decimal(10,2) not null,
	primary key(order_id));

insert into orders(order_id, purchase_date, customer, car_num, credit_card, price)
values (1112, '2020-6-29', 530, 328, '4539425140455263', 11.00),
	   (1113, '2020-6-29', 120, 328, '7164562074522296', 6.00),
	   (1114, '2020-7-7', 790, 328, '4916532565327951', 14.00),
	   (1115, '2020-7-18', 300, 921, '6011286135523032', 21.00),
	   (1116, '2020-9-1', 901, 921, '6011289841740312', 7.00),
	   (1117, '2020-9-25', 500, 567, '6011989770570837', 1.50),
	   (1118, '2020-10-20', 432, 450, '5132597552148418', 48.50),
	   (1119, '2020-10-21', 181, 450, '5599102013855411', 6.50),
	   (1120, '2020-10-25', 672, 328, '5467642467167957', 16.00),
	   (1121, '2020-10-28', 770, 888, '5598272219821834', 11.00);
	   
create table if not exists credit_card(
	card_number varchar(255) not null, 
	cvv int not null,
	exp_date varchar(255) not null,
	cust_id int not null,
	primary key(card_number));

insert into credit_card(card_number, cvv, exp_date, cust_id)
values ('4539425140455263', 412, '09/23', 120),	
       ('7164562074522296', 892, '01/21', 181),
       ('4916532565327951', 956, '11/22', 300),
       ('6011286135523032', 194, '03/24', 432),
       ('6011289841740312', 544, '05/25', 500),
       ('6011989770570837', 313, '06/22', 530),
       ('5132597552148418', 687, '12/20', 672),
       ('5599102013855411', 441, '05/23', 770),
       ('5467642467167957', 700, '08/24', 790),
       ('5598272219821834', 332, '10/21', 901);