CREATE ROLE order_svc     LOGIN PASSWORD 'order_svc';
CREATE ROLE saga_svc      LOGIN PASSWORD 'saga_svc';
CREATE ROLE inventory_svc LOGIN PASSWORD 'inventory_svc';
CREATE ROLE payment_svc   LOGIN PASSWORD 'payment_svc';
CREATE ROLE ticket_svc    LOGIN PASSWORD 'ticket_svc';

CREATE DATABASE order_db     OWNER order_svc;
CREATE DATABASE saga_db      OWNER saga_svc;
CREATE DATABASE inventory_db OWNER inventory_svc;
CREATE DATABASE payment_db   OWNER payment_svc;
CREATE DATABASE ticket_db    OWNER ticket_svc;

REVOKE CONNECT ON DATABASE order_db     FROM PUBLIC;
REVOKE CONNECT ON DATABASE saga_db      FROM PUBLIC;
REVOKE CONNECT ON DATABASE inventory_db FROM PUBLIC;
REVOKE CONNECT ON DATABASE payment_db   FROM PUBLIC;
REVOKE CONNECT ON DATABASE ticket_db    FROM PUBLIC;
