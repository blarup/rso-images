package si.thoughts.texts;

import java.time.Instant;

public class Text {
    private Integer id;
    private String title;
    private String content;
    private Instant created;

    public Integer getId(){return id;}

    public void setId(Integer id) {this.id = id;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title = title;}

    public String getContent(){return content;}

    public void setContent(String content){this.content = content;}

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
