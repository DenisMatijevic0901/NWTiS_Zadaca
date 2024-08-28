package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// TODO: Auto-generated Javadoc
/**
 * Klasa RadarResurs.
 * 
 * @author Denis Matijević
 */
@Path("nwtis/v1/api/radari")
public class RadarResurs extends SviResursi {

  /** adresa registracije. */
  private String adresaRegistracije;

  /** mrezna vrata registracije. */
  private int mreznaVrataRegistracije;

  /**
   * Pripremi.
   */
  @PostConstruct
  private void pripremi() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavkeZaResurs("NWTiS_REST_R.txt");
    } catch (NeispravnaKonfiguracija e) {
      e.printStackTrace();
    }
  }

  /**
   * Preuzmi postavke za resurs.
   *
   * @param nazivDatoteke naziv datoteke
   * @throws NeispravnaKonfiguracija neispravna konfiguracija
   */
  private void preuzmiPostavkeZaResurs(String nazivDatoteke) throws NeispravnaKonfiguracija {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

    this.adresaRegistracije = konfig.dajPostavku("adresaRegistracije");
    this.mreznaVrataRegistracije = Integer.parseInt(konfig.dajPostavku("mreznaVrataRegistracije"));
  }

  /**
   * Vraća sve radare.
   *
   * @param tipOdgovora - tip odgovora
   * @return odgovor
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora) {
    String odgovor = posaljiKomanduRadarSvi();
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder()
          .add("odgovor",
              "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan. Neuspješan zahtjev GET.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Posalji komandu radar svi.
   *
   * @return odgovor
   */
  private String posaljiKomanduRadarSvi() {
    String komandaSviRadari = "RADAR SVI\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaRegistracije,
        mreznaVrataRegistracije, komandaSviRadari);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.");
      return null;
    }
  }

  /**
   * Metoda getJsonProvjeri - pokreće provjeru radaru sa zadanim id.
   *
   * @param tipOdgovora - tip odgovora
   * @param id - id radara
   * @return odgovor
   */
  @Path("/{id}/provjeri")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonProvjeri(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    String odgovor = posaljiKomanduRadarIdProvjeri(id);
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder().add("odgovor",
          "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan. Neuspješan zahtjev GET/{id}/provjeri.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Metoda posaljiKomanduRadarIdProvjeri.
   *
   * @param id - id radara
   * @return odgovor
   */
  private String posaljiKomanduRadarIdProvjeri(int id) {
    String komandaRadarIdProvjeri = "RADAR " + id + "\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaRegistracije,
        mreznaVrataRegistracije, komandaRadarIdProvjeri);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.");
      return null;
    }
  }

  /**
   * Metoda deleteJson - briše sve radare.
   *
   * @param tipOdgovora - tip odgovora
   * @return odgovor
   */
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteJson(@HeaderParam("Accept") String tipOdgovora) {
    String odgovor = posaljiKomanduObrisiSveRadare();
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder()
          .add("odgovor",
              "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan. Neuspješan zahtjev DELETE.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Metoda posaljiKomanduObrisiSveRadare.
   *
   * @return odgovor
   */
  private String posaljiKomanduObrisiSveRadare() {
    String komandaObrisiSveRadare = "RADAR OBRIŠI SVE\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaRegistracije,
        mreznaVrataRegistracije, komandaObrisiSveRadare);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.");
      return null;
    }
  }

  /**
   * Metoda deleteJsonId - brise radar.
   *
   * @param tipOdgovora - tip odgovora
   * @param id - id radara
   * @return odgovor
   */
  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteJsonId(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    String odgovor = posaljiKomanduObrisiRadarId(id);
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder().add("odgovor",
          "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan. Neuspješan zahtjev DELETE/{id}.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Posalji komandu obrisi radar id.
   *
   * @param id - id radara
   * @return odgovor
   */
  private String posaljiKomanduObrisiRadarId(int id) {
    String komandaObrisiRadarId = "RADAR OBRIŠI " + id + "\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaRegistracije,
        mreznaVrataRegistracije, komandaObrisiRadarId);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.");
      return null;
    }
  }

  /**
   * Metoda getJsonId - vraća radara sa zadanim id.
   *
   * @param tipOdgovora - tip odgovora
   * @param id - id radara
   * @return odgovor
   */
  @Path("{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonId(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    String odgovor = posaljiKomanduRadarSviZaDohvacanjeJednogRadara(id);
    if (odgovor != null) {
      if (odgovor.equals("OK {}")) {
        JsonObject jsonOdgovorOk = Json.createObjectBuilder()
            .add("odgovor", "ERROR 12 Radar s ID-om " + id + " ne postoji.").build();
        return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
      } else if (odgovor.contains("ERROR 12")) {
        JsonObject jsonOdgovorOk = Json.createObjectBuilder()
            .add("odgovor", "ERROR 12 Radar s ID-om " + id + " ne postoji.").build();
        return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
      } else {
        String trazeniRadar = pronadiTrazeniRadar(odgovor, id);
        if (trazeniRadar != null) {
          JsonObject jsonOdgovorOk =
              Json.createObjectBuilder().add("odgovor", trazeniRadar).build();
          return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
        } else {
          JsonObject jsonOdgovorOk = Json.createObjectBuilder()
              .add("odgovor", "ERROR 12 Radar s ID-om " + id + " ne postoji.").build();
          return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
        }
      }
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder()
          .add("odgovor",
              "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan. Neuspješan zahtjev GET/{id}.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Metoda za pronalazak traženog radara unutar svih radara.
   *
   * @param odgovor - odgovor sa svim radarima
   * @param id - id radara
   * @return odgovor sa traženim radarom ili null ako radar nije pronađen
   */
  private String pronadiTrazeniRadar(String odgovor, int id) {
    String sviRadariBezUglatih = odgovor.substring(4, odgovor.length() - 1).trim();
    String[] sviRadariPosebno = sviRadariBezUglatih.split("], \\[");

    for (String radar : sviRadariPosebno) {
      radar = radar.replace("[", "").replace("]", "");
      String[] podaciRadara = radar.split(" ");

      if (id == Integer.parseInt(podaciRadara[0])) {
        return "OK {[" + radar + "]}";
      }
    }

    return null;
  }

  /**
   * Posalji komandu radar svi za dohvacanje jednog radara.
   *
   * @param id - id radara
   * @return odgovor
   */
  private String posaljiKomanduRadarSviZaDohvacanjeJednogRadara(int id) {
    String komandaSviRadari = "RADAR SVI\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaRegistracije,
        mreznaVrataRegistracije, komandaSviRadari);

    if (odgovor != null) {
      if (odgovor.equals("OK {}")) {
        System.out.println("ERROR 12 Radar s ID-om " + id + " ne postoji.");
      } else {
        boolean pronaden = false;
        String sviRadariBezUglatih = odgovor.substring(4, odgovor.length() - 1).trim();
        String[] sviRadariPosebno = sviRadariBezUglatih.split("], \\[");

        for (String radar : sviRadariPosebno) {
          radar = radar.replace("[", "").replace("]", "");
          String[] podaciRadara = radar.split(" ");

          if (id == Integer.parseInt(podaciRadara[0])) {
            pronaden = true;
            System.out.println("OK {[" + radar + "]}");
            break;
          }
        }

        if (pronaden == false) {
          System.out.println("ERROR 12 Radar s ID-om " + id + " ne postoji.");
        }
      }

      return odgovor;
    } else {
      System.out.println("ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.");
      return null;
    }
  }

  /**
   * Metoda getJsonReset - postupak resetiranja svih radara.
   *
   * @param tipOdgovora - tip odgovora
   * @return odgovor
   */
  @Path("/reset")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonReset(@HeaderParam("Accept") String tipOdgovora) {
    String odgovor = posaljiKomanduRadarReset();
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder().add("odgovor",
          "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan. Neuspješan zahtjev GET/reset.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Posalji komandu radar reset.
   *
   * @return odgovor
   */
  private String posaljiKomanduRadarReset() {
    String komandaRadarReset = "RADAR RESET\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaRegistracije,
        mreznaVrataRegistracije, komandaRadarReset);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.");
      return null;
    }
  }
}
