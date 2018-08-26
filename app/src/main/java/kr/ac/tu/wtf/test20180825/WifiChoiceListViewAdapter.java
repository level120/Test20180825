package kr.ac.tu.wtf.test20180825;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WifiChoiceListViewAdapter extends BaseAdapter {

    private ArrayList<WifiListViewItem> list;

    public WifiChoiceListViewAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_wifi_view_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.itemImg);
        TextView textTextView = (TextView) convertView.findViewById(R.id.itemText);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        WifiListViewItem listViewItem = list.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.icon);
        textTextView.setText(listViewItem.text);

        return convertView;
    }

    public void removeAll() {
        for (int i=0, size=list.size(); i<size; ++i)
            list.remove(0);
    }

    public void addItem(Drawable icon, String text) {
        WifiListViewItem item = new WifiListViewItem();

        item.icon = icon;
        item.text = text;

        list.add(item);
    }
}
