package com.example.covid.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid.R;
import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>
{
    class ViewHolder extends RecyclerView.ViewHolder
    {
        public final CardView newsCardView;
        public final ImageView newsImage;
        public final TextView newsTitle;
        public final TextView newsDate;
        public final TextView newsContent;


        public ViewHolder(View view)
        {
            super(view);
            newsCardView = (CardView)itemView.findViewById(R.id.news_CardView);
            newsImage = (ImageView)itemView.findViewById(R.id.news_image);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsDate = (TextView) itemView.findViewById(R.id.news_date);
            newsContent = (TextView) itemView.findViewById(R.id.news_content);
        }
    }

    private List<Article> articles;
    private Context mContext;

    public NewsRecyclerViewAdapter(List<Article> list, Context context) {
        this.articles = list;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {return articles.size();}

    public List<Article> getArticleList() {
        return articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_news_recyclerview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Article currentArticle = articles.get(position);

        String pubDateString;
        try {
            String sourceDateString = currentArticle.getPubDate();

            SimpleDateFormat sourceSdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date date = sourceSdf.parse(sourceDateString);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            pubDateString = sdf.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            pubDateString = currentArticle.getPubDate();
        }

        viewHolder.newsTitle.setText(currentArticle.getTitle());
        viewHolder.newsContent.setText(Html.fromHtml(currentArticle.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        //viewHolder.newsContent.setText(currentArticle.getDescription());

        //Glide.with(viewHolder.itemView.getContext()).load(currentArticle.getImage()).into(viewHolder.newsImage);
        String url = currentArticle.getImage();
        if (url != null) {
            Picasso.get()
                    .load(url)
                    .transform(new RoundedCornersTransformation(30,0))
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.newsImage);
        }


        viewHolder.newsDate.setText(pubDateString);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.getLink()));
                view.getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
