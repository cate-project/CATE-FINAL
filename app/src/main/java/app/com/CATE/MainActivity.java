package app.com.CATE;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.CATE.models.YoutubeDataModel;
import app.com.youtubeapiv3.R;

public class MainActivity extends AppCompatActivity {
    String strId;
    public static String  AAA;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private Toolbar toolbar = null;
    public String category,channel;
    TextView txtResult;
    public ArrayList<YoutubeDataModel> listData = new ArrayList<>();

    public String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDDNXQW5vUsBy91h_swoSAc_uFFAG14Clo";  //here you should use your api key for testing purpose you can use this api also
    public String CHANNEL_ID = "UCEgdi0XIXXZ-qJOFPf4JSKw";  //here you should use your channel id for testing purpose you can use this api also
    public String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&channelId=" + CHANNEL_ID + "&eventType=live&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";
    public String PLAYLIST_ID = "PLFgquLnL59al_vjBToIrYqC2l-CiO78U6";//here you should use your playlist id for testing purpose you can use this api also
    public String PLAYLIST_GET_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + PLAYLIST_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        category=intent.getStringExtra("Category");

        strId = intent.getStringExtra("userID");
        AAA=strId;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  // 왼쪽 버튼 사용 여부 true
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_search_24px); // 왼쪽 버튼 이미지 설정
        actionBar.setDisplayShowTitleEnabled(false);    // 타이틀 안보이게 하기

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //setting the tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Channel"));
        tabLayout.addTab(tabLayout.newTab().setText("PlayList"));
        tabLayout.addTab(tabLayout.newTab().setText("Live"));
        tabLayout.addTab(tabLayout.newTab().setText("Category"));

        //setup the view pager
        final PagerAdapter adapter = new app.com.CATE.adapters.PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);      // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 클릭된 메뉴 아이템의 아이디 마다 switch 구절로 클릭시 동작을 설정한다.
        switch(item.getItemId()){
            case android.R.id.home :    // 검색 버튼
                Snackbar.make(toolbar,"Menu pressed",Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menu_add : // 추가 버튼
                Snackbar.make(toolbar,"Search menu pressed",Snackbar.LENGTH_SHORT).show();
                AddDialog dialog = new AddDialog(this);
                dialog.show();
                return true;
            case R.id.menu_account : // 계정 버튼
                Snackbar.make(toolbar,"Account menu pressed",Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout :// 로그아웃 버튼
                Snackbar.make(toolbar,"Logout menu pressed",Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //팝업 엑티비티 종료
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }


}
