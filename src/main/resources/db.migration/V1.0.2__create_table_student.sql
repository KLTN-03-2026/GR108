CREATE TABLE student
(
    id          INT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255)       NULL,
    mssv        VARCHAR(255)       NOT NULL,
    phone       VARCHAR(255)       NULL,
    address     VARCHAR(255)       NULL,
    dateOfBirth date               NULL,
    dateOfEntry date               NULL,
    account_id  INT                NOT NULL,
    room_id     INT                NOT NULL,
    status      VARCHAR(255)       NULL,
    CONSTRAINT pk_student PRIMARY KEY (id)
);

ALTER TABLE student
    ADD CONSTRAINT uc_student_account UNIQUE (account_id);

ALTER TABLE student
    ADD CONSTRAINT uc_student_mssv UNIQUE (mssv);

ALTER TABLE student
    ADD CONSTRAINT FK_STUDENT_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES ACCOUNT (id);

ALTER TABLE student
    ADD CONSTRAINT FK_STUDENT_ON_ROOM FOREIGN KEY (room_id) REFERENCES room (id);