package com.dm.model;

/**
 * Created by Dataman on 12/27/2016.
 */
public class AppEnviroData {
    private String DistSearch;
    private String ItemSearch;
    private String Itemwisesale;
    private String AreawiseDistributor;
    private String Host;
    private String UserName;
    private String Password;
    private String FtpDirectory;
    private String ImageDirectoryName;
    private String DistDiscussionStock;
    private String DsrRemarkMandatory;
    private String PartyCaption;
    private String Partyretailer;
    private String DSREntry_WithWhom;
    private String DSREntry_NextVisitWithWhom;
    private String DSREntry_NextVisitDate;
    private String DSREntry_RetailerOrderByEmail;
    private String DSREntry_RetailerOrderByPhone;
    private String DSREntry_Remarks;
    private String DSREntry_ExpensesFromArea;
    private String DSREntry_VisitType;
    private String DSREntry_Attendance;
    private String DSREntry_OtherExpenses;
    private String DSREntry_OtherExpensesRemarks;
    private String DSREntry_WithWhom_Rq;
    private String DSREntry_NextVisitWithWhom_Rq;
    private String DSREntry_NextVisitDate_Rq;
    private String DSREntry_RetailerOrderByEmail_Rq;
    private String DSREntry_RetailerOrderByPhone_Rq;
    private String DSREntry_Remarks_Rq;
    private String DSREntry_ExpensesFromArea_Rq;
    private String DSREntry_VisitType_Rq;
    private String DSREntry_Attendance_Rq;
    private String DSREntry_OtherExpenses_Rq;
    private String DSREntry_OtherExpensesRemarks_Rq;
    private String DSRENTRY_ExpenseToArea;
    private String DSRENTRY_ExpenseToArea_req;
    private String DSRENTRY_Chargeable;
    private String DSRENTRY_Chargeable_req;
    private String Show_PartyCollection;
    private String CheckTransactionForLock;
    private String Show_GeneratePDF;

    private String Show_UseCamera;

    private String notificationinterval;
    private String nextVisitTime, nextVisitTimeRequired;
    private String prime_failed_visit_next_time, prime_next_visit_time_req;

    String DSREntry_AttendanceByphoto,DSREntry_AttendanceByphoto_Req,DSREntry_Attendancebyorder,DSREntry_Attendancebyorder_Req,DSREntry_Attendancemannual,DSREntry_AttendancemannualReq;


    public void setDSREntry_Attendancebyorder(String DSREntry_Attendancebyorder) {
        this.DSREntry_Attendancebyorder = DSREntry_Attendancebyorder;
    }

    public String getDSREntry_Attendancebyorder() {
        return DSREntry_Attendancebyorder;
    }

    public String getDSREntry_Attendancebyorder_Req() {
        return DSREntry_Attendancebyorder_Req;
    }

    public void setDSREntry_Attendancebyorder_Req(String DSREntry_Attendancebyorder_Req) {
        this.DSREntry_Attendancebyorder_Req = DSREntry_Attendancebyorder_Req;
    }

    public String getDSREntry_AttendanceByphoto() {
        return DSREntry_AttendanceByphoto;
    }

    public void setDSREntry_AttendanceByphoto(String DSREntry_AttendanceByphoto) {
        this.DSREntry_AttendanceByphoto = DSREntry_AttendanceByphoto;
    }

    public String getDSREntry_AttendanceByphoto_Req() {
        return DSREntry_AttendanceByphoto_Req;
    }

    public void setDSREntry_AttendanceByphoto_Req(String DSREntry_AttendanceByphoto_Req) {
        this.DSREntry_AttendanceByphoto_Req = DSREntry_AttendanceByphoto_Req;
    }

    public String getDSREntry_Attendancemannual() {
        return DSREntry_Attendancemannual;
    }

    public void setDSREntry_Attendancemannual(String DSREntry_Attendancemannual) {
        this.DSREntry_Attendancemannual = DSREntry_Attendancemannual;
    }

    public String getDSREntry_AttendancemannualReq() {
        return DSREntry_AttendancemannualReq;
    }

    public void setDSREntry_AttendancemannualReq(String DSREntry_AttendancemannualReq) {
        this.DSREntry_AttendancemannualReq = DSREntry_AttendancemannualReq;
    }






