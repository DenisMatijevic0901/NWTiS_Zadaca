package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.CentralniSustav;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.PosluziteljZaVozila;

// TODO: Auto-generated Javadoc
/**
 * Klasa RadnikZaVozila.
 *
 * @author Denis Matijević
 */
public class RadnikZaVozila implements Runnable {

  /** Mrežna utičnica poslužitelja za vozila. */
  private Socket mreznaUticnica;

  /** Poslužitelj za vozila. */
  private PosluziteljZaVozila posluziteljZaVozila;

  /** Centralni sustav. */
  private CentralniSustav centralniSustav;

  /** Predložak za provjeru ispravnosti komande koju šalje SimulatorVozila. */
  private Pattern predlozakSimulator = Pattern
      .compile("^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) "
          + "(?<snaga>-?\\d+([.]\\d+)?) (?<struja>-?\\d+([.]\\d+)?) (?<visina>\\d+[.]\\d+) "
          + "(?<gpsBrzina>\\d+[.]\\d+) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) "
          + "(?<naponBaterija>\\d+[.]\\d+) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) "
          + "(?<preostaloKm>\\d+[.]\\d+) (?<ukupnoKm>\\d+[.]\\d+) (?<gpsSirina>\\d+[.]\\d+) "
          + "(?<gpsDuzina>\\d+[.]\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande koju šalje SimulatorVozila. */
  private Matcher poklapanjeSimulator;

  /** Predložak za provjeru komande vozilo start. */
  private Pattern predlozakVoziloStart = Pattern.compile("^VOZILO START " + "(?<id>\\d+)$");

  /** Poklapanje za provjeru komande vozilo start. */
  private Matcher poklapanjeVoziloStart;

  /** Predložak za provjeru komande vozilo stop. */
  private Pattern predlozakVoziloStop = Pattern.compile("^VOZILO STOP " + "(?<id>\\d+)$");

  /** Poklapanje za provjeru komande vozilo stop. */
  private Matcher poklapanjeVoziloStop;


  /**
   * Inicijalizacija objekta RadnikZaVozila.
   *
   * @param mreznaUticnica - mrežna utičnica poslužitelja za vozila
   * @param centralniSustav - centralni sustav
   * @param posluziteljZaVozila - poslužitelj za vozila
   */
  public RadnikZaVozila(Socket mreznaUticnica, CentralniSustav centralniSustav,
      PosluziteljZaVozila posluziteljZaVozila) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.centralniSustav = centralniSustav;
    this.posluziteljZaVozila = posluziteljZaVozila;
  }

  /**
   * Metoda run - obrađivanje dolaznih zahtjeva.
   */
  @Override
  public void run() {
    try {
      BufferedReader citac =
          new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
      OutputStream out = mreznaUticnica.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      var redak = citac.readLine();
      mreznaUticnica.shutdownInput();
      pisac.println(obradaZahtjeva(redak));
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      mreznaUticnica.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Metoda obradaZahtjeva - za dolazni zahtjev provjerava ispravnost komande te izvršava komandu.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 29 Zahtjev je null.\n";
    }

    poklapanjeSimulator = predlozakSimulator.matcher(zahtjev);
    if (poklapanjeSimulator.matches()) {
      obradaZahtjevaSimulatora(zahtjev);
    }

    poklapanjeVoziloStart = predlozakVoziloStart.matcher(zahtjev);
    if (poklapanjeVoziloStart.matches()) {
      return obradaZahtjevaVoziloStart(zahtjev);
    }

    poklapanjeVoziloStop = predlozakVoziloStop.matcher(zahtjev);
    if (poklapanjeVoziloStop.matches()) {
      return obradaZahtjevaVoziloStop(zahtjev);
    }

    return "ERROR 20 Neispravna sintaksa komande.\n";
  }


  /**
   * Metoda obradaZahtjevaSimulatora - provjeravanje je li vozilo u dosegu radara.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   */
  private void obradaZahtjevaSimulatora(String zahtjev) {
    spremiVozilo(zahtjev);
    posaljiPost();

    int id = Integer.valueOf(this.poklapanjeSimulator.group("id"));
    if (this.centralniSustav.pracenaVozila.containsKey(id)) {
      posaljiPostPracenaVoznja();
    } else {
      System.out.println("ERROR 29 Vozilo s ID-om " + this.poklapanjeSimulator.group("id")
          + " nije pronađeno u kolekciji vozila čiji se podaci o vožnji šalju na RESTful web servis"
          + " za praćenje odabranih e-vozila.");
      provjeriDoseg();
    }
  }

