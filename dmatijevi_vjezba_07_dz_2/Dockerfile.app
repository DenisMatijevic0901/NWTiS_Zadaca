FROM eclipse-temurin:21-jre

COPY ./dmatijevi_vjezba_07_dz_2_app/target/dmatijevi_vjezba_07_dz_2_app-1.1.0-jar-with-dependencies.jar /usr/app/
COPY ./dmatijevi_vjezba_07_dz_2_app/NWTiS_DZ1_CS.txt /usr/app/
COPY ./dmatijevi_vjezba_07_dz_2_app/NWTiS_DZ1_PK.txt /usr/app/
COPY ./dmatijevi_vjezba_07_dz_2_app/NWTiS_DZ1_R1.txt /usr/app/
COPY ./dmatijevi_vjezba_07_dz_2_app/NWTiS_DZ1_SV.txt /usr/app/
COPY ./dmatijevi_vjezba_07_dz_2_app/NWTiS_DZ1_V1.csv /usr/app/

WORKDIR /usr/app

EXPOSE 8000
EXPOSE 8001
EXPOSE 8010
EXPOSE 8011
EXPOSE 8012
EXPOSE 8013
EXPOSE 8020
EXPOSE 8080

COPY ./docker-entrypoint.app.sh /docker-entrypoint.sh
RUN chmod -R 777 /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["go"]
