package com.dm.model;

public class ProductModel {
private String nameProduct;
private double priceProduct;
private double mpProduct;
private double dpProduct;


	public ProductModel(String nameProduct,double priceProduct,double mpProduct,double dpProduct) {
		this.setPriceProduct(priceProduct);
		this.setNameProduct(nameProduct);
		this.setMpProduct(mpProduct);
		this.setDpProduct(dpProduct);
	}


	/**
	 * @return the nameProduct
	 */
	public String getNameProduct() {
		return nameProduct;
	}


	/**
	 * @param nameProduct the nameProduct to set
	 */
	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}


	/**
	 * @return the mpProduct
	 */
	public double getMpProduct() {
		return mpProduct;
	}


	/**
	 * @param mpProduct the mpProduct to set
	 */
	public void setMpProduct(double mpProduct) {
		this.mpProduct = mpProduct;
	}


	/**
	 * @return the priceProduct
	 */
	public double getPriceProduct() {
		return priceProduct;
	}


	/**
	 * @param priceProduct the priceProduct to set
	 */
	public void setPriceProduct(double priceProduct) {
		this.priceProduct = priceProduct;
	}


	/**
	 * @return the dpProduct
	 */
	public double getDpProduct() {
		return dpProduct;
	}


	/**
	 * @param dpProduct the dpProduct to set
	 */
	public void setDpProduct(double dpProduct) {
		this.dpProduct = dpProduct;
	}
	

}
