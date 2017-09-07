package tw.com.geminihsu.app01Client.serverbean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by geminihsu on 2016/12/28.
 */

public class ServerCarbrand extends RealmObject implements Cloneable ,Serializable
{

    private static final long serialVersionUID = -3384034513438630734L;
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
