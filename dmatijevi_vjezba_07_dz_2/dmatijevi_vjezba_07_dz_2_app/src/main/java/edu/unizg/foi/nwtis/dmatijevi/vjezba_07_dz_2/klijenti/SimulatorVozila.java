package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.klijenti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa SimulatorVozila.
 *
 * @author Denis Matijević
 */
public class SimulatorVozila {

  /**
   * Adresa PosluziteljZaVozila koja je potrebna kod spajanja SimulatorVozila na
   * PosluziteljZaVozila.
   */
  private String adresaVozila;

  /**
   * Mrežna vrata PosluziteljZaVozila koja su potrebna kod spajanja SimulatorVozila na
   * PosluziteljZaVozila.
   */
  private int mreznaVrataVozila;

  /** Trajanje sekunde postavka koja se čita iz konfiguracije. */
  private int trajanjeSek;

  /** Trajanje pauze postavka koja se čita iz konfiguracije. */
  private int trajanjePauze;

  /** Predložak za provjeru ispravnosti komande unesenih argumenata. */
  private static Pattern predlozakSimulator =
      Pattern.compile("(?<nazivDatoteke>^[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.(txt|xml|bin|json)) "
          + "(?<nazivDatotekeCsv>[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.csv) (?<id>\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande unesenih argumenata. */
  private static Matcher poklapanjeSimulator;

  /**
   * Metoda main - provjerava unesene argumente, ovisno o ulaznim argumentima provjerava i obrađuje
   * zahtjev klijenta.
   *
   * @param args - uneseni argumenti (tri (konfiguracijska datoteka, .csv datoteka i id vozila)).
   */
  public static void main(String[] args) {
    if ((args.length != 3)) {
      System.out.println("Broj argumenata nije 3.");
      return;
    }
    SimulatorVozila simulatorVozila = new SimulatorVozila();
    try {
      String argumentiSpojeno = String.join(" ", args);
      simulatorVozila.preuzmiPostavke(args);

      poklapanjeSimulator = predlozakSimulator.matcher(argumentiSpojeno);
      var statusSimulator = poklapanjeSimulator.matches();
      if (statusSimulator) {
        simulatorVozila.ucitajPodatke(poklapanjeSimulator);
      } else {
        System.out.println("Komanda ne zadovoljava format.");
      }

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Metoda preuzmiPostavke - preuzima postavke iz konfiguracijske datoteke.
   *
   * @param args - uneseni argumenti
   * @throws NeispravnaKonfiguracija - neispravna konfiguracija iznimka (iznimka kod problema s
   *         učitavanjem konfiguracijske datoteke)
   * @throws NumberFormatException - number format iznimka (iznimke kod parsiranja)
   * @throws UnknownHostException - unknown host iznimka
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.adresaVozila = konfig.dajPostavku("adresaVozila");
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.trajanjeSek = Integer.valueOf(konfig.dajPostavku("trajanjeSek"));
    this.trajanjePauze = Integer.valueOf(konfig.dajPostavku("trajanjePauze"));
  }

  /**
   * Metoda ucitajPodatke - čita podatke iz .csv datoteke te ih asinkrono šalje PosluziteljZaVozila.
   *
   * @param poklapanje - poklapanje za provjeru ispravnosti komande unesenih argumenata.
   */
  private void ucitajPodatke(Matcher poklapanje) {
    if (Files.exists(Paths.get(poklapanje.group("nazivDatotekeCsv")))) {
      try (BufferedReader citac =
          new BufferedReader(new FileReader(poklapanje.group("nazivDatotekeCsv")))) {

        citac.readLine();
        String redak;
        int id = Integer.parseInt(poklapanje.group("id"));
        int brojRetka = 2;
        long prethodnoVrijeme = 0;

        List<CompletableFuture<Void>> futureObjekti = new ArrayList<>();

        while ((redak = citac.readLine()) != null) {
          try {
            String[] podaci = redak.split(",");
            long vrijeme = Long.parseLong(podaci[0]);
            long pauza = izracunajVrijemeSpavanja(prethodnoVrijeme, vrijeme);
            Thread.sleep(pauza);

            PodaciVozila podaciVozila = ucitajPodatkeIzRetka(redak, id, brojRetka);

            String komandaSimulator = "VOZILO " + id + " " + brojRetka + " "
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
            System.out.println("Greška prilikom pretvaranja podataka u retku " + brojRetka + ": "
                + e.getMessage());
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
        System.out.println(
            "Greška prilikom čitanja csv datoteke: " + poklapanje.group("nazivDatotekeCsv"));
      }
    } else {
      System.out.println("Csv datoteka ne postoji: " + poklapanje.group("nazivDatotekeCsv"));
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
