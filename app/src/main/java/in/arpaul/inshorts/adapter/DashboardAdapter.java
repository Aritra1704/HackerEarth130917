package in.arpaul.inshorts.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.arpaul.inshorts.BrowserActivity;
import in.arpaul.inshorts.DashboardActivity;
import in.arpaul.inshorts.R;
import in.arpaul.inshorts.common.SelectedNews;
import in.arpaul.inshorts.dataobjects.NewsDO;

import static in.arpaul.inshorts.common.AppConstant.BUNDLE_DISPLAY;

/**
 * Created by aritrapal on 12/09/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private String TAG = "DashboardAdapter";
    private List<NewsDO> mValues;
    private Context context;
    private SelectedNews listener;
    private ArrayList<String> arrSelect;

    public DashboardAdapter(Context context, List<NewsDO> items, ArrayList<String> arrSelect, SelectedNews listener) {
        this.context = context;
        this.mValues = items;
        this.arrSelect = arrSelect;
        this.listener = listener;
    }

    public void refresh(List<NewsDO> items) {
        this.mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.tvTitle.setText("#" + holder.mItem.title);
        holder.tvPublisher.setText(holder.mItem.publisher);

        holder.tvHostname.setText(holder.mItem.hostname);
        holder.tvUrl.setText(holder.mItem.url);

        if(arrSelect.contains(holder.mItem.id))
            holder.ivSelected.setImageResource(R.drawable.ic_selected);
        else
            holder.ivSelected.setImageResource(R.drawable.ic_unselect);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvUrl.performClick();
            }
        });

        holder.tvUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BrowserActivity.class);
                intent.putExtra(BUNDLE_DISPLAY, holder.mItem.url);
                context.startActivity(intent);
            }
        });

        holder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrSelect.contains(holder.mItem.id))
                    holder.ivSelected.setImageResource(R.drawable.ic_unselect);
                else
                    holder.ivSelected.setImageResource(R.drawable.ic_selected);

                if(listener != null)
                    listener.selected(holder.mItem.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView tvTitle;
        public final TextView tvPublisher;
        public final TextView tvHostname;
        public final TextView tvUrl;
        public final ImageView ivSelected;

        public NewsDO mItem;

        public ViewHolder(View view) {
            super(view);
            mView               = view;
            tvTitle             = (TextView) view.findViewById(R.id.tvTitle);
            tvPublisher         = (TextView) view.findViewById(R.id.tvPublisher);
            tvHostname          = (TextView) view.findViewById(R.id.tvHostname);
            tvUrl               = (TextView) view.findViewById(R.id.tvUrl);
            ivSelected          = (ImageView) view.findViewById(R.id.ivSelected);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvTitle.getText() + "'";
        }
    }
}
