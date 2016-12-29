package tw.com.geminihsu.app01.serverbean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 2016/12/28.
 */

public class ServerSpecial extends RealmObject implements Cloneable ,Serializable
{

    private static final long serialVersionUID = 2117268267624273659L;
    private String id;
    private String dtype;
    private String dtype_cht;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDtype_cht() {
        return dtype_cht;
    }

    public void setDtype_cht(String dtype_cht) {
        this.dtype_cht = dtype_cht;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
