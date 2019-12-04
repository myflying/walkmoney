package com.ydys.moneywalk.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ydys.moneywalk.R;

public class QuestionItemAdapter extends BaseExpandableListAdapter {

    private String[] parentsData = {"没有网络还能计步吗", "没有网络还能计步吗", "没有网络还能计步吗"};
    private String[][] childData = {{"可以计步，很准确的"}, {"可以计步，很准确的"}, {"可以计步，很准确的"}};

    private Context mContent;

    public QuestionItemAdapter(Context context) {
        this.mContent = context;
    }

    @Override
    public int getGroupCount() {
        return parentsData.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView tv = null;
        if (convertView == null) {
            convertView = View.inflate(mContent, R.layout.question_parent, null);
            tv = (TextView) convertView.findViewById(R.id.tv_parent_title);
            convertView.setTag(tv);
        } else {
            tv = (TextView) convertView.getTag();
        }
        tv.setText(parentsData[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContent, R.layout.question_children, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_children);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(childData[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {
        TextView tv;
    }
}

