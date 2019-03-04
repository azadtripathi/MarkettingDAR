package com.dm.model;

public class DashboardMenu {
	private String Page_id = "";
	private String parent_name;
	private String child_name;
	private String Page_NAme;
	private String View_p = "";
	private String Add_p = "";
	private String Edit_p = "";
	private String Delete_p = "";
	private String ParentMenu = "";
	private String display_name = "";
	private String icon = "";


	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setdisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getdisplay_name() {
		return display_name;
	}

	public void setPage_id(String Page_id) {
		this.Page_id = Page_id;
	}
	public String getPage_id() {
		return Page_id;
	}

	public void setParentMenu(String ParentMenu) {
		this.ParentMenu = ParentMenu;
	}
	public String getParentMenu() {
		return ParentMenu;
	}

	public void setparent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public String getparent_name() {
		return parent_name;
	}

	public void setchild_name(String child_name) {
		this.child_name = child_name;
	}
	public String getchild_name() {
		return child_name;
	}

	public void setPage_NAme(String Page_NAme) {
		this.Page_NAme = Page_NAme;
	}
	public String getPage_NAme() {
		return Page_NAme;
	}

	public void setView_p(String View_p) {
		this.View_p = View_p;
	}
	public String getView_p() {
		return View_p;
	}

	public void setAdd_p(String Add_p) {
		this.Add_p = Add_p;
	}
	public String getAdd_p() {
		return Add_p;
	}

	public void setEdit_p(String Edit_p) {
		this.Edit_p = Edit_p;
	}
	public String getEdit_p() {
		return Edit_p;
	}
	

	public void setDelete_p(String Delete_p) {
		this.Delete_p = Delete_p;
	}
	public String getDelete_p() {return Delete_p;}


}
