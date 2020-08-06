package com.example.fade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    GroupAdapter groupAdapter;
    GroupDAO dao;
    ArrayList<Group> groupList=new ArrayList<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //서랍에 연결해주면, 원래 배경은 어차피 서랍에서 백(activity_main.xml)으로 인클루드 해줌
        setContentView(R.layout.drawer_main);

        //툴바 설정
        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar) ;
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar() ;
        ab.setDisplayShowTitleEnabled(false);
        //이걸 해줘야 사용자가 버튼을 누를 수 있음
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //메뉴 버튼 설정
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawerLayout_main);

        //그룹을 보여줄 리스트뷰
        RecyclerView rv = findViewById(R.id.rv_groupList);
        //db 만들기
        GroupDatabase db =GroupDatabase.getInstance(getApplicationContext());
        dao=db.groupDAO();
        //groupList에 DB불러오기
        new SelectGroupThraed(dao, groupList).start();
        //어댑터 생성 후 리싸이클러뷰 어뎁터랑 연결
        groupAdapter = new GroupAdapter(dao, groupList);
        rv.setAdapter(groupAdapter);

        Group group1 = new Group("테스트");
        Group group2 = new Group("테스트2");
        InsertTGroupThraed t1 = new InsertTGroupThraed(dao, group1);
        InsertTGroupThraed t3 = new InsertTGroupThraed(dao, group2);
        //꼭 삽입하고 리스트뷰 갱신을 위해 groupList를 바뀐 DB로 재갱신 해줘야함!
        SelectGroupThraed t2 = new SelectGroupThraed(dao, groupList);
        t1.start();
        //join은 스레드가 끝날 때까지 기다려 줌
        try { t1.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        t3.start();
        //join은 스레드가 끝날 때까지 기다려 줌
        try { t3.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        t2.start();
        try { t2.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        groupAdapter.notifyDataSetChanged();



    }

    //메뉴 버튼을 눌렀을 때 드로우가 열리도록 해줌
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if(item_id==android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //다른 아이템이 클릭되었을 시 닫아줌
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}


//UI문제때문에 DAO는 메인스레드에서 쓸 수 없음, 백그라운드 스레드에서 실행해야 함!
class InsertTGroupThraed extends Thread {
    GroupDAO dao;
    Group group;
    public InsertTGroupThraed(GroupDAO dao, Group group) {
        this.dao = dao;
        this.group=group;
    }
    @Override
    public void run(){
        dao.insert(group);
    }
}
//UI문제때문에 DAO는 메인스레드에서 쓸 수 없음, 백그라운드 스레드에서 실행해야 함!
//인자인 personList를 갱신해줌
class SelectGroupThraed extends Thread {
    GroupDAO dao;
    ArrayList<Group> groupList;
    public SelectGroupThraed(GroupDAO dao, ArrayList<Group> groupList) {
        this.dao = dao;
        this.groupList = groupList;
    }
    @Override
    public void run(){
        this.groupList.clear();
        this.groupList.addAll(dao.getAll());
    }
}
class DeleteGroupThraed extends Thread {
    GroupDAO dao;
    Group  group;
    public DeleteGroupThraed(GroupDAO dao, Group group) {
        this.dao=dao;
        this.group = group;}
    @Override
    public void run(){
        dao.delete(this.group);
    }
}


class  GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GVHolder>{

    ArrayList<Group> groupList;
    GroupDAO dao;

    class GVHolder extends RecyclerView.ViewHolder{
        public View view;
        public GVHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
    }

    GroupAdapter(GroupDAO dao, ArrayList<Group> groupList){
        this.dao=dao;
        this.groupList=groupList;
    }

    @NonNull
    @Override
    public GVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grouplist, parent, false);
        return new GroupAdapter.GVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GVHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

}