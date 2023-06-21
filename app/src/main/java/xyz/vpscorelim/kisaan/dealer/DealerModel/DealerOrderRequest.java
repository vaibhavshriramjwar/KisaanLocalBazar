package xyz.vpscorelim.kisaan.dealer.DealerModel;

import java.util.List;

import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;

public class DealerOrderRequest {

    private String order_id;
    private String farmer_id;
    private String phone;
    private String dealer_name;
    private String address;
    private String total;
    private String status;
    private String payment_method;
    private String dealer_needed_date;
    private String farmer_delivery_date;
    private List<DealerOrderListModel> orderModels;


    public DealerOrderRequest() {
    }


    public DealerOrderRequest(String order_id, String farmer_id, String phone, String dealer_name, String address, String total, String status, String payment_method, String dealer_needed_date, String farmer_delivery_date, List<DealerOrderListModel> orderModels) {
        this.order_id = order_id;
        this.farmer_id = farmer_id;
        this.phone = phone;
        this.dealer_name = dealer_name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.payment_method = payment_method;
        this.dealer_needed_date = dealer_needed_date;
        this.farmer_delivery_date = farmer_delivery_date;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
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

    public String getDealer_needed_date() {
        return dealer_needed_date;
    }

    public void setDealer_needed_date(String dealer_needed_date) {
        this.dealer_needed_date = dealer_needed_date;
    }

    public String getFarmer_delivery_date() {
        return farmer_delivery_date;
    }

    public void setFarmer_delivery_date(String farmer_delivery_date) {
        this.farmer_delivery_date = farmer_delivery_date;
    }

    public List<DealerOrderListModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(List<DealerOrderListModel> orderModels) {
        this.orderModels = orderModels;
    }
}
