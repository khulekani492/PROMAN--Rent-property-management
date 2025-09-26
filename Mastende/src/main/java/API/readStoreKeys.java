package API;


import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.emailvalidations.WaitingStrategy;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.ClientCertificateAuthenticationProvider;

import com.verifalia.api.emailvalidations.models.Validation;

import java.io.*;

public class readStoreKeys {

    String certAlias = "khule";
    String password = "khule@20ct15";

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
        InputStream is = readStoreKeys.class.getClassLoader()
                .getResourceAsStream("keystore/identity.jks");

        if (is == null) {
            System.err.println("identity.jks NOT found in classpath!");
        } else {
            System.out.println("identity.jks loaded successfully!");
        }
    }

}
