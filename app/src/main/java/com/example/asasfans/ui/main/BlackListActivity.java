package com.example.asasfans.ui.main;

import static com.example.asasfans.util.ViewUtilsKt.setMargin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asasfans.AsApplication;
import com.example.asasfans.R;
import com.example.asasfans.data.DBOpenHelper;
import com.example.asasfans.data.VideoDataStoragedInMemory;
import com.example.asasfans.ui.customcomponent.RecyclerViewDecoration;
import com.example.asasfans.ui.main.adapter.BlackListAdapter;
import com.example.asasfans.ui.main.adapter.PubdateVideoAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BlackListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private List<VideoDataStoragedInMemory> videoDataStoragedInMemoryList = new ArrayList<>();
    private PubdateVideoAdapter pubdateVideoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);

        View emptyView = findViewById(R.id.emptyViewBlack);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AsApplication.Companion.getStatusBarHeight());
        emptyView.setLayoutParams(layoutParams);
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        setMargin(appBarLayout, 0, AsApplication.Companion.getStatusBarHeight(),0,0);

        recyclerView = findViewById(R.id.black_list_recyclerview);
        setMargin(recyclerView, 0, 0, 0, AsApplication.Companion.getStatusBarHeight());
        ImageView back = findViewById(R.id.activity_black_list_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this,"blackList.db",null,1);
        SQLiteDatabase sqliteDatabase = dbOpenHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query("blackBvid",null,null,null,null,null,null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                videoDataStoragedInMemoryList.add(new VideoDataStoragedInMemory(
                        cursor.getString(cursor.getColumnIndex("PicUrl")),
                        cursor.getString(cursor.getColumnIndex("Title")),
                        cursor.getInt(cursor.getColumnIndex("Duration")),
                        cursor.getString(cursor.getColumnIndex("Author")),
                        cursor.getInt(cursor.getColumnIndex("ViewNum")),
                        cursor.getInt(cursor.getColumnIndex("LikeNum")),
                        cursor.getString(cursor.getColumnIndex("Tname")),
                        cursor.getString(cursor.getColumnIndex("bvid")),
                        true
                ));
            }
        }
        pubdateVideoAdapter = new BlackListAdapter(BlackListActivity.this, videoDataStoragedInMemoryList);
        recyclerView.setAdapter(pubdateVideoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setInitialPrefetchItemCount(2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(12, 12));
        sqliteDatabase.close();
        dbOpenHelper.close();
    }

}
