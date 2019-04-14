package com.route.todo.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.route.todo.R;

import java.util.List;

import com.route.todo.Database.Model.Todo;


public class TodoRecycleViewAdapter extends RecyclerView.Adapter<TodoRecycleViewAdapter.ViewHolder>
{
    List<Todo> item;
    OnItemClickListner onItemClickListner;

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public TodoRecycleViewAdapter(List<Todo> item) {
        this.item= item;
    }

    public Todo getSwipedItem(int pos)
    {
        return item.get(pos);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
       final Todo object=item.get(i);
        viewHolder.vtitle.setText(object.getTitle());
        viewHolder.vtime.setText(object.getTime());
        if (onItemClickListner!=null)
        {viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
onItemClickListner.OnItemClick(i,object);
            }
        });}


    }
    public void changedata(List<Todo> item)
       {this.item=item;
        notifyDataSetChanged();}

    @Override
    public int getItemCount() {

        if (item==null) return 0;
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView vtitle;
        TextView vtime;
        public ViewHolder(View view) {
            super(view);
            vtitle=view.findViewById(R.id.titlehome);
            vtime=view.findViewById(R.id.timehome);
        }
    }
    public interface OnItemClickListner{
        public void OnItemClick(int pos,Todo todo);
    }
}