    public void setPrime_failed_visit_next_time(String prime_failed_visit_next_time) {
        this.prime_failed_visit_next_time = prime_failed_visit_next_time;
    }

    public void setPrime_next_visit_time_req(String prime_next_visit_time_req) {
        this.prime_next_visit_time_req = prime_next_visit_time_req;
    }

    public String getPrime_failed_visit_next_time() {
        return prime_failed_visit_next_time;
    }

    public String getPrime_next_visit_time_req() {
        return prime_next_visit_time_req;
    }

    public void setNextVisitTime(String nextVisitTime) {
        this.nextVisitTime = nextVisitTime;
    }

    public String getNextVisitTime() {
        return nextVisitTime;
    }

    public String getNextVisitTimeRequired() {
        return nextVisitTimeRequired;
    }

    public void setNextVisitTimeRequired(String nextVisitTimeRequired) {
        this.nextVisitTimeRequired = nextVisitTimeRequired;
    }

    public String getNotificationInterval() {
        return this.notificationinterval;
    }
    public void setNotificationInterval(String notificationinterval) {this.notificationinterval = notificationinterval;}

    public String getShow_UseCamera() {
        return this.Show_UseCamera;
    }
    public void setShow_UseCamera(String Show_UseCamera) {this.Show_UseCamera = Show_UseCamera;}

    public String getShow_GeneratePDF() {
        return this.Show_GeneratePDF;
    }

    public void setShow_GeneratePDF(String Show_GeneratePDF) {
        this.Show_GeneratePDF = Show_GeneratePDF;
    }

    public String getCheckTransactionForLock() {
        return this.CheckTransactionForLock;
    }

    public void setCheckTransactionForLock(String CheckTransactionForLock) {
        this.CheckTransactionForLock = CheckTransactionForLock;
    }
    public String getShow_PartyCollection() {
        return this.Show_PartyCollection;
    }

    public void setShow_PartyCollection(String Show_PartyCollection) {
        this.Show_PartyCollection = Show_PartyCollection;
    }
    public String getDSRENTRY_Chargeable_req() {
        return this.DSRENTRY_Chargeable_req;
    }

    public void setDSRENTRY_Chargeable_req(String DSRENTRY_Chargeable_req) {
        this.DSRENTRY_Chargeable_req = DSRENTRY_Chargeable_req;
    }
    public String getDSRENTRY_Chargeable() {
        return this.DSRENTRY_Chargeable;
    }

    public void setDSRENTRY_Chargeable(String DSRENTRY_Chargeable) {
        this.DSRENTRY_Chargeable = DSRENTRY_Chargeable;
    }

    public String getDSREntry_WithWhom() {
        return this.DSREntry_WithWhom;
    }

    public void setDSREntry_WithWhom(String DSREntry_WithWhom) {
        this.DSREntry_WithWhom = DSREntry_WithWhom;
    }
    public String getDSREntry_NextVisitWithWhom() {
        return this.DSREntry_NextVisitWithWhom;
    }

    public void setDSREntry_NextVisitWithWhom(String DSREntry_NextVisitWithWhom) {
        this.DSREntry_NextVisitWithWhom = DSREntry_NextVisitWithWhom;
    }
    public String getDSREntry_NextVisitDate() {
        return this.DSREntry_NextVisitDate;
    }

    public void setDSREntry_NextVisitDate(String DSREntry_NextVisitDate) {
        this.DSREntry_NextVisitDate = DSREntry_NextVisitDate;
    }
    public String getDSREntry_RetailerOrderByEmail() {
        return this.DSREntry_RetailerOrderByEmail;
    }

    public void setDSREntry_RetailerOrderByEmail(String DSREntry_RetailerOrderByEmail) {
        this.DSREntry_RetailerOrderByEmail = DSREntry_RetailerOrderByEmail;
    }
    public String getDSREntry_RetailerOrderByPhone() {
        return this.DSREntry_RetailerOrderByPhone;
    }

