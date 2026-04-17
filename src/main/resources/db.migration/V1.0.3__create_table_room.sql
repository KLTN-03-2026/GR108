CREATE TABLE room
(
    id               INT AUTO_INCREMENT NOT NULL,
    roomNumber       VARCHAR(255)       NOT NULL,
    type             VARCHAR(255)       NULL,
    capacity         INT                NULL,
    currentOccupancy INT                NULL,
    price            DECIMAL            NULL,
    `description`    VARCHAR(255)       NULL,
    status           VARCHAR(255)       NULL,
    CONSTRAINT pk_room PRIMARY KEY (id)
);

ALTER TABLE room
    ADD CONSTRAINT uc_room_roomnumber UNIQUE (roomNumber);