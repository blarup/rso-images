package si.thoughts.texts.lib;

import java.time.Instant;

public class TextMetadata {

    private Integer textId;
    private String title;
    private String description;
    private Instant created;
    private Integer numOfRatings;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }

    public Integer getNumOfRatings(){return numOfRatings;}

    public void setNumOfRatings(Integer numOfRatings){this.numOfRatings = numOfRatings;}
}
