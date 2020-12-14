package com.laimewow.stgc.client.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laimewow.stgc.client.config.TgClientConfiguration;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class TgClient {

    @Getter
    private final TgClientConfiguration configuration;
    private final String connectionString;

    private final ObjectMapper objectMapper;
    private ExecutorService executorService = Executors.newSingleThreadExecutor(p -> new Thread(p, "TgClientExecutor"));


    public TgClient(TgClientConfiguration configuration, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.configuration = configuration;
        connectionString = String.format("https://api.telegram.org/bot%s/sendMessage", configuration.getToken());
    }

    public MessageBuilder messageBuilder() {
        return new MessageBuilder(this);
    }

    @SneakyThrows
    Future<String> sendMessage(@NotNull MessageBuilder messageBuilder) {
        if (!configuration.isAsync()) {
            return new FutureTask<>(() -> _sendMessage(messageBuilder));
        }
        else {
            return executorService.submit(() -> _sendMessage(messageBuilder));
        }
    }

    private String _sendMessage(MessageBuilder messageBuilder) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String json = objectMapper.writeValueAsString(messageBuilder.toMessageStruct());
            HttpPost post = new HttpPost(connectionString);
            HttpEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = httpClient.execute(post);
            InputStream content = response.getEntity().getContent();
            JsonNode jsonNode = objectMapper.readTree(content);
            return jsonNode.get("result").get("message_id").asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
