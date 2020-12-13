## Simple Telegram Message Client

This allows you to send simple messages to any chat your bot belongs to.

### Quick Start

### build.gradle
```gradle
repositories {
...
maven { url 'https://jitpack.io' }
...
}
```


```
dependencies {
...
implementation 'com.github.laimewow:SimpleTgClient:master-SNAPSHOT'
...
}
```


### Setup

#### Plain Java

```
TgClientConfiguration configuration = new TgClientConfiguration();
configuration.setChatId("<chat id>");
configuration.setToken("<bot token>");
TgClient client = new TgClient(configuration, new ObjectMapper());
```

#### Spring boot

Just define a `@Bean` of TgClient in your configuration

### Usage

```
client.messageBuilder()
      .markdown()
          .text("this is the simple line of text").lineBreak()
          .bold("It's a bold texdt").lineBreak()
          .italic("Italic text").lineBreak()
          .strikethrough("strikethrough text").lineBreak()
          .underline("underlined text").lineBreak()
          .formatLine("format line").lineBreak()
          .formatBlock("format block").lineBreak()
          .url("Link description", "Link url").lineBreak()
      .end()
      .disableNotification()
      .send();
```

You may use such methods as `disableNotification()` or `enableNotification()` to manage push sound of bot's message
You also may use `disablePreview()` and `enablePreview()` to disable link preview in the message.

## What's next

- Images / Photos
- Files
- Async variant