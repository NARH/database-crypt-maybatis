create table if not exists MEMBER_MASTER (
    ID          varchar(32) primary key
  , NAME        binary(128) not null
  , KANA        binary(256) not null
  , POSTAL_CODE binary(28)
  , ADDRESS     binary(1024)
);