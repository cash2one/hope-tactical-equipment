/*
 * Copyright (c) 2015. Hope6537 The Founder of Lab.JiChuang ,ChangChun University,
 * JiLin Province,China
 * JiChuang CloudStroage is a maven webapp using Hadoop Distributed File System for storage ' s Cloud Stroage System
 */

package org.hope6537.basic.dataobject;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.hope6537.context.ApplicationConstant;

import java.io.Serializable;

/**
 * Created by Hope6537 on 2015/3/10.
 * Any Question sent to hope6537@qq.com
 */
public abstract class BasicDO implements Serializable {

    private static final long serialVersionUID = 9162306082675078600L;

    /**
     * 结果行数
     */
    protected Integer limit;
    /**
     * 起始行 从0开始
     */
    protected Integer offset;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    protected Long created;

    protected Long updated;

    protected String isDeleted;

    protected String status;

    protected String extra;

    public static <T extends BasicDO> T getInstance(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract String commonId();

    public String getStatus() {
        if (status == null) {
            this.status = ApplicationConstant.STATUS_NORMAL;
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public final void disable() {
        this.setStatus(ApplicationConstant.STATUS_DIE);
    }

    public final void enable() {
        this.setStatus(ApplicationConstant.STATUS_NORMAL);
    }

    public Long getCreated() {
        if (this.created == null) {
            created = System.currentTimeMillis();
        }
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        if (this.updated == null) {
            updated = System.currentTimeMillis();
        }
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public String getIsDeleted() {
        if (this.isDeleted == null) {
            isDeleted = String.valueOf(ApplicationConstant.EFFECTIVE_LINE_ZERO);
        }
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setExtra(String key, Object obj) {
        JSONObject extra;
        if (this.extra == null) {
            extra = new JSONObject();
        } else {
            extra = JSONObject.parseObject(this.extra);
        }
        extra.putIfAbsent(key, obj);
        this.extra = extra.toJSONString();

    }

    public Object getExtraByKey(String key, Class clz) {
        if (this.extra == null) {
            return null;
        } else {
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(this.extra);
            } catch (JSONException e) {
                return null;
            }
            return jsonObject.getObject(key, clz);
        }
    }

    @Override
    public String toString() {
        return "BasicDO{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", created=" + created +
                ", updated=" + updated +
                ", isDeleted='" + isDeleted + '\'' +
                ", status='" + status + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
