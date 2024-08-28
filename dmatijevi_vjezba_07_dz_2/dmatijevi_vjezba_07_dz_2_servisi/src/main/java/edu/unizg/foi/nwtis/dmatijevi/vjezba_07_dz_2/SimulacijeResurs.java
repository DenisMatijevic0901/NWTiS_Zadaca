package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.VoznjaDAO;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
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
 * Klasa SimulacijeResurs.
 * 
 * @author Denis Matijević
 */
@Path("nwtis/v1/api/simulacije")
public class SimulacijeResurs extends SviResursi {

  /** voznja DAO. */
  private VoznjaDAO voznjaDAO = null;

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
      preuzmiPostavkeZaResurs("NWTiS_REST_S.txt");
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.voznjaDAO = new VoznjaDAO(vezaBP);
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
   * Dohvaća vožnje u intervalu.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   * @return lista vožnji
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    return Response.status(Response.Status.OK)
        .entity(voznjaDAO.dohvatiVoznje(odVremena, doVremena).toArray()).build();
  }

  /**
   * Dohvaća vožnje za definirano vozilo.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   * @return lista vožnji
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznjaVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK).entity(voznjaDAO.dohvatiVoznjeVozila(id)).build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(voznjaDAO.dohvatiVoznjeVozila(id, odVremena, doVremena)).build();
    }
  }

  /**
   * Dodaje novu vožnju.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaVoznja podaci nove vožnje
   * @return OK ako je vožnja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postJsonDodajVoznju(@HeaderParam("Accept") String tipOdgovora,
      Voznja novaVoznja) {

    var odgovor = voznjaDAO.dodajVoznju(novaVoznja);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis vožnje u bazu podataka.").build();
    }
  }
}
