package kalevko_10po2.labs.entry_adapter;

/**
 * Created by admin on 11.09.2014.
 */
public class EntryItem implements Item{

    public final String title;
    public final String subtitle;

    public EntryItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}