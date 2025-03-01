package com.ticketbox.auth_service.utils;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyStoreUtil {

    static {
        // Đăng ký Bouncy Castle nếu chưa có
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

//    @Value("${daw}")
    static String KEYSTORE_PATH = "keystore.p12";
    static String KEYSTORE_PASSWORD = "keystore_pass";

    public static KeyStore loadOrCreateKeyStore() throws Exception {
        File keystoreFile = new File(KEYSTORE_PATH);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        if (keystoreFile.exists()) {
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
            }
        } else {
            keyStore.load(null, KEYSTORE_PASSWORD.toCharArray());
            saveKeyStore(keyStore);
        }
        return keyStore;
    }

    public static void saveKeyStore(KeyStore keyStore) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_PATH)) {
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
        }
    }

    public static void generateAndStoreUserKey(String userId, KeyStore keyStore, KeyPair keyPair) throws Exception {

        X509Certificate cert = generateSelfSignedCertificate(keyPair);

        KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(
                keyPair.getPrivate(), new Certificate[]{cert}
        );
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());
        keyStore.setEntry(userId, privateKeyEntry, passwordProtection);

        saveKeyStore(keyStore);
    }

    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        X500Name dnName = new X500Name("CN=User, O=MyApp, C=VN");

        BigInteger serialNumber = new BigInteger(64, new SecureRandom());
        Date startDate = new Date();
        Date expiryDate = new Date(startDate.getTime() + (365L * 24 * 60 * 60 * 1000)); // 1 năm

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                dnName, serialNumber, startDate, expiryDate, dnName, keyPair.getPublic()
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC") // Dùng Bouncy Castle để ký
                .build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }

    public static PrivateKey getUserPrivateKey(String userId, KeyStore keyStore) throws Exception {
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());
        KeyStore.Entry entry = keyStore.getEntry(userId, passwordProtection);

        if (entry instanceof KeyStore.PrivateKeyEntry) {
            return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
        }
        return null;
    }
}
