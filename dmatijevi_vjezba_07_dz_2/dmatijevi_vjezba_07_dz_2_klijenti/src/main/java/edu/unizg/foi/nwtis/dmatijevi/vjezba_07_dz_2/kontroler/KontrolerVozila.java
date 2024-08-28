package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model.RestKlijentVozila;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PracenaVoznja;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

// TODO: Auto-generated Javadoc
/**
 * Klasa KontrolerVozila.
 *
 * @author Denis Matijević
 */
@Controller
@Path("vozila")
@RequestScoped
public class KontrolerVozila {

  /** Models model. */
  @Inject
  private Models model;

  /** BindingResult bindingResult. */
  @Inject
  private BindingResult bindingResult;

  /**
   * Metoda pocetak - prikaz stranice za vozila.
   */
  @GET
  @Path("pocetak")
  @View("vozilaSve.jsp")
  public void pocetak() {}

  /**
   * Metoda json_pi - prikaz stranice ispisa praćenih vožnji u intervalu od do.
   *
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   */
  @POST
  @Path("pretrazivanjeVoziloInterval")
  @View("vozila.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentVozila v = new RestKlijentVozila();
    List<PracenaVoznja> praceneVoznje = v.getPraceneVoznjeJSON_od_do(odVremena, doVremena);
    model.put("praceneVoznje", praceneVoznje);
  }

  /**
   * Metoda jsonPraceneVoznjeZaVozilo - prikaz stranice ispisa praćenih vožnji za vozilo.
   *
   * @param id - id vozila
   */
  @POST
  @Path("ispisPracenihVoznjiZaVozilo")
  @View("vozila.jsp")
  public void jsonPraceneVoznjeZaVozilo(@FormParam("id") String id) {
    RestKlijentVozila v = new RestKlijentVozila();
    List<PracenaVoznja> praceneVoznje = v.getPraceneVoznjeJSON_vozilo(id);
    model.put("praceneVoznje", praceneVoznje);
  }

  /**
   * Metoda jsonPraceneVoznjeZaVoziloInterval - prikaz stranice ispisa praćenih vožnji za vozilo
   * unutar intervala od do.
   *
   * @param id - id vozila
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   */
  @POST
  @Path("ispisPracenihVoznjiZaVoziloInterval")
  @View("vozila.jsp")
  public void jsonPraceneVoznjeZaVoziloInterval(@FormParam("id") String id,
      @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentVozila v = new RestKlijentVozila();
    List<PracenaVoznja> praceneVoznje =
        v.getPraceneVoznjeJSON_vozilo_od_do(id, odVremena, doVremena);
    model.put("praceneVoznje", praceneVoznje);
  }

  /**
   * Metoda jsonPokreniPracenjeVoznje - prikaz stranice za pokretanje praćenja vožnji vozila.
   *
   * @param id - id vozila
   */
  @POST
  @Path("pokreniPracenjeVoznje")
  @View("vozilaOdgovor.jsp")
  public void jsonPokreniPracenjeVoznje(@FormParam("id") String id) {
    RestKlijentVozila v = new RestKlijentVozila();

    String odgovor = v.getPraceneVoznjeJSON_vozilo_start(id);
    model.put("odgovor", odgovor);
  }

  /**
   * Metoda jsonPrekiniPracenjeVoznje - prikaz stranice za prekidanje praćenja vožnji vozila.
   *
   * @param id - id vozila
   */
  @POST
  @Path("prekiniPracenjeVoznje")
  @View("vozilaOdgovor.jsp")
  public void jsonPrekiniPracenjeVoznje(@FormParam("id") String id) {
    RestKlijentVozila v = new RestKlijentVozila();

    String odgovor = v.getPraceneVoznjeJSON_vozilo_stop(id);
    model.put("odgovor", odgovor);
  }
}
