package xyz.vpscorelim.kisaan.farmer.farmer_Model;

import java.util.List;

import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderModel;

public class FarmerRequest {

    private String order_id;
    private String dealer_id;
    private String phone;
    private String farmer_name;
    private String address;
    private String total;
    private String status;
    private String payment_method;
    private String customer_needed_date;
    private String dealer_delivery_date;
    private List<FarmerOrderModel> orderModels;


    public FarmerRequest() {
    }


    public FarmerRequest(String order_id, String dealer_id, String phone, String farmer_name, String address, String total, String status, String payment_method, String customer_needed_date, String dealer_delivery_date, List<FarmerOrderModel> orderModels) {
        this.order_id = order_id;
        this.dealer_id = dealer_id;
        this.phone = phone;
        this.farmer_name = farmer_name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.payment_method = payment_method;
        this.customer_needed_date = customer_needed_date;
        this.dealer_delivery_date = dealer_delivery_date;
        this.orderModels = orderModels;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDealer_delivery_date() {
        return dealer_delivery_date;
    }

    public void setDealer_delivery_date(String dealer_delivery_date) {
        this.dealer_delivery_date = dealer_delivery_date;
    }

    public List<FarmerOrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(List<FarmerOrderModel> orderModels) {
        this.orderModels = orderModels;
    }
}
