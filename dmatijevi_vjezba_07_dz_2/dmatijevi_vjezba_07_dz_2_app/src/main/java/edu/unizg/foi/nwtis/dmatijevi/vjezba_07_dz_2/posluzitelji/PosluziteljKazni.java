package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa PosluziteljKazni.
 *
 * @author Denis Matijević
 */
public class PosluziteljKazni {

  /** Format datuma na hrvatskom jeziku */
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

  /** Mrežna vrata PosluziteljKazni. */
  int mreznaVrata;

  /** Predložak za provjeru ispravnosti komande za kreiranje kazne. */
  private Pattern predlozakKazna =
      Pattern.compile("^VOZILO (?<id>\\d+) " + "(?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) "
          + "(?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) "
          + "(?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) "
          + "(?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande za kreiranje kazne. */
  private Matcher poklapanjeKazna;

  /** Predložak za provjeru ispravnosti komande za statistiku. */
  private static Pattern predlozakStatistika =
      Pattern.compile("^STATISTIKA " + "(?<vrijemeOd>\\d+) " + "(?<vrijemeDo>\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande za statistiku. */
  private static Matcher poklapanjeStatistika;

  /** Predložak za provjeru ispravnosti komande za vraćanje zadnje kazne vozila. */
  private static Pattern predlozakVratiZadnjuKaznu =
      Pattern.compile("^VOZILO " + "(?<id>\\d+) " + "(?<vrijemeOd>\\d+) " + "(?<vrijemeDo>\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande za vraćanje zadnje kazne vozila. */
  private static Matcher poklapanjeVratiZadnjuKaznu;

  /** Predložak za provjeru poslužitelja kazni. */
  private static Pattern predlozakProvjeriPosluzitelja = Pattern.compile("^TEST$");

  /** Poklapanje za provjeru poslužitelja kazni. */
  private static Matcher poklapanjeProvjeriPosluzitelja;

  /** Popis svih kazni u sustavu. */
  volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

  /**
   * Metoda main - provjerava unesene argumente, pokreće poslužitelja.
   *
   * @param args - uneseni argumenti (jedan (konfiguracijska datoteka)).
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    try {
      posluziteljKazni.preuzmiPostavke(args);
      posluziteljKazni.pokreniPosluzitelja();
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

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }

  /**
   * Metoda pokreniPosluzitelja - pokreće poslužitelj PosluziteljKazni
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
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
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda obradaZahtjeva - za dolazni zahtjev provjerava ispravnost komande te izvršava komandu.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor poslužitelja
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 49 Zahtjev je null.\n";
    }

    poklapanjeKazna = predlozakKazna.matcher(zahtjev);
    if (poklapanjeKazna.matches()) {
      return obradaZahtjevaKazna(zahtjev);
    }

    poklapanjeVratiZadnjuKaznu = predlozakVratiZadnjuKaznu.matcher(zahtjev);
    if (poklapanjeVratiZadnjuKaznu.matches()) {
      return obradaZahtjevaVratiZadnjuKaznu(zahtjev);
    }

    poklapanjeStatistika = predlozakStatistika.matcher(zahtjev);
    if (poklapanjeStatistika.matches()) {
      return obradaZahtjevaStatistika(zahtjev);
    }

    poklapanjeProvjeriPosluzitelja = predlozakProvjeriPosluzitelja.matcher(zahtjev);
    if (poklapanjeProvjeriPosluzitelja.matches()) {
      return obradaZahtjevaProvjeriPosluzitelja(zahtjev);
    }

    return "ERROR 40 Neispravna sintaksa komande.\n";
  }

  /**
   * Metoda obradaZahtjevaProvjeriPosluzitelja - provjera poslužitelja kazni.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor - OK, poslužitelj kazni je aktivan.
   */
  private String obradaZahtjevaProvjeriPosluzitelja(String zahtjev) {
    return "OK\n";
  }

  /**
   * Metoda obradaZahtjevaKazna - generiranje nove kazne za vozilo.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor - greška kod POST zahtjeva ili OK.
   */
  public String obradaZahtjevaKazna(String zahtjev) {
    var kazna = new PodaciKazne(Integer.valueOf(this.poklapanjeKazna.group("id")),
        Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
        Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
        Double.valueOf(this.poklapanjeKazna.group("brzina")),
        Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
        Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
        Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
        Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

    this.sveKazne.add(kazna);
    System.out.println("Id: " + kazna.id() + "  Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
        + "   Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + "  Brzina: " + kazna.brzina()
        + "  GPS: " + kazna.gpsSirina() + ",  " + kazna.gpsDuzina());

    JSONObject jsonKazna = new JSONObject();
    jsonKazna.put("id", Integer.valueOf(this.poklapanjeKazna.group("id")));
    jsonKazna.put("vrijemePocetak", Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")));
    jsonKazna.put("vrijemeKraj", Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")));
    jsonKazna.put("brzina", Double.valueOf(this.poklapanjeKazna.group("brzina")));
    jsonKazna.put("gpsSirina", Double.valueOf(this.poklapanjeKazna.group("gpsSirina")));
    jsonKazna.put("gpsDuzina", Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")));
    jsonKazna.put("gpsSirinaRadar", Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")));
    jsonKazna.put("gpsDuzinaRadar", Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

    HttpClient httpKlijent = HttpClient.newHttpClient();
    HttpRequest httpZahtjev =
        HttpRequest.newBuilder().uri(URI.create("http://20.24.5.5:8080/nwtis/v1/api/kazne"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonKazna.toString())).build();

    try {
      HttpResponse<String> httpOdgovor = httpKlijent.send(httpZahtjev, BodyHandlers.ofString());
      if (httpOdgovor.statusCode() == 200) {
        return "OK\n";
      } else {
        return "ERROR 42 Greška prilikom dodavanja kazne POST metodom.\n";
      }
    } catch (Exception e) {
      return "ERROR 42 Greška prilikom dodavanja kazne POST metodom.\n";
    }
  }

  /**
   * Metoda obradaZahtjevaVratiZadnjuKaznu - vraća zadnju kaznu za zadano vozilo.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor tj.vraća zadnju kaznu vozila ako postoji, a ako ne postoji vraća ERROR
   */
  public String obradaZahtjevaVratiZadnjuKaznu(String zahtjev) {
    int id = Integer.parseInt(poklapanjeVratiZadnjuKaznu.group("id"));
    long vrijemeOd = Long.parseLong(poklapanjeVratiZadnjuKaznu.group("vrijemeOd"));
    long vrijemeDo = Long.parseLong(poklapanjeVratiZadnjuKaznu.group("vrijemeDo"));

    PodaciKazne zadnjaKazna = null;
    for (PodaciKazne trenutnaKazna : this.sveKazne) {
      if (id == trenutnaKazna.id()) {
        if (trenutnaKazna.vrijemePocetak() >= vrijemeOd
            && trenutnaKazna.vrijemeKraj() <= vrijemeDo) {
          if (zadnjaKazna == null) {
            zadnjaKazna = trenutnaKazna;
          } else if (trenutnaKazna.vrijemeKraj() > zadnjaKazna.vrijemeKraj()) {
            zadnjaKazna = trenutnaKazna;
          }
        }
      }
    }

    if (zadnjaKazna == null) {
      return "ERROR 41 E-vozilo " + id + " nema kazne u zadanom vremenu.\n";
    } else {
      return "OK " + zadnjaKazna.vrijemePocetak() + " " + zadnjaKazna.brzina() + " "
          + zadnjaKazna.gpsSirinaRadar() + " " + zadnjaKazna.gpsDuzinaRadar() + "\n";
    }
  }

  /**
   * Metoda obradaZahtjevaStatistika - vraća statistiku kazni vozila.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return tj.vraća statistiku kazni vozila ako postoji, a ako ne postoji vraća ERROR
   */
  public String obradaZahtjevaStatistika(String zahtjev) {
    long vrijemeOd = Long.parseLong(poklapanjeStatistika.group("vrijemeOd"));
    long vrijemeDo = Long.parseLong(poklapanjeStatistika.group("vrijemeDo"));
    HashMap<Integer, Integer> sumaKazniZaVozilo = new HashMap<>();
    String vracenaStatistika = "OK ";

    for (PodaciKazne trenutnaKazna : this.sveKazne) {
      if (trenutnaKazna.vrijemePocetak() >= vrijemeOd && trenutnaKazna.vrijemeKraj() <= vrijemeDo) {
        int id = trenutnaKazna.id();
        if (sumaKazniZaVozilo.containsKey(id)) {
          sumaKazniZaVozilo.put(id, sumaKazniZaVozilo.get(id) + 1);
        } else {
          sumaKazniZaVozilo.put(id, 1);
        }
      }
    }

    if (sumaKazniZaVozilo.isEmpty()) {
      return "ERROR 49 nema kazni u zadanom vremenu.\n";
    }

    for (HashMap.Entry<Integer, Integer> podatak : sumaKazniZaVozilo.entrySet()) {
      vracenaStatistika += podatak.getKey() + " " + podatak.getValue() + "; ";
    }
    vracenaStatistika += "\n";
    return vracenaStatistika;
  }

  /**
   * Metoda postaviPoklapanjeKazna - koja služi za postavljanje poklapanja zbog testova.
   *
   * @param poklapanje - poklapanje za generiranje kazne
   */
  public void postaviPoklapanjeKazna(Matcher poklapanje) {
    this.poklapanjeKazna = poklapanje;
  }
}
