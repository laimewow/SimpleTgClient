package com.laimewow.stgc.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TgClientConfiguration {
    private String token;
    private String chatId;
    private boolean isAsync = false;
}
