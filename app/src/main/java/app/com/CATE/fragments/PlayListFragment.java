package app.com.CATE.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
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
    private ArrayList<YoutubeDataModel> nListData = new ArrayList<>();
    String cateName, cateDetail, video_id, video_kind;
    int requestNum = 0;
    String userID;

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
        mListData = mainActivity.listData;
        initList(nListData);

        userID = mainActivity.strId;

        if (!mListData.isEmpty()) {
            for (int i = 0; i < mListData.size(); i++) {
                new RequestVideoThumbnail(i).execute();
            }
        }

        //new RequestYoutubeAPI().execute();
        return view;
    }


    private void initList(ArrayList<YoutubeDataModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoPostAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                if (youtubeDataModel.getVideo_kind().equals("YOUTUBE")) {                                //유튜브 플레이어
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                    intent.putExtra("userID", userID);
                    intent.putExtra("video_index", youtubeDataModel.getVideo_index());
                    startActivity(intent);
                }
                if (youtubeDataModel.getVideo_kind().equals("TWITCH")) {
                    Intent intent = new Intent(getActivity(), TwitchActivity.class);                   //트위치 플레이어
                    intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                    intent.putExtra("userID", userID);
                    intent.putExtra("video_index", youtubeDataModel.getVideo_index());
                    startActivity(intent);
                }
            }
        });
        mList_videos.setAdapter(adapter);
    }


    //동영상 썸네일 주소 받아오기
    class RequestVideoThumbnail extends AsyncTask<Void, String, String> {
        int num;

        RequestVideoThumbnail(int num) {
            this.num = num;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            //String THUMBNAIL_GET_URL = "https://www.googleapis.com/youtube/v3/videos?key=" + GOOGLE_YOUTUBE_API_KEY + "&part=snippet&id=" + video_id;

            if (mListData.get(num).getVideo_kind().equals("TWITCH")) {
                String THUMBNAIL_GET_URL = "https://api.twitch.tv/kraken/videos/" + mListData.get(num).getVideo_id() + "?client_id=ikngsfikq2ke5ub9hw5203pjekqp69";
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            YoutubeDataModel youtubeDataModel = new YoutubeDataModel();
            youtubeDataModel.setTitle(mListData.get(requestNum).getTitle());
            youtubeDataModel.setThumbnail(mListData.get(requestNum).getThumbnail());
            youtubeDataModel.setVideo_id(mListData.get(requestNum).getVideo_id());
            youtubeDataModel.setVideo_kind(mListData.get(requestNum).getVideo_kind());
            youtubeDataModel.setVideo_index(mListData.get(requestNum).getVideo_index());

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String thumbnail = jsonObject.getJSONObject("thumbnails").getJSONArray("large").getJSONObject(0).getString("url");
                    youtubeDataModel.setThumbnail(thumbnail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            nListData.add(youtubeDataModel);
            initList(nListData);
            requestNum++;
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
