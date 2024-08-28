package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PracenaVoznja;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PracenaVoznjaDAO;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// TODO: Auto-generated Javadoc
/**
 * Klasa VozilaResurs.
 * 
 * @author Denis Matijević
 */
@Path("nwtis/v1/api/vozila")
public class VozilaResurs extends SviResursi {

  /** pracena voznja DAO. */
  private PracenaVoznjaDAO pracenaVoznjaDAO = null;

  /** adresa vozila. */
  private String adresaVozila;

  /** mrezna vrata vozila. */
  private int mreznaVrataVozila;

  /**
   * Pripremi korisnik DAO.
   */
  @PostConstruct
  private void pripremiKorisnikDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavkeZaResurs("NWTiS_REST_V.txt");
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.pracenaVoznjaDAO = new PracenaVoznjaDAO(vezaBP);
    } catch (Exception e) {
      e.printStackTrace();
      return;
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

    this.adresaVozila = konfig.dajPostavku("adresaVozila");
    this.mreznaVrataVozila = Integer.parseInt(konfig.dajPostavku("mreznaVrataVozila"));
  }

  /**
   * Dohvaća praćene vožnje u intervalu.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   * @return lista praćenih vožnji
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    return Response.status(Response.Status.OK)
        .entity(pracenaVoznjaDAO.dohvatiPraceneVoznje(odVremena, doVremena).toArray()).build();
  }

  /**
   * Dohvaća praćene vožnje za definirano vozilo.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   * @return lista praćenih vožnji
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonPracenaVoznjaVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK)
          .entity(pracenaVoznjaDAO.dohvatiPraceneVoznjeVozila(id)).build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(pracenaVoznjaDAO.dohvatiPraceneVoznjeVozila(id, odVremena, doVremena)).build();
    }
  }

  /**
   * Dodaje novu praćenu vožnju.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaPracenaVoznja podaci nove praćene vožnje
   * @return OK ako je praćena vožnja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postJsonDodajPracenuVoznju(@HeaderParam("Accept") String tipOdgovora,
      PracenaVoznja novaPracenaVoznja) {

    var odgovor = pracenaVoznjaDAO.dodajPracenuVoznju(novaPracenaVoznja);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis praćene vožnje u bazu podataka.").build();
    }
  }

  /**
   * Metoda getJsonStart - pokreće praćenje vožnji za vozilo.
   *
   * @param tipOdgovora the tip odgovora
   * @param id - id vozila
   * @return odgovor
   */
  @Path("/vozilo/{id}/start")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonStart(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    String odgovor = posaljiKomanduStart(id);
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder().add("odgovor",
          "ERROR 29 PosluziteljZaVozila nije aktivan. Neuspješan zahtjev GET/vozilo/{id}/start.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Posalji komandu start.
   *
   * @param id - id vozila
   * @return odgovor
   */
  private String posaljiKomanduStart(int id) {
    String komandaStart = "VOZILO START " + id + "\n";

    var odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju(adresaVozila, mreznaVrataVozila, komandaStart);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 29 PosluziteljZaVozila nije aktivan.");
      return null;
    }
  }

  /**
   * Metoda getJsonStop - prekida praćenje vožnji za vozilo.
   *
   * @param tipOdgovora - tip odgovora
   * @param id - id vozila
   * @return odgovor
   */
  @Path("/vozilo/{id}/stop")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonStop(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    String odgovor = posaljiKomanduStop(id);
    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder()
          .add("odgovor",
              "ERROR 29 PosluziteljZaVozila nije aktivan. Neuspješan zahtjev GET/vozilo/{id}/stop.")
          .build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Posalji komandu stop.
   *
   * @param id - id vozila
   * @return odgovor
   */
  private String posaljiKomanduStop(int id) {
    String komandaStop = "VOZILO STOP " + id + "\n";

    var odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju(adresaVozila, mreznaVrataVozila, komandaStop);

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 29 PosluziteljZaVozila nije aktivan.");
      return null;
    }
  }
}
