#!/bin/sh
TIMESTAMP="./src/main/resources/db/migration/`date +V%Y%m%d%I%M%s__$1`.sql"
touch $TIMESTAMP