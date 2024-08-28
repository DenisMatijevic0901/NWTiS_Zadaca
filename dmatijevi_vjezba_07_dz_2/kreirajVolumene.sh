#!/bin/bash

# Kreiranje volumena ako ne postoje
if ! docker volume ls | grep -q 'nwtis_hsqldb_data'; then
  echo "Kreiranje volumena nwtis_hsqldb_data"
  docker volume create nwtis_hsqldb_data
  echo "Volumen nwtis_hsqldb_data je uspješno kreiran."
  echo "Kopiranje podataka u nwtis_hsqldb_data volumen"
  sudo cp -R /opt/hsqldb-2.7.2/hsqldb/data/* /var/lib/docker/volumes/nwtis_hsqldb_data/_data/
else
  echo "Volumen nwtis_hsqldb_data već postoji."
fi

if ! docker volume ls | grep -q 'nwtis_h2_data'; then
  echo "Kreiranje volumena nwtis_h2_data"
  docker volume create nwtis_h2_data
  echo "Volumen nwtis_h2_data je uspješno kreiran."
  echo "Kopiranje podataka u nwtis_h2_data volumen"
  sudo cp -R /opt/database/* /var/lib/docker/volumes/nwtis_h2_data/_data/
else
  echo "Volumen nwtis_h2_data već postoji."
fi
