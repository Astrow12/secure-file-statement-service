package com.test.statementservice.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HexFormat;

public class StatementCheckSumUtil {

    private static final String CHECK_SUM_ALGO = "SHA-256";

    public static String calculateChecksum(MultipartFile uploadedFile) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(CHECK_SUM_ALGO);

        try (InputStream is = uploadedFile.getInputStream()) {
            byte[] buffer = new byte[8192]; // 8KB buffer
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }
}
