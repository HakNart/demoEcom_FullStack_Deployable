package com.kt.rest.demoEcommerce.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@Component
@Slf4j
public class KeyUtils {
    private static String PRIVATE_HEADER = "-----BEGIN PRIVATE KEY-----\n";
    private static String PRIVATE_FOOTER = "\n-----END PRIVATE KEY-----\n";
    private static String PUBLIC_HEADER = "-----BEGIN PUBLIC KEY-----\n";
    private static String PUBLIC_FOOTER = "\n-----END PUBLIC KEY-----\n";
    public static String ALGORITHM_RSA = "RSA";

    final Environment environment;

    @Value("${access-token.private}")
    private String accessTokenPrivateKeyPath;

    @Value("${access-token.public}")
    private String accessTokenPublicKeyPath;

    private KeyPair _accessTokenKeyPair;

    public KeyUtils(Environment environment) {
        this.environment = environment;
    }



    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(_accessTokenKeyPair)) {
            _accessTokenKeyPair = loadKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
//            _accessTokenKeyPair = loadKeyPairBase64(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return _accessTokenKeyPair;
    }
    private KeyPair loadKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair = getKeyPair(publicKeyPath, privateKeyPath);
        if (keyPair != null) return keyPair;

        // Only run non-prod active profile
        File directory = new File("access_token_keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            log.info("Generating new public and private keys: {}, {}", publicKeyPath, privateKeyPath);
            keyPair = generateKeyPair(ALGORITHM_RSA,2048);
            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Problem generating the keys");
        }

        return keyPair;
    }

    private static void saveKeyBase64(String filePath, Key key, String header, String footer) {
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(header.getBytes());
            fos.write(encoder.encodeToString(key.getEncoded()).getBytes());
            fos.write(footer.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private KeyPair loadKeyPairBase64(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair1 = getKeyPair(publicKeyPath, privateKeyPath);
        if (keyPair1 != null) return keyPair1;
        KeyPair keyPair;
        File directory = new File("access-token-keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            log.info("Generating new public and private keys: {}, {}", publicKeyPath, privateKeyPath);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

            saveKeyToFile(keyPair.getPublic(), publicKeyPath, PUBLIC_HEADER, PUBLIC_FOOTER);
            saveKeyToFile(keyPair.getPrivate(), privateKeyPath, PRIVATE_HEADER, PRIVATE_FOOTER);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem generating the keys");
        }
        return keyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair;

        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            log.info("loading keys from file: {}, {}", publicKeyPath, privateKeyPath);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
                keyPair = new KeyPair(publicKey, privateKey);
                return keyPair;
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equals("prod"))) {
                throw new RuntimeException("public and private keys don't exist");
            }
        }
        return null;
    }

    private static byte[] sanitizeKeyBase64(byte[] keyBytes, String header, String footer) {
        try {
            String keyString = new String(keyBytes);
            keyString = keyString.replace(header, "");
            keyString = keyString.replace(footer, "");
            byte[] newKeyBytes = Base64.getDecoder().decode(keyString.getBytes());
            return newKeyBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    private static KeyPair generateKeyPair(String algorithm,int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    private static void saveKeyToFile(Key key, String filePath, String header, String footer) {
        try {
            byte[] keyBytes = key.getEncoded();
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(header.getBytes());
                fos.write(Base64.getEncoder().encodeToString(keyBytes).getBytes());
                fos.write(footer.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }
    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }
}
