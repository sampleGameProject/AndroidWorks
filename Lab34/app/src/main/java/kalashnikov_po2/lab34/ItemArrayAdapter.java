package kalashnikov_po2.lab34;

/**
 * Created by admin on 22.10.2014.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<String[]> {
    private List<String[]> dataList = new ArrayList<String[]>();

    static class ItemViewHolder {
        TextView id;
        TextView firstName;
        TextView lastName;
    }

    public ItemArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(String[] object) {
        super.add(object);
        dataList.add(object);
    }

    @Override
    public int getCount() {
        return this.dataList.size();
    }

    @Override
    public String[] getItem(int index) {
        return this.dataList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_layout, parent, false);
            viewHolder = new ItemViewHolder();
            viewHolder.id = (TextView) row.findViewById(R.id.id);
            viewHolder.firstName = (TextView) row.findViewById(R.id.firstName);
            viewHolder.lastName = (TextView) row.findViewById(R.id.lastName);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder)row.getTag();
        }
        String[] stat = getItem(position);
        viewHolder.id.setText(stat[0]);
        viewHolder.firstName.setText(stat[1]);
        viewHolder.lastName.setText(stat[2]);

        return row;
    }
}
