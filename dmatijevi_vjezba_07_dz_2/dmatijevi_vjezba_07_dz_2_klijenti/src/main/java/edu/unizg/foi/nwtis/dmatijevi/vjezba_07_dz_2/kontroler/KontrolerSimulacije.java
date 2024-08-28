package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.kontroler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

// TODO: Auto-generated Javadoc
/**
 * Klasa KontrolerSimulacije.
 *
 * @author Denis Matijević
 */
@Controller
@Path("simulacije")
@RequestScoped
public class KontrolerSimulacije {
  /** Models model. */
  @Inject
  private Models model;

  /** BindingResult bindingResult. */
  @Inject
  private BindingResult bindingResult;

  /** trajanje sekunde. */
  private int trajanjeSek;

  /** trajanje pauze. */
  private int trajanjePauze;

  /** adresa vozila. */
  private String adresaVozila = "20.24.5.2";

  /** mrezna vrata vozila. */
  private int mreznaVrataVozila = 8001;

  /**
   * Metoda pocetak - prikaz stranice za simulacije.
   */
  @GET
  @Path("pocetak")
  @View("simulacijeSve.jsp")
  public void pocetak() {}

  /**
   * Metoda json_pi - prikaz stranice ispisa vožnji u intervalu od do.
   *
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   */
  @POST
  @Path("pretrazivanjeSimulacijeInterval")
  @View("simulacije.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_od_do(odVremena, doVremena);
    model.put("voznje", voznje);
  }

  /**
   * Metoda jsonVoznjeZaVozilo - prikaz stranice ispisa vožnji za vozilo.
   *
   * @param id - id vozila
   */
  @POST
  @Path("ispisVoznjiZaVozilo")
  @View("simulacije.jsp")
  public void jsonVoznjeZaVozilo(@FormParam("id") String id) {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_vozilo(id);
    model.put("voznje", voznje);
  }

  /**
   * Metoda jsonVoznjeZaVoziloInterval - prikaz stranice ispisa vožnji za vozilo unutar intervala od
   * do.
   *
   * @param id - id vozila
   * @param odVremena - od vremena
   * @param doVremena - do vremena
   */
  @POST
  @Path("ispisVoznjiZaVoziloInterval")
  @View("simulacije.jsp")
  public void jsonVoznjeZaVoziloInterval(@FormParam("id") String id,
      @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_vozilo_od_do(id, odVremena, doVremena);
    model.put("voznje", voznje);
  }

  /**
   * Metoda pokreniSimulaciju - pokretanje simulacije do.
   *
   ** @param nazivDatoteke - naziv datoteke.
   ** @param idVozilaString - id vozila.
   ** @param trajanjeSekString - trajanje sekunde.
   ** @param trajanjePauzeString - trajanje pauze.
   ** @param context - context.
   */
  @POST
  @Path("simulacija")
  @View("simulacijePokrenutaSimulacija.jsp")
  public void pokreniSimulaciju(@FormParam("nazivDatoteke") String nazivDatoteke,
      @FormParam("idVozila") String idVozilaString,
      @FormParam("trajanjeSek") String trajanjeSekString,
      @FormParam("trajanjePauze") String trajanjePauzeString, @Context ServletContext context) {

    int idVozila = Integer.parseInt(idVozilaString);
    trajanjeSek = Integer.parseInt(trajanjeSekString);
    trajanjePauze = Integer.parseInt(trajanjePauzeString);

    String putanjaDatoteke = Paths.get(context.getRealPath("/WEB-INF"), nazivDatoteke).toString();

    StringBuilder greske = new StringBuilder();
    ucitajPodatke(putanjaDatoteke, idVozila, greske);
    model.put("greske", greske.toString());
  }

