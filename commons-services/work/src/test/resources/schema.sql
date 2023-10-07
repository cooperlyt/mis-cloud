SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

CREATE TABLE attachment
(
    id bigint NOT NULL,
    name varchar(128) NOT NULL,
    must boolean NOT NULL,
    have boolean NOT NULL,
    work_id bigint NOT NULL,
    version bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE attachment_define
(
    id bigint NOT NULL,
    name varchar(128) NOT NULL,
    define_id varchar(32) NOT NULL,
    must boolean NOT NULL,
    description varchar(512),
    version bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE work_define
(
    define_id varchar(32) NOT NULL,
    work_name varchar(32) NOT NULL,
    process boolean NOT NULL,
    enabled boolean NOT NULL,
    version bigint NOT NULL,
    type varchar(16) NOT NULL,
    PRIMARY KEY (define_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE work_file
(
    fid varchar(32) NOT NULL,
    sha256 varchar(512),
    attach_id bigint,
    size int,
    mime varchar(256) NOT NULL,
    e_tag varchar(32),
    order_num int NOT NULL,
    filename varchar(512),
    task_id varchar(64),
    PRIMARY KEY (fid)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;



/* Create Foreign Keys */

ALTER TABLE work_file
    ADD FOREIGN KEY (attach_id)
        REFERENCES attachment (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;


ALTER TABLE attachment_define
    ADD FOREIGN KEY (define_id)
        REFERENCES work_define (define_id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;



