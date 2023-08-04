package com.hzq.plainplugin.swing.config;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Huangzq
 * @description
 * @date 2023/8/4 11:24
 */
public class ConfigCache {
    private MpIdeaModule mpIdeaModule = new MpIdeaModule();
    private static ConfigCache cache;

    private ConfigCache() {
    }

    public static ConfigCache getInstance() {
        if (cache == null) {
            cache = new ConfigCache();
        }
        return cache;
    }

    static {
        File file = new File(Constants.CONFIG_PATH);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                ByteArrayOutputStream byteBufferOutputStream = new ByteArrayOutputStream();
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1) {
                    byteBufferOutputStream.write(bytes, 0, len);
                }
                String s = new String(byteBufferOutputStream.toByteArray(), StandardCharsets.UTF_8);

                MpIdeaModule mpIdeaModule = JSONObject.parseObject(s, MpIdeaModule.class);
                if (mpIdeaModule != null) {
                    //全局缓存
                    ConfigCache.getInstance().setMpIdeaModule(mpIdeaModule);
                }

                inputStream.close();
                byteBufferOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public MpIdeaModule getMpIdeaModule() {
        return mpIdeaModule;
    }

    public void setMpIdeaModule(MpIdeaModule mpIdeaModule) {
        this.mpIdeaModule = mpIdeaModule;
    }
}
