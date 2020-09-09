package com.cloudwalk.livenesscameraproject.activity.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.activity.launch.MainActivity;
import com.cloudwalk.livenesscameraproject.adapter.BaseListViewAdapter;
import com.cloudwalk.livenesscameraproject.adapter.BaseViewHodler;
import com.cloudwalk.livenesscameraproject.db.DBManager;
import com.cloudwalk.livenesscameraproject.db.DaoMaster;
import com.cloudwalk.livenesscameraproject.db.PersonFeatureModel;
import com.cloudwalk.livenesscameraproject.manager.ThreadManager;
import com.cloudwalk.livenesscameraproject.utils.FileUtil;
import com.cloudwalk.livenesscameraproject.utils.ImgUtil;
import com.cloudwalk.livenesscameraproject.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FaceLibraryListActivity extends BaseActivity implements View.OnCreateContextMenuListener {
    private FaceLibraryAdapter adapter;
    private ListView mLVLib;
    private List<PersonFeatureModel> modelList = new ArrayList<>();
    private int loadSize = 20;
    private int libOffset = 0;

    private boolean isDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_face_library);
        adapter = new FaceLibraryAdapter(this);
        mLVLib = findViewById(R.id.listView);
        mLVLib.setAdapter(adapter);
        mLVLib.setOnCreateContextMenuListener(this);
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final List<PersonFeatureModel> list = DBManager.getInstance().getPersonFeatureModelDao().queryBuilder().offset(libOffset * loadSize).limit(loadSize).list();
                    libOffset++;
                    if (list != null && list.size() > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.update(list, true);
                            }
                        });
                    }else {
                        return;
                    }
                }
            }
        }).start();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "删除");
        menu.add(0, 1, 0, "删除所有");
    }

    private Runnable deleteAllRunable = new Runnable() {
        @Override
        public void run() {
            DBManager.getInstance().getPersonFeatureModelDao().deleteAll();
            deleteDirectory(new File("/sdcard/cloudwalk/feature"));
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        Logger.e("TAG", "info == " + info.id + " -- " + info.position);
        switch (item.getItemId()) {
            case 0:
                // 删除操作
                if (isDelete) {
                   showToast("正在删除");
                   break;
                }
                isDelete = true;
                int pos = info.position;
                final PersonFeatureModel personItemModel = adapter.getData().get(pos);
                DBManager.getInstance().getPersonFeatureModelDao().deleteByKey(personItemModel.getId());
                deleteDirectory(new File(personItemModel.getPath()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.getData().remove(personItemModel);
                        adapter.notifyDataSetChanged();
                    }
                });
                isDelete = false;
                break;
            case 1:
                if (isDelete) {
                    showToast("正在删除");
                    break;
                }

                // 删除所有
                ThreadManager.getThreadPool().execute(deleteAllRunable);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isDelete = true;
                        adapter.getData().clear();
                        adapter.notifyDataSetChanged();
                        isDelete = false;
                    }
                });
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deleteAllRunable != null) {
            ThreadManager.getThreadPool().cancel(deleteAllRunable);
        }
    }

    private void initData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.update(modelList, false);
            }
        });
    }
    private void deleteDirectory(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        folder.delete();
    }

    static class FaceLibraryAdapter extends BaseListViewAdapter<PersonFeatureModel> {

        public FaceLibraryAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_face_library_layout;
        }

        @Override
        public void setItemViewData(BaseViewHodler holder, int position) {
            ImageView imageView = holder.getView(R.id.faceImage);
            TextView name = holder.getView(R.id.tv_name);
            PersonFeatureModel person = list.get(position);
            name.setText(person.getName());
            if (person.getPath() != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(person.getPath());
                    imageView.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FaceLibraryListActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
