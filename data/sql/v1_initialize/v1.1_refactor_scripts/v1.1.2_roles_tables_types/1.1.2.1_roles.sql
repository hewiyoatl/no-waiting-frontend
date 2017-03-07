CREATE USER 'no_admin'@'%'
  IDENTIFIED BY 'no_admin';
CREATE USER 'no_app'@'%'
  IDENTIFIED BY 'no_app';

GRANT USAGE ON hewiyoat_no_waiting.* TO 'no_app'@'%'
IDENTIFIED BY 'no_app';
GRANT USAGE ON hewiyoat_no_waiting.* TO 'no_app'@'localhost'
IDENTIFIED BY 'no_app';
GRANT INSERT, DELETE ON hewiyoat_no_waiting.* TO 'no_app'@'%'
IDENTIFIED BY 'no_app';
GRANT INSERT, DELETE ON hewiyoat_no_waiting.* TO 'no_app'@'localhost'
IDENTIFIED BY 'no_app';
GRANT SELECT, UPDATE ON hewiyoat_no_waiting.* TO 'no_app'@'%'
IDENTIFIED BY 'no_app';
GRANT SELECT, UPDATE ON hewiyoat_no_waiting.* TO 'no_app'@'localhost'
IDENTIFIED BY 'no_app';

GRANT USAGE ON hewiyoat_no_waiting.* TO 'no_admin'@'%'
IDENTIFIED BY 'no_admin';
GRANT USAGE ON hewiyoat_no_waiting.* TO 'no_admin'@'localhost'
IDENTIFIED BY 'no_admin';
GRANT ALL PRIVILEGES ON hewiyoat_no_waiting.* TO 'no_admin'@'%'
IDENTIFIED BY 'no_admin';
GRANT ALL PRIVILEGES ON hewiyoat_no_waiting.* TO 'no_admin'@'localhost'
IDENTIFIED BY 'no_admin';
