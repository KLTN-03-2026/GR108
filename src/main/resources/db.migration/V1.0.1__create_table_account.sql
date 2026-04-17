create  table Account
(
    id   INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(50) NULL,
    password VARCHAR(100) NULL,
    email    VARCHAR(100) NULL,
    role   VARCHAR(20) NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);