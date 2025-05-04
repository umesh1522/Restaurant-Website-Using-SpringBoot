FROM mysql:8

# The environment variables will be provided by Render
# MYSQL_ROOT_PASSWORD
# MYSQL_DATABASE
# MYSQL_USER
# MYSQL_PASSWORD

# Set the default database name
ENV MYSQL_DATABASE=hotel
