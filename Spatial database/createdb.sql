create table position(
p_id varchar2(10),
address varchar2(50),
Location SDO_GEOMETRY,
mechanism varchar2(100),
primary key (p_id)
)
/
create table users(
user_id varchar2(10),
u_name varchar2(20),
Location SDO_GEOMETRY,
u_phone varchar2(20),
mechanism varchar2(100),
primary key(user_id)
)
/
INSERT INTO USER_SDO_GEOM_METADATA
VALUES ('position','Location',
		SDO_DIM_ARRAY(
		SDO_DIM_ELEMENT('X',0,600,0.5),
		SDO_DIM_ELEMENt('Y',0,500,0.5)
		),
		NULL
		);

INSERT INTO USER_SDO_GEOM_METADATA
VALUES ('users','Location',
		SDO_DIM_ARRAY(
		SDO_DIM_ELEMENT('X',0,600,0.5),
		SDO_DIM_ELEMENt('Y',0,500,0.5)
		),
		NULL
		);


CREATE INDEX position_idx ON position(Location)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;	

CREATE INDEX users_idx ON users(Location)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;