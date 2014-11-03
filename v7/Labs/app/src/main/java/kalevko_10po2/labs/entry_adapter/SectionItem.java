package kalevko_10po2.labs.entry_adapter;

/**
 * Created by admin on 11.09.2014.
 */
public class SectionItem implements Item{

    private final String title;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public boolean isSection() {
        return true;
    }

}
