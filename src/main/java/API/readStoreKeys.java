package API;


import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.emailvalidations.WaitingStrategy;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.ClientCertificateAuthenticationProvider;

import com.verifalia.api.emailvalidations.models.Validation;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;

public class readStoreKeys {

    String certAlias = "1";
    String password = "Khule@20ct15";

//    Load the resource as a stream.
//
//    Copy it to a temporary file on disk.
//
//    Pass that temporary file to the constructor.
    //Copying  to a temporary the File references  identity keystore and truststore.
    private File copyResourceToTempFile(String resourcePath, String prefix) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) throw new FileNotFoundException(resourcePath);
        File tempFile = File.createTempFile(prefix, ".jks");
        tempFile.deleteOnExit();
        try (OutputStream os = new FileOutputStream(tempFile)) {
            is.transferTo(os);
        }
        return tempFile;
    }


    public VerifaliaRestClient createClient() throws IOException {
        File identityFile = copyResourceToTempFile("keystore/identity.jks", "identity");
        File trustStoreFile = copyResourceToTempFile("keystore/truststore.jks", "truststore");

        return new VerifaliaRestClient(
                new ClientCertificateAuthenticationProvider(certAlias, password, identityFile, trustStoreFile)
        );
    }
    public static void main(String[] args) {
        String keystorePath = "src/main/resources/keystore/identity.jks";
        String truststorePath = "src/main/resources/keystore/truststore.jks";
        String keystorePassword = "Khule@20ct15";  // Your keystore password
        String alias = "1"; // your key alias

        try {
            // Load keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

            if (!keyStore.containsAlias(alias)) {
                System.out.println("Alias '" + alias + "' not found!");
            } else {
                System.out.println("Alias '" + alias + "' found!");
            }

            // Load truststore
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(truststorePath), keystorePassword.toCharArray());

            // Initialize KeyManager and TrustManager
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keystorePassword.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            System.out.println("SSLContext initialized successfully! Keystore and Truststore are usable.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize SSLContext: " + e.getMessage());
        }
    }
    }



