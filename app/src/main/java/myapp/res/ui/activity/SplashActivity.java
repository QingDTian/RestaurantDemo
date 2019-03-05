package myapp.res.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    public static final int CODE = 1001;
    private Button btn_m;
    private static final int TIME = 3000;
    private MyHandle myHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        myHandle = new MyHandle(this);
        Message msg = Message.obtain();
        msg.what = CODE;
        msg.arg1 = TIME;
        myHandle.sendMessage(msg);
    }

    private void mStartActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initEvent() {
        btn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartActivity();
                SplashActivity.this.finish();
                myHandle.removeMessages(CODE);

            }
        });
    }

    private void initView() {
        btn_m = findViewById(R.id.button_m);
    }

    public static class MyHandle extends Handler {
        public static final String CLICK_JUMP = "点击跳过";
        public final WeakReference<SplashActivity> mWeakReference;

        public MyHandle(SplashActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mWeakReference.get();
            if (msg.what == CODE) {
                if (activity != null) {
                    int time = msg.arg1;
                    if (time >= 0) {
                        activity.btn_m.setText(time / 1000 + CLICK_JUMP);
                        time -= 1000;
                        Message message = Message.obtain();
                        message.what = CODE;
                        message.arg1 = time;
                        sendMessageDelayed(message, 1000);
                    } else {
                        activity.mStartActivity();
                        activity.finish();
                    }
                }


            }
        }

    }

}
