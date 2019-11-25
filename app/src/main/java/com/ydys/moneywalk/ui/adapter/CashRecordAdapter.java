package com.ydys.moneywalk.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyInfo;
import com.ydys.moneywalk.bean.CashRecordInfo;

import java.util.List;


public class CashRecordAdapter extends BaseQuickAdapter<CashRecordInfo, BaseViewHolder> {

    private Context mContext;

    public CashRecordAdapter(Context context, List<CashRecordInfo> datas) {
        super(R.layout.cash_record_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CashRecordInfo temp) {

    }
}