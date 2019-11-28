package com.mycompany.oshi.sprint;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class TesteSlack {

    private static HttpURLConnection con;

    public String mensagemSlack() throws IOException {

        String url = "https://hooks.slack.com/services/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";

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/json");

            JSONObject meuJson = new JSONObject();

            meuJson.put("text", "Teste Modila0");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {

                wr.writeBytes(meuJson.toString());
            }

            StringBuilder content;

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());

        } finally {

            con.disconnect();
        }

        return ("");
    }
}
