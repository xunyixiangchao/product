package com.leyou.library.le_library.comm.view.banner.view;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.ichsy.libs.core.comm.bus.BusEventObserver;
import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.comm.helper.AnimationHelper;
import com.leyou.library.le_library.comm.helper.ImageHelper;
import com.leyou.library.le_library.config.EventKeys;


import library.liuyh.com.lelibrary.R;


/**
 * 视频
 * Created by zhaoye
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SimpleVideoView extends RelativeLayout implements OnClickListener, BusEventObserver {

    private Context context;
    private View mView;
    private VideoView mVideoView;//视频控件
    private ImageView mBigPlayBtn;//大的播放按钮
    private ImageView mFullScreenBtn;//全屏按钮
    private SeekBar mPlayProgressBar;//播放进度条
    private TextView mPlayTime;//播放时间
    private ImageView mVideoCover; //视频封面
    private LinearLayout mControlPanel;

    private Uri mVideoUri = null;
    private String coverUrl; //封面地址

    private Animation outAnima;//控制面板出入动画
    private Animation inAnima;//控制面板出入动画

    private int mVideoDuration;//视频毫秒数
    private int mCurrentProgress;//毫秒数

    private Runnable mUpdateTask;
    private Thread mUpdateThread;

    private final int UPDATE_PROGRESS = 0;
    private final int EXIT_CONTROL_PANEL = 1;
    private boolean stopThread = true;//停止更新进度线程标志

    private Point screenSize = new Point();//屏幕大小
    private boolean mIsFullScreen = false;//是否全屏标志

    private int mWidth;//控件的宽度
    private int mHeigth;//控件的高度

    private ObjectAnimator objectAnimator;
    private ImageView mImageLoading;
    private ImageView mImageBigPause;

    private boolean isVideoInitComplete;


    public SimpleVideoView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }


    public SimpleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    mPlayProgressBar.setProgress(mCurrentProgress);
                    setPlayTime(mCurrentProgress);
                    break;
                case EXIT_CONTROL_PANEL:
                    //执行退出动画
                    mImageBigPause.setVisibility(GONE);
                    if (mControlPanel.getVisibility() != View.GONE) {
                        mControlPanel.startAnimation(outAnima);
                        mControlPanel.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    //初始化控件
    private void init(final Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.simple_video_view, this);
        isVideoInitComplete = false;
        mImageLoading = (ImageView) mView.findViewById(R.id.video_loading);
        mImageLoading.setVisibility(VISIBLE);
        mImageLoading.setImageResource(R.drawable.product_video_loading);
        objectAnimator = AnimationHelper.rotate(mImageLoading, 1000, -1);
        objectAnimator.start();
        mImageBigPause = (ImageView) mView.findViewById(R.id.big_pause_button);
        mBigPlayBtn = (ImageView) mView.findViewById(R.id.big_play_button);
        mFullScreenBtn = (ImageView) mView.findViewById(R.id.full_screen_button);
        mPlayProgressBar = (SeekBar) mView.findViewById(R.id.progress_bar);
        mPlayTime = (TextView) mView.findViewById(R.id.time);
        mControlPanel = (LinearLayout) mView.findViewById(R.id.control_panel);
        mVideoView = (VideoView) mView.findViewById(R.id.video_view);
        mVideoCover = (ImageView) mView.findViewById(R.id.video_cover);
        mVideoCover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止遮罩点击显示暂停按钮
            }
        });
        //获取屏幕大小
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
        //加载动画
        outAnima = AnimationUtils.loadAnimation(context, R.anim.exit_from_bottom);
        inAnima = AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom);

        //设置全屏按钮不可见
        mFullScreenBtn.setVisibility(GONE);
        //设置控制面板初始不可见
        mControlPanel.setVisibility(View.GONE);
        //设置大的播放按钮可见
        mBigPlayBtn.setVisibility(View.GONE);
        //设置封面初始
        mVideoCover.setVisibility(View.VISIBLE);
        //设置媒体控制器
//        MediaController mMediaController = new MediaController(context);
//		mVideoView.setMediaController(mMediaController);

        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isVideoInitComplete = true;
                objectAnimator.cancel();
                mImageLoading.setVisibility(GONE);
                //视频加载完成后才能获取视频时长
                initVideo();
                if (mBigPlayBtn.getVisibility() != VISIBLE) {
                    startVideo();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mVideoCover.setVisibility(GONE);
                        }
                    }, 300);
                } else {
                    mVideoCover.setVisibility(VISIBLE);
                }
            }
        });
        //视频播放完成监听器
        mVideoView.setOnCompletionListener(new OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCompletion(MediaPlayer mp) {
                initVideoForStart();
                setPlayTime(0);
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                objectAnimator.cancel();
                mImageLoading.setVisibility(GONE);
                return false;
            }
        });
        mBigPlayBtn.setOnClickListener(this);
        mView.setOnClickListener(this);
    }

    private void initVideoForStart() {
        mImageBigPause.setVisibility(GONE);
        objectAnimator.cancel();
        mImageLoading.setVisibility(GONE);
        mBigPlayBtn.setVisibility(View.VISIBLE);
        setInitPicture();
        mVideoView.seekTo(0);
        mPlayProgressBar.setProgress(0);
        stopThread = true;
        sendHideControlPanelMessage();
    }

    private void startVideo() {
        mVideoView.setBackground(null);
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            //开始更新进度线程
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
    }


    //初始化视频，设置视频时间和进度条最大值
    private void initVideo() {
        //初始化时间和进度条
        mVideoDuration = mVideoView.getDuration();//毫秒数
        int seconds = mVideoDuration / 1000;
        mPlayTime.setText("00:00/" +
                ((seconds / 60 > 9) ? (seconds / 60) : ("0" + seconds / 60)) + ":" +
                ((seconds % 60 > 9) ? (seconds % 60) : ("0" + seconds % 60)));
        mPlayProgressBar.setMax(mVideoDuration);
        mPlayProgressBar.setProgress(0);
        //更新进度条和时间任务
        mUpdateTask = new Runnable() {
            @Override
            public void run() {
                while (!stopThread) {
                    mCurrentProgress = mVideoView.getCurrentPosition();
                    handler.sendEmptyMessage(UPDATE_PROGRESS);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mImageBigPause.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        //进度条进度改变监听器
        mPlayProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 3000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(EXIT_CONTROL_PANEL);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoView.seekTo(progress);//设置视频
                    setPlayTime(progress);//设置时间
                }
            }
        });
        mWidth = this.getWidth();
        mHeigth = this.getHeight();
    }

    @Override
    public void onClick(View v) {
        if (v == mView) {
            if (mBigPlayBtn.getVisibility() == View.VISIBLE) {
                return;
            }
            if (mControlPanel.getVisibility() == View.VISIBLE) {
                //执行退出动画
                mImageBigPause.setVisibility(GONE);
                mControlPanel.startAnimation(outAnima);
                mControlPanel.setVisibility(View.GONE);
            } else {
                //执行进入动画
                mImageBigPause.setVisibility(VISIBLE);
                mControlPanel.startAnimation(inAnima);
                mControlPanel.setVisibility(View.VISIBLE);
                sendHideControlPanelMessage();
            }
        } else if (v.getId() == R.id.big_play_button) {//大的播放按钮
            if (isVideoInitComplete) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoCover.setVisibility(GONE);
                    }
                }, 300);
            } else {
                mImageLoading.setVisibility(VISIBLE);
                objectAnimator.start();
            }
            mBigPlayBtn.setVisibility(View.GONE);
            startVideo();
        } else if (v.getId() == R.id.big_pause_button) {//播放/暂停按钮
            startAndPause();
        } else if (v.getId() == R.id.full_screen_button) {//全屏
            if (context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                setFullScreen();
            } else {
                setNoFullScreen();
            }
            sendHideControlPanelMessage();
        }
    }

    //设置当前时间
    private void setPlayTime(int millisSecond) {
        int currentSecond = millisSecond / 1000;
        String currentTime = ((currentSecond / 60 > 9) ? (currentSecond / 60 + "") : ("0" + currentSecond / 60)) + ":" +
                ((currentSecond % 60 > 9) ? (currentSecond % 60 + "") : ("0" + currentSecond % 60));
        StringBuilder text = new StringBuilder(mPlayTime.getText().toString());
        try {
            text.replace(0, text.indexOf("/"), currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayTime.setText(text);
    }

    //设置控件的宽高
    private void setSize() {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        if (mIsFullScreen) {
            lp.width = screenSize.y;
            lp.height = screenSize.x;
        } else {
            lp.width = mWidth;
            lp.height = mHeigth;
        }
        this.setLayoutParams(lp);
    }

    //视频播放暂停
    private void startAndPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mImageBigPause.setImageResource(R.drawable.product_video_icon);
        } else {
            if (mUpdateThread == null || !mUpdateThread.isAlive()) {
                //开始更新进度线程
                mUpdateThread = new Thread(mUpdateTask);
                stopThread = false;
                mUpdateThread.start();
            }
            mVideoView.start();
            mImageBigPause.setImageResource(R.drawable.product_video_pause);
        }
        sendHideControlPanelMessage();
    }

    //两秒后隐藏控制面板
    private void sendHideControlPanelMessage() {
        handler.removeMessages(EXIT_CONTROL_PANEL);
        handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 3000);
    }

    //设置视频路径
    public void setVideoUri(Uri uri) {
        this.mVideoUri = uri;
        mVideoView.setVideoURI(mVideoUri);
    }

    //获取视频路径
    public Uri getVideoUri() {
        return mVideoUri;
    }

    public VideoView getVideoView() {
        return mVideoView;
    }


    //设置视频封面
    public void setInitCover(String coverUrl) {
        this.coverUrl = coverUrl;
        setInitPicture();
    }

    //设置封面网络初始画面
    public void setInitPicture() {
        mVideoCover.setVisibility(VISIBLE);
        ImageHelper.with(context).load(coverUrl, R.drawable.seat_goods1080x1080).into(mVideoCover);
    }

    //挂起视频
    public void suspend() {
        if (mVideoView != null) {
            mVideoView.suspend();
        }
    }

    //设置视频进度
    public void setVideoProgress(int millisSecond, boolean isPlaying) {
        mVideoView.setBackground(null);
        mBigPlayBtn.setVisibility(View.GONE);
        mPlayProgressBar.setProgress(millisSecond);
        setPlayTime(millisSecond);
        if (mUpdateThread == null || !mUpdateThread.isAlive()) {
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
        mVideoView.seekTo(millisSecond);
        if (isPlaying) {
            mVideoView.start();
        } else {
            mVideoView.pause();
        }
    }

    //获取视频进度
    public int getVideoProgress() {
        return mVideoView.getCurrentPosition();
    }

    //判断视频是否正在播放
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    //判断是否为全屏状态
    public boolean isFullScreen() {
        return mIsFullScreen;
    }

    //设置竖屏
    public void setNoFullScreen() {
        this.mIsFullScreen = false;
        Activity ac = (Activity) context;
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ac.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSize();
    }

    //设置横屏
    public void setFullScreen() {
        this.mIsFullScreen = true;
        Activity ac = (Activity) context;
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ac.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSize();
    }


    @Override
    public void onBusEvent(String event, @Nullable Object message) {
        int code = (int) message;
        if (event.equals(EventKeys.PRODUCT_BANNER_VIDEO_PAUSE)) {
            mVideoView.pause();
            initVideoForStart();
        } else if (event.equals(EventKeys.PRODUCT_BANNER_VIDEO_DESTROY)) {
            suspend();
            isVideoInitComplete = false;
        } else if (event.equals(EventKeys.PRODUCT_ACTIVITY_VIDEO_PAUSE)) {
            mVideoView.pause();
            initVideoForStart();
            if(code == 0){
                isVideoInitComplete = false;
            }else{
                isVideoInitComplete = true;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusManager.getDefault().unRegister(EventKeys.PRODUCT_BANNER_VIDEO_PAUSE, this);
        BusManager.getDefault().unRegister(EventKeys.PRODUCT_BANNER_VIDEO_DESTROY, this);
        BusManager.getDefault().unRegister(EventKeys.PRODUCT_ACTIVITY_VIDEO_PAUSE, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusManager.getDefault().register(EventKeys.PRODUCT_BANNER_VIDEO_PAUSE, this);
        BusManager.getDefault().register(EventKeys.PRODUCT_BANNER_VIDEO_DESTROY, this);
        BusManager.getDefault().register(EventKeys.PRODUCT_ACTIVITY_VIDEO_PAUSE, this);
    }
}
