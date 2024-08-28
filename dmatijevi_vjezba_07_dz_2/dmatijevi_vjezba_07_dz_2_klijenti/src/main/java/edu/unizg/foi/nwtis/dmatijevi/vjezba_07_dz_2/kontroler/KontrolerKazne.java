/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.kontroler;

import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.Kazna;
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
 * Klasa KontrolerKazne.
 *
 * @author NWTiS
 */
@Controller
@Path("kazne")
@RequestScoped
public class KontrolerKazne {

  /** Models model. */
  @Inject
  private Models model;

  /** BindingResult bindingResult. */
  @Inject
  private BindingResult bindingResult;

  /**
   * Metoda pocetak - prikaz stranice za kazne.
   */
  @GET
  @Path("pocetak")
  @View("kazneSve.jsp")
  public void pocetak() {}

  /**
   * Metoda json - prikaz stranice ispisa svih kazni.
   */
  @GET
  @Path("ispisKazni")
  @View("kazne.jsp")
  public void json() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON();
    model.put("kazne", kazne);
  }

  /**
   * Metoda json_pi - prikaz stranice ispisa kazni unutar intervala od do.
   *
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   */
  @POST
  @Path("pretrazivanjeKazniInterval")
  @View("kazne.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_od_do(odVremena, doVremena);
    model.put("kazne", kazne);
  }

  /**
   * Metoda jsonKaznaRb - prikaz stranice ispisa kazne s rednim brojem.
   *
   * @param rb - redni broj kazne
   */
  @POST
  @Path("ispisKaznaRb")
  @View("kazne.jsp")
  public void jsonKaznaRb(@FormParam("rb") String rb) {
    RestKlijentKazne k = new RestKlijentKazne();
    Kazna kazna = k.getKaznaJSON_rb(rb);
    model.put("kazne", Arrays.asList(kazna));
  }

  /**
   * Metoda jsonKazneZaVozilo - prikaz stranice ispisa kazne za vozilo.
   *
   * @param id - id vozila
   */
  @POST
  @Path("ispisKazneZaVozilo")
  @View("kazne.jsp")
  public void jsonKazneZaVozilo(@FormParam("id") String id) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(id);
    model.put("kazne", kazne);
  }

  /**
   * Metoda jsonKazneZaVoziloInterval - prikaz stranice ispisa kazne za vozilo unutar intervala od
   * do.
   *
   * @param id - id vozila
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   */
  @POST
  @Path("ispisKazneZaVoziloInterval")
  @View("kazne.jsp")
  public void jsonKazneZaVoziloInterval(@FormParam("id") String id,
      @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo_od_do(id, odVremena, doVremena);
    model.put("kazne", kazne);
  }
}
