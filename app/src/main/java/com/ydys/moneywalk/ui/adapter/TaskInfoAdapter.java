package com.ydys.moneywalk.ui.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.TaskInfo;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.ui.fragment.MakeMoneyFragment;
import com.ydys.moneywalk.util.MyTimeUtil;

import java.util.List;


public class TaskInfoAdapter extends BaseQuickAdapter<TaskInfo, BaseViewHolder> {

    private Context mContext;

    public TaskInfoAdapter(Context context, List<TaskInfo> datas) {
        super(R.layout.task_info_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TaskInfo taskInfo) {
        String tempTitle = taskInfo.getTitle();
        if (taskInfo.getTaskType().equals("ad") || taskInfo.getTaskType().equals("share")) {
            tempTitle = taskInfo.getTitle() + "(<font color='#ff5555'>" + taskInfo.getHasCompleteNum() + "</font>/" + taskInfo.getNum() + ")";
        }

        if (taskInfo.getTaskType().equals("target_num") && taskInfo.getNum() > 0) {
            tempTitle = taskInfo.getTitle() + "(<font color='#ff5555'>" + App.newStepNum + "</font>/" + taskInfo.getNum() + ")";
        }

        holder.setText(R.id.tv_task_title, Html.fromHtml(tempTitle))
                .setText(R.id.tv_task_desc, taskInfo.getDescribe())
                .setText(R.id.tv_task_gold_num, "+" + taskInfo.getGold());

        Glide.with(mContext).load(Constants.BASE_IMAGE_URL + taskInfo.getPic()).into((ImageView) holder.getView(R.id.iv_task_icon));
        holder.addOnClickListener(R.id.btn_get_now);

        Button getNowBtn = holder.getView(R.id.btn_get_now);

        if (taskInfo.getState() == 1) {
            getNowBtn.setClickable(true);
            holder.setText(R.id.btn_get_now, "去完成");
            holder.setBackgroundRes(R.id.btn_get_now, R.mipmap.go_to_done_bg);
            holder.setTextColor(R.id.btn_get_now, ContextCompat.getColor(mContext, R.color.white));
        } else if (taskInfo.getState() == 2) {
            getNowBtn.setClickable(true);
            holder.setText(R.id.btn_get_now, "立即领取");
            holder.setBackgroundRes(R.id.btn_get_now, R.drawable.task_get_now_bg);
            holder.setTextColor(R.id.btn_get_now, ContextCompat.getColor(mContext, R.color.white));
        } else {
            getNowBtn.setClickable(false);
            holder.setText(R.id.btn_get_now, "已完成");
            holder.setBackgroundRes(R.id.btn_get_now, R.drawable.task_count_down_bg);
            holder.setTextColor(R.id.btn_get_now, ContextCompat.getColor(mContext, R.color.common_title_color));
        }

        if (SPUtils.getInstance().getLong("see_video_date", 0L) > 0) {
            taskInfo.setTaskState(3);
            long diffSecond = TimeUtils.getTimeSpanByNow(SPUtils.getInstance().getLong("see_video_date") + MakeMoneyFragment.COUNT_SPACE * 1000, TimeConstants.SEC);
            taskInfo.setCountDownSecond((int) diffSecond);
        }

        if (taskInfo.getTaskType().equals("ad") && taskInfo.getTaskState() == 3 && taskInfo.getCountDownSecond() > 0) {
            holder.setBackgroundRes(R.id.btn_get_now, R.drawable.task_count_down_bg);
            holder.setTextColor(R.id.btn_get_now, ContextCompat.getColor(mContext, R.color.common_title_color));
            getNowBtn.setClickable(false);
            CountDownTimer seeVideoTimer = new CountDownTimer(taskInfo.getCountDownSecond() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("seeVideoTimer 剩余时间--->" + millisUntilFinished / 1000);
                    holder.setText(R.id.btn_get_now, MyTimeUtil.secToTime((int) millisUntilFinished / 1000) + "");
                }

                @Override
                public void onFinish() {
                    getNowBtn.setClickable(true);
                    SPUtils.getInstance().put("see_video_date", 0L);

                    //设定的次数已经都完成了
                    if (taskInfo.getHasCompleteNum() == taskInfo.getNum()) {
                        getNowBtn.setClickable(false);
                        holder.setText(R.id.btn_get_now, "已完成");
                        holder.setBackgroundRes(R.id.btn_get_now, R.drawable.task_count_down_bg);
                        holder.setTextColor(R.id.btn_get_now, ContextCompat.getColor(mContext, R.color.common_title_color));
                    } else {
                        holder.setText(R.id.btn_get_now, "去完成");
                        holder.setBackgroundRes(R.id.btn_get_now, R.mipmap.go_to_done_bg);
                        holder.setTextColor(R.id.btn_get_now, ContextCompat.getColor(mContext, R.color.white));
                    }

                }
            }.start();
        }
    }
}