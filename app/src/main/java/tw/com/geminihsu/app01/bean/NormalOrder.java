package tw.com.geminihsu.app01.bean;
import java.io.Serializable;

import io.realm.RealmObject;

public class NormalOrder extends RealmObject implements Cloneable ,Serializable {

    private static final long serialVersionUID = 4463209540010808209L;
    private int order_id;//account uid
    private int user_id;//account uid
    private String user_uid;
    private String user_did;
    private String user_name;
    private String accesskey;
    private String dtype;
    private OrderLocationBean begin;
    private OrderLocationBean stop;
    private OrderLocationBean end;
    private String begin_address;
    private String stop_address;
    private String end_address;
    private String cargo_size;
    private String cargo_imgs;
    private String car_special;
    private String remark;
    private String price;
    private String tip;
    private String ticket_id;
    private String ticket_status;//0:未接單 1:進行中 2:已結案
    private String orderdate;
    private String target;//0:person, 1:merchandise

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUser_did() {
        return user_did;
    }

    public void setUser_did(String user_did) {
        this.user_did = user_did;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public OrderLocationBean getBegin() {
        return begin;
    }

    public void setBegin(OrderLocationBean begin) {
        this.begin = begin;
    }

    public OrderLocationBean getStop() {
        return stop;
    }

    public void setStop(OrderLocationBean stop) {
        this.stop = stop;
    }

    public OrderLocationBean getEnd() {
        return end;
    }

    public void setEnd(OrderLocationBean end) {
        this.end = end;
    }

    public String getCargo_size() {
        return cargo_size;
    }

    public void setCargo_size(String cargo_size) {
        this.cargo_size = cargo_size;
    }

    public String getCargo_imgs() {
        return cargo_imgs;
    }

    public void setCargo_imgs(String cargo_imgs) {
        this.cargo_imgs = cargo_imgs;
    }

    public String getCar_special() {
        return car_special;
    }

    public void setCar_special(String car_special) {
        this.car_special = car_special;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getBegin_address() {
        return begin_address;
    }

    public void setBegin_address(String begin_address) {
        this.begin_address = begin_address;
    }

    public String getStop_address() {
        return stop_address;
    }

    public void setStop_address(String stop_address) {
        this.stop_address = stop_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}