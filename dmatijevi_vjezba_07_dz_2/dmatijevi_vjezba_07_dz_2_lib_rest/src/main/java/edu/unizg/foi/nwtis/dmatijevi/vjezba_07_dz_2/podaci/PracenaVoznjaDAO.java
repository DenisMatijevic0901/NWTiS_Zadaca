package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa PracenaVoznjaDAO za rad s tablicom PraceneVoznje
 *
 * @author Denis Matijević
 */
public class PracenaVoznjaDAO {
  /** Veza na bazu podataka. */
  private Connection vezaBP;

  /**
   * Instantiates a new pracena voznja DAO.
   *
   * @param vezaBP veza na bazu podataka
   */
  public PracenaVoznjaDAO(Connection vezaBP) {
    super();
    this.vezaBP = vezaBP;
  }

  /**
   * Dohvati praćene vožnje od nekog vremena do nekog vremena.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return lista praćenih vožnji
   */
  public List<PracenaVoznja> dohvatiPraceneVoznje(long odVremena, long doVremena) {
    String upit = "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, "
        + "postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, "
        + "ukupnoKm, gpsSirina, gpsDuzina "
        + "FROM pracenevoznje WHERE vrijeme >= ? AND vrijeme <= ?";

    List<PracenaVoznja> praceneVoznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setLong(1, odVremena);
      s.setLong(2, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var pracenaVoznja = kreirajObjektPracenaVoznja(rs);
        praceneVoznje.add(pracenaVoznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(PracenaVoznjaDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return praceneVoznje;
  }

  /**
   * Kreiraj objekt praćena vožnja.
   *
   * @param rs skup rezultata SQL upisa
   * @return pracena voznja
   * @throws SQLException SQL iznimka
   */
  private PracenaVoznja kreirajObjektPracenaVoznja(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    int broj = rs.getInt("broj");
    long vrijeme = rs.getLong("vrijeme");
    double brzina = rs.getDouble("brzina");
    double snaga = rs.getDouble("snaga");
    double struja = rs.getDouble("struja");
    double visina = rs.getDouble("visina");
    double gpsBrzina = rs.getDouble("gpsBrzina");
    int tempVozila = rs.getInt("tempVozila");
    int postotakBaterija = rs.getInt("postotakBaterija");
    double naponBaterija = rs.getDouble("naponBaterija");
    int kapacitetBaterija = rs.getInt("kapacitetBaterija");
    int tempBaterija = rs.getInt("tempBaterija");
    double preostaloKm = rs.getDouble("preostaloKm");
    double ukupnoKm = rs.getDouble("ukupnoKm");
    double gpsSirina = rs.getDouble("gpsSirina");
    double gpsDuzina = rs.getDouble("gpsDuzina");

    PracenaVoznja pv = new PracenaVoznja(id, broj, vrijeme, brzina, snaga, struja, visina,
        gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija,
        preostaloKm, ukupnoKm, gpsSirina, gpsDuzina);
    return pv;
  }

  /**
   * Dohvati praćene vožnje vozila.
   *
   * @param id id vozila
   * @return lista praćenih vožnji
   */
  public List<PracenaVoznja> dohvatiPraceneVoznjeVozila(int id) {
    String upit = "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, "
        + "postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, "
        + "ukupnoKm, gpsSirina, gpsDuzina " + "FROM pracenevoznje WHERE id = ?";

    List<PracenaVoznja> praceneVoznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var pracenaVoznja = kreirajObjektPracenaVoznja(rs);
        praceneVoznje.add(pracenaVoznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(PracenaVoznjaDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return praceneVoznje;
  }

  /**
   * Dohvati praćene vožnje vozila unutar intervala.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return lista praćenih vožnji
   */
  public List<PracenaVoznja> dohvatiPraceneVoznjeVozila(int id, long odVremena, long doVremena) {
    String upit = "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, "
        + "postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, "
        + "ukupnoKm, gpsSirina, gpsDuzina "
        + "FROM pracenevoznje WHERE id = ? AND vrijeme >= ? AND vrijeme <= ?";

    List<PracenaVoznja> praceneVoznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      s.setLong(2, odVremena);
      s.setLong(3, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var pracenaVoznja = kreirajObjektPracenaVoznja(rs);
        praceneVoznje.add(pracenaVoznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(PracenaVoznjaDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return praceneVoznje;
  }

  /**
   * Dodaj praćenu vožnju.
   *
   * @param pracenaVoznja pracenaVoznja
   * @return true, ako je uspješno dodavanje
   */
  public boolean dodajPracenuVoznju(PracenaVoznja pracenaVoznja) {
    String upit =
        "INSERT INTO pracenevoznje (id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, "
            + "tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, "
            + "preostaloKm, ukupnoKm, gpsSirina, gpsDuzina) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {

      s.setInt(1, pracenaVoznja.getId());
      s.setInt(2, pracenaVoznja.getBroj());
      s.setLong(3, pracenaVoznja.getVrijeme());
      s.setDouble(4, pracenaVoznja.getBrzina());
      s.setDouble(5, pracenaVoznja.getSnaga());
      s.setDouble(6, pracenaVoznja.getStruja());
      s.setDouble(7, pracenaVoznja.getVisina());
      s.setDouble(8, pracenaVoznja.getGpsBrzina());
      s.setInt(9, pracenaVoznja.getTempVozila());
      s.setInt(10, pracenaVoznja.getPostotakBaterija());
      s.setDouble(11, pracenaVoznja.getNaponBaterija());
      s.setInt(12, pracenaVoznja.getKapacitetBaterija());
      s.setInt(13, pracenaVoznja.getTempBaterija());
      s.setDouble(14, pracenaVoznja.getPreostaloKm());
      s.setDouble(15, pracenaVoznja.getUkupnoKm());
      s.setDouble(16, pracenaVoznja.getGpsSirina());
      s.setDouble(17, pracenaVoznja.getGpsDuzina());

      int brojAzuriranja = s.executeUpdate();

      return brojAzuriranja == 1;

    } catch (Exception ex) {
      Logger.getLogger(PracenaVoznjaDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }
}
