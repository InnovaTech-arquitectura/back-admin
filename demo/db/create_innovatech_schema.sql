-- Crear el esquema si no existe
DROP SCHEMA IF EXISTS innovatech CASCADE;
CREATE SCHEMA innovatech;

-- Crear tablas dentro del esquema innovatech
CREATE TABLE innovatech.administrative_employee (
    id SERIAL PRIMARY KEY,
    id_user INTEGER REFERENCES innovatech.user(id)
);

CREATE TABLE innovatech.role (
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE innovatech.user (
    id SERIAL PRIMARY KEY,
    id_card INTEGER,
    name VARCHAR,
    email VARCHAR UNIQUE,
    password VARCHAR,
    id_rol INTEGER REFERENCES innovatech.role(id)
);

CREATE TABLE innovatech.plan (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    price REAL,
    id_plan_payment INTEGER UNIQUE
);

CREATE TABLE innovatech.coupon (
    id SERIAL PRIMARY KEY,
    id_entrepreneurship INTEGER REFERENCES innovatech.entrepreneurship(id),
    description VARCHAR,
    discount DOUBLE PRECISION,
    code VARCHAR
);

CREATE TABLE innovatech.client (
    id SERIAL PRIMARY KEY,
    id_user INTEGER REFERENCES innovatech.user(id),
    id_card VARCHAR
);

CREATE TABLE innovatech.entrepreneurship (
    id SERIAL PRIMARY KEY,
    id_plan INTEGER REFERENCES innovatech.plan(id),
    id_user INTEGER REFERENCES innovatech.user(id),
    name VARCHAR,
    logo VARCHAR,
    nit INTEGER,
    description VARCHAR
);

CREATE TABLE innovatech."order" (
    id SERIAL PRIMARY KEY,
    id_client INTEGER REFERENCES innovatech.client(id),
    id_payment INTEGER REFERENCES innovatech.payment(id),
    id_city INTEGER REFERENCES innovatech.city(id),
    id_state INTEGER REFERENCES innovatech.state(id),
    sale_number VARCHAR,
    additional_info VARCHAR,
    address VARCHAR
);

CREATE TABLE innovatech.product (
    id SERIAL PRIMARY KEY,
    id_entrepreneurship INTEGER REFERENCES innovatech.entrepreneurship(id),
    quantity INTEGER,
    price DOUBLE PRECISION,
    cost DOUBLE PRECISION,
    picture VARCHAR,
    description VARCHAR,
    id_color INTEGER REFERENCES innovatech.color(id),
    id_size INTEGER REFERENCES innovatech.size(id)
);

CREATE TABLE innovatech.supplier (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    description VARCHAR,
    contact_number VARCHAR
);

CREATE TABLE innovatech.review (
    id SERIAL PRIMARY KEY,
    id_product INTEGER REFERENCES innovatech.product(id),
    id_service INTEGER REFERENCES innovatech.service(id),
    content VARCHAR,
    score INTEGER,
    id_client INTEGER REFERENCES innovatech.client(id),
    title VARCHAR
);

CREATE TABLE innovatech.service (
    id SERIAL PRIMARY KEY,
    id_inventory INTEGER,
    id_entrepreneurship INTEGER REFERENCES innovatech.entrepreneurship(id),
    id_review INTEGER REFERENCES innovatech.review(id),
    name VARCHAR,
    price DOUBLE PRECISION,
    quantity INTEGER,
    picture VARCHAR,
    description VARCHAR
);

CREATE TABLE innovatech.category (
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE innovatech.tag (
    id SERIAL PRIMARY KEY,
    id_order INTEGER REFERENCES innovatech."order"(id),
    id_product INTEGER REFERENCES innovatech.product(id),
    tag VARCHAR
);

CREATE TABLE innovatech.city (
    id SERIAL PRIMARY KEY,
    id_state INTEGER REFERENCES innovatech.state(id),
    name VARCHAR
);

CREATE TABLE innovatech.state (
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE innovatech.color (
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE innovatech.size (
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE innovatech.subscription (
    id SERIAL PRIMARY KEY,
    id_entrepreneurship INTEGER REFERENCES innovatech.entrepreneurship(id),
    id_plan INTEGER REFERENCES innovatech.plan(id),
    initial_date DATE,
    expiration_date DATE,
    amount DOUBLE PRECISION
);

CREATE TABLE innovatech.payment (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION,
    date DATE,
    responsible_entity VARCHAR,
    id_card INTEGER
);

CREATE TABLE innovatech.payment_card (
    id SERIAL PRIMARY KEY,
    card_number VARCHAR,
    name VARCHAR,
    responsible_entity VARCHAR,
    expiration_date DATE
);

CREATE TABLE innovatech.order_state (
    id SERIAL PRIMARY KEY,
    state VARCHAR
);

CREATE TABLE innovatech.engagement (
    id SERIAL PRIMARY KEY,
    id_publication INTEGER REFERENCES innovatech.publication(id),
    interaction INTEGER
);

CREATE TABLE innovatech.publication (
    id SERIAL PRIMARY KEY,
    title VARCHAR,
    description VARCHAR,
    multimedia VARCHAR,
    administrative_employee INTEGER REFERENCES innovatech.administrative_employee(id)
);

CREATE TABLE innovatech.enquiry (
    id SERIAL PRIMARY KEY,
    id_entrepreneurship INTEGER REFERENCES innovatech.entrepreneurship(id),
    field VARCHAR,
    question VARCHAR
);

CREATE TABLE innovatech.response (
    id SERIAL PRIMARY KEY,
    id_admin_employee INTEGER REFERENCES innovatech.administrative_employee(id),
    id_enquiry INTEGER REFERENCES innovatech.enquiry(id),
    response VARCHAR
);

CREATE TABLE innovatech.product_supplier (
    id SERIAL PRIMARY KEY,
    id_product INTEGER REFERENCES innovatech.product(id),
    id_supplier INTEGER REFERENCES innovatech.supplier(id)
);

CREATE TABLE innovatech.entrepreneurship_course (
    id SERIAL PRIMARY KEY,
    id_entrepreneurship INTEGER REFERENCES innovatech.entrepreneurship(id),
    id_course INTEGER REFERENCES innovatech.course(id)
);

CREATE TABLE innovatech.course (
    id SERIAL PRIMARY KEY,
    content VARCHAR,
    description VARCHAR,
    date DATE,
    score INTEGER
);

CREATE TABLE innovatech.functionality (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    description VARCHAR
);

CREATE TABLE innovatech.plan_functionality (
    id SERIAL PRIMARY KEY,
    id_plan INTEGER REFERENCES innovatech.plan(id),
    id_functionality INTEGER REFERENCES innovatech.functionality(id)
);
