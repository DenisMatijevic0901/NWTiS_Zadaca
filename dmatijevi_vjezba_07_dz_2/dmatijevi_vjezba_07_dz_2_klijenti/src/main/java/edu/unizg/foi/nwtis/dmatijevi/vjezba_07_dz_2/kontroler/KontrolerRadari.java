package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.kontroler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model.RestKlijentRadari;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

// TODO: Auto-generated Javadoc
/**
 * Klasa KontrolerRadari.
 *
 * @author Denis Matijević
 */
@Controller
@Path("radari")
@RequestScoped
public class KontrolerRadari {
  /** Models model. */
  @Inject
  private Models model;

  /** BindingResult bindingResult. */
  @Inject
  private BindingResult bindingResult;

  /**
   * Metoda pocetak - prikaz stranice za radare.
   */
  @GET
  @Path("pocetak")
  @View("radariSve.jsp")
  public void pocetak() {}

  /**
   * Metoda jsonDohvatiRadare - prikaz stranice za dohvaćanje svih radara.
   *
   */
  @GET
  @Path("dohvatiSveRadare")
  @View("radariOdgovor.jsp")
  public void jsonDohvatiRadare() {
    RestKlijentRadari r = new RestKlijentRadari();

    Response odgovor = r.getRadariJSON();

    List<Map<String, Object>> sviRadari = new ArrayList<>();

    if (odgovor.getStatus() == 200) {
      String odgovorString = odgovor.readEntity(String.class);

      JsonReader jsonCitac = Json.createReader(new StringReader(odgovorString));
      JsonObject jsonObjekt = jsonCitac.readObject();

      String radariString = jsonObjekt.getString("odgovor").substring(3);
      radariString = radariString.substring(1, radariString.length() - 1);

      String[] radari = radariString.split("], \\[");

      for (String radar : radari) {
        radar = radar.replace("[", "").replace("]", "");
        String[] podaciRadara = radar.split(" ");
        if (podaciRadara.length == 6) {
          Map<String, Object> radarMap = new HashMap<>();
          radarMap.put("id", Integer.parseInt(podaciRadara[0]));
          radarMap.put("adresa", podaciRadara[1]);
          radarMap.put("mreznaVrata", Integer.parseInt(podaciRadara[2]));
          radarMap.put("gpsSirina", Double.parseDouble(podaciRadara[3]));
          radarMap.put("gpsDuzina", Double.parseDouble(podaciRadara[4]));
          radarMap.put("maksUdaljenost", Integer.parseInt(podaciRadara[5]));
          sviRadari.add(radarMap);
        }
      }
    }
    model.put("radari", sviRadari);
  }

  /**
   * Metoda jsonResetirajRadare - prikaz stranice za resetiranje svih radara.
   *
   */
  @GET
  @Path("resetSvihRadara")
  @View("radariOdgovorReset.jsp")
  public void jsonResetirajRadare() {
    RestKlijentRadari r = new RestKlijentRadari();

    Response odgovor = r.resetRadarJSON();
    String odgovorString = odgovor.readEntity(String.class);

    JsonReader jsonCitac = Json.createReader(new StringReader(odgovorString));
    JsonObject jsonObjekt = jsonCitac.readObject();
    String odgovorVrijednost = jsonObjekt.getString("odgovor");

    model.put("odgovor", odgovorVrijednost);
  }

