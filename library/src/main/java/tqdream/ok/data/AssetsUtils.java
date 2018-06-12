/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tqdream.ok.data;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import tqdream.ok.data.json.GsonUtil;

/**
 * Assets操作
 *
 * @author venshine
 */
public class AssetsUtils {

    private static final String ENCODING = "UTF-8";

    /**
     * Get file from assets
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static InputStream getFileFromAssets(Context context, String fileName) throws IOException {
        AssetManager am = context.getAssets();
        return am.open(fileName);
    }

    /**
     * Get text file from assets
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTextFromAssets(Context context, String fileName) {
        String result = null;
        try {
            InputStream in = getFileFromAssets(context, fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, Charset.forName(ENCODING));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get text file from assets
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTextFromAssets2(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getFileFromAssets(context, fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse a json file in the assets to ArrayList<?>
     *
     * @param context
     * @param jsonName
     * @return
     */
    public static <T> ArrayList<T> parseJsonToArrayList(Context context, String jsonName) {
        String json = getTextFromAssets(context, jsonName);
        return GsonUtil.jsonToArrayList(json);
    }

    /**
     * Parse a json file in the assets to List<?>
     *
     * @param context
     * @param jsonName
     * @return
     */
    public static List<?> parseJsonToList(Context context, String jsonName) {
        String json = getTextFromAssets(context, jsonName);
        return GsonUtil.jsonToList(json);
    }

    /**
     * Parse a json file in the assets to Bean
     *
     * @param context
     * @param jsonName
     * @param clazz
     * @return
     */
    public static <T> T parseJsonToObject(Context context, String jsonName, Class<T> clazz) {
        String json = getTextFromAssets(context, jsonName);
        return GsonUtil.jsonToObject(json, clazz);
    }

}
