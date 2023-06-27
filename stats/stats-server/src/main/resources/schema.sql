create table if not exists public.endpoint_hits (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app varchar(255) NOT NULL,
    uri varchar(512) NOT NULL,
    ip varchar(255) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);