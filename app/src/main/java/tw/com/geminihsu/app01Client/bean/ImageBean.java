package tw.com.geminihsu.app01Client.bean;

import java.io.Serializable;

import io.realm.RealmObject;

public class ImageBean extends RealmObject implements Cloneable ,Serializable {
    private static final long serialVersionUID = 3723153663244129895L;

    private int user_id;
    private String user_name;
    private String uploadtype;
    private String file_id;
    private String file_url;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUploadtype() {
        return uploadtype;
    }

    public void setUploadtype(String uploadtype) {
        this.uploadtype = uploadtype;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}