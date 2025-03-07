create table IF NOT EXISTS author (
        id bigint generated by default as identity,
        fio varchar(255),
        primary key (id)
    );

CREATE TEMPORARY TABLE temp_budget AS
SELECT
    b.id,
    b.mount,
    b.year,
    b.amount,
    b.budget_type,
    NULL::bigint as author_id,
    NULL::timestamp(6) as create_data
FROM budget b;

DROP TABLE budget;

ALTER TABLE temp_budget RENAME TO budget;

ALTER TABLE budget
ADD CONSTRAINT budget_pkey PRIMARY KEY (id);

ALTER TABLE budget
ADD CONSTRAINT budget_type_check
CHECK (budget_type in ('credit','debit'));

ALTER TABLE budget
ALTER COLUMN amount SET NOT NULL;
ALTER TABLE budget
ALTER COLUMN mount SET NOT NULL;
ALTER TABLE budget
ALTER COLUMN year SET NOT NULL;
ALTER TABLE budget
ALTER COLUMN budget_type SET NOT NULL;