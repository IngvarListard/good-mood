create table reports (
  id SERIAL primary key,
  user_id INTEGER references users(id),
  comment text,
  mood_grade INTEGER check (mood_grade > 0 and mood_grade <= 10),
  activity_grade INTEGER check (activity_grade > 0 and activity_grade <= 10),
  happiness_grade INTEGER check (happiness_grade > 0 and happiness_grade <= 10),
  details json,
  report_date TIMESTAMP not null default current_timestamp,
  added_at TIMESTAMP not null default current_timestamp
);
--;;
create table details_schema (
	id serial primary key,
	user_id integer references users(id)
);
--;;
create table detail_type (
	id serial primary key,
	name text,
	display_name text
);
--;;
create table detail_item (
	id serial primary key,
	detail_type_id integer references detail_type (id),
	fields_names text[],
	fields_types text[],
	nested_ids integer[]
);