  /**
   * Metoda posaljiPost - slanje POST zahtjeva za dodavanje vožnje.
   */
  private void posaljiPost() {
    JSONObject jsonVoznja = kreirajVoznjuJson();

    HttpClient httpKlijent = HttpClient.newHttpClient();
    HttpRequest httpZahtjev =
        HttpRequest.newBuilder().uri(URI.create("http://20.24.5.5:8080/nwtis/v1/api/simulacije"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonVoznja.toString())).build();

    try {
      HttpResponse<String> httpOdgovor = httpKlijent.send(httpZahtjev, BodyHandlers.ofString());
      if (httpOdgovor.statusCode() == 200) {
        // System.out.println("OK");
      } else {
        // System.out.println("ERROR 29 Greška prilikom dodavanja vožnje POST metodom.");
      }
    } catch (Exception e) {
      // System.out.println("ERROR 29 Greška prilikom dodavanja vožnje POST metodom.");
    }
  }

  /**
   * Metoda posaljiPostPracenaVoznja - slanje POST zahtjeva za dodavanje praćene vožnje.
   */
  private void posaljiPostPracenaVoznja() {
    JSONObject jsonPracenaVoznja = kreirajPracenuVoznjuJson();

    HttpClient httpKlijentPracenaVoznja = HttpClient.newHttpClient();
    HttpRequest httpZahtjevPracenaVoznja =
        HttpRequest.newBuilder().uri(URI.create("http://20.24.5.5:8080/nwtis/v1/api/vozila"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPracenaVoznja.toString())).build();

    try {
      HttpResponse<String> httpOdgovorPracenaVoznja =
          httpKlijentPracenaVoznja.send(httpZahtjevPracenaVoznja, BodyHandlers.ofString());
      if (httpOdgovorPracenaVoznja.statusCode() == 200) {
        provjeriDoseg();
      } else {
        System.out.println("ERROR 21 Greška prilikom dodavanja praćene vožnje POST metodom.");
      }
    } catch (Exception e) {
      System.out.println("ERROR 21 Greška prilikom dodavanja praćene vožnje POST metodom.");
    }
  }



