package com.dm.model;

/**
 * Created by Dataman on 9/9/2017.
 */

public class OrderMarkingData
{
    String partyId, partyName,ItemId,ItemName,totalQty, deliverQty, Ord1Id;
    boolean closeOrder;

    public void setOrd1Id(String ord1Id) {
        Ord1Id = ord1Id;
    }

    public String getOrd1Id() {
        return Ord1Id;
    }

    public boolean isCloseOrder() {
        return closeOrder;
    }

    public void setCloseOrder(boolean closeOrder) {
        this.closeOrder = closeOrder;
    }

    public void setPartyId(String partyId)
    {
        this.partyId = partyId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyName(String partyName)
    {
        this.partyName = partyName;
    }

    public String getItemId()
    {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public void setDeliverQty(String deliverQty) {
        this.deliverQty = deliverQty;
    }

    public String getDeliverQty() {
        return deliverQty;
    }
}
