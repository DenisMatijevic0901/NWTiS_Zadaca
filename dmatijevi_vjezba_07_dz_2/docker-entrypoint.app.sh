#!/bin/bash
set -e

if [ "$1" = 'go' ]; then
    echo "Pokrećem Centralni Sustav"
    nohup java -cp /usr/app/dmatijevi_vjezba_07_dz_2_app-1.1.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &

    sleep 5

    echo "Pokrećem Poslužitelj Kazni"
    nohup java -cp /usr/app/dmatijevi_vjezba_07_dz_2_app-1.1.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &

    sleep 5

    echo "Pokrećem Poslužitelj Radara R1"
    nohup java -cp /usr/app/dmatijevi_vjezba_07_dz_2_app-1.1.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &

    sleep 5

    echo "Pokrećem Simulator Vozila V1"
    nohup java -cp /usr/app/dmatijevi_vjezba_07_dz_2_app-1.1.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.klijenti.SimulatorVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 &
    
    tail -f /dev/null
    
else
    exec "$@"
fi
