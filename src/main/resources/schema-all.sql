DROP TABLE transactions IF EXISTS;

CREATE TABLE transactions  (
    id INTEGER NOT NULL PRIMARY KEY,
    buyer VARCHAR NOT NULL,
    store VARCHAR NOT NULL,
    item VARCHAR NOT NULL,
    price VARCHAR NOT NULL,
);
