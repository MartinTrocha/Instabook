package com.example.martin.instabook;


import android.support.annotation.NonNull;
import android.util.Log;

import com.example.martin.instabook.model.PostModel;
import com.example.martin.instabook.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class FirebaseHelpers {

    private static final String TAG = "FIREBASE DATABASE";
    private static final String USERS_TABLE = "users";
    private static final String POSTS_TABLE = "posts";

    // Zisti ci je niekto prihlaseny alebo nie
    public static String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            return user.getUid();
        } else {
            return null;
        }
    }

    public static void getCurrentUserDataFromDb(String uid, final FirebaseResults callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_TABLE);
        Query query = ref.child(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel result;
                // data su dojebane a obcas to hodi date ako mapu a obcas normalne ako Long
                try {

                    result = new UserModel();//dataSnapshot.getValue(UserModel.class);
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    result.setDate((Long) ((HashMap) map.get("date")).get("date"));
                    result.setEmail((String) map.get("email"));
                    result.setNumberOfPosts(Integer.valueOf(map.get("numberOfPosts").toString())); // konverzia na pana
                    result.setUsername((String) map.get("username"));
                    result.setEmail((String) map.get("email"));
                    result.setDbKey(dataSnapshot.getKey());

                }catch ( java.lang.ClassCastException e){
                    result=dataSnapshot.getValue(UserModel.class);
                    if (result != null) {
                        result.setDbKey(dataSnapshot.getKey());
                    }
                }

                callback.onUserResult(result);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onUserResultFailed();
            }
        });
    }

    // TODO osetrit
    // Tato metoda vrati vsetky prispevky prihlaseneho usera alebo vlozeneho userId
    public static void getPostsByUserId(final String userIdFromCall, final FirebaseResults callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(POSTS_TABLE);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PostModel> resultList = new ArrayList<PostModel>();
                Log.i(TAG, "Number of Posts: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    PostModel resultPost = post.getValue(PostModel.class);
                    if (resultPost.getUserId().equals(userIdFromCall)) {
                        resultList.add(resultPost);
                    }
                }
                callback.onPostResultById(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /* TODO osetrit
    Tato metoda ti ma vratit zoradene vsetky prispevky zoradene podla najnovsieho
     */
    public static void getAllPostsFromDbOrderedByRecency(final FirebaseResults callback) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(POSTS_TABLE);

        // TODO: Zoradit podla firebase ale je to taka picovina ze to nedokaze podla tych childov urcit ze ktory
        ref.orderByChild(ref.child("date").getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PostModel> resultList = new ArrayList<>();
                Log.i(TAG, "Number of Posts: " + dataSnapshot.getChildrenCount());
                for(DataSnapshot post : dataSnapshot.getChildren()) {
                    PostModel result = post.getValue(PostModel.class);
                    resultList.add(result);
                }

                Collections.sort(resultList, new Comparator<PostModel>() {
                    @Override
                    public int compare(PostModel o1, PostModel o2) {
                        Long d1 = o1.getDateAsLong();
                        Long d2 = o2.getDateAsLong();
                        return d1 > d2 ? -1 : (d1 < d2 ? 1 : 0);
                    }
                });
                callback.onPostResultAll(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    Tato metoda prida prispevok a este aj updatne udaje o uzivatelovi ze kolko ich uz tam najebal
     */
    public static void addPostToDb(final PostModel newPost) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(POSTS_TABLE);
        ref.push().setValue(newPost);
        Log.d(TAG, "Uploaded post by " + newPost.getUsername());

        getPostsByUserId(newPost.getUserId(), new FirebaseResultsImpl() {
            @Override
            public void onPostResultById(List<PostModel> posts) {
                super.onPostResultById(posts);
                Integer numOfPosts = posts.size();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_TABLE);
                ref.child(newPost.getUserId()).child("numberOfPosts").setValue(numOfPosts);
            }
        });
    }

}
