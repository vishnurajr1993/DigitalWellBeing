package com.project.digitalwellbeing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import com.google.firebase.messaging.RemoteMessage;
import com.project.digitalwellbeing.MainActivity;
import com.project.digitalwellbeing.R;
import com.project.digitalwellbeing.data.FCMActions;
import com.project.digitalwellbeing.data.model.AppDataBase;
import com.project.digitalwellbeing.data.model.DigitalWellBeingDao;
import com.project.digitalwellbeing.data.model.TaskDetails;
import com.project.digitalwellbeing.remote.Communicator;

import java.util.Calendar;

import static com.project.digitalwellbeing.utils.CommonDataArea.context;

public class Popup extends Activity {
    TextView popupTitle, popupMessage;
    Button btnOK, yesBtn, noBtn;
    EditText pairEdit;
    Context context;
    private DatePickerDialog picker;

    public Popup(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void singleChoice(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_single_view, null);
        popupTitle = (TextView) layout.findViewById(R.id.title);
        popupMessage = (TextView) layout.findViewById(R.id.message);
        btnOK = (Button) layout.findViewById(R.id.okbtn);
        popupTitle.setText(title);
        popupMessage.setText(message);
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void doubleChoice(String title, String message, final RemoteMessage remoteMessage, final int callingFrom) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_double_view, null);
        popupTitle = (TextView) layout.findViewById(R.id.dtitle);
        popupMessage = (TextView) layout.findViewById(R.id.dmessage);
        yesBtn = (Button) layout.findViewById(R.id.dyesbtn);
        noBtn = (Button) layout.findViewById(R.id.dnobtn);
        popupTitle.setText(title);
        popupMessage.setText(message);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();

//        new Handler().postDelayed(new Runnable() {
//            public void run() {
        alertDialog.show();
//            }
//        }, 100);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingFrom == 1) {
                    CommonDataArea.context = Popup.this;
                    new FCMActions().registerParent(remoteMessage);
                }
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void SingleEditTextChoice(String title, final int callingFrom, final Context contextCurr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_edit_view, null);
        popupTitle = (TextView) layout.findViewById(R.id.dtitle);
        pairEdit = (EditText) layout.findViewById(R.id.edt_reg_uuid);
        yesBtn = (Button) layout.findViewById(R.id.dyesbtn);
        noBtn = (Button) layout.findViewById(R.id.dnobtn);
        popupTitle.setText(title);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingFrom == 1) {
                    CommonDataArea.FIREBASETOPIC = "/topics/" + pairEdit.getText();


                    new Communicator(contextCurr).sendMessage(new FCMMessages(context).registerMessage());
                    alertDialog.dismiss();
                }
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    public void taskPopup(String title, final Context contextCurr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_task_view, null);
        // popupTitle = (TextView) layout.findViewById(R.id.dtitle);
        EditText activityEdt = (EditText) layout.findViewById(R.id.activity_edt);
        EditText calenderEdt = (EditText) layout.findViewById(R.id.calender_edt);
        EditText clockEdt = (EditText) layout.findViewById(R.id.clock_edt);
        Button submitBtn = (Button) layout.findViewById(R.id.activity_submit_btn);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        calenderEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calenderEdt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        clockEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        clockEdt.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activityEdt.getText().toString().equalsIgnoreCase("") && !calenderEdt.getText().toString().equalsIgnoreCase("") && !clockEdt.getText().toString().equalsIgnoreCase("")) {
                    TaskDetails taskDetails = new TaskDetails();
                    taskDetails.setTaskName(activityEdt.getText().toString());
                    taskDetails.setDate(calenderEdt.getText().toString());
                    taskDetails.setTime(clockEdt.getText().toString());
                    taskDetails.setUpload(0);

                    AppDataBase appDataBase = AppDataBase.getInstance(context);
                    DigitalWellBeingDao digitalWellBeingDao = appDataBase.userDetailsDao();
                    digitalWellBeingDao.insertTaskDetails(taskDetails);
                }
            }
        });

    }


}
