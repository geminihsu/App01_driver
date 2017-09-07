package tw.com.geminihsu.app01Client.serverbean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 2016/12/28.
 */

public class ServerImageType extends RealmObject implements Cloneable ,Serializable
{

    private static final long serialVersionUID = -3148937662760158369L;
    private String utype;
    private String utype_cht;

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public String getUtype_cht() {
        return utype_cht;
    }

    public void setUtype_cht(String utype_cht) {
        this.utype_cht = utype_cht;
    }
}
