package com.ariful.basisvotarlistapp.model;

import org.chalup.microorm.annotations.Column;

import java.io.Serializable;

/**
 * Created by Dream71 on 13/11/2017.
 */

public class Person  implements Serializable{
    @Column("id")
   public String id;
    @Column("name")
    public String name;
    @Column("address")
    public String address;
    @Column("company_name")
    public String company_name;
    @Column("MID")
    public String MID;
    @Column("designation")
    public String designation;
    @Column("mobile")
    public String mobile;
    @Column("comments")
    public String comments;
    @Column("possiblity")
    public String possiblity;
 @Column("isCalled")
 public int isCalled;



 @Column("isvisited")

 public int isvisited;


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

 public String getAddress() {
  return address;
 }

 public void setAddress(String address) {
  this.address = address;
 }

 public String getCompany_name() {
  return company_name;
 }

 public void setCompany_name(String company_name) {
  this.company_name = company_name;
 }

 public String getMID() {
  return MID;
 }

 public void setMID(String MID) {
  this.MID = MID;
 }

 public String getDesignation() {
  return designation;
 }

 public void setDesignation(String designation) {
  this.designation = designation;
 }

 public String getMobile() {
  return mobile;
 }

 public void setMobile(String mobile) {
  this.mobile = mobile;
 }

 public String getComments() {
  return comments;
 }

 public void setComments(String comments) {
  this.comments = comments;
 }

 public String getPossiblity() {
  return possiblity;
 }

 public void setPossiblity(String possiblity) {
  this.possiblity = possiblity;
 }
}
