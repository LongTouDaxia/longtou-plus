package com.mall.xiaomi;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.lettuce.core.models.command.CommandDetail.Flag.RANDOM;

@SpringBootTest
public class ApplicationTest {

    @Test
    public void test() {
        // 如果是普通字符串，直接使用
        byte[] keyBytes = "115605Liu".getBytes(StandardCharsets.UTF_8);
        // 确保密钥长度至少256位
        if (keyBytes.length < 32) {
            // 如果不够长，扩展到32字节
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
            System.out.println(new SecretKeySpec(paddedKey, "HmacSHA256"));
        }
    }
}
