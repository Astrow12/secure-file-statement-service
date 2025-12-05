package com.test.statementservice.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

public class StatementCheckSumUtil {

    private static final String CHECK_SUM_ALGO = "SHA-256";

    public static String calculateChecksum(Path filePath) throws Exception {

        byte[] fileBytes = Files.readAllBytes(filePath);
        MessageDigest digest = MessageDigest.getInstance(CHECK_SUM_ALGO);
        byte[] hashBytes = digest.digest(fileBytes);
        return HexFormat.of().formatHex(hashBytes);
    }
}
