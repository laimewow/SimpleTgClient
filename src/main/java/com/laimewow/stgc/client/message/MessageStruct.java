package com.laimewow.stgc.client.message;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
class MessageStruct {
    @JsonProperty(value = "message_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String messageId;

    @JsonProperty(value = "chat_id")
    private String chatId;

    @JsonProperty(value = "text")
    private String text;

    @JsonProperty(value = "parse_mode")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String parseMode;

    @JsonProperty(value = "disable_web_page_preview")
    private boolean disableWebPagePreview;

    @JsonProperty(value = "disable_notification")
    private boolean disableNotification;
}
