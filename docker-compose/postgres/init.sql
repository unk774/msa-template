create database keycloak;
create user keycloak with encrypted password 'keycloak';
grant all privileges on database keycloak to keycloak;
ALTER DATABASE keycloak OWNER TO keycloak;