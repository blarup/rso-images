package si.thoughts.texts.services;

import si.thoughts.texts.lib.Text;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TextsBean {

    private Logger log = Logger.getLogger(TextsBean.class.getName());

    private List<Text> texts;

    @PostConstruct
    private void init() {
        texts = new ArrayList<>();
        texts.add(new Text(1, "Do you think there will be WW3?", "Hope not."));
    }

    public List<Text> getTexts() {
        return texts;
    }
}
