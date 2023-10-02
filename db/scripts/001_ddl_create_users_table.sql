CREATE TABLE IF NOT EXISTS public.users
(
    id       integer                NOT NULL DEFAULT nextval('users_id_seq'::regclass) UNIQUE PRIMARY KEY,
    username character varying(255) NOT NULL UNIQUE,
    password character varying(255) NOT NULL
)