package app.com.CATE.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.com.CATE.TwitchActivity;
import app.com.CATE.adapters.VideoPostAdapter;
import app.com.CATE.DetailsActivity;
import app.com.CATE.MainActivity;
import app.com.youtubeapiv3.R;
import app.com.CATE.interfaces.OnItemClickListener;
import app.com.CATE.models.YoutubeDataModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends Fragment {

    public String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDDNXQW5vUsBy91h_swoSAc_uFFAG14Clo";//here you should use your api key for testing purpose you can use this api also
    public String PLAYLIST_ID = "PLHRoF1XPhCHXQhWkViQveuVa-k6P8_aD2";//here you should use your playlist id for testing purpose you can use this api also
    public String PLAYLIST_GET_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + PLAYLIST_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";

    private RecyclerView mList_videos = null;
    private VideoPostAdapter adapter = null;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();
    String cateName, cateDetail, video_id, video_kind;


    public MainActivity mainActivity;

    public PlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        mainActivity = (MainActivity) getActivity();
        PLAYLIST_GET_URL = mainActivity.PLAYLIST_GET_URL;
        mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos);
        initList(mListData);

        try {
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(mainActivity.channel);

            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            while (count < jsonArray.length()) {

                JSONObject object = jsonArray.getJSONObject(count);

                YoutubeDataModel youtubeObject = new YoutubeDataModel();
                String thumbnail = "";

                cateName = object.getString("title");
                video_kind = object.getString("kind");
                cateDetail = object.getString("url");

                if(video_kind.equals("YOUTUBE")) {
                    video_id = cateDetail.substring(cateDetail.indexOf("=") + 1);
                    thumbnail = "https://i.ytimg.com/vi/" + video_id + "/hqdefault.jpg";
                }
                if(video_kind.equals("TWITCH")) {
                    String[] split = cateDetail.split("/");
                    video_id = split[4];
                    thumbnail = "https://static-cdn.jtvnw.net/jtv_user_pictures/twitch-profile_image-8a8c5be2e3b64a9a-300x300.png";
                }

                youtubeObject.setTitle(cateName);
                youtubeObject.setThumbnail(thumbnail);
                youtubeObject.setVideo_id(video_id);
                youtubeObject.setVideo_kind(video_kind);

                count++;
                mListData.add(youtubeObject);

            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        //new RequestVideoThumbnail().execute();
        //new RequestYoutubeAPI().execute();
        return view;
    }


    private void initList(ArrayList<YoutubeDataModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoPostAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                if(youtubeDataModel.getVideo_kind().equals("YOUTUBE")) {                                //유튜브 플레이어
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                    startActivity(intent);
                }
                if(youtubeDataModel.getVideo_kind().equals("TWITCH")) {
                    Intent intent = new Intent(getActivity(), TwitchActivity.class);                   //트위치 플레이어
                    intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                    startActivity(intent);
                }
            }
        });
        mList_videos.setAdapter(adapter);

    }


    //동영상 썸네일 주소 받아오기
    private class RequestVideoThumbnail extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            //String THUMBNAIL_GET_URL = "https://www.googleapis.com/youtube/v3/videos?key=" + GOOGLE_YOUTUBE_API_KEY + "&part=snippet&id=" + video_id;
            String THUMBNAIL_GET_URL = "https://www.googleapis.com/youtube/v3/videos?key=AIzaSyDEBonEwRKGBCXdaSEn-M9z_yW17GuQUL8&part=snippet&id=" + video_id;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(THUMBNAIL_GET_URL);
            Log.e("URL", THUMBNAIL_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            /*if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    JSONObject json = jsonArray.getJSONObject(0);
                    JSONObject jsonSnippet = json.getJSONObject("snippet");
                    String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    //String thumbnail = "https://i.ytimg.com/vi/dn9g1v4Sht0/hqdefault.jpg";

                    YoutubeDataModel youtubeObject = new YoutubeDataModel();
                    youtubeObject.setTitle(cateName);
                    youtubeObject.setThumbnail(thumbnail);
                    youtubeObject.setVideo_id(video_id);
                    mListData.add(youtubeObject);

                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }*/
            try {
                //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
                JSONObject jsonObject = new JSONObject(mainActivity.channel);

                //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                while (count < jsonArray.length()) {

                    JSONObject object = jsonArray.getJSONObject(count);

                    YoutubeDataModel youtubeObject = new YoutubeDataModel();


                    cateName = object.getString("title");
                    cateDetail = object.getString("url");
                    video_id = cateDetail.substring(cateDetail.indexOf("=") + 1);
                    String thumbnail = "https://i.ytimg.com/vi/" + video_id + "/hqdefault.jpg";
                    youtubeObject.setTitle(cateName);
                    youtubeObject.setThumbnail(thumbnail);
                    youtubeObject.setVideo_id(video_id);

                    count++;
                    mListData.add(youtubeObject);

                }
            } catch (
                    Exception e) {
                e.printStackTrace();
            }



            //API 키 만료됐을 때




            initList(mListData);
        }

    }





    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(PLAYLIST_GET_URL);
            Log.e("URL", PLAYLIST_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("kind")) {
                        if (json.getString("kind").equals("youtube#playlistItem")) {
                            YoutubeDataModel youtubeObject = new YoutubeDataModel();
                            JSONObject jsonSnippet = json.getJSONObject("snippet");
                            String video_id = "";
                            if (jsonSnippet.has("resourceId")) {
                                JSONObject jsonResource = jsonSnippet.getJSONObject("resourceId");
                                video_id = jsonResource.getString("videoId");

                            }
                            String title = jsonSnippet.getString("title");
                            String description = jsonSnippet.getString("description");
                            String publishedAt = jsonSnippet.getString("publishedAt");
                            String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                            youtubeObject.setTitle(title);
                            youtubeObject.setDescription(description);
                            youtubeObject.setPublishedAt(publishedAt);
                            youtubeObject.setThumbnail(thumbnail);
                            youtubeObject.setVideo_id(video_id);
                            mList.add(youtubeObject);

                        }
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }
}
