CREATE TABLE Inventory (
    id varchar(38) PRIMARY KEY,
    productId varchar(38) NOT NULL,
    quantity INTEGER NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INTEGER NOT NULL
);
