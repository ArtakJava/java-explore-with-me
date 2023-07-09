create table if not exists public.categories (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

create table if not exists public.users (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    email varchar(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email),
    CONSTRAINT UQ_USER_NAME UNIQUE (name)
);

create table if not exists public.events (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation varchar(2048) NOT NULL,
    category_id bigint NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description varchar(8192) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id bigint NOT NULL,
    lat float NOT NULL,
    lon float NOT NULL,
    paid boolean NOT NULL,
    participant_limit int NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean NOT NULL,
    state varchar(16) NOT NULL,
    title varchar(128) NOT NULL,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
    CONSTRAINT fk_events_to_users FOREIGN KEY(user_id) REFERENCES users(id)
);

create table if not exists public.requests (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    requester_id bigint NOT NULL,
    event_id bigint NOT NULL,
    state varchar(128) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id)
);

create table if not exists public.compilations (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar(128) NOT NULL,
    pinned boolean NOT NULL
);

create table if not exists public.compilations_events (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id bigint NOT NULL CONSTRAINT compilations_events_event_fkey REFERENCES events,
    compilation_id bigint NOT NULL CONSTRAINT compilations_events_compilation_fkey REFERENCES compilations
);