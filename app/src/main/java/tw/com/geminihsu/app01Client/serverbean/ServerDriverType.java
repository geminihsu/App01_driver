package tw.com.geminihsu.app01Client.serverbean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 2016/12/28.
 */

public class ServerDriverType extends RealmObject implements Cloneable ,Serializable
{

    private static final long serialVersionUID = 4284294453838563460L;
    private String dtype;
    private String dtype_cht;

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
}
