package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * Klasa MrezneOperacije.
 */
public class MrezneOperacije {


  /**
   * Šalje zahtjev poslužitelju.
   *
   * @param adresa adresa poslužitelja
   * @param mreznaVrata mrežna vrata poslužitelja
   * @param poruka tekst poruke koja se šalje
   * @return odgovor. Ako nije u redu vraća se null, inače primljeni odgovor od poslužitelja
   */
  public static String posaljiZahtjevPosluzitelju(String adresa, int mreznaVrata, String poruka) {
    try (Socket mreznaUticnica = new Socket(adresa, mreznaVrata)) {
      BufferedReader citac =
          new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
      OutputStream out = mreznaUticnica.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      pisac.print(poruka);
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      var odgovor = citac.readLine();
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
      return odgovor;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Šalje zahtjev poslužitelju asinkrono.
   *
   * @param adresa adresa poslužitelja
   * @param mreznaVrata mrežna vrata poslužitelja
   * @param poruka tekst poruke koja se šalje
   * @return odgovor. Ako nije u redu vraća se null, inače primljeni odgovor od poslužitelja
   */
  public static CompletableFuture<Void> posaljiZahtjevPosluziteljuAsinkrono(String adresa,
      int mreznaVrata, String poruka) {
    return CompletableFuture.runAsync(() -> {
      try (Socket mreznaUticnica = new Socket(adresa, mreznaVrata)) {
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        pisac.print(poruka);
        pisac.flush();
        mreznaUticnica.shutdownOutput();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Šalje zahtjev poslužitelju asinkrono drugi nacin.
   *
   * @param adresa adresa poslužitelja
   * @param mreznaVrata mrežna vrata poslužitelja
   * @param poruka tekst poruke koja se šalje
   * @return odgovor. Ako nije u redu vraća se null, inače primljeni odgovor od poslužitelja
   */
  // Napravljeno prema primjeru s predavanja - Primjer33_26
  public static CompletableFuture<Void> posaljiZahtjevPosluziteljuAsinkronoDrugiNacin(String adresa,
      int mreznaVrata, String poruka) {
    CompletableFuture<Void> future = new CompletableFuture<>();

    try {
      AsynchronousSocketChannel klijent = AsynchronousSocketChannel.open();
      InetSocketAddress serverAdresa = new InetSocketAddress(adresa, mreznaVrata);
      Attachment attach = new Attachment();
      attach.klijentskiKanal = klijent;
      attach.buffer = ByteBuffer.wrap(poruka.getBytes(StandardCharsets.UTF_8));
      attach.future = future;

      klijent.connect(serverAdresa, attach, new CompletionHandler<Void, Attachment>() {
        @Override
        public void completed(Void result, Attachment attach) {
          WriteHandler wHandler = new WriteHandler();
          attach.klijentskiKanal.write(attach.buffer, attach, wHandler);
        }

        @Override
        public void failed(Throwable e, Attachment attach) {
          attach.future.completeExceptionally(e);
        }
      });
    } catch (IOException e) {
      future.completeExceptionally(e);
    }

    return future;
  }

  static class Attachment {
    AsynchronousSocketChannel klijentskiKanal;
    ByteBuffer buffer;
    CompletableFuture<Void> future;
  }

  static class WriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer result, Attachment attach) {
      if (attach.buffer.hasRemaining()) {
        attach.klijentskiKanal.write(attach.buffer, attach, this);
      } else {
        try {
          attach.klijentskiKanal.close();
          attach.future.complete(null);
        } catch (IOException e) {
          attach.future.completeExceptionally(e);
        }
      }
    }

    @Override
    public void failed(Throwable e, Attachment attach) {
      attach.future.completeExceptionally(e);
    }
  }
}
