package com.robillo.readrush.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robillo.readrush.data.db.DbHelper;
import com.robillo.readrush.data.network.ApiHelper;
import com.robillo.readrush.data.prefs.PreferencesHelper;
import com.robillo.readrush.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by robinkamboj on 08/10/17.
 */

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
//        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }
    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public Long getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(Long userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPreferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath) {

        setAccessToken(accessToken);
        setCurrentUserId(userId);
        setCurrentUserLoggedInMode(loggedInMode);
        setCurrentUserName(userName);
        setCurrentUserEmail(email);
        setCurrentUserProfilePicUrl(profilePicPath);

        updateApiHeader(userId, accessToken);
    }

    @Override
    public void updateApiHeader(Long userId, String accessToken) {

    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                null,
                null,
                null);
    }


    @Override
    public Observable<Boolean> seedDatabaseQuestions() {

        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();

//        return mDbHelper.isQuestionEmpty()
//                .concatMap(new Function<Boolean, ObservableSource<? extends Boolean>>() {
//                    @Override
//                    public ObservableSource<? extends Boolean> apply(Boolean isEmpty)
//                            throws Exception {
//                        if (isEmpty) {
//                            Type type = $Gson$Types
//                                    .newParameterizedTypeWithOwner(null, List.class,
//                                            Question.class);
//                            List<Question> questionList = gson.fromJson(
//                                    CommonUtils.loadJSONFromAsset(mContext,
//                                            AppConstants.SEED_DATABASE_QUESTIONS),
//                                    type);
//
//                            return saveQuestionList(questionList);
//                        }
//                        return Observable.just(false);
//                    }
//                });
        return Observable.just(false);
    }

    @Override
    public Observable<Boolean> seedDatabaseOptions() {

        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();

//        return mDbHelper.isOptionEmpty()
//                .concatMap(new Function<Boolean, ObservableSource<? extends Boolean>>() {
//                    @Override
//                    public ObservableSource<? extends Boolean> apply(Boolean isEmpty)
//                            throws Exception {
//                        if (isEmpty) {
//                            Type type = new TypeToken<List<Option>>() {
//                            }
//                                    .getType();
//                            List<Option> optionList = gson.fromJson(
//                                    CommonUtils.loadJSONFromAsset(mContext,
//                                            AppConstants.SEED_DATABASE_OPTIONS),
//                                    type);
//
//                            return saveOptionList(optionList);
//                        }
//                        return Observable.just(false);
//                    }
//                });
        return Observable.just(false);
    }
}
