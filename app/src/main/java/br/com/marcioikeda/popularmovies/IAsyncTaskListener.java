package br.com.marcioikeda.popularmovies;

/**
 * Created by marcio.ikeda on 22/08/2017.
 */

public interface IAsyncTaskListener<T> {

    public void onPreExecute();
    public void onComplete(T result);
}
