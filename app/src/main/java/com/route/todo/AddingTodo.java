package com.route.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.route.todo.Base.BaseActivity;

import java.util.Calendar;

import com.route.todo.Database.Model.Todo;
import com.route.todo.Database.Mydatabase;

public class AddingTodo extends BaseActivity implements View.OnClickListener {

    protected EditText title;
    protected TextView reminder;
    protected Switch aSwitch;
    protected TextView time;
    protected TextView date;
    protected EditText content;
    protected Button button;
    public static Todo todoitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_adding_todo);
        initView();
        if (todoitem != null) {
            button.setText(R.string.update);
            title.setText(todoitem.getTitle());
            time.setText(todoitem.getTime());
            date.setText(todoitem.getDate());
            content.setText(todoitem.getContent());
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addbutton) {

                String stitle = title.getText().toString();
                String stime = time.getText().toString();
                String sdate = date.getText().toString();
                String scontent = content.getText().toString();
                if (validatefields(stitle, scontent)) {
                    if(aSwitch.isChecked()&&(validatetimeanddate(stime, sdate)))
                        {setAlarmForTodo();
                            if (todoitem == null) {
                                Todo item = new Todo(stitle, stime, sdate, scontent);
                                Mydatabase.getInstance(activity).todoDaoobj().Additem(item);
                                ShowMessage(R.string.sucess, R.string.you_added_it, R.string.ok);

                            }
                            else{
                                todoitem.setTitle(stitle);
                                todoitem.setContent(scontent);
                                todoitem.setTime(stime);
                                todoitem.setDate(sdate);
                                Mydatabase.getInstance(activity).todoDaoobj().updateitem(todoitem);
                                ShowMessage(R.string.sucess, R.string.you_updated_it, R.string.ok);}
                    }
                    else if (!aSwitch.isChecked())
                    {  if (todoitem == null) {
                        Todo item = new Todo(stitle, stime, sdate, scontent);
                        Mydatabase.getInstance(activity).todoDaoobj().Additem(item);
                        ShowMessage(R.string.sucess, R.string.you_added_it, R.string.ok);

                    }
                    else{
                        todoitem.setTitle(stitle);
                        todoitem.setContent(scontent);
                        todoitem.setTime(stime);
                        todoitem.setDate(sdate);
                        Mydatabase.getInstance(activity).todoDaoobj().updateitem(todoitem);
                        ShowMessage(R.string.sucess, R.string.you_updated_it, R.string.ok);}
                    }
                }

        }
    }


    public Boolean validatefields(String stitle, String scontent) {
        if (stitle.isEmpty()) {
            title.setError("field can't be empty");
            return false;
        } else {
            title.setError(null);
        }

        if (scontent.isEmpty()) {
            content.setError("field can't be empty");
            return false;
        } else {
            content.setError(null);
        }
        return true;
    }

    public Boolean validatetimeanddate(String stime, String sdate) {
        if (stime.isEmpty()) {
            time.setError("field can't be empty");
            return false;
        } else {
            time.setError(null);
        }

        if (sdate.isEmpty()) {
            date.setError("field can't be empty");
            return false;
        } else {
            date.setError(null);
        }
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoitem = null;
    }

    private void initView() {
        title = findViewById(R.id.title);
        reminder=findViewById(R.id.setalarm);
        aSwitch=findViewById(R.id.switchreminder);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        content = findViewById(R.id.content);
        button = findViewById(R.id.addbutton);
        button.setOnClickListener(AddingTodo.this);
    }




    int hour = 0;
    int min = 0;

    //open time picker
    public void opentimepicker(View view) {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
                time.setText(hour + ":" + min);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    int theyear =1;
    int themonth = 1;
    int thedayofmonth = 1;

    //open date picker
    public void opendatepicker(View view) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                theyear = year;
                themonth = month+1;
                thedayofmonth = dayOfMonth;
                date.setText(thedayofmonth + "/" + themonth + "/" + theyear);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    public void setAlarmForTodo() {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        Intent alarmintent = new Intent(activity, TaskAlarmBroadcastReciever.class);
        alarmintent.putExtra("title", title.getText().toString());
        alarmintent.putExtra("desc", content.getText().toString());
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, alarmintent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.DAY_OF_MONTH,thedayofmonth);
        calendar.set(Calendar.MONTH,themonth);
        calendar.set(Calendar.YEAR,theyear);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

    }

}