  /**
   * Metoda provjeriDoseg - provjerava je li vozilo u dosegu radara.
   */
  private void provjeriDoseg() {
    for (Integer kljuc : this.centralniSustav.sviRadari.keySet()) {
      PodaciRadara radar = this.centralniSustav.sviRadari.get(kljuc);
      double udaljenost = GpsUdaljenostBrzina.udaljenostKm(radar.gpsSirina(), radar.gpsDuzina(),
          Double.parseDouble(this.poklapanjeSimulator.group("gpsSirina")),
          Double.parseDouble(this.poklapanjeSimulator.group("gpsDuzina")));

      if (udaljenost <= ((double) radar.maksUdaljenost() / 1000.0)) {
        String komandaPracenjaVozila = "VOZILO " + this.poklapanjeSimulator.group("id") + " "
            + this.poklapanjeSimulator.group("vrijeme") + " "
            + this.poklapanjeSimulator.group("brzina") + " "
            + this.poklapanjeSimulator.group("gpsSirina") + " "
            + this.poklapanjeSimulator.group("gpsDuzina") + "\n";

        String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(),
            radar.mreznaVrataRadara(), komandaPracenjaVozila);

        if (odgovor != null) {
          System.out.println(odgovor);
        }
      }
    }
  }


  /**
   * Metoda kreirajVoznjuJson - kreira JSON objekt vožnje.
   *
   * @return JSONObject objekt
   */
  private JSONObject kreirajVoznjuJson() {
    JSONObject jsonVoznja = new JSONObject();
    jsonVoznja.put("id", Integer.valueOf(this.poklapanjeSimulator.group("id")));
    jsonVoznja.put("broj", Integer.valueOf(this.poklapanjeSimulator.group("broj")));
    jsonVoznja.put("vrijeme", Long.valueOf(this.poklapanjeSimulator.group("vrijeme")));
    jsonVoznja.put("brzina", Double.valueOf(this.poklapanjeSimulator.group("brzina")));
    jsonVoznja.put("snaga", Double.valueOf(this.poklapanjeSimulator.group("snaga")));
    jsonVoznja.put("struja", Double.valueOf(this.poklapanjeSimulator.group("struja")));
    jsonVoznja.put("visina", Double.valueOf(this.poklapanjeSimulator.group("visina")));
    jsonVoznja.put("gpsBrzina", Double.valueOf(this.poklapanjeSimulator.group("gpsBrzina")));
    jsonVoznja.put("tempVozila", Integer.valueOf(this.poklapanjeSimulator.group("tempVozila")));
    jsonVoznja.put("postotakBaterija",
        Integer.valueOf(this.poklapanjeSimulator.group("postotakBaterija")));
    jsonVoznja.put("naponBaterija",
        Double.valueOf(this.poklapanjeSimulator.group("naponBaterija")));
    jsonVoznja.put("kapacitetBaterija",
        Integer.valueOf(this.poklapanjeSimulator.group("kapacitetBaterija")));
    jsonVoznja.put("tempBaterija", Integer.valueOf(this.poklapanjeSimulator.group("tempBaterija")));
    jsonVoznja.put("preostaloKm", Double.valueOf(this.poklapanjeSimulator.group("preostaloKm")));
    jsonVoznja.put("ukupnoKm", Double.valueOf(this.poklapanjeSimulator.group("ukupnoKm")));
    jsonVoznja.put("gpsSirina", Double.valueOf(this.poklapanjeSimulator.group("gpsSirina")));
    jsonVoznja.put("gpsDuzina", Double.valueOf(this.poklapanjeSimulator.group("gpsDuzina")));

    return jsonVoznja;
  }

  /**
   * Metoda kreirajPracenuVoznjuJson - kreira JSON objekt praćene vožnje.
   *
   * @return JSONObject objekt
   */
  private JSONObject kreirajPracenuVoznjuJson() {
    JSONObject jsonPracenaVoznja = new JSONObject();
    jsonPracenaVoznja.put("id", Integer.valueOf(this.poklapanjeSimulator.group("id")));
    jsonPracenaVoznja.put("broj", Integer.valueOf(this.poklapanjeSimulator.group("broj")));
    jsonPracenaVoznja.put("vrijeme", Long.valueOf(this.poklapanjeSimulator.group("vrijeme")));
    jsonPracenaVoznja.put("brzina", Double.valueOf(this.poklapanjeSimulator.group("brzina")));
    jsonPracenaVoznja.put("snaga", Double.valueOf(this.poklapanjeSimulator.group("snaga")));
    jsonPracenaVoznja.put("struja", Double.valueOf(this.poklapanjeSimulator.group("struja")));
    jsonPracenaVoznja.put("visina", Double.valueOf(this.poklapanjeSimulator.group("visina")));
    jsonPracenaVoznja.put("gpsBrzina", Double.valueOf(this.poklapanjeSimulator.group("gpsBrzina")));
    jsonPracenaVoznja.put("tempVozila",
        Integer.valueOf(this.poklapanjeSimulator.group("tempVozila")));
    jsonPracenaVoznja.put("postotakBaterija",
        Integer.valueOf(this.poklapanjeSimulator.group("postotakBaterija")));
    jsonPracenaVoznja.put("naponBaterija",
        Double.valueOf(this.poklapanjeSimulator.group("naponBaterija")));
    jsonPracenaVoznja.put("kapacitetBaterija",
        Integer.valueOf(this.poklapanjeSimulator.group("kapacitetBaterija")));
    jsonPracenaVoznja.put("tempBaterija",
        Integer.valueOf(this.poklapanjeSimulator.group("tempBaterija")));
    jsonPracenaVoznja.put("preostaloKm",
        Double.valueOf(this.poklapanjeSimulator.group("preostaloKm")));
    jsonPracenaVoznja.put("ukupnoKm", Double.valueOf(this.poklapanjeSimulator.group("ukupnoKm")));
    jsonPracenaVoznja.put("gpsSirina", Double.valueOf(this.poklapanjeSimulator.group("gpsSirina")));
    jsonPracenaVoznja.put("gpsDuzina", Double.valueOf(this.poklapanjeSimulator.group("gpsDuzina")));

    return jsonPracenaVoznja;
  }


  /**
   * Metoda spremiVozilo - spremanje vozila.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   */
  public void spremiVozilo(String zahtjev) {
    String[] podaci = zahtjev.split(" ");

    int id = Integer.parseInt(poklapanjeSimulator.group("id"));
    int broj = Integer.parseInt(poklapanjeSimulator.group("broj"));
    long vrijeme = Long.parseLong(poklapanjeSimulator.group("vrijeme"));
    double brzina = Double.parseDouble(poklapanjeSimulator.group("brzina"));
    double snaga = Double.parseDouble(poklapanjeSimulator.group("snaga"));
    double struja = Double.parseDouble(poklapanjeSimulator.group("struja"));
    double visina = Double.parseDouble(poklapanjeSimulator.group("visina"));
    double gpsBrzina = Double.parseDouble(poklapanjeSimulator.group("gpsBrzina"));
    int tempVozila = Integer.parseInt(poklapanjeSimulator.group("tempVozila"));
    int postotakBaterija = Integer.parseInt(poklapanjeSimulator.group("postotakBaterija"));
    double naponBaterija = Double.parseDouble(poklapanjeSimulator.group("naponBaterija"));
    int kapacitetBaterija = Integer.parseInt(poklapanjeSimulator.group("kapacitetBaterija"));
    int tempBaterija = Integer.parseInt(poklapanjeSimulator.group("tempBaterija"));
    double preostaloKm = Double.parseDouble(poklapanjeSimulator.group("preostaloKm"));
    double ukupnoKm = Double.parseDouble(poklapanjeSimulator.group("ukupnoKm"));
    double gpsSirina = Double.parseDouble(poklapanjeSimulator.group("gpsSirina"));
    double gpsDuzina = Double.parseDouble(poklapanjeSimulator.group("gpsDuzina"));

    PodaciVozila podaciVozila = new PodaciVozila(id, broj, vrijeme, brzina, snaga, struja, visina,
        gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija,
        preostaloKm, ukupnoKm, gpsSirina, gpsDuzina);

    RedPodaciVozila redPodaciVozila = new RedPodaciVozila(id);
    redPodaciVozila.dodajPodatakVozila(podaciVozila);
    this.centralniSustav.svaVozila.put(id, redPodaciVozila);
  }

  /**
   * Metoda postaviPoklapanjeSimulator - koja služi za postavljanje poklapanja zbog testova.
   *
   * @param poklapanje - poklapanje za provjeru ispravnosti komande koju šalje SimulatorVozila
   */
  public void postaviPoklapanjeSimulator(Matcher poklapanje) {
    this.poklapanjeSimulator = poklapanje;
  }

  /**
   * Metoda obradaZahtjevaVoziloStart - spremanje vozila u kolekciju praćenih vozila.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  private String obradaZahtjevaVoziloStart(String zahtjev) {
    int id = Integer.valueOf(this.poklapanjeVoziloStart.group("id"));
    if (this.centralniSustav.pracenaVozila.containsKey(id)) {
      return "OK\n";
    } else {
      this.centralniSustav.pracenaVozila.put(id, id);
      return "OK\n";
    }
  }

  /**
   * Metoda obradaZahtjevaVoziloStop - brisanje vozila iz kolekcije praćenih vozila.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  private String obradaZahtjevaVoziloStop(String zahtjev) {
    int id = Integer.valueOf(this.poklapanjeVoziloStop.group("id"));

    if (this.centralniSustav.pracenaVozila.containsKey(id)) {
      this.centralniSustav.pracenaVozila.remove(id);
      return "OK\n";
    } else {
      return "ERROR 29 Vozilo s ID-om " + id
          + " nije pronađeno u kolekciji vozila čiji se podaci o vožnji šalju na RESTful web"
          + " servis za praćenje odabranih e-vozila.\n";
    }
  }
}
