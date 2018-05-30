package keSt93.springmoviedb.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Level;

public final class DataUriHelper {

    public static URI getDataURIForURL(URL url) {
        URI dataUri = null;
        if (null != url) {
            try (InputStream inStreamGuess = url.openStream();
                 InputStream inStreamConvert = url.openStream();
                 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                // String contentType = URLConnection.guessContentTypeFromStream(inStreamGuess);
                String contentType = "image/jpeg";
                if (null != contentType) {
                    byte[] chunk = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inStreamConvert.read(chunk)) > 0) {
                        os.write(chunk, 0, bytesRead);
                    }
                    os.flush();
                    dataUri = new URI("data:" + contentType + ";base64," +
                            Base64.getEncoder().encodeToString(os.toByteArray()));
                } else {
                    System.out.println("could not get content type from " + url.toExternalForm());
                }
            } catch (IOException e) {
                System.out.println("error loading data from url");
            } catch (URISyntaxException e) {
                System.out.println("error building uri");
            }
        }
        return dataUri;
    }
}
