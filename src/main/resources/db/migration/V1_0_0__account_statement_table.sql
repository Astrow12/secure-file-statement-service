--  Books table
create table if not exists account_statement (
        id bigint not null auto_increment,
        signed_statement_url varchar(255),
        file_name varchar(255) not null,
        upload_status varchar(50) not null,
        user_id varchar(255) not null,
        deleted boolean default false,
        checksum bytea not null,
        created_date timestamp not null,
        modified_date timestamp not null,
        constraint id_key primary key (id)
    );


create index user_id_document_id_idx on account_statement(user_id, id);

create unique index account_statement_checksum_idx on account_statement(checksum);