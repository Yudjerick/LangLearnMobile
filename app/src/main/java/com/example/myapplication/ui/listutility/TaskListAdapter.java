package com.example.myapplication.ui.listutility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.viewmodels.OrderTaskViewModel;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    public static Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public String TAG = "RecyclerAdapter";
        final TextView textView;
        final ImageView imageView;

        OrderTask task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(imageView.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, textView.getText().toString());
                    NavController navController = Navigation.findNavController(view);
                    OrderTaskViewModel model = new ViewModelProvider((ViewModelStoreOwner) context).get(OrderTaskViewModel.class);
                    model.setTask(task);
                    model.clearAnswer();
                    navController.navigate(R.id.orderTaskFragment);
                }
            });

            this.textView = itemView.findViewById(R.id.task_list_item_textview);
            this.imageView = itemView.findViewById(R.id.completed_status_icon);
        }
    }

    private final LayoutInflater inflater;
    private final List<TaskItem> items;
    public TaskListAdapter(Context context, List<TaskItem> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_select_list_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskItem item = items.get(position);
        holder.textView.setText(item.getText());
        holder.task = item.getTask();
        holder.imageView.setImageResource(item.getImageId());
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}