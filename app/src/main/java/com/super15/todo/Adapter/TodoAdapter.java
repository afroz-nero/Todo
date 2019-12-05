package com.super15.todo.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import com.super15.todo.db.TodoDb;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<TodoModel> mTodos;

    private Calendar cal;

    private TodoModel todoModel;

    private TextInputEditText update_title, update_note;

    private TextView update_date, update_time;
    private ViewHolder holder;

    public TodoAdapter(Context mContext, ArrayList<TodoModel> mTodos) {
        this.mContext = mContext;
        this.mTodos = mTodos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.todoitem,parent,false);

        return new TodoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        todoModel = mTodos.get(position);
        final String title;
        title = todoModel.getTitle();
        holder.title.setText(title);
        cal=Calendar.getInstance();
        this.holder=holder;


        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(holder.settings,position);
            }
        });

        holder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.vib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.activatior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView settings,alarm,vib,ring,text,calender,activatior;
        LinearLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            settings=itemView.findViewById(R.id.settings);
            alarm=itemView.findViewById(R.id.imgAlarm);
            vib=itemView.findViewById(R.id.imgVibrate);
            ring=itemView.findViewById(R.id.imgRing);
            text=itemView.findViewById(R.id.imgText);
            calender=itemView.findViewById(R.id.imgCalender);
            activatior=itemView.findViewById(R.id.activator);
            layout=itemView.findViewById(R.id.mylayout);

        }
    }

    private void showPopup(View v, final int position){
        PopupMenu popup = new PopupMenu(mContext,v);
        popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.update:
                        showDialoguebox(position);
                        return true;
                    case R.id.remove:
                        TodoDb todoDb = new TodoDb(mContext);
                        todoDb.deleteData(mTodos.get(position).getId());
                        mTodos.remove(position);

                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        return true;

                    default:
                        return false;
                }
            }
        });

        popup.show();
    }


    private void showDialoguebox(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogueView = li.inflate(R.layout.update_dialoge_box,null);
        update_title = dialogueView.findViewById(R.id.edt_update_title);
        update_note = dialogueView.findViewById(R.id.edt_update_note);
        update_date = dialogueView.findViewById(R.id.tv_update_date);
        update_time = dialogueView.findViewById(R.id.tv_update_time);
        Button updateButtton = dialogueView.findViewById(R.id.btn_update);
        update_title.setText(mTodos.get(position).getTitle());
        update_note.setText(mTodos.get(position).getNote());
        update_date.setText(mTodos.get(position).getDate());
        update_time.setText(mTodos.get(position).getTime());
        builder.setView(dialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        update_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date= update_date.getText().toString();
                String[] strDate=date.split("/",3);
                int day=Integer.parseInt(strDate[0]);
                int month=Integer.parseInt(strDate[1]);
                int year=Integer.parseInt(strDate[2]);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year, final int month, final int dayOfMonth) {
                        Log.e("day",String.valueOf(dayOfMonth));
                        Log.e("month",String.valueOf(month));
                        Log.e("year",String.valueOf(year));
                        dateFormater(dayOfMonth,month,year);
                        cal.set(year,month,dayOfMonth);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });
        update_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time= update_time.getText().toString();
                String[] strTime=time.split(":",2);
                int hour=Integer.parseInt(strTime[0]);
                int minute=Integer.parseInt(strTime[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeFormater(hourOfDay, minute);
                        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cal.set(Calendar.MINUTE,minute);
                        cal.set(Calendar.SECOND,0);

                    }
                },hour,minute, DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });
        updateButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = update_title.getText().toString();
                String note = update_note.getText().toString();
                String date = update_date.getText().toString();
                String time = update_time.getText().toString();

                if (title.isEmpty() || note.isEmpty()){
                    Toast.makeText(mContext, "Fields must contain data", Toast.LENGTH_SHORT).show();
                } else {
                    TodoDb todoDb = new TodoDb(mContext);

                    TodoModel model = new TodoModel(mTodos.get(position).getId(),title,note,date,time);

                    if (todoDb.updateData(model) != 1){
                        Toast.makeText(mContext, "Someting went wrong!!", Toast.LENGTH_SHORT).show();
                    } else {
                        alertDialog.cancel();
                        mTodos.set(position, model);
                        notifyItemChanged(position, model);
                        notifyDataSetChanged();

                    }
                }
            }
        });
    }
    private void dateFormater(int day, int month, int year){
        String sDay, sMonth, sYear;
        if (day<10){sDay="0"+day;}else{sDay=""+day; }
        if (month<10){sMonth="0"+month;}else{sMonth=""+month; }
        sYear = ""+year;
        String date= sDay + "/" +sMonth+"/" +sYear;
        update_date.setText(date);
    }
    private void timeFormater(int hour, int minute){
        String sMinute, sHour;
        if(minute < 10){sMinute="0"+minute;}else{sMinute=""+minute;}
        if(hour<10){sHour="0"+hour;}else{sHour=""+hour;}
        update_time.setText(sHour + ":" +sMinute );
    }



}
