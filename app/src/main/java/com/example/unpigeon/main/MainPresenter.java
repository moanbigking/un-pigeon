package com.example.unpigeon.main;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.unpigeon.loader.task.ThreadPool;
import com.example.unpigeon.repository.RecordPieceDao;
import com.example.unpigeon.repository.RecordPieceDatabase;
import com.example.unpigeon.repository.RecordPieceEntity;
import com.example.unpigeon.utils.Constant;

import java.util.ArrayList;
import java.util.List;

class MainPresenter implements MainContract.Presenter{
    private final MainContract.View mView;
    private RecordPieceDao mRecordPieceDao;
    private ThreadPool mThreadPool;
    private List<RecordPieceEntity> mRecordPieceEntityList = new ArrayList<>();
    private Handler mHandler;

    MainPresenter(MainContract.View view, Handler handler) {
        mView = view;
        mHandler = handler;
        mThreadPool = new ThreadPool(5, 5);
    }

    @Override
    public void load(final Context context) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mRecordPieceEntityList = RecordPieceDatabase.getInstance(context).recordPieceDao().getAll();
                if (mRecordPieceEntityList != null) {
                    Message loadFinished = new Message();
                    loadFinished.what = Constant.LOAD_FINISHED;
                    mHandler.dispatchMessage(loadFinished);
                }
            }
        });
    }
}
