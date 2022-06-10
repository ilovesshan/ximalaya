package com.ilovesshan.ximalaya.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/10
 * @description:
 */

@SuppressLint("NotifyDataSetChanged")
public class SearchRecommendWordListAdapter extends RecyclerView.Adapter<SearchRecommendWordListAdapter.InnerHolder> {

    List<QueryResult> mKeyWordList = new ArrayList<>();
    private OnRecommendHotWordItemClickListener mOnRecommendHotWordItemClickListener;

    @NonNull
    @Override
    public SearchRecommendWordListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recommend_hot_words_list, null);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecommendWordListAdapter.InnerHolder holder, int position) {
        View itemView = holder.itemView;
        String keyword = mKeyWordList.get(position).getKeyword();
        TextView mHotWordsName = itemView.findViewById(R.id.hot_words_name);
        mHotWordsName.setText(keyword);

        itemView.setOnClickListener((v) -> mOnRecommendHotWordItemClickListener.onClick(keyword));
    }

    @Override
    public int getItemCount() {
        return mKeyWordList == null ? 0 : mKeyWordList.size();
    }

    public void setData(List<QueryResult> keyWordList) {
        this.mKeyWordList.clear();
        this.mKeyWordList = keyWordList;
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public  void  setOnRecommendHotWordItemClick(OnRecommendHotWordItemClickListener listener){
        this.mOnRecommendHotWordItemClickListener = listener;
    }

    public interface OnRecommendHotWordItemClickListener {
        void onClick(String hotWord);
    }
}
