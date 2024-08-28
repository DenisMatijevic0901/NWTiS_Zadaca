package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.klijenti;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa Klijent.
 *
 * @author Denis Matijević
 */
public class Klijent {

  /** Adresa PosluziteljKazni koja je potrebna kod spajanja Klijent na PosluziteljKazni. */
  private String adresaKazne;

  /** Mrežna vrata PosluziteljKazni koja su potrebna kod spajanja Klijent na PosluziteljKazni. */
  private int mreznaVrataKazne;

  /** Predložak za provjeru ispravnosti komande u slučaju tri unesena argumenta (statistika). */
  private static Pattern predlozakTriArgumenta =
      Pattern.compile("(?<nazivDatoteke>^[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.(txt|xml|bin|json)) "
          + "(?<vrijemeOd>\\d+) " + "(?<vrijemeDo>\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande u slučaju tri argumenta (statistika). */
  private static Matcher poklapanjeTriArgumenta;

  /**
   * Predložak za provjeru ispravnosti komande u slučaju četiri unesena argumenta (vrati zadnju
   * kaznu vozila).
   */
  private static Pattern predlozakCetiriArgumenta =
      Pattern.compile("(?<nazivDatoteke>^[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.(txt|xml|bin|json)) "
          + "(?<id>\\d+) " + "(?<vrijemeOd>\\d+) " + "(?<vrijemeDo>\\d+)$");

  /**
   * Poklapanje za provjeru ispravnosti komande u slučaju četiri unesena argumenta (vrati zadnju
   * kaznu vozila).
   */
  private static Matcher poklapanjeCetiriArgumenta;

  /**
   * Metoda main - provjerava unesene argumente, ovisno o ulaznim argumentima provjerava i obrađuje
   * zahtjev klijenta.
   *
   * @param args - uneseni argumenti (tri (konfiguracijska datoteka, vrijeme od i vrijeme do) ili
   *        četiri argumenta(konfiguracijska datoteka, id vozila, vrijeme od i vrijeme do)).
   */
  public static void main(String[] args) {
    if ((args.length != 3) && (args.length != 4)) {
      System.out.println("Broj argumenata nije 3 ili 4.");
      return;
    }
    Klijent klijent = new Klijent();
    try {
      String argumentiSpojeno = String.join(" ", args);
      klijent.preuzmiPostavke(args);

      if (args.length == 3) {
        poklapanjeTriArgumenta = predlozakTriArgumenta.matcher(argumentiSpojeno);
        var statusTriArgumenta = poklapanjeTriArgumenta.matches();
        if (statusTriArgumenta) {
          klijent.vratiStatistiku(poklapanjeTriArgumenta);
        }
      } else if (args.length == 4) {
        poklapanjeCetiriArgumenta = predlozakCetiriArgumenta.matcher(argumentiSpojeno);
        var statusCetiriArgumenta = poklapanjeCetiriArgumenta.matches();
        if (statusCetiriArgumenta) {
          klijent.vratiZadnjuKaznu(poklapanjeCetiriArgumenta);
        } else {
          System.out.println("Komanda ne zadovoljava format.");
        }
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

    this.adresaKazne = konfig.dajPostavku("adresaKazne");
    this.mreznaVrataKazne = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }


  /**
   * Metoda vratiStatistiku - u evidenciji kazni traži podatke o broju kazni unutar zadanog
   * vremenskog intervala od do. Ako postoje kazne vraća: OK idVozilo brojKazni; idVozilo brojKazni;
   * idVozilo brojKazni;.
   *
   * @param poklapanje - poklapanje za provjeru ispravnosti komande u slučaju tri argumenta
   *        (statistika).
   * @return true, ako je dobiven odgovor od PosluziteljKazni
   * @return false, ako nije dobiven odgovor od PosluziteljKazni
   */
  private boolean vratiStatistiku(Matcher poklapanje) {
    String komandaStatistika =
        "STATISTIKA " + poklapanje.group("vrijemeOd") + " " + poklapanje.group("vrijemeDo") + "\n";
    String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaKazne,
        this.mreznaVrataKazne, komandaStatistika);

    if (odgovor != null) {
      System.out.println(odgovor);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda vratiZadnjuKaznu - u evidenciji kazni traži podatke o vozilu unutar zadanog vremenskog
   * intervala od do. Ako postoje kazne vraća zadnju (najsvježiju) kaznu: OK vrijeme brzina
   * gpsSirinaRadar gpsDuzinaRadar.
   *
   * @param poklapanje - poklapanje za provjeru ispravnosti komande u slučaju četiri unesena
   *        argumenta (vrati zadnju kaznu vozila).
   * @return true, ako je dobiven odgovor od PosluziteljKazni
   * @return false, ako nije dobiven odgovor od PosluziteljKazni
   */
  private boolean vratiZadnjuKaznu(Matcher poklapanje) {
    String komandaVratiZadnjuKaznu = "VOZILO " + poklapanje.group("id") + " "
        + poklapanje.group("vrijemeOd") + " " + poklapanje.group("vrijemeDo") + "\n";
    String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaKazne,
        this.mreznaVrataKazne, komandaVratiZadnjuKaznu);

    if (odgovor != null) {
      System.out.println(odgovor);
      return true;
    } else {
      return false;
    }
  }
}
