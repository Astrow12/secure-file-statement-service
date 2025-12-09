--  account_statement table
create table if not exists account_statement (
        id serial primary key,
        s3_path_key varchar(255) not null,
        signed_statement_url varchar(255),
        file_name varchar(255) not null,
        upload_status varchar(50) not null,
        user_id varchar(255) not null,
        deleted boolean default false,
        checksum bytea not null,
        created_date timestamp not null,
        modified_date timestamp not null
    );


create index user_id_document_id_idx on account_statement(user_id, id);

create unique index account_statement_checksum_idx on account_statement(checksum);