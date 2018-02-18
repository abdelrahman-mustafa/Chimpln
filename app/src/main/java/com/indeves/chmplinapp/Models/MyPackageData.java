package com.indeves.chmplinapp.Models;

public class MyPackageData {
    private String packageName;
    private String packageType;
    private String packagePrice;
    private String packageCurrency;
    private String PackageCondition;
    private String PackageSpecs;

    public MyPackageData(String packageName, String packageType, String packagePrice, String packageCurrency, String packageCondition, String packageSpecs) {
        this.packageName = packageName;
        this.packageType = packageType;
        this.packagePrice = packagePrice;
        this.packageCurrency = packageCurrency;
        PackageCondition = packageCondition;
        PackageSpecs = packageSpecs;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageCurrency() {
        return packageCurrency;
    }

    public void setPackageCurrency(String packageCurrency) {
        this.packageCurrency = packageCurrency;
    }

    public String getPackageCondition() {
        return PackageCondition;
    }

    public void setPackageCondition(String packageCondition) {
        PackageCondition = packageCondition;
    }

    public String getPackageSpecs() {
        return PackageSpecs;
    }

    public void setPackageSpecs(String packageSpecs) {
        PackageSpecs = packageSpecs;
    }
}
