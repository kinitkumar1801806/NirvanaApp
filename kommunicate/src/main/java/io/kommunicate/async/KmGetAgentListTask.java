package io.kommunicate.async;

import android.content.Context;
import android.os.AsyncTask;

import com.applozic.mobicommons.json.GsonUtils;

import java.lang.ref.WeakReference;

import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.models.KmAppSettingModel;
import io.kommunicate.services.KmUserService;

public class KmGetAgentListTask extends AsyncTask<Void, Void, KmAppSettingModel> {

    private final String appKey;
    private final KmUserService userService;
    private Exception exception;
    private final KmCallback callback;

    public KmGetAgentListTask(Context context, String appKey, KmCallback callback) {
        this.appKey = appKey;
        this.callback = callback;
        userService = new KmUserService(new WeakReference<>(context).get());
    }

    @Override
    protected KmAppSettingModel doInBackground(Void... voids) {
        try {
            return (KmAppSettingModel) GsonUtils.getObjectFromJson(userService.getAgentList(appKey), KmAppSettingModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(KmAppSettingModel agentModel) {
        if (callback != null) {
            if (exception != null) {
                callback.onFailure(exception);
            } else if (agentModel != null) {
                if (agentModel.isSuccess()) {
                    callback.onSuccess(agentModel.getResponse());
                } else {
                    callback.onFailure(agentModel);
                }
            }
        }
    }
}
