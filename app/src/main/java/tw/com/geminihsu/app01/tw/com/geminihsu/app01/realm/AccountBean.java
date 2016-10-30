package tw.com.geminihsu.app01.tw.com.geminihsu.app01.realm;

/**
 * Created by geminihsu on 16/10/28.
 */
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
public class AccountBean extends RealmObject{
    @PrimaryKey
    private String code;//DateBase编号
    private String name;//姓名
    private String identify;//身分證字號
    private String phoneNumber;//手機號碼
    private String loginPassword;//登入密碼
    private String recommendedId;//推薦人代號
    private int typeId;// 1:客戶 2:司機 3:管理員

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getRecommendedId() {
        return recommendedId;
    }

    public void setRecommendedId(String recommendedId) {
        this.recommendedId = recommendedId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

}