  /**
   * Metoda ucitajPodatke - čita podatke iz .csv datoteke te ih asinkrono šalje PosluziteljZaVozila.
   *
   * @param putanja - putanja datoteke.
   * @param idVozila - id vozila.
   */
  private void ucitajPodatke(String putanja, int idVozila, StringBuilder greske) {
    if (Files.exists(Paths.get(putanja))) {
      try (BufferedReader citac = new BufferedReader(new FileReader(putanja))) {
        citac.readLine();
        String redak;
        int brojRetka = 2;
        long prethodnoVrijeme = 0;

        List<CompletableFuture<Void>> futureObjekti = new ArrayList<>();

        while ((redak = citac.readLine()) != null) {
          try {
            String[] podaci = redak.split(",");
            long vrijeme = Long.parseLong(podaci[0]);
            long pauza = izracunajVrijemeSpavanja(prethodnoVrijeme, vrijeme);
            Thread.sleep(pauza);

            PodaciVozila podaciVozila = ucitajPodatkeIzRetka(redak, idVozila, brojRetka);

            String komandaSimulator = "VOZILO " + idVozila + " " + brojRetka + " "
                + podaciVozila.vrijeme() + " " + podaciVozila.brzina() + " " + podaciVozila.snaga()
                + " " + podaciVozila.struja() + " " + podaciVozila.visina() + " "
                + podaciVozila.gpsBrzina() + " " + podaciVozila.tempVozila() + " "
                + podaciVozila.postotakBaterija() + " " + podaciVozila.naponBaterija() + " "
                + podaciVozila.kapacitetBaterija() + " " + podaciVozila.tempBaterija() + " "
                + podaciVozila.preostaloKm() + " " + podaciVozila.ukupnoKm() + " "
                + podaciVozila.gpsSirina() + " " + podaciVozila.gpsDuzina() + "\n";

            CompletableFuture<Void> future =
                MrezneOperacije.posaljiZahtjevPosluziteljuAsinkronoDrugiNacin(this.adresaVozila,
                    this.mreznaVrataVozila, komandaSimulator);
            futureObjekti.add(future);

            brojRetka++;
            prethodnoVrijeme = vrijeme;
            Thread.sleep(trajanjePauze);
          } catch (NumberFormatException | InterruptedException e) {
            greske.append("Greška prilikom pretvaranja podataka u retku ").append(brojRetka)
                .append(": ").append(e.getMessage()).append("\n");
          }
        }

        CompletableFuture<Void> sviFutureObjekti =
            CompletableFuture.allOf(futureObjekti.toArray(new CompletableFuture[0]));
        try {
          sviFutureObjekti.get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }

      } catch (IOException e) {
        greske.append("Greška prilikom čitanja csv datoteke: ").append(putanja).append("\n");
      }
    } else {
      greske.append("Csv datoteka ne postoji: ").append(putanja).append("\n");
    }
  }

  /**
   * Metoda izracunajVrijemeSpavanja - izračunava vrijeme spavanje dretve između dva retka kod
   * čitanja podataka iz .csv datoteke.
   *
   * @param prethodnoVrijeme - vrijeme prethodnog retka u milisekundama
   * @param trenutnoVrijeme - vrijeme trenutnog retka u milisekundama
   * @return razlika - potrebno vrijeme spavanja dretve između dva retka u milisekundama
   */
  private long izracunajVrijemeSpavanja(long prethodnoVrijeme, long trenutnoVrijeme) {
    if (prethodnoVrijeme == 0) {
      return 0;
    }
    long razlika = trenutnoVrijeme - prethodnoVrijeme;
    return razlika * trajanjeSek / 1000;
  }

  /**
   * Metoda ucitajPodatkeIzRetka - kreira objekte tipa Record kako bi se vozila kasnije spremila u
   * sustav.
   *
   * @param redak - redak u .csv datoteci (jedan zapis)
   * @param id - id vozila
   * @param brojRetka - redni broj retka unutar .csv datoteke
   * @return objekt tipa Record podaci o vozilu
   * @throws NumberFormatException - number format iznimka (iznimke kod parsiranja)
   */
  private PodaciVozila ucitajPodatkeIzRetka(String redak, int id, int brojRetka)
      throws NumberFormatException {
    String[] podaci = redak.split(",");

    long vrijeme = Long.parseLong(podaci[0]);
    double brzina = Double.parseDouble(podaci[1]);
    double snaga = Double.parseDouble(podaci[2]);
    double struja = Double.parseDouble(podaci[3]);
    double visina = Double.parseDouble(podaci[4]);
    double gpsBrzina = Double.parseDouble(podaci[5]);
    int tempVozila = Integer.parseInt(podaci[6]);
    int postotakBaterija = Integer.parseInt(podaci[7]);
    double naponBaterija = Double.parseDouble(podaci[8]);
    int kapacitetBaterija = Integer.parseInt(podaci[9]);
    int tempBaterija = Integer.parseInt(podaci[10]);
    double preostaloKm = Double.parseDouble(podaci[11]);
    double ukupnoKm = Double.parseDouble(podaci[12]);
    double gpsSirina = Double.parseDouble(podaci[13]);
    double gpsDuzina = Double.parseDouble(podaci[14]);

    return new PodaciVozila(id, brojRetka, vrijeme, brzina, snaga, struja, visina, gpsBrzina,
        tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm,
        ukupnoKm, gpsSirina, gpsDuzina);
  }
}
