package com.github.RandyDpoe45.amilozdemo.config.keys;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class KeyConfig {

    @Value("${security-server-config.access-token.private-key-path}")
    private String accessTokenPrivateKeyPath;

    @Value("${security-server-config.access-token.public-key-path}")
    private String accessTokenPublicKeyPath;

    @Value("${security-server-config.refresh-token.private-key-path}")
    private String refreshTokenPrivateKeyPath;

    @Value("${security-server-config.refresh-token.public-key-path}")
    private String refreshTokenPublicKeyPath;

    @Value("${security-server-config.default-creation-key-directory}")
    private String defaultKeyCreationFolder;

    @Value("${security-server-config.rsa-key-size}")
    private Integer rsaKeySize;

    @Bean
    public KeyPair accessTokenKeyPair() {
        return getKeyPair(accessTokenPrivateKeyPath, accessTokenPublicKeyPath);
    }

    @Bean
    public KeyPair refreshTokenKeyPair() {
        return getKeyPair(refreshTokenPrivateKeyPath, refreshTokenPublicKeyPath);
    }

    @SneakyThrows
    private KeyPair getKeyPair(
            String privateKeyPath,
            String publicKeyPath
    ) {
        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        if (!publicKeyFile.exists() || !privateKeyFile.exists())
            return generateKeyPair(
                    privateKeyPath,
                    publicKeyPath
            );

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    @SneakyThrows
    private KeyPair generateKeyPair(
            String privateKeyPath,
            String publicKeyPath
    ) {
        File directory = new File(defaultKeyCreationFolder);
        if (!directory.exists())
            directory.mkdir();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(rsaKeySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        try (
                FileOutputStream fosPub = new FileOutputStream(publicKeyPath);
                FileOutputStream fosPri = new FileOutputStream(privateKeyPath)
        ) {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
            fosPub.write(publicKeySpec.getEncoded());
            fosPri.write(privateKeySpec.getEncoded());
        }
        return keyPair;
    }


}
