FROM mysql:8

# Add any MySQL customization here if needed
# For example:
# COPY ./my-custom.cnf /etc/mysql/conf.d/
# COPY ./init.sql /docker-entrypoint-initdb.d/

# The environment variables will be provided by Render
# - MYSQL_DATABASE
# - MYSQL_USER
# - MYSQL_PASSWORD
# - MYSQL_ROOT_PASSWORD
