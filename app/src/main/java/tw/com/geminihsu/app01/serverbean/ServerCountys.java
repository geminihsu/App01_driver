package tw.com.geminihsu.app01.serverbean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 2016/12/28.
 */

public class ServerCountys extends RealmObject implements Cloneable ,Serializable
{

    private static final long serialVersionUID = -8618657844596642315L;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