  /**
   * Metoda jsonDohvatiRadar - prikaz stranice za dohvaćanje jednog radara.
   *
   * @param id id radara
   */
  @POST
  @Path("dohvatiJednogRadara")
  @View("radariOdgovor.jsp")
  public void jsonDohvatiRadar(@FormParam("id") String id) {
    RestKlijentRadari r = new RestKlijentRadari();

    Response odgovor = r.getRadarJSON(id);

    List<Map<String, Object>> sviRadari = new ArrayList<>();

    if (odgovor.getStatus() == 200) {
      String odgovorString = odgovor.readEntity(String.class);

      JsonReader jsonCitac = Json.createReader(new StringReader(odgovorString));
      JsonObject jsonObjekt = jsonCitac.readObject();

      String radarString = jsonObjekt.getString("odgovor");

      if (radarString.contains("[") && radarString.contains("]")) {
        radarString = radarString.substring(radarString.indexOf("[") + 1, radarString.indexOf("]"));
        String[] podaciRadara = radarString.split(" ");

        if (podaciRadara.length == 6) {
          Map<String, Object> radarMap = new HashMap<>();
          radarMap.put("id", Integer.parseInt(podaciRadara[0]));
          radarMap.put("adresa", podaciRadara[1]);
          radarMap.put("mreznaVrata", Integer.parseInt(podaciRadara[2]));
          radarMap.put("gpsSirina", Double.parseDouble(podaciRadara[3]));
          radarMap.put("gpsDuzina", Double.parseDouble(podaciRadara[4]));
          radarMap.put("maksUdaljenost", Integer.parseInt(podaciRadara[5]));
          sviRadari.add(radarMap);
        }
      }
    }
    model.put("radari", sviRadari);
  }

  /**
   * Metoda jsonProvjeriRadar - prikaz stranice za provjeru radara.
   *
   * @param id id radara
   */
  @POST
  @Path("provjeriRadar")
  @View("radariOdgovorProvjeri.jsp")
  public void jsonProvjeriRadar(@FormParam("id") String id) {
    RestKlijentRadari r = new RestKlijentRadari();

    Response odgovor = r.provjeriRadarJSON(id);
    String odgovorString = odgovor.readEntity(String.class);

    JsonReader jsonCitac = Json.createReader(new StringReader(odgovorString));
    JsonObject jsonObjekt = jsonCitac.readObject();
    String odgovorVrijednost = jsonObjekt.getString("odgovor");

    model.put("odgovor", odgovorVrijednost);
  }

  /**
   * Metoda obrisiRadare - prikaz stranice za brisanje svih radara.
   * 
   * @param metoda tip metode
   *
   */
  @POST
  @Path("obrisiRadare")
  @View("radariOdgovorObrisi.jsp")
  public void obrisiRadare(@FormParam("metoda") String metoda) {
    if ("DELETE".equals(metoda)) {
      jsonObrisiRadare();
    }
  }

  /**
   * Metoda jsonObrisiRadare - prikaz stranice za brisanje svih radara.
   *
   */
  @DELETE
  @Path("obrisiRadare")
  @View("radariOdgovorObrisi.jsp")
  public void jsonObrisiRadare() {
    RestKlijentRadari r = new RestKlijentRadari();

    Response odgovor = r.deleteRadariJSON();
    String odgovorString = odgovor.readEntity(String.class);

    JsonReader jsonCitac = Json.createReader(new StringReader(odgovorString));
    JsonObject jsonObjekt = jsonCitac.readObject();
    String odgovorVrijednost = jsonObjekt.getString("odgovor");

    model.put("odgovor", odgovorVrijednost);
  }

  /**
   * Metoda obrisiJednogRadara - prikaz stranice za brisanje radara.
   * 
   * @param metoda tip metode
   * @param id id radara
   *
   */
  @POST
  @Path("obrisiJednogRadara")
  @View("radariOdgovorObrisi.jsp")
  public void obrisiJednogRadara(@FormParam("metoda") String metoda, @FormParam("id") String id) {
    if ("DELETE".equals(metoda)) {
      jsonObrisiRadar(id);
    }
  }

  /**
   * Metoda jsonObrisiRadar - prikaz stranice za brisanje radara.
   *
   */
  @DELETE
  @Path("obrisiJednogRadara")
  @View("radariOdgovorObrisi.jsp")
  public void jsonObrisiRadar(String id) {
    RestKlijentRadari r = new RestKlijentRadari();

    Response odgovor = r.deleteRadarJSON(id);
    String odgovorString = odgovor.readEntity(String.class);

    JsonReader jsonCitac = Json.createReader(new StringReader(odgovorString));
    JsonObject jsonObjekt = jsonCitac.readObject();
    String odgovorVrijednost = jsonObjekt.getString("odgovor");

    model.put("odgovor", odgovorVrijednost);
  }
}
