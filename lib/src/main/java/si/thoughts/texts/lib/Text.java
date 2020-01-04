package si.thoughts.texts.lib;

import java.time.Instant;

public class Text {

    private Integer textId;
    private Instant createdAt;
    private String title;
    private String content;

    public Text(Integer textId, String title, String content) {
        this.textId = textId;
        this.createdAt = Instant.now();
        this.title = title;
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
