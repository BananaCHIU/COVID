package com.example.covid.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

public class NewsViewModel extends ViewModel {
    private MutableLiveData<Channel> articleListLive = null;
    private String urlString = "https://www.news.gov.hk/en/categories/covid19/html/articlelist.rss.xml";

    private MutableLiveData<String> snackbar = new MutableLiveData<>();

    public MutableLiveData<Channel> getChannel() {
        if (articleListLive == null) {
            articleListLive = new MutableLiveData<>();
        }
        return articleListLive;
    }

    private void setChannel(Channel channel) {
        this.articleListLive.postValue(channel);
    }

    public LiveData<String> getSnackbar() {
        return snackbar;
    }

    public void onSnackbarShowed() {
        snackbar.setValue(null);
    }

    public void fetchFeed() {

        Parser parser = new Parser();
        parser.onFinish(new OnTaskCompleted() {

            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(Channel channel) {
                setChannel(channel);
            }

            //what to do in case of error
            @Override
            public void onError(Exception e) {
                setChannel(new Channel(null, null, null, null,  new ArrayList<Article>()));
                e.printStackTrace();
                snackbar.postValue("An error has occurred. Please try again");
            }
        });
        parser.execute(urlString);
        parser.cancel();
    }
}
