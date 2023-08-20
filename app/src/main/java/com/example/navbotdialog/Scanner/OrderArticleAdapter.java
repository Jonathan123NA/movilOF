package com.example.navbotdialog.Scanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navbotdialog.R;

import java.util.List;

public class OrderArticleAdapter extends RecyclerView.Adapter<OrderArticleAdapter.OrderArticleViewHolder> {

    private List<OrderArticle> orderArticleList;
    private int scannedArticleId = -1;

    public OrderArticleAdapter(List<OrderArticle> orderArticleList) {
        this.orderArticleList = orderArticleList;
    }

    public void setScannedArticleId(int scannedArticleId) {
        this.scannedArticleId = scannedArticleId;
    }

    @NonNull
    @Override
    public OrderArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_article, parent, false);
        return new OrderArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderArticleViewHolder holder, int position) {
        OrderArticle orderArticle = orderArticleList.get(position);

        if (orderArticle.getId_articulo() == scannedArticleId) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.AzulMarino));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
        }

        holder.bind(orderArticle);
    }

    @Override
    public int getItemCount() {
        return orderArticleList.size();
    }

    public class OrderArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView orderIdTextView;
        private TextView articleIdTextView;

        public OrderArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id_text_view);
            articleIdTextView = itemView.findViewById(R.id.article_id_text_view);
        }

        public void bind(OrderArticle orderArticle) {
            orderIdTextView.setText("ID Orden: " + orderArticle.getId_orden());
            articleIdTextView.setText("ID Artículo: " + orderArticle.getId_articulo());
        }
    }
}

