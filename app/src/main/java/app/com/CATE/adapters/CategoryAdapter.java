package app.com.CATE.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import app.com.CATE.models.CategoryModel;
import app.com.youtubeapiv3.R;

/**
 * Created by mdmunirhossain on 12/18/17.
 */

public class CategoryAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CategoryModel> listViewItemList = new ArrayList<CategoryModel>() ;

    // ListViewAdapter의 생성자
    public CategoryAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_layout, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView cate_id = (ImageView) convertView.findViewById(R.id.text_id) ;
        TextView cate_name = (TextView) convertView.findViewById(R.id.text_name) ;
        TextView cate_detail = (TextView) convertView.findViewById(R.id.text_detail) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CategoryModel listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        if(listViewItem.getName().equals("영화")) {
            cate_id.setImageResource(R.drawable.icon_movie);
        }else if(listViewItem.getName().equals("게임")) {
            cate_id.setImageResource(R.drawable.ic_baseline_games_24px);
        }else if(listViewItem.getName().equals("스포츠")) {
            cate_id.setImageResource(R.drawable.icon_sport);
        }else if(listViewItem.getName().equals("음악")) {
            cate_id.setImageResource(R.drawable.icon_music);
        }else{
            cate_id.setImageResource(R.drawable.ic_baseline_clear_24px);
        }
        cate_name.setText(listViewItem.getName());
        cate_detail.setText(listViewItem.getDetail());
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String id, String title, String desc, String channelId) {
        CategoryModel item = new CategoryModel(id, title, desc, channelId);

        item.setId(id);
        item.setName(title);
        item.setDetail(desc);
        item.setKey(channelId);
        listViewItemList.add(item);
    }
}
