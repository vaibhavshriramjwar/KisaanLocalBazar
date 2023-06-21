package xyz.vpscorelim.kisaan.customer;

import java.util.List;

public class CustomerRequest {

    private String order_id;
    private String farmer_id;
    private String phone;
    private String customer_name;
    private String address;
    private String total;
    private String status;
    private String payment_method;
    private String customer_needed_date;
    private String farmer_delivery_date;
    private List<CustomerOrderModel> orderModels;


    public CustomerRequest() {
    }

    public CustomerRequest(String order_id,String farmer_id, String phone,String customer_name, String address, String total, String status,String payment_method,String customer_needed_date,String farmer_delivery_date, List<CustomerOrderModel> orderModels) {
        this.order_id    = order_id;
        this.farmer_id   = farmer_id;
        this.phone       = phone;
        this.customer_name = customer_name;
        this.address     = address;
        this.total       = total;
        this.status      = "Wait For Response";
        this.payment_method=payment_method;
        this.customer_needed_date=customer_needed_date;
        this.farmer_delivery_date=farmer_delivery_date;
        this.orderModels = orderModels;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getCustomer_needed_date() {
        return customer_needed_date;
    }

    public void setCustomer_needed_date(String customer_needed_date) {
        this.customer_needed_date = customer_needed_date;
    }

    public String getFarmer_delivery_date() {
        return farmer_delivery_date;
    }

    public void setFarmer_delivery_date(String farmer_delivery_date) {
        this.farmer_delivery_date = farmer_delivery_date;
    }

    public List<CustomerOrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(List<CustomerOrderModel> orderModels) {
        this.orderModels = orderModels;
    }
}
