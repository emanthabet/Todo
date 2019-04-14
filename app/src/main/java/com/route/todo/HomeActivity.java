package com.route.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.route.todo.Adapter.TodoRecycleViewAdapter;
import com.route.todo.Base.BaseActivity;

import java.util.List;

import com.route.todo.Database.Model.Todo;
import com.route.todo.Database.Mydatabase;

public class HomeActivity extends BaseActivity {

    RecyclerView recyclerView;
    TodoRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    Todo swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(activity);
        adapter = new TodoRecycleViewAdapter(null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        //adding note
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddingTodo.class);
                startActivity(intent);
            }
        });

        //update
        adapter.setOnItemClickListner(new TodoRecycleViewAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int pos, Todo todo) {
                AddingTodo.todoitem = todo;
                Intent intent = new Intent(HomeActivity.this, AddingTodo.class);
                startActivity(intent);
            }
        });


        //swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int pos = viewHolder.getAdapterPosition();
                Todo item = adapter.getSwipedItem(pos);
                swipe = item;
                deleteitem(item);
            }

           /*@Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View item = viewHolder.itemView;
                int iconmargin=(item.getHeight() - deleteicon.getIntrinsicHeight())/2;
                if(dX>0)
                {background.setBounds(item.getLeft(),item.getTop(), (int)dX ,item.getBottom());
                deleteicon.setBounds(item.getLeft()+iconmargin,item.getTop()+iconmargin,item.getLeft()+iconmargin+deleteicon.getIntrinsicWidth(),item.getBottom()+iconmargin);}
                background.draw(c);
                c.save();
                if(dX>0)
                    c.clipRect(item.getLeft(),item.getTop(), (int)dX ,item.getBottom());
                deleteicon.draw(c);
                c.restore();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }*/
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);


        //swipe to refresh
        swipeRefreshLayout = findViewById(R.id.swipetorefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getallitems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    public void deleteitem(Todo item) {
        Mydatabase.getInstance(activity).todoDaoobj().deleteitem(item);
        Snackbar.make(findViewById(R.id.swipetorefresh), R.string.item_deleted, 8000)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Mydatabase.getInstance(activity).todoDaoobj().Additem(swipe);
                    }
                }).show();
        getallitems();


    }

    public void getallitems() {
        List<Todo> data = Mydatabase.getInstance(activity).todoDaoobj().getalltodo();
        adapter.changedata(data);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getallitems();

    }
}