    public void setDSREntry_RetailerOrderByPhone(String DSREntry_RetailerOrderByPhone) {
        this.DSREntry_RetailerOrderByPhone = DSREntry_RetailerOrderByPhone;
    }
    public String getDSREntry_Remarks() {
        return this.DSREntry_Remarks;
    }

    public void setDSREntry_Remarks(String DSREntry_Remarks) {
        this.DSREntry_Remarks = DSREntry_Remarks;
    }
    public String getDSREntry_ExpensesFromArea() {
        return this.DSREntry_ExpensesFromArea;
    }

    public void setDSREntry_ExpensesFromArea(String DSREntry_ExpensesFromArea) {
        this.DSREntry_ExpensesFromArea = DSREntry_ExpensesFromArea;
    }
    public String getDSREntry_VisitType() {
        return this.DSREntry_VisitType;
    }

    public void setDSREntry_VisitType(String DSREntry_VisitType) {
        this.DSREntry_VisitType = DSREntry_VisitType;
    }
    public String getDSREntry_Attendance() {
        return this.DSREntry_Attendance;
    }

    public void setDSREntry_Attendance(String DSREntry_Attendance) {
        this.DSREntry_Attendance = DSREntry_Attendance;
    }
    public String getDSREntry_OtherExpenses() {
        return this.DSREntry_OtherExpenses;
    }

    public void setDSREntry_OtherExpenses(String DSREntry_OtherExpenses) {
        this.DSREntry_OtherExpenses = DSREntry_OtherExpenses;
    }
    public String getDSREntry_OtherExpensesRemarks() {
        return this.DSREntry_OtherExpensesRemarks;
    }

    public void setDSREntry_OtherExpensesRemarks(String DSREntry_OtherExpensesRemarks) {
        this.DSREntry_OtherExpensesRemarks = DSREntry_OtherExpensesRemarks;
    }
    public String getDSREntry_WithWhom_Rq() {
        return this.DSREntry_WithWhom_Rq;
    }

    public void setDSREntry_WithWhom_Rq(String DSREntry_WithWhom_Rq) {
        this.DSREntry_WithWhom_Rq = DSREntry_WithWhom_Rq;
    }
    public String getDSREntry_NextVisitWithWhom_Rq() {
        return this.DSREntry_NextVisitWithWhom_Rq;
    }

    public void setDSREntry_NextVisitWithWhom_Rq(String DSREntry_NextVisitWithWhom_Rq) {
        this.DSREntry_NextVisitWithWhom_Rq = DSREntry_NextVisitWithWhom_Rq;
    }
    public String getDSREntry_NextVisitDate_Rq() {
        return this.DSREntry_NextVisitDate_Rq;
    }

    public void setDSREntry_NextVisitDate_Rq(String DSREntry_NextVisitDate_Rq) {
        this.DSREntry_NextVisitDate_Rq = DSREntry_NextVisitDate_Rq;
    }

    public String getDSREntry_RetailerOrderByEmail_Rq() {
        return this.DSREntry_RetailerOrderByEmail_Rq;
    }

    public void setDSREntry_RetailerOrderByEmail_Rq(String DSREntry_RetailerOrderByEmail_Rq) {
        this.DSREntry_RetailerOrderByEmail_Rq = DSREntry_RetailerOrderByEmail_Rq;
    }
    public String getDSREntry_RetailerOrderByPhone_Rq() {
        return this.DSREntry_RetailerOrderByPhone_Rq;
    }

    public void setDSREntry_RetailerOrderByPhone_Rq(String DSREntry_RetailerOrderByPhone_Rq) {
        this.DSREntry_RetailerOrderByPhone_Rq = DSREntry_RetailerOrderByPhone_Rq;
    }
    public String getDSREntry_Remarks_Rq() {
        return this.DSREntry_Remarks_Rq;
    }

    public void setDSREntry_Remarks_Rq(String DSREntry_Remarks_Rq) {
        this.DSREntry_Remarks_Rq = DSREntry_Remarks_Rq;
    }
    public String getDSREntry_ExpensesFromArea_Rq() {
        return this.DSREntry_ExpensesFromArea_Rq;
    }

    public void setDSREntry_ExpensesFromArea_Rq(String DSREntry_ExpensesFromArea_Rq) {
        this.DSREntry_ExpensesFromArea_Rq = DSREntry_ExpensesFromArea_Rq;
    }
    public String getDSREntry_VisitType_Rq() {
        return this.DSREntry_VisitType_Rq;
    }

