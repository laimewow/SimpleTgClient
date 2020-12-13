package com.laimewow.stgc.client.message;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    private String textMessage = "";
    private final TgClient client;
    private final List<byte[]> images = new ArrayList<>();
    private boolean silent = false;
    private boolean disablePreview = false;
    private String chatId;

    private boolean markdown;

    MessageBuilder(TgClient client) {
        this.client = client;
        chatId = client.getConfiguration().getChatId();
    }


    public MessageBuilder addTextMessage(String text) {
        textMessage = text;
        return this;
    }

    public MessageBuilder addImage(byte[] image) {
        if (image != null) {
            images.add(image);
        }
        return this;
    }

    public MessageBuilder disableNotification() {
        silent = true;
        return this;
    }

    public MessageBuilder enableNotification() {
        silent = false;
        return this;
    }

    public MessageBuilder disablePreview() {
        disablePreview = true;
        return this;
    }

    public MessageBuilder enablePreview() {
        disablePreview = false;
        return this;
    }


    public String send() {
        return client.sendMessage(this);
    }

    public void toChat(String chatId) {
        chatId = chatId;
    }

    public MarkdownSupport markdown() {
        markdown = true;
        MarkdownSupport markdownSupport = new MarkdownSupport();
        return markdownSupport;
    }


    public class MarkdownSupport {

        private StringBuilder buffer = new StringBuilder();

        private void processText(String escape, String text) {
            text = text.replace(escape, "\\" + escape);
            buffer.append(escape).append(text).append(escape);
        }

        public MarkdownSupport lineBreak() {
            buffer.append("\n");
            return this;
        }

        public MarkdownSupport bold(String text) {
            processText("*", text);
            return this;
        }

        public MarkdownSupport italic(String text) {
            processText("_", text);
            return this;
        }

        public MarkdownSupport underline(String text) {
            processText("__", text);
            return this;
        }

        public MarkdownSupport strikethrough(String text) {
            processText("~", text);
            return this;
        }

        public MarkdownSupport formatLine(String text) {
            processText("`", text);
            return this;
        }

        public MarkdownSupport formatBlock(String text) {
            buffer.append("```").append("\n").append(text).append("\n").append("```");
            return this;
        }

        public MarkdownSupport url(String text, String url) {
            text = text.replace("!", "\\!");
            buffer.append("[").append(text).append("]").append("(").append(url).append(")");
            return this;
        }

        public MessageBuilder end() {
            MessageBuilder.this.textMessage += buffer.toString();
            return MessageBuilder.this;
        }

        public MarkdownSupport text(String text) {
            buffer.append(text);
            return this;
        }
    }


    public MessageStruct toMessageStruct() {
        MessageStruct result = new MessageStruct();
        result.setChatId(chatId);
        result.setDisableNotification(silent);
        result.setDisableWebPagePreview(disablePreview);
        result.setText(textMessage);
        if (markdown) {
            result.setParseMode("MarkdownV2");
        }
        return result;
    }

}
