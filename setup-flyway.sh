#!/bin/bash

wget https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/5.1.4/flyway-commandline-5.1.4-linux-x64.tar.gz
tar xzvf flyway-commandline-5.1.4-linux-x64.tar.gz
mv flyway-5.1.4/ flyway

for f in `find atrs-initdb/src/sqls/integration-test-postgres -type f`;do 
  cp $f ./flyway/sql/V`basename $f | sed 's/_/__/'`;
done