    public void setDSREntry_VisitType_Rq(String DSREntry_VisitType_Rq) {
        this.DSREntry_VisitType_Rq = DSREntry_VisitType_Rq;
    }
    public String getDSREntry_Attendance_Rq() {
        return this.DSREntry_Attendance_Rq;
    }

    public void setDSREntry_Attendance_Rq(String DSREntry_Attendance_Rq) {
        this.DSREntry_Attendance_Rq = DSREntry_Attendance_Rq;
    }

    public String getDSREntry_OtherExpenses_Rq() {
        return this.DSREntry_OtherExpenses_Rq;
    }

    public void setDSREntry_OtherExpenses_Rq(String DSREntry_OtherExpenses_Rq) {
        this.DSREntry_OtherExpenses_Rq = DSREntry_OtherExpenses_Rq;
    }
    public String getDSREntry_OtherExpensesRemarks_Rq() {
        return this.DSREntry_OtherExpensesRemarks_Rq;
    }

    public void setDSREntry_OtherExpensesRemarks_Rq(String DSREntry_OtherExpensesRemarks_Rq) {
        this.DSREntry_OtherExpensesRemarks_Rq = DSREntry_OtherExpensesRemarks_Rq;
    }
    public String getDSRENTRY_ExpenseToArea() {
        return this.DSRENTRY_ExpenseToArea;
    }

    public void setDSRENTRY_ExpenseToArea(String DSRENTRY_ExpenseToArea) {
        this.DSRENTRY_ExpenseToArea = DSRENTRY_ExpenseToArea;
    }

    public String getDSRENTRY_ExpenseToArea_req() {
        return this.DSRENTRY_ExpenseToArea_req;
    }

    public void setDSRENTRY_ExpenseToArea_req(String DSRENTRY_ExpenseToArea_req) {
        this.DSRENTRY_ExpenseToArea_req = DSRENTRY_ExpenseToArea_req;
    }
    public String getDistSearch() {
        return this.DistSearch;
    }

    public void setDistSearch(String DistSearch) {
        this.DistSearch = DistSearch;
    }
    public String getPartyCaption() {
        return this.PartyCaption;
    }

    public void setPartyCaption(String PartyCaption) {
        this.PartyCaption = PartyCaption;
    }
    public String getPartyretailer() {
        return this.Partyretailer;
    }

    public void setPartyretailer(String Partyretailer) {
        this.Partyretailer = Partyretailer;
    }

    public String getItemSearch() {
        return this.ItemSearch;
    }

    public void setItemSearch(String ItemSearch) {
        this.ItemSearch = ItemSearch;
    }

    public String getItemwisesale() {
        return this.Itemwisesale;
    }

    public void setItemwisesale(String Itemwisesale) {
        this.Itemwisesale = Itemwisesale;
    }

    public String getAreawiseDistributor() {
        return this.AreawiseDistributor;
    }

    public void setAreawiseDistributor(String AreawiseDistributor) {
        this.AreawiseDistributor = AreawiseDistributor;
    }

    public String getHost() {
        return this.Host;
    }

    public void setHost(String Host) {
        this.Host = Host;
    }
    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getFtpDirectory() {
        return this.FtpDirectory;
    }

    public void setFtpDirectory(String FtpDirectory) {
        this.FtpDirectory = FtpDirectory;
    }
    public String getImageDirectoryName() {
        return this.ImageDirectoryName;
    }

    public void setImageDirectoryName(String ImageDirectoryName) {
        this.ImageDirectoryName = ImageDirectoryName;
    }
    public String getDistDiscussionStock() {
        return this.DistDiscussionStock;
    }

    public void setDistDiscussionStock(String DistDiscussionStock) {
        this.DistDiscussionStock = DistDiscussionStock;
    }

    public String getDsrRemarkMandatory() {
        return this.DsrRemarkMandatory;
    }

    public void setDsrRemarkMandatory(String DsrRemarkMandatory) {
        this.DsrRemarkMandatory = DsrRemarkMandatory;
    }
}
