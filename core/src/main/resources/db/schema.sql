CREATE TABLE if not exists rabbit_message_dto
(
    id          BIGINT NOT NULL,
    type        VARCHAR(255),
    description VARCHAR(255),
    CONSTRAINT pk_rabbit_message_dto PRIMARY KEY (id)
);
