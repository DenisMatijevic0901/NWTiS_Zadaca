/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.Kazna;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.KaznaDAO;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
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
 * REST Web Service uz korištenje klase Kazna.
 *
 * @author Dragutin Kermek
 */
@Path("nwtis/v1/api/kazne")
public class KazneResurs extends SviResursi {

  /** The kazna DAO. */
  private KaznaDAO kaznaDAO = null;

  /** adresa kazne. */
  private String adresaKazne;

  /** mrezna vrata kazne. */
  private int mreznaVrataKazne;

  /**
   * Pripremi korisnik DAO.
   */
  @PostConstruct
  private void pripremiKorisnikDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavkeZaResurs("NWTiS_REST_K.txt");
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.kaznaDAO = new KaznaDAO(vezaBP);
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

    this.adresaKazne = konfig.dajPostavku("adresaKazne");
    this.mreznaVrataKazne = Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne"));
  }

  /**
   * Dohvaća sve kazne ili kazne u intervalu, ako je definiran.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param odVremena the od vremena
   * @param doVremena the do vremena
   * @return lista kazni
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK).entity(kaznaDAO.dohvatiSveKazne().toArray())
          .build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(kaznaDAO.dohvatiKazne(odVremena, doVremena).toArray()).build();
    }
  }

  /**
   * Dohvaća kaznu za definirani redni broj.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param rb redni broj zapisa
   * @return lista kazni
   */
  @Path("{rb}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonKaznaRb(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("rb") int rb) {

    return Response.status(Response.Status.OK).entity(kaznaDAO.dohvatiKaznu(rb)).build();
  }

  /**
   * Dohvaća kazne za definirano vozilo.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @param odVremena the od vremena
   * @param doVremena the do vremena
   * @return lista kazni
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonKaznaVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK).entity(kaznaDAO.dohvatiKazneVozila(id)).build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(kaznaDAO.dohvatiKazneVozila(id, odVremena, doVremena)).build();
    }
  }

  /**
   * Provjerava stanje.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @return OK
   */
  @HEAD
  @Produces({MediaType.APPLICATION_JSON})
  public Response head(@HeaderParam("Accept") String tipOdgovora) {
    String odgovor = provjeriPosluzitelja();

    if (odgovor != null) {
      JsonObject jsonOdgovorOk = Json.createObjectBuilder().add("odgovor", odgovor).build();
      return Response.ok(jsonOdgovorOk.toString(), MediaType.APPLICATION_JSON).build();
    } else {
      JsonObject jsonOdgovorError = Json.createObjectBuilder()
          .add("odgovor", "ERROR 34 PosluziteljKazni nije aktivan.").build();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(jsonOdgovorError.toString()).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Dodaje novu kaznu.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaKazna podaci nove kazne
   * @return OK ako je kazna uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes(MediaType.APPLICATION_JSON)
  public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Kazna novaKazna) {

    var odgovor = kaznaDAO.dodajKaznu(novaKazna);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis kazne u bazu podataka.").build();
    }
  }

  /**
   * Provjeri poslužitelja.
   *
   * @return true ako je odgovor uredu
   */
  private String provjeriPosluzitelja() {
    var poruka = new StringBuilder();
    poruka.append("TEST").append("\n");

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaKazne, mreznaVrataKazne,
        poruka.toString());

    if (odgovor != null) {
      System.out.println(odgovor);
      return odgovor;
    } else {
      System.out.println("ERROR 34 PosluziteljKazni nije aktivan.");
      return null;
    }
  }
}
