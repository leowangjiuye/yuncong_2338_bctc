package com.cloudwalk.livenesscameraproject.activity.settings;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.activity.bean.StrategyBean;
import com.cloudwalk.livenesscameraproject.adapter.LivenessStrategyAdapter;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.recycleview_adapter.listener.EasyOnItemChildCheckChangeListener;
import com.cloudwalk.livenesscameraproject.utils.CalculateUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LivenessStrategyParamsActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = LivenessStrategyParamsActivity.class.getSimpleName();

    private EditText etLivenessMinutes;
    private EditText etLivenessCount;
    private EditText etLivenessSuccess;
    private RecyclerView rvModel;
    private List<Object> strategyList;
    private LivenessStrategyAdapter adapter;
    private int checkedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveness_check);

        initView();
        initOriginalUI();
        initListener();
        initData();
    }

    private void initOriginalUI() {
        etLivenessCount.setText(CacheManager.getInstance().loadKeyM(0) + "");
        etLivenessSuccess.setText(CacheManager.getInstance().loadKeyN(0) + "");
        etLivenessMinutes.setText(CacheManager.getInstance().loadKeyS(0) + "");
    }


    private void initView() {
        strategyList = new ArrayList<>();
        etLivenessMinutes = findViewById(R.id.et_liveness_minute);
        etLivenessCount = findViewById(R.id.et_liveness_count);
        etLivenessSuccess = findViewById(R.id.et_liveness_success);
        rvModel = findViewById(R.id.rv_model);

        GridLayoutManager layoutManager = new GridLayoutManager(LivenessStrategyParamsActivity.this, 4);
        rvModel.setLayoutManager(layoutManager);
        adapter = new LivenessStrategyAdapter(LivenessStrategyParamsActivity.this, R.layout.item_strategy_detail, strategyList);
        rvModel.setAdapter(adapter);

        adapter.setEasyOnItemChildCheckChangeListener(new EasyOnItemChildCheckChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton childView, int position, boolean isChecked) {
                if (isChecked) {
                    //清空所有策略选中
                    if (strategyList != null && strategyList.size() > 0) {
                        for (int i = 0; i < strategyList.size(); i++) {
                            ((StrategyBean) strategyList.get(i)).isCheck = false;
                        }
                    }
                    ((StrategyBean) strategyList.get(position)).isCheck = true;
                    checkedPosition = position;
                    adapter.setDatas(strategyList);
                } else {
                    ((StrategyBean) strategyList.get(position)).isCheck = false;
                    checkedPosition = -1;
                    adapter.setDatas(strategyList);
                }
            }
        });

    }

    private void initListener() {
        findViewById(R.id.iv_config_back).setOnClickListener(this);
        findViewById(R.id.tv_config_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_config_back:
                finish();
                break;
            case R.id.tv_config_save:
                if (checkInput()) {
                    saveConfigParams();
                }
                break;
        }
    }

    /**
     * 保存配置参数
     */
    private void saveConfigParams() {
        try {
            CacheManager.getInstance().saveKeyM(!TextUtils.isEmpty(etLivenessCount.getText().toString().trim()) ? Integer.parseInt(etLivenessCount.getText().toString().trim()) : 0);
            CacheManager.getInstance().saveKeyN(!TextUtils.isEmpty(etLivenessSuccess.getText().toString().trim()) ? Integer.parseInt(etLivenessSuccess.getText().toString().trim()) : 0);
            CacheManager.getInstance().saveKeyS(!TextUtils.isEmpty(etLivenessMinutes.getText().toString().trim()) ? Integer.parseInt(etLivenessMinutes.getText().toString().trim()) : 0);
            CacheManager.getInstance().saveKeyId(checkedPosition + 1);
            Toast.makeText(LivenessStrategyParamsActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initData() {
        if (strategyList == null) {
            strategyList = new ArrayList<>();
        } else {
            strategyList.clear();
        }
        //默认是第四种策略
        strategyList.add(new StrategyBean(false, 1, "策略一"));
        strategyList.add(new StrategyBean(false, 2, "策略二"));
        strategyList.add(new StrategyBean(false, 3, "策略三"));
        strategyList.add(new StrategyBean(false, 4, "策略四"));

        if (CacheManager.getInstance().loadKeyId(-1) == -1) {
            checkedPosition = strategyList.size() - 1;
        } else {
            checkedPosition = CacheManager.getInstance().loadKeyId(-1) - 1;
        }

        for (int i = 0; i < strategyList.size(); i++) {
            if (i == checkedPosition) {
                ((StrategyBean) strategyList.get(i)).isCheck = true;
            }
        }

        adapter.setDatas(strategyList);

    }

    /**
     * 校验：
     *
     * @return
     */
    private boolean checkInput() {
        if (checkedPosition == -1) {
            Toast.makeText(LivenessStrategyParamsActivity.this, "请选择一种策略", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (0 == checkedPosition) {
            if (TextUtils.isEmpty(etLivenessMinutes.getText().toString().trim()) || CalculateUtils.compare(etLivenessMinutes.getText().toString().trim(), "0") <= 0) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "请输入活检时间或者活检时间不合法", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (1 == checkedPosition) {
            if (TextUtils.isEmpty(etLivenessCount.getText().toString().trim()) || TextUtils.isEmpty(etLivenessMinutes.getText().toString().trim()) ||
                    TextUtils.isEmpty(etLivenessSuccess.getText().toString().trim())) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "缺少必要的参数", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (CalculateUtils.compare(etLivenessMinutes.getText().toString().trim(), "0") < 0 || CalculateUtils.compare(etLivenessCount.getText().toString().trim(), "0") < 0
                    || CalculateUtils.compare(etLivenessSuccess.getText().toString().trim(), "0") < 0) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "必要参数应大于等于0", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (CalculateUtils.compare(etLivenessSuccess.getText().toString().trim(), etLivenessCount.getText().toString().trim()) > 0) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "连续活检次数应大于等于活体成功次数", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (2 == checkedPosition) {
            if (TextUtils.isEmpty(etLivenessMinutes.getText().toString().trim()) || TextUtils.isEmpty(etLivenessSuccess.getText().toString().trim())) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "必要参数不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (CalculateUtils.compare(etLivenessMinutes.getText().toString().trim(), "0") < 0 || CalculateUtils.compare(etLivenessSuccess.getText().toString().trim(), "0") < 0) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "必要参数应大于等于0", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (3 == checkedPosition) {
            if (TextUtils.isEmpty(etLivenessCount.getText().toString().trim()) || TextUtils.isEmpty(etLivenessSuccess.getText().toString().trim())) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "必要参数不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (CalculateUtils.compare(etLivenessCount.getText().toString().trim(), "0") < 0 || CalculateUtils.compare(etLivenessSuccess.getText().toString().trim(), "0") < 0) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "必要参数应大于等于0", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (CalculateUtils.compare(etLivenessCount.getText().toString().trim(), etLivenessSuccess.getText().toString().trim()) < 0) {
                Toast.makeText(LivenessStrategyParamsActivity.this, "连续活检次数应大于等于活体成功次数", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

}
