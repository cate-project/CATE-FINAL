package app.com.CATE.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import app.com.CATE.MainActivity;
import app.com.CATE.adapters.CategoryAdapter;
import app.com.CATE.models.CategoryModel;
import app.com.CATE.models.YoutubeDataModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends ListFragment {

    private CategoryAdapter adapter = null;
    private ListView listView;
    private List<CategoryModel> categoryList;
    private MainActivity mainActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Adapter 생성 및 Adapter 지정.
        adapter = new CategoryAdapter();
        mainActivity = (MainActivity) getActivity();
        setListAdapter(adapter);
        categoryList = new ArrayList<CategoryModel>();
//        adapter.addItem("1",
//                "음악" , "인기트랙 - 한국", "PLFgquLnL59alGJcdc0BEZJb2p7IgkL0Oe") ;
//        adapter.addItem("2",
//                "음악" , "인기트랙 - 리히텐슈타인", "PLFgquLnL59al_vjBToIrYqC2l-CiO78U6") ;
//        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.icon_sport),
//                "스포츠", "인기", "P L8fVUTBmJhHJmpP7sLb9JfLtdwCmYX9xC") ;
//        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music),
//                "영화", "예고편", "PLzjFbaFzsmMTVOSvr4n2XOJZC_-Fgtukm") ;
//        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music),
//                "영화", "기생충", "PLTT8pIIYwG188DHwILUTZBfrYOsY8oo8H") ;
//        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music),
//                "영화", "어벤져스: 엔드게임", "PLtHJJiKvsulsRQ7Sr7OpxOZF0n1XBmw90") ;
//        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.icon_action_rollplaying_game),
//                "액션 롤플레잉 게임", "예고편", "PLHRoF1XPhCHXQhWkViQveuVa-k6P8_aD2") ;

        try {
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(mainActivity.category);


            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            String cateId, cateName, cateDetail, cateKey;

            //JSON 배열 길이만큼 반복문을 실행
            while (count < jsonArray.length()) {
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);

                cateId = object.getString("id");//여기서 ID가 대문자임을 유의
                cateName = object.getString("name");
                cateDetail = object.getString("detail");
                cateKey = object.getString("key");

                //값들을 User클래스에 묶어줍니다
                CategoryModel CategoryModel = new CategoryModel(cateId, cateName, cateDetail, cateKey);
                categoryList.add(CategoryModel);//리스트뷰에 값을 추가해줍니다
                adapter.addItem(cateId, cateName, cateDetail, cateKey);
                count++;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //아이템 클릭 이벤트
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // get TextView's Text.
        CategoryModel item = (CategoryModel) l.getItemAtPosition(position);
        new PcOfSeat().execute(item.getDetail());

        mainActivity.PLAYLIST_ID = item.getKey();
        String titleStr = item.getName();
        String descStr = item.getDetail();
        String iconDrawable = item.getId();
        String channelStr = item.getKey();

        // TODO : use item data.
    }

    public void addItem(String icon, String title, String desc, String channel) {
        adapter.addItem(icon, title, desc, channel);
    }


    public class PcOfSeat extends AsyncTask<String, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "https://catapro.000webhostapp.com/fow_getVideo.php";
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String tag = (String) params[0];

                URL url = new URL(target);//URL 객체 생성

                String data = URLEncoder.encode("tag", "UTF-8") + "=" + URLEncoder.encode(tag, "UTF-8");
                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());

                wr.write(data);
                wr.flush();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();


                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //    Toast.makeText(mainActivity, result, Toast.LENGTH_SHORT).show();
            mainActivity.channel = result;

            try {
                ArrayList<YoutubeDataModel> listData = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                while (count < jsonArray.length()) {

                    JSONObject object = jsonArray.getJSONObject(count);

                    YoutubeDataModel youtubeObject = new YoutubeDataModel();
                    String thumbnail = "";
                    String video_id = "";
                    String cateName, video_kind, cateDetail;
                    int video_index;

                    cateName = object.getString("title");
                    video_kind = object.getString("kind");
                    cateDetail = object.getString("url");
                    video_index = Integer.parseInt(object.getString("id"));

                    if (video_kind.equals("YOUTUBE")) {
                        video_id = cateDetail.substring(cateDetail.indexOf("=") + 1);
                        thumbnail = "https://i.ytimg.com/vi/" + video_id + "/hqdefault.jpg";
                    }
                    if (video_kind.equals("TWITCH")) {
                        String[] split = cateDetail.split("/");
                        video_id = split[4];
                        thumbnail = "https://static-cdn.jtvnw.net/jtv_user_pictures/twitch-profile_image-8a8c5be2e3b64a9a-300x300.png";
                    }

                    youtubeObject.setVideo_index(video_index);
                    youtubeObject.setTitle(cateName);
                    youtubeObject.setThumbnail(thumbnail);
                    youtubeObject.setVideo_id(video_id);
                    youtubeObject.setVideo_kind(video_kind);

                    count++;
                    listData.add(youtubeObject);
                }
                mainActivity.listData = listData;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}