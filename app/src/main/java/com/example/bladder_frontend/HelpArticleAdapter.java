package com.example.bladder_frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HelpArticleAdapter extends RecyclerView.Adapter<HelpArticleAdapter.ViewHolder> {

    private List<HelpArticle> articles;
    private List<HelpArticle> filteredArticles;
    private OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(HelpArticle article);
    }

    public HelpArticleAdapter(List<HelpArticle> articles, OnArticleClickListener listener) {
        this.articles = articles;
        this.filteredArticles = new ArrayList<>(articles);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelpArticle article = filteredArticles.get(position);
        holder.textTitle.setText(article.getTitle());
        holder.itemView.setOnClickListener(v -> listener.onArticleClick(article));
    }

    @Override
    public int getItemCount() {
        return filteredArticles.size();
    }

    public void filter(String query) {
        filteredArticles.clear();
        if (query.isEmpty()) {
            filteredArticles.addAll(articles);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (HelpArticle article : articles) {
                if (article.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        article.getContent().toLowerCase().contains(lowerCaseQuery)) {
                    filteredArticles.add(article);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
        }
    }
}
