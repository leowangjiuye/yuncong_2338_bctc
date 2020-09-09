package com.cloudwalk.livenesscameraproject.activity.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.view.SlideButton;

public class RenderSettingsActivity extends BaseActivity {

    private SlideButton mSlideBtnIsPreview;
    private RadioGroup mRgRenderRotation;
    private RadioGroup mRgRenderMirror;
    private EditText mEtLivenessTips;
    private Spinner mSpinnerFontSize;
    private Spinner mSpinnerFontColor;
    private RenderSettingsActivity mContext;
    private int mRotation = 90;
    private boolean mMirror = true;
    private int mFontsize;
    private String mFontColor;
    private RadioButton mRb0;
    private RadioButton mRb90;
    private RadioButton mRb180;
    private RadioButton mRb270;
    private RadioButton mRbIsMirror;
    private RadioButton mRbIsNotMirror;
    private String[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_para_set);
        initView();
        this.mContext = this;
        initParams();
    }

    private void initView() {
        ImageView imageView = findViewById(R.id.iv_preview_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TextView mBtnSave = findViewById(R.id.tv_preview_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacheManager.getInstance().saveIsPreview(mSlideBtnIsPreview.isOpen());
                CacheManager.getInstance().saveRotation(mRotation);
                CacheManager.getInstance().saveMirror(mMirror);
                CacheManager.getInstance().saveLivenessTips(mEtLivenessTips.getText().toString().trim());
                CacheManager.getInstance().saveFontSize(mFontsize);
                CacheManager.getInstance().saveFontColor(mFontColor);
            finish();
            }
        });

        mSlideBtnIsPreview = (SlideButton) findViewById(R.id.slide_button_is_preview);

        mRgRenderRotation = (RadioGroup) findViewById(R.id.rg_render_rotation);
        mRgRenderRotation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_0:
                        mRotation = 0;
                        break;
                    case R.id.rb_90:
                        mRotation = 90;
                        break;
                    case R.id.rb_180:
                        mRotation = 180;
                        break;
                    case R.id.rb_270:
                        mRotation = 270;
                        break;
                }
            }
        });
        mRb0 = (RadioButton) findViewById(R.id.rb_0);
        mRb90 = (RadioButton) findViewById(R.id.rb_90);
        mRb180 = (RadioButton) findViewById(R.id.rb_180);
        mRb270 = (RadioButton) findViewById(R.id.rb_270);


        mRgRenderMirror = (RadioGroup) findViewById(R.id.rg_mirror);

        mRgRenderMirror.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_is_mirror:
                        mMirror = true;
                        break;
                    case R.id.rb_is_not_mirror:
                        mMirror = false;
                        break;
                }
            }
        });
        mRbIsMirror = (RadioButton) findViewById(R.id.rb_is_mirror);
        mRbIsNotMirror = (RadioButton) findViewById(R.id.rb_is_not_mirror);

        mEtLivenessTips = (EditText) findViewById(R.id.et_liveness_tips);

        mSpinnerFontSize = (Spinner) findViewById(R.id.spinner_font_size);
        mSpinnerFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFontsize = 10 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerFontColor = (Spinner) findViewById(R.id.spinner_font_color);

        colors = getResources().getStringArray(R.array.fontcolor);
        ColorAdapter colorAdapter = new ColorAdapter(colors);
        mSpinnerFontColor.setAdapter(colorAdapter);
        mSpinnerFontColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFontColor = colors[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    class ColorAdapter extends BaseAdapter {


        private final String[] mColors;

        public ColorAdapter(String[] colors) {
            this.mColors = colors;
        }

        @Override
        public int getCount() {
            return mColors.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_item, null);
                viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvItem.setBackgroundColor(Color.parseColor(mColors[position]));
            return convertView;


        }

        class ViewHolder {
            TextView tvItem;
        }
    }

    private void initParams() {
        boolean isPreviewInit = CacheManager.getInstance().loadIsPreviewInit(false);
        if (!isPreviewInit) {
            CacheManager.getInstance().saveIsPreview(true);
            CacheManager.getInstance().saveRotation(90);
            CacheManager.getInstance().saveMirror(true);
            CacheManager.getInstance().saveLivenessTips("");
            CacheManager.getInstance().saveFontSize(25);
            CacheManager.getInstance().saveFontColor("#FFFFFF");

            mSlideBtnIsPreview.setOpen(true);
            mRotation = 90;
            mMirror = true;
            mEtLivenessTips.setText("");
            mFontsize = 10;
            mFontColor = "#ffffff";

            CacheManager.getInstance().saveIsPreviewInit(true);
        } else {
            mSlideBtnIsPreview.setOpen(CacheManager.getInstance().loadIsPreview(true));

            switch (CacheManager.getInstance().loadRotation(90)) {
                case 0:
                    mRb0.setChecked(true);
                    break;

                case 90:
                    mRb90.setChecked(true);
                    break;

                case 180:
                    mRb180.setChecked(true);
                    break;

                case 270:
                    mRb270.setChecked(true);
                    break;
            }

            if (CacheManager.getInstance().loadMirror(true)) {
                mRbIsMirror.setChecked(true);
            } else {
                mRbIsNotMirror.setChecked(true);
            }

            mEtLivenessTips.setText(CacheManager.getInstance().loadLivenessTips(""));
            mSpinnerFontSize.setSelection(CacheManager.getInstance().loadFontSize(25) - 10);
            for (int i = 0; i < colors.length; i++) {
                if (colors[i].equals(CacheManager.getInstance().loadFontColor("#FFFFFF"))) {
                    mSpinnerFontColor.setSelection(i);

                }
            }
        }
    }
}
