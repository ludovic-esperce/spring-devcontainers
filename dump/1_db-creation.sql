
DROP TABLE if exists public.user CASCADE;
DROP TABLE if exists public.account CASCADE;

CREATE TABLE "client" (
	id uuid PRIMARY KEY,
	first_name VARCHAR(100),
	last_name VARCHAR(100),
	birthdate date,
	email VARCHAR(50)
);

CREATE TABLE account (
	id SERIAL PRIMARY KEY,
	creation_time timestamp,
	balance bigint,
	active boolean,
	user_id uuid,
	CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES "client"(id)
);

