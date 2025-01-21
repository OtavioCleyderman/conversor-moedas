package br.com.java.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.Properties;

public class ExchangeRateClient {
    private final String apiKey;
    private final String apiUrl;

    public ExchangeRateClient() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar propriedades da aplicação", e);
        }
        this.apiKey = properties.getProperty("exchange.rate.api.key");
        this.apiUrl = properties.getProperty("exchange.rate.api.url");
    }

    public double getExchangeRate(String fromCurrency, String toCurrency, double amount) throws IOException {
        String amountFormatted = String.format("%.0f", amount);
        String url = String.format("%s/convert?from=%s&to=%s&amount=%s&api_key=%s", apiUrl, fromCurrency, toCurrency, amountFormatted, apiKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpEntity entity = httpClient.execute(httpGet).getEntity();

            if (entity != null) {
                String response = EntityUtils.toString(entity);
                System.out.println("Resposta da API: " + response); // Exibe a resposta da API para depuração
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response);

                if (!jsonNode.has("result")) {
                    throw new RuntimeException("Erro ao buscar taxas de conversão");
                }

                JsonNode resultNode = jsonNode.get("result");

                if (resultNode.has(toCurrency)) {
                    return resultNode.get(toCurrency).asDouble();
                } else {
                    throw new RuntimeException("Taxa de conversão não encontrada para a moeda de destino: " + toCurrency);
                }
            } else {
                throw new IOException("Erro ao obter a resposta da API");
            }
        } catch (ClientProtocolException e) {
            throw new IOException("Erro de protocolo ao acessar a API", e);
        } catch (ParseException e) {  // Capturando a exceção ParseException
            throw new IOException("Erro ao processar a resposta da API", e);
        }
    }
}
