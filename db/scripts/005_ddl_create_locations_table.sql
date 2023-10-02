CREATE TABLE IF NOT EXISTS public.locations
(
    id        integer                NOT NULL DEFAULT nextval('locations_id_seq'::regclass) PRIMARY KEY UNIQUE,
    latitude  double precision       NOT NULL,
    longitude double precision       NOT NULL,
    name      character varying(255) NOT NULL,
    userid    integer                NOT NULL
)