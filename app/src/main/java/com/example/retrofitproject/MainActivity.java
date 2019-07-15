package com.example.retrofitproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private JsonApi jsonApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.text_view_result);

        Gson gson = new GsonBuilder().serializeNulls().create(); //if forced to delete  value

        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://jsonplaceholder.typicode.com/")
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();//when gson is not using .addConverterFactory(GsonConverterFactory.create())

         jsonApi = retrofit.create(JsonApi.class);
         // getPost();
        // getComment();
        // createPost();
        //  updatePost();

        deletePost();
    }

    private void deletePost() {
        Call<Void> call = jsonApi.deletePost(5);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textView.setText("Code: "+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                textView.setText(throwable.getMessage());
            }
        });
    }

    private void updatePost() {

        Post post = new Post(23,null,"New massage update");
        Call<Post>call = jsonApi.patchPost(5,post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful())
                {
                    textView.setText("Code :"+ response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content +="Code: "+response.code()+"\n";
                content +="ID: "+postResponse.getId() + "\n";
                content +="User ID: "+postResponse.getUserId() + "\n";
                content +="Title: "+postResponse.getTitle() + "\n";
                content +="Text: "+postResponse.getText() + "\n\n";
                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable throwable) {
                textView.setText(throwable.getMessage());
            }
        });
    }

    private void createPost() {
        Post post = new Post(23,"New Title","New massage");

        Map <String,String> fields = new HashMap<>();
        fields.put("userId","25");
        fields.put("title","New Title");

        //Call <Post> call = jsonApi.createPost(24,"New Title","New massage");
        Call <Post> call = jsonApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful())
                {
                    textView.setText("Code :"+ response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content +="Code: "+response.code()+"\n";
                content +="ID: "+postResponse.getId() + "\n";
                content +="User ID: "+postResponse.getUserId() + "\n";
                content +="Title: "+postResponse.getTitle() + "\n";
                content +="Text: "+postResponse.getText() + "\n\n";
                textView.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable throwable) {
                textView.setText(throwable.getMessage());
            }
        });
    }

    private void getComment() {

        Call<List<Comment>> call = jsonApi.getComments("comments?postId=1");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if (!response.isSuccessful())
                {
                    textView.setText("Code :"+ response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for (Comment comment : comments)
                {
                    String content = "";
                    content +="ID: "+comment.getId() + "\n";
                    content +="Post ID: "+comment.getPostId() + "\n";
                    content +="Name: "+comment.getName() + "\n";
                    content +="Email: "+comment.getEmail() + "\n";
                    content +="Text: "+comment.getText() + "\n\n";
                    textView.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable throwable) {

                textView.setText(throwable.getMessage());

            }
        });
    }

    private void getPost() {

        Map< String , String> parameters = new HashMap<>();
        parameters.put("userId" , "1");
        parameters.put("_sort" , "id");
        parameters.put("_order" , "desc");

       // Call<List<Post>> call = jsonApi.getPosts(parameters); //sort null, order null
       Call <List<Post>> call = jsonApi.getPosts(new Integer[]{1,2,3},"id","desc");

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful())
                {
                    textView.setText("Code :"+ response.code());
                    return;
                }

                List<Post> posts = response.body();
                for (Post post : posts)
                {
                    String content = "";
                    content +="ID: "+post.getId() + "\n";
                    content +="User ID: "+post.getUserId() + "\n";
                    content +="Title: "+post.getTitle() + "\n";
                    content +="Text: "+post.getText() + "\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable throwable) {

                textView.setText(throwable.getMessage());
            }
        });
    }
}
