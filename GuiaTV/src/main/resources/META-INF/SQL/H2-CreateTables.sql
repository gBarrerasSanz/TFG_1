drop table EVENTO;
DROP TABLE OPENJPA_SEQUENCE_TABLE;

CREATE TABLE OPENJPA_SEQUENCE_TABLE (ID TINYINT NOT NULL, SEQUENCE_VALUE BIGINT, PRIMARY KEY (ID));
CREATE TABLE EVENTO (ID BIGINT generated by default as identity, CHANNEL VARCHAR(255), TITLE VARCHAR(255), 
					TIME_START TIMESTAMP, TIME_END TIMESTAMP, PRIMARY KEY (ID));