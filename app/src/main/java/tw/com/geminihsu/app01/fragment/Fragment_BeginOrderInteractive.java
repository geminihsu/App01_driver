package tw.com.geminihsu.app01.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.adapter.BeginOrderListItem;
import tw.com.geminihsu.app01.adapter.BeginOrderRecyclerAdapter;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.orderDivider.DividerItemDecoration;


public class Fragment_BeginOrderInteractive extends Fragment {
    private OnListItemClickListener mListItemClickListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PageFragment.
     */
    public static Fragment_BeginOrderInteractive newInstance() {
        return new Fragment_BeginOrderInteractive();
    }

    public Fragment_BeginOrderInteractive() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beginorderinteractive, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //add ItemDecoration
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        List<BeginOrderListItem> orderList = new ArrayList<BeginOrderListItem>();

        for (int i = 0; i < 10; i++) {
            BeginOrderListItem beginOrderListItem = new BeginOrderListItem();
            beginOrderListItem.order_title = "一般搭乘(小費:50元)";
            beginOrderListItem.departure = "從:台中市大道一段1號";
            beginOrderListItem.destination = "到:台中市政府";
            beginOrderListItem.order_time = "即時";
            orderList.add(beginOrderListItem);
        }

        BeginOrderRecyclerAdapter adapter = new BeginOrderRecyclerAdapter(orderList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemTapListener(mListItemClickListener);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListItemClickListener = (OnListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnListItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListItemClickListener = null;
    }


    public interface OnListItemClickListener {
        void onListItemClick(String title);
    }
}