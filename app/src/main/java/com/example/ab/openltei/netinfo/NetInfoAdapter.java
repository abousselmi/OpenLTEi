package com.example.ab.openltei.netinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ab.openltei.R;

import java.util.ArrayList;

public class NetInfoAdapter extends ArrayAdapter<NetInfo> {
    public NetInfoAdapter(Context context, ArrayList<NetInfo> info) {
        super(context, 0, info);
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        NetInfo netInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.net_info,
                    parent, false);
        }
        TextView netInfoTvKey = (TextView) convertView.findViewById(R.id.net_info_tv_key);
        TextView netInfoTvValues = (TextView) convertView.findViewById(R.id.net_info_tv_value);
        if (netInfo != null) {
            netInfoTvKey.setText(netInfo.infoKey);
            netInfoTvValues.setText(netInfo.infoValue);
        } else {
            netInfoTvKey.setText(R.string.not_available);
            netInfoTvValues.setText(R.string.not_available);
        }
        return convertView;
    }
}