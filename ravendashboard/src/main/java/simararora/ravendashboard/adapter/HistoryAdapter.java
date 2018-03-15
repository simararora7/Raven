package simararora.ravendashboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import simararora.ravendashboard.R;
import simararora.ravendashboard.model.History;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ResourceHistoryViewHolder> {
    private List<History> resourceHistoryList;

    public class ResourceHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvEncodedKey, tvValue;

        public ResourceHistoryViewHolder(View itemView) {
            super(itemView);
            tvEncodedKey = itemView.findViewById(R.id.tv_encoded_key);
            tvValue = itemView.findViewById(R.id.tv_value);
        }

    }

    public HistoryAdapter(List<History> resourceHistoryList) {
        this.resourceHistoryList = resourceHistoryList;
    }

    @Override
    public ResourceHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ResourceHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResourceHistoryViewHolder holder, int position) {
        if (position == -1) return;
        History resourceHistory = this.resourceHistoryList.get(position);
        holder.tvEncodedKey.setText(resourceHistory.getEncodedKey());
        holder.tvValue.setText(resourceHistory.getValue());
    }

    @Override
    public int getItemCount() {
        return this.resourceHistoryList.size();
    }
}
