
DROP DATABASE IF EXISTS dictionary;
CREATE DATABASE dictionary;

USE dictionary;

# SET SESSION FOREIGN_KEY_CHECKS=0;

/* Create Tables */

CREATE TABLE dictionary_category
(
    id varchar(32) NOT NULL,
    name varchar(32) NOT NULL,
    description varchar(512),
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE dictionary_word
(
    category varchar(32) NOT NULL,
    -- 0,100,200,300 整百数为其它
    value int NOT NULL COMMENT '0,100,200,300 整百数为其它',
    label varchar(64) NOT NULL,
    pin varchar(64),
    seq int NOT NULL,
    report_value varchar(32) NOT NULL,
    description varchar(512),
    _group varchar(32),
    enabled boolean DEFAULT 1 NOT NULL,
    PRIMARY KEY (category, value)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE district
(
    id int NOT NULL,
    level int NOT NULL,
    name varchar(32) NOT NULL,
    py_code varchar(32) NOT NULL,
    address varchar(128),
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;



/* Create Foreign Keys */

ALTER TABLE dictionary_word
    ADD FOREIGN KEY (category)
        REFERENCES dictionary_category (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;



/* Create Indexes */

CREATE INDEX category USING BTREE ON dictionary_word (category ASC);
