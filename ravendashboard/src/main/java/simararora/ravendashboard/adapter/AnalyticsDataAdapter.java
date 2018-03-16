package simararora.ravendashboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import simararora.ravendashboard.R;
import simararora.ravendashboard.model.Analytics;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class AnalyticsDataAdapter extends RecyclerView.Adapter<AnalyticsDataAdapter.AnalyticsDataViewHolder> {
    private List<Analytics> analyticsList;

    public class AnalyticsDataViewHolder extends RecyclerView.ViewHolder {
        TextView tvData, tvDataIdentifier;

        public AnalyticsDataViewHolder(View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tv_data);
            tvDataIdentifier = itemView.findViewById(R.id.tv_data_identifier);
        }

    }

    public AnalyticsDataAdapter(List<Analytics> analyticsList) {
        Collections.sort(analyticsList, new Comparator<Analytics>() {
            @Override
            public int compare(Analytics lhs, Analytics rhs) {
                return Integer.valueOf(rhs.getDataIdentifier()).compareTo(Integer.valueOf(lhs.getDataIdentifier()));
            }
        });
        this.analyticsList = analyticsList;
    }

    @Override
    public AnalyticsDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analytics, parent, false);
        return new AnalyticsDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnalyticsDataViewHolder holder, int position) {
        if (position == -1) return;
        Analytics analytics = this.analyticsList.get(position);
        holder.tvData.setText(analytics.getData());
        holder.tvDataIdentifier.setText(analytics.getDataIdentifier());
    }

    @Override
    public int getItemCount() {
        return this.analyticsList.size();
    }
}
