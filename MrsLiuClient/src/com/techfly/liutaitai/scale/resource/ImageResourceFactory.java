package com.techfly.liutaitai.scale.resource;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class ImageResourceFactory {
    private Map<ResourceType, Class<? extends BaseImageResource>> mResourceTypes;

    private static ImageResourceFactory mFactory;

    public static final String SEPERATOR = "://";

    private ImageResourceFactory(Context context) {
        mResourceTypes = new HashMap<ResourceType, Class<? extends BaseImageResource>>();
        registerResourceType(ResourceType.RES_HTTP, HttpImageResource.class);
        registerResourceType(ResourceType.RES_LOCAL, LocalImageResource.class);
        registerResourceType(ResourceType.RES_ASSET, AssetImageResource.class);
    }

    public static ImageResourceFactory getInstance(Context context) {
        if (mFactory == null) {
            mFactory = new ImageResourceFactory(context);
        }
        return mFactory;
    }

    public void registerResourceType(ResourceType type,
            Class<? extends BaseImageResource> clazz) {
        mResourceTypes.put(type, clazz);
    }

    public <T> BaseImageResource getResource(ResourceType type,
            List<T> resources) {
        if (type != null && mResourceTypes.containsKey(type)) {
            Class<? extends BaseImageResource> resourceType = mResourceTypes
                    .get(type);
            try {
                Constructor<? extends BaseImageResource> constructor = resourceType
                        .getConstructor(List.class);
                return (BaseImageResource) constructor.newInstance(resources);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
