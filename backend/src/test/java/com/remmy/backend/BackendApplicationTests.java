package com.remmy.backend;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void statTest() throws Exception {
        Pattern pattern = Pattern.compile("^Mode: ([a-z]+)$");
        StringReader reader = new StringReader("Zookeeper version: 3.7.0-e3704b390a6697bfdf4b0bef79e3da7a4f6bac4b, built on 2021-03-17 09:46 UTC\n" +
                "Clients:\n" +
                " /192.168.109.6:53782[0](queued=0,recved=1,sent=0)\n" +
                "\n" +
                "Latency min/avg/max: 0/0.0/0\n" +
                "Received: 1\n" +
                "Sent: 0\n" +
                "Connections: 1\n" +
                "Outstanding: 0\n" +
                "Zxid: 0x15000000fb\n" +
                "Mode: follower\n" +
                "Node count: 10\n" +
                "Connection closed by foreign host.");
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                log.info("find {}", matcher.group(1));
            }
        }
    }

}
