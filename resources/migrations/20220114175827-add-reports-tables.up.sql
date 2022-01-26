create table reports (
  id SERIAL primary key,
  user_id INTEGER references users(id),
  comment text,
  mood_grade INTEGER check (mood_grade > 0 and mood_grade <= 10),
  activity_grade INTEGER check (activity_grade > 0 and activity_grade <= 10),
  happiness_grade INTEGER check (happiness_grade > 0 and happiness_grade <= 10),
  details json,
  report_date TIMESTAMP not null default current_timestamp,
  added_at TIMESTAMP not null default current_timestamp,
  details_schema_id integer references users (id)
);
--;;
create table details_schema (
	id serial primary key,
  detail_items_ids integer[],
  description text,
  created_by integer references users(id)
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
	nested_ids integer[],
  schema jsonb
);
