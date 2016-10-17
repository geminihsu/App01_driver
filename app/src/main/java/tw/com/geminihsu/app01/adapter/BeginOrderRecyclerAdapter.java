package tw.com.geminihsu.app01.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.R;


public class BeginOrderRecyclerAdapter extends RecyclerView.Adapter<BeginOrderRecyclerAdapter.ViewHolder> {

    private List<BeginOrderListItem> mItemsList;
    private Fragment_BeginOrderInteractive.OnListItemClickListener mListItemClickListener;

    public BeginOrderRecyclerAdapter(List<BeginOrderListItem> itemsList) {
        mItemsList = itemsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_begin_order_list_item, parent, false);

        // Set the view to the ViewHolder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String order_title = mItemsList.get(position).order_title;
        holder.order_title.setText(order_title);

        // 出發地點
        final String departure = mItemsList.get(position).departure;
        holder.departure.setText(departure);

        // 目的地
        final String destination = mItemsList.get(position).destination;
        holder.destination.setText(destination);

        //時間類型(即時，預約)
        final String order_time = mItemsList.get(position).order_time;
        holder.order_time.setText(order_time);

        final String btn_take = mItemsList.get(position).button_information;
        holder.btn_take_over.setText(btn_take);

        holder.btn_take_look.setVisibility(mItemsList.get(position).button_take_look_visible);

        holder.btn_take_look.setTag(position);
        holder.btn_take_over.setTag(position);

    }

   /* @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String itemTitle = mItemsList.get(position);
        holder.title.setText(itemTitle);
    }*/

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    // Create the ViewHolder class to keep references to your views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView order_title;
        TextView departure;
        TextView destination;
        TextView order_time;

        Button btn_take_look;
        Button btn_take_over;

        /**
         * Constructor
         * @param v The container view which holds the elements from the row item xml
         */
        public ViewHolder(View v) {
            super(v);

            order_title = (TextView) v.findViewById(R.id.order_title);
            departure = (TextView)v.findViewById(R.id.departure);
            destination = (TextView) v.findViewById(R.id.destination);
            order_time = (TextView)v.findViewById(R.id.order_time);

            btn_take_look = (Button) v.findViewById(R.id.take_look);
            btn_take_over = (Button) v.findViewById(R.id.take_over);

            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (null != mListItemClickListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListItemClickListener.onListItemClick(view);
            }


        }
    }

    public void setOnItemTapListener(Fragment_BeginOrderInteractive.OnListItemClickListener itemClickListener) {
        mListItemClickListener = itemClickListener;
    }


}
