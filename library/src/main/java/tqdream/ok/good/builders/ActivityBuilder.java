package tqdream.ok.good.builders;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ActivityBuilder helps to build {@link Activity} {@link Intent} and start {@link Activity}.
 *
 * @author Leonardo Taehwan Kim
 */
public class ActivityBuilder {

    final Intent intent;

    public <C extends Activity> ActivityBuilder(@NonNull Class<C> clazz) {
        intent = null;
//        intent = new Intent(Base.getContext(), clazz);
    }

    public <T extends Serializable> ActivityBuilder set(@NonNull String key, T value) {
        intent.putExtra(key, value);
        return this;
    }

    public ActivityBuilder set(@NonNull String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }

    public ActivityBuilder set(@NonNull String key, Parcelable[] value) {
        intent.putExtra(key, value);
        return this;
    }

    public <T extends Parcelable> ActivityBuilder set(@NonNull String key, ArrayList<T> value) {
        intent.putExtra(key, value);
        return this;
    }

    public ActivityBuilder remove(@NonNull String key) {
        intent.removeExtra(key);
        return this;
    }

    public ActivityBuilder setFlags(int flags) {
        intent.setFlags(flags);
        return this;
    }

    public ActivityBuilder addFlags(int flags) {
        intent.addFlags(flags);
        return this;
    }

    public Intent buildIntent() {
        return intent;
    }

    public void start() {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Base.getContext().startActivity(intent);
    }

    public void startForResult(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    @TargetApi(16)
    public void startForResult(@NonNull Activity activity, int requestCode, @Nullable Bundle options) {
        activity.startActivityForResult(intent, requestCode, options);
    }
}
